<?php
header('Content-Type: application/json charset=utf-8');

$response = array();
$response["erro"] = true;

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    include 'dbConnection.php';

    $conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);
    mysqli_set_charset($conn, "utf8");

    if($conn->connect_error){
        die("Conexão falhou.");
    }

    $tabela = $_POST['tabela'];
    $id = $_POST['id'];
    $campo = $_POST['campo'];
    $valor = $_POST['valor'];

    if($campo == "imagem" || $campo == "foto"){
        $valor = "'".base64_encode($valor)."'";
    }
    else{
        $valor = "'".$valor."'";
    }

    $sql = "UPDATE $tabela SET $campo = $valor WHERE $tabela.id = $id";

    if ($conn->query($sql) === TRUE) {
        $response["erro"] = false;
        $response["mensagem"] = "Campo modificado com sucesso.";
    }
    else {
        $response["mensagem"] = "Não foi possível alterar o campo. Tente novamente.";
    }

    $conn->close();
}
else{
    $response["mensagem"] = "Algo deu errado. Tente novamente.";
}

echo json_encode($response);
?>