<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
		<form action="LobbyData?award/queryPage" method="post">
			pageIdx:
			<input type="text" name="pageIdx" value="">
			pageSize:
			<input type="password" name="pageSize" value="">
			<input type="submit" name="post" value="send">
		</form>
		<form action="UserData?account/register" method="post">
			Account:
			<input type="text" name="account" value="">
			Password:
			<input type="password" name="password" value="">
			Email:
			<input type="text" name="email" value="">
			Code:
			<input type="text" name="validate" value="">
			<input type="submit" name="register" value="register">
		</form>
		<form action="UserData?account/login" method="post">
			Account:
			<input type="text" name="account" value="">
			Password:
			<input type="password" name="password" value="">
			<input type="submit" name="login" value="login">
		</form>
		<form action="CreateEmailValidate" method="post">
			Email:
			<input type="text" name="email" value="">
			<input type="submit" name="login" value="sendCode">
		</form>
		<form action="FileUtils?file/upload" method="post" enctype="multipart/form-data">
			<input type="file" name="file" value="Select">
			<input type="submit" name="upload" value="Upload">
		</form>
</body>
<script type="text/javascript">
	
	function toRegister() {
		//xmlhttp = new XMLHttpRequest();
		//xmlhttp.open("POST","demo_post.asp",true);
	}

	function toLogin() {
		
	}
</script>
</html>