<!DOCTYPE html>
<html>
<head>
    <title>Web Authoring Tool</title>
    <link href="style.css" rel="stylesheet" type="text/css" />
</head>
<?php
    
    include_once("dbconnect.inc.php");
    $mysqli = new mysqli($host, $user, $password, $database);
    
    if (mysqli_connect_errno())
    {
        printf("Connect failed: %s\n", mysqli_connect_error());
        exit();
    }
    $id = $_POST["exerciseID"];
    $result = $mysqli->query("SELECT * FROM exercise where idexercise = $id") or die($mysqli->error.__LINE__);
    $result2 = $mysqli->query("SELECT * FROM exercise_annotation where exercise_idexercise = $id") or die($mysqli->error.__LINE__);
    $exercise = $result->fetch_assoc();
    $name = $exercise['exercise_name'];
    $url = $exercise['exercise_video_url'];
    $inst = $exercise['exercise_instruction'];
    ?>
<script type='text/javascript'>
        	function addFields(){
            // Number of inputs to create
            var one = 1;
            document.getElementById("num").value = (document.getElementById("num").value*1) + (one*1);
            var number = document.getElementById("num").value;
            // Container <div> where dynamic content will be placed
            var container = document.getElementById("notes");
            
            // Clear previous contents of the container
                // Create an <input> element, set its type and name attributes
                var fieldwrapper = document.createElement("div");
                fieldwrapper.name = "field" + number;
                var input0 = document.createElement("input");
                input0.type = "number";
                input0.name = "min" + number;
                input0.size = "2";
                input0.min = "0";
                var input1 = document.createElement("input");
                input1.type = "number";
                input1.name = "sec" + number;
                input1.size = "2"
                input1.min = "0";
                input1.max = "59";
                var input2 = document.createElement("textarea");
                input2.rows = "2";
                input2.cols = "50";
                input2.name = "text" + number;
                var label1 = document.createTextNode("Time");
                var col = document.createTextNode(":");
                var space = document.createTextNode(" ");
                var label2 = document.createTextNode("Note");
                var rem = document.createElement("input");
                rem.type = "button";
                rem.value = "remove";
                rem.onclick = function() {fieldwrapper.remove();};
                fieldwrapper.appendChild(label1);
                fieldwrapper.appendChild(input0);
                fieldwrapper.appendChild(col);
                fieldwrapper.appendChild(input1);
                fieldwrapper.appendChild(space);
                fieldwrapper.appendChild(label2);
                fieldwrapper.appendChild(input2);
                fieldwrapper.appendChild(rem);
                container.appendChild(fieldwrapper);   
        }

        function addExisting(p1, p2, p3){
            // Number of inputs to create
            var one = 1;
            document.getElementById("num").value = (document.getElementById("num").value*1) + (one*1);
            var number = document.getElementById("num").value;
            // Container <div> where dynamic content will be placed
            var container = document.getElementById("notes");
            
            // Clear previous contents of the container
                // Create an <input> element, set its type and name attributes
                var fieldwrapper = document.createElement("div");
                fieldwrapper.name = "field" + number;
                var input0 = document.createElement("input");
                input0.type = "number";
                input0.name = "min" + number;
                input0.min = "0";
                input0.size = "2";
                input0.value = p1;
                var input1 = document.createElement("input");
                input1.type = "number";
                input1.name = "sec" + number;
                input1.min = "0";
                input1.max = "59";
                input1.size = "2";
                input1.value = p2;
                var input2 = document.createElement("textarea");
                input2.rows = "2";
                input2.cols = "50";
                input2.name = "text" + number;
                input2.value = p3;
                var label1 = document.createTextNode("Time");
                var col = document.createTextNode(":");
                var space = document.createTextNode(" ");
                var label2 = document.createTextNode("Note");
                var rem = document.createElement("input");
                rem.type = "button";
                rem.value = "remove";
                rem.onclick = function() {fieldwrapper.remove();};
                fieldwrapper.appendChild(label1);
                fieldwrapper.appendChild(input0);
                fieldwrapper.appendChild(col);
                fieldwrapper.appendChild(input1);
                fieldwrapper.appendChild(space);
                fieldwrapper.appendChild(label2);
                fieldwrapper.appendChild(input2);
                fieldwrapper.appendChild(rem);
                container.appendChild(fieldwrapper);     
        }

    </script>
<body>
<h1> Clemson Physical Therapy Web Authoring Tool</h1>
<br/>
<div style="text-align:center">
    <video src="<?= $url ?>" type="video/mp4" height="450" controls autoplay>
</div>
<form action="annotate.php" method="post">
<h3><u>Annotations</u></h3>
<input type="hidden" id="num" name="number" value="0"/>
    <a href="#" id="filldetails" onclick="addFields()">Add Annotation</a>
<fieldset id="notes">
	<legend>Video annotations</legend>
    <?php
    while($annotations = $result2->fetch_assoc()):
        $time = $annotations['exercise_annotation_video_time'];
        $pieces = explode(":", $time);
        $text = $annotations['exercise_annotation_annotation'];
        echo"<script>addExisting(\"$pieces[0]\", \"$pieces[1]\",\"$text\");</script>";
    ?>
    <?php
    endwhile;
    mysqli_close($mysqli);
    ?>
</fieldset>
<input type="hidden" name="exerciseID" value="<?= $id ?>"/>
<input type="submit" value="Make Annotates"><br/><br/>
</form>
<br/>

</body></html>