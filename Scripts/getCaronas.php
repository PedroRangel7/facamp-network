<?php
header('Content-Type: application/json charset=utf-8');

$response = array();
$response["erro"] = true;

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    include 'dbConnection.php';

    $conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);
    mysqli_set_charset($conn, "utf8");

    if($conn->connect_error){
        die("Conexão falhou.");
    }

    $sql = "SELECT * FROM carona";
    $response["erro"] = false;

    if($result = $conn->query($sql)){
        $response["mensagem"] = $result->num_rows." Caronas encontradas.";
        $response["numeroCaronas"] = $result->num_rows;
        $i = 0;
        while ($row = $result->fetch_row()) {
            $response[$i]["id"] = $row[0];
            $response[$i]["saida"] = $row[1];
            $response[$i]["destino"] = $row[2];
            $response[$i]["preco"] = $row[3];
            $response[$i]["placa"] = $row[4];
            $response[$i]["dataCarona"] = $row[5];
            $response[$i]["idUsuario"] = $row[6];
            $i++;
        }
        $result->close();
    }
    else{
        $response["mensagem"] = "Nenhuma carona encontrada.";
    }

    $conn->close();
}
else{
    $response["mensagem"] = "Algo deu errado. Tente novamente.";
}
echo json_encode($response);
?>