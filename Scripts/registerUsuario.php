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
    $login = "'".$_POST['login']."'";
    $senha = "'".$_POST['senha']."'";
    $telefone = "'".$_POST['telefone']."'";
    $biografia = "'".$_POST['biografia']."'";

    $sql = "INSERT INTO usuario (nome, login, senha, telefone, biografia) VALUES ($nome, $login, $senha, $telefone, $biografia)";

    if ($conn->query($sql) === TRUE) {
        $response["erro"] = false;
        $response["mensagem"] = "Registrado com sucesso.";
    }
    else {
        $response["mensagem"] = "Não foi possível registrar. Tente novamente.";
    }

    $conn->close();
}
else{
    $response["mensagem"] = "Algo deu errado. Tente novamente.";
}

echo json_encode($response);
?>