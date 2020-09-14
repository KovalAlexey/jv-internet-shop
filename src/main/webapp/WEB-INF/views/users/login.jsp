<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login page</title>
</head>
<body>
<h1>Login page</h1>
<h3 style="color: blue">${errorMsg}</h3>
<form action="${pageContext.request.contextPath}/login" method="post">
    <label>Login</label><input type="text" name="login">
    <label>Password</label><input type="password" name="pwd">
    <button type="submit">Login</button>
</form>
</body>
</html>
