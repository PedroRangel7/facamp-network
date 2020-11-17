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

    $saida = "'".$_POST['saida']."'";
    $destino = "'".$_POST['destino']."'";
    $preco = "'".$_POST['preco']."'";
    $placa = "'".$_POST['placa']."'";
    $dataCarona = "'".$_POST['dataCarona']."'";
    $idUsuario = "'".$_POST['idUsuario']."'";

    $preco = str_replace(",", ".", $preco);

    $sql = "INSERT INTO carona (saida, destino, preco, placa, dataCarona, idUsuario) VALUES ($saida, $destino, $preco, $placa, $dataCarona, $idUsuario)";

    if ($conn->query($sql) === TRUE) {
        $response["erro"] = false;
        $response["mensagem"] = "Carona adicionada com sucesso.";
    }
    else {
        $response["mensagem"] = "A carona não pôde ser adicionada. Tente novamente.";
    }

    $conn->close();
}
else{
    $response["mensagem"] = "Algo deu errado. Tente novamente.";
}

echo json_encode($response);
?>