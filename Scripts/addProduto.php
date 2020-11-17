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

    $nome = "'".$_POST['nome']."'";
    $preco = "'".$_POST['preco']."'";
    $descricao = "'".$_POST['descricao']."'";
    $idUsuario = "'".$_POST['idUsuario']."'";

    $preco = str_replace(",", ".", $preco);

    $sql = "INSERT INTO produto (nome, preco, descricao, idUsuario) VALUES ($nome, $preco, $descricao, $idUsuario)";

    if ($conn->query($sql) === TRUE) {
        $response["erro"] = false;
        $response["mensagem"] = "Produto adicionado com sucesso.";
    }
    else {
        $response["mensagem"] = "O produto não pôde ser adicionado. Tente novamente.";
    }

    $conn->close();
}
else{
    $response["mensagem"] = "Algo deu errado. Tente novamente.";
}

echo json_encode($response);
?>