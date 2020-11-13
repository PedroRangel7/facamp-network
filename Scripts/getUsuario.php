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

    $idUsuario = "'".$_POST['idUsuario']."'";

    $sql = "SELECT * FROM usuario WHERE id = $idUsuario";
    $result = $conn->query($sql);

    if($result->num_rows > 0){
        $registro = mysqli_fetch_array($result);

        $sql = "SELECT * FROM tipo_usuario WHERE id = $registro[tipo]";
        $result = $conn->query($sql);
        $tipoUsuario = mysqli_fetch_array($result);
        
        $response["erro"] = false;
        $response["mensagem"] = "Usuário encontrado.";
        $response["id"] = $registro['id'];
        $response["nome"] = $registro['nome'];
        $response["telefone"] = $registro['telefone'];
        $response["biografia"] = $registro['biografia'];
        $response["tipo"] = $registro['tipo'];
        $response["tipoString"] = $tipoUsuario['tipo'];
        $response["foto"] = base64_encode($registro['foto']);
    }
    else{
        $response["mensagem"] = "Usuário não encontrado.";
    }

    $conn->close();
}

echo json_encode($response);

?>