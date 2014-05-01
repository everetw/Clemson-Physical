<html>
<head>
    <title>Web Authoring Tool</title>
    <link href="style.css" rel="stylesheet" type="text/css" />
</head>
<?php
    $id = $_POST["exerciseID"];
    include_once("dbconnect.inc.php");
    $mysqli = new mysqli($host, $user, $password, $database);
    
    if (mysqli_connect_errno())
    {
        printf("Connect failed: %s\n", mysqli_connect_error());
        exit();
    }
    $result = $mysqli->query("SELECT * FROM exercise where idexercise = $id") or die($mysqli->error.__LINE__);
    $exercise = $result->fetch_assoc();
    $name = $exercise['exercise_name'];
    $url = $exercise['exercise_video_url'];
    $inst = $exercise['exercise_instruction'];
    ?>
<body>
<h1> Clemson Physical Therapy Web Authoring Tool</h1>
<br/>
<form action="update.php" method="post" enctype="multipart/form-data">
<h3><u>Exercise Name</u></h3>
<input type="text" name="name" size="50" value="<?= $name ?>"><br/>
<h3><u>Video File (only if different from the current video)</u></h3>
<input type="file" name="load" accept="video/*"><br/>
<h3><u>Instructions</u></h3><h4>either place text instructions or a url to a page containing text instructions here.</h4>
<textarea name="instruct" rows="25" cols="150"><?= $inst ?></textarea>
<input type="hidden" name="exerciseID" value="<?= $id ?>"/>
<input type="submit" value="Update"><br/><br/>
</form>
<br/>

</body></html>