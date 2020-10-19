<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="../header.jsp"%>
<html>
<head>
    <title>Login page</title>
</head>
<body>
<h1>Login page</h1>
<h3 style="color: blue">${errorMsg}</h3>
<form method="post" action="${pageContext.request.contextPath}/login">
    <label>Login</label><input type="text" name="login">
    <label>Password</label><input type="password" name="pwd">
    <button type="submit">Login</button>
</form>
<form action="${pageContext.request.contextPath}/users/registration">
    <input type="submit" value="Registration!"><br></form>
</body>
</html>
