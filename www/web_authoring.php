<html>
<head><title>Web Authoring Tool</title></head>
<body>
<h1> Clemson Physical Therapy Web Authoring Tool</h1>
<br/>
<form action="submit.php" method="post" enctype="multipart/form-data">
<h3><u>Exercise Name</u></h3>
<input type="text" name="name" size="50" required><br/>
<h3><u>Video File</u></h3>
<input type="file" name="load" accept="video/*" required><br/>
<h3><u>Instrctions</u></h3><h4>either place text instructions or a url to a page containing text instructions here.</h4>
<textarea name="instruct" rows="25" cols="150"></textarea>
<input type="submit" value="Submit"><br/><br/>
</form>
<br/>

</body></html>