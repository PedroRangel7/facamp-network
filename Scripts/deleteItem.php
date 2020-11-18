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

    if($tabela == "usuario"){
        $sql = "DELETE FROM produto WHERE produto.idUsuario = $id";
        if ($conn->query($sql) === TRUE) {
            $response["mensagemP"] = "Produtos do usuário deletados com sucesso.";
        }
        else {
            $response["mensagemP"] = "Não foi possível deletar os produtos do usuário. Tente novamente.";
        }

        $sql = "DELETE FROM aula WHERE aula.idUsuario = $id";
        if ($conn->query($sql) === TRUE) {
            $response["mensagemA"] = "Aulas do usuário deletadas com sucesso.";
        }
        else {
            $response["mensagemA"] = "Não foi possível deletar as aulas do usuário. Tente novamente.";
        }

        $sql = "DELETE FROM carona WHERE carona.idUsuario = $id";
        if ($conn->query($sql) === TRUE) {
            $response["mensagemC"] = "Caronas do usuário deletadas com sucesso.";
        }
        else {
            $response["mensagemC"] = "Não foi possível deletar as caronas do usuário. Tente novamente.";
        }
    }

    $sql = "DELETE FROM $tabela WHERE $tabela.id = $id";

    if ($conn->query($sql) === TRUE) {
        $response["erro"] = false;
        if($tabela == "usuario"){
            $response["mensagem"] = "Usuário deletado com sucesso.";
        }
        else{
            $response["mensagem"] = "Item deletado com sucesso.";
        }
    }
    else {
        if($tabela == "usuario"){
            $response["mensagem"] = "Não foi possível deletar o usuário. Tente novamente.";
        }
        else{
            $response["mensagem"] = "Não foi possível deletar o item. Tente novamente.";
        }
    }

    $conn->close();
}
else{
    $response["mensagem"] = "Algo deu errado. Tente novamente.";
}

echo json_encode($response);
?>
