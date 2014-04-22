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
    $result = $mysqli->query("SELECT * FROM exercise_annotation") or die($mysqli->error.__LINE__);

     
    // check for empty result
    if ($result->num_rows > 0) {
        // looping through all results
        // receipts node
        $response["annotations"] = array();
     
         while ($row = $result->fetch_assoc()) {
            // temp user array
            $annotaion = array();
    		$annotaion["idexercise_annotation"] = $row["idexercise_annotation"];
    		$annotaion["exercise_idexercise"] = $row["exercise_idexercise"];
    		$annotaion["exercise_annotation_video_time"] = $row["exercise_annotation_video_time"];
    		$annotaion["exercise_annotation_annotation"] = $row["exercise_annotation_annotation"];
            $annotaion["create_time"] = $row["create_time"];
     
            // push single receipt into final response array
            array_push($response["annotations"], $annotaion);
        }
        // success
        $response["success"] = 1;
     
        // echoing JSON response
        echo json_encode($response);
    } else {
        // no receipts found
        $response["success"] = 0;
        $response["message"] = "No annotations found";
     
        // echo no users JSON
        echo json_encode($response);
    }
?>