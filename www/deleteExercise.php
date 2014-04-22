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
    $id = $_POST["exerciseID"];
	$result = $mysqli->query("DELETE FROM exercise WHERE idexercise=$id") or die($mysqli->error.__LINE__);
	$result = $mysqli->query("DELETE FROM exercise_annotation WHERE exercise_idexercise=$id") or die($mysqli->error.__LINE__);
	mysqli_close($mysqli);
	header( 'Location: http://people.cs.clemson.edu/~everetw/clemsonphysical/' ) ;
	?>
</body>
</script>
</html>