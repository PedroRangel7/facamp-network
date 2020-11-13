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

    $materia = "'".$_POST['materia']."'";
    $descricao = "'".$_POST['descricao']."'";
    $preco = "'".$_POST['preco']."'";
    $dataAula = "'".$_POST['dataAula']."'";
    $idUsuario = "'".$_POST['idUsuario']."'";

    $preco = str_replace(",", ".", $preco);

    $sql = "INSERT INTO aula (id, materia, descricao, preco, dataAula, idUsuario) VALUES (NULL, $materia, $descricao, $preco, $dataAula, $idUsuario)";

    if ($conn->query($sql) === TRUE) {
        $response["erro"] = false;
        $response["mensagem"] = "Aula adicionada com sucesso.";
    }
    else {
        $response["mensagem"] = "A aula não pôde ser adicionada. Tente novamente.";
    }

    $conn->close();
}
else{
    $response["mensagem"] = "Algo deu errado. Tente novamente.";
}

echo json_encode($response);
?>