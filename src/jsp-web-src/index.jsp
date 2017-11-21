<!DOCTYPE html>
<html>

<head>
  	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
</head>	
<body>
	<div class="container">
		<form action = "reasoning" method = "POST">
			<input type="text" name="nameSearch" placeholder="nama yang ingin di cari"/>
			<button type="submit" class="btn btn-primary"> Submit </button>
		</form>
	</div>
        <%= request.getParameter("") %>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
</body>
</html>