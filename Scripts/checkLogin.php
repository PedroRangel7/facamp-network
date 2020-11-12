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

    $login = "'".$_POST['login']."'";
    $senha = "'".$_POST['senha']."'";

    $sql = "SELECT * FROM usuario WHERE login = $login AND senha = $senha";
    $result = $conn->query($sql);

    if($result->num_rows > 0){
        $registro = mysqli_fetch_array($result);
        
        $response["erro"] = false;
        $response["mensagem"] = $result->num_rows." Registros encontrados.";
        $response["tipo"] = $registro['tipo'];
    }
    else{
        $response["mensagem"] = "Dados inválidos.";
    }

    $conn->close();
}

echo json_encode($response);

?>