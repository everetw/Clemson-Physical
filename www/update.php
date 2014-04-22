<html><head><title>PHP Submit</title></head>
<body>
<?php
    
    include_once("dbconnect.inc.php");
    
    $mysqli = new mysqli($host, $user, $password, $database);
    
    if (mysqli_connect_errno())
    {
        printf("Connect failed: %s\n", mysqli_connect_error());
        exit();
    }
    
    /* retrieve */
    $name = $_POST["name"];
    $inst = $_POST["instruct"];
    $id = $_POST["exerciseID"];
    if($_FILES['load']['error'] == UPLOAD_ERR_OK) {
    $upload = "videos/".basename($_FILES['load']['name']);
    move_uploaded_file($_FILES["load"]["tmp_name"], $upload);
    chmod($upload, 0755);
    $url = "http://people.cs.clemson.edu/~everetw/clemsonphysical/".$upload;

    $result = $mysqli->query("UPDATE exercise SET exercise_name=\"$name\", exercise_video_url=\"$url\", exercise_instruction=\"$inst\" WHERE idexercise=$id") or die($mysqli->error.__LINE__);
    $result = $mysqli->query("DELETE FROM exercise_annotation WHERE exercise_idexercise=$id") or die($mysqli->error.__LINE__);
    }
    else{
    $result = $mysqli->query("UPDATE exercise SET exercise_name=\"$name\", exercise_instruction=\"$inst\" WHERE idexercise=$id") or die($mysqli->error.__LINE__);
    }
    mysqli_close($mysqli);
    echo var_dump($_POST);
    echo "done";
    header( 'Location: http://people.cs.clemson.edu/~everetw/clemsonphysical/' ) ;
    ?>

</body></html>