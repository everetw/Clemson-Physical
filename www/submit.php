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
    $upload = "videos/".basename($_FILES['load']['name']);
    move_uploaded_file($_FILES["load"]["tmp_name"], $upload);
    chmod($upload, 0755);
    $url = "http://people.cs.clemson.edu/~everetw/clemsonphysical/".$upload;
    $result = $mysqli->query("INSERT INTO exercise VALUES(null, \"$name\", \"$url\", \"$inst\", null, null)") or die($mysqli->error.__LINE__);
    
    mysqli_close($mysqli);
    echo var_dump($_POST);
    echo "done";
    header( 'Location: http://people.cs.clemson.edu/~everetw/clemsonphysical/' ) ;
    ?>

</body></html>