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
    $count = $_POST["number"];
    $id = $_POST["exerciseID"];
    $min_array = array();
    $sec_array = array();
    $text_array = array();
    $entries = 0;
    $result = $mysqli->query("DELETE FROM exercise_annotation WHERE exercise_idexercise=$id") or die($mysqli->error.__LINE__);
    for ($i=1; $i <= $count; $i++) { 
        if(isset($_POST["text".$i])){
        array_push($min_array, $_POST["min".$i]);
        array_push($sec_array, $_POST["sec".$i]);
        array_push($text_array, $_POST["text".$i]);
        $entries++;
        }
    }
    
    for ($i=0; $i < $entries; $i++) { 
        if(is_numeric($min_array[$i]) && $text_array[$i] && is_numeric($sec_array[$i])){
        $min = $min_array[$i];
        $sec = $sec_array[$i];
        $text = $text_array[$i];
        $time = "".$min.":".$sec;
        $result = $mysqli->query("INSERT INTO exercise_annotation VALUES(null, $id, \"$time\", \"$text\", null)") or die($mysqli->error.__LINE__);
        }
    }
    
    mysqli_close($mysqli);
    echo var_dump($_POST);
    echo "done";
    header( 'Location: http://people.cs.clemson.edu/~everetw/clemsonphysical/' ) ;
    ?>

</body></html>