<?php
 
/*
 * Following code will list all the receipts
 */
 
// array for JSON response
$response = array();
 
// include db connect class
include_once("dbconnect.inc.php");
 
// connecting to db
 $mysqli = new mysqli($host, $user, $password, $database);

 if (mysqli_connect_errno())
    {
        printf("Connect failed: %s\n", mysqli_connect_error());
        exit();
    }
 
// get all receipts from receipts table
$result = $mysqli->query("SELECT * FROM exercise") or die($mysqli->error.__LINE__);

 
// check for empty result
if ($result->num_rows > 0) {
    // looping through all results
    // receipts node
    $response["exercises"] = array();
 
     while ($row = $result->fetch_assoc()) {
        // temp user array
        $exercise = array();
		$exercise["idexercise"] = $row["idexercise"];
		$exercise["exercise_name"] = $row["exercise_name"];
		$exercise["exercise_video_url"] = $row["exercise_video_url"];
		$exercise["exercise_instruction"] = $row["exercise_instruction"];
        $exercise["create_time"] = $row["create_time"];
        $exercise["update_time"] = $row["update_time"];
 
        // push single receipt into final response array
        array_push($response["exercises"], $exercise);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no receipts found
    $response["success"] = 0;
    $response["message"] = "No exercises found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>