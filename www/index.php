<html>
<head>
	<title>Web Authoring Tool</title>
    <link href="style.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<h1>Exercise Repository</h1>

	<table border="black">
            <tr><th>Exercise</th><th>Time Created</th></tr>
	<?php
    
    include_once("dbconnect.inc.php");
    $mysqli = new mysqli($host, $user, $password, $database);
    
    if (mysqli_connect_errno())
    {
        printf("Connect failed: %s\n", mysqli_connect_error());
        exit();
    }
    $result = $mysqli->query("SELECT * FROM exercise") or die($mysqli->error.__LINE__);
    while($row = $result->fetch_assoc()):
    	echo "<tr><td>" . htmlentities($row['exercise_name']) . "</td>";
                echo "<td>" . htmlentities($row['create_time']) . "</td>";
                $exerciseID = $row['idexercise'];
    ?>
    <td>
        <form name="editExercise" action="editExercise.php" method="POST">
            <input type="hidden" name="exerciseID" value="<?php echo $exerciseID; ?>"/>
            <input type="submit" name="editExercise" value="Edit"/>
        </form>
    </td>
    <td>
        <form name="deleteExercise" action="deleteExercise.php" method="POST" onsubmit="return confirm('Is it okay to delete this entry?')">
            <input type="hidden" name="exerciseID" value="<?php echo $exerciseID; ?>"/>
            <input type="submit" name="deleteExercise" value="Delete"/>
        </form>
    </td>
    <td>
        <form name="addAnnotation" action="addAnnotation.php" method="POST">
            <input type="hidden" name="exerciseID" value="<?php echo $exerciseID; ?>"/>
            <input type="submit" name="editExercise" value="Annotate"/>
        </form>
    </td>
    <?php
    echo "</tr>\n";
    endwhile;
    mysqli_close($mysqli);
    ?>
</table>
<form name="addNewWish" action="web_authoring.php">
    <input type="submit" value="New Exercise"/>
</form>
</body>

</html>