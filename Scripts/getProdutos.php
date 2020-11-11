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

    $id = "'".$_POST['id']."'";

    $sql = "SELECT * FROM produto WHERE id = $id";
    $result = $conn->query($sql);

    if($result->num_rows > 0){
        $registro = mysqli_fetch_array($result);

        $sql = "SELECT * FROM usuario WHERE id = $registro[idUsuario]";
        $result = $conn->query($sql);
        $usuario = mysqli_fetch_array($result);
        
        $response["erro"] = false;
        $response["mensagem"] = "Produtos encontrados.";
        $response["nome"] = $registro['nome'];
        $response["preco"] = $registro['preco'];
        $response["descricao"] = $registro['descricao'];
        $response["anunciante"] = $usuario['nome'];
        //$response["imagem"] = $registro['imagem'];
    }
    else{
        $response["mensagem"] = "Nenhum produto encontrado.";
    }

    $conn->close();
}

echo json_encode($response);

?>