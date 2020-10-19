<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="../header.jsp"%>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<h1>Please, provide you user details</h1>

<h4 style="color: blue">${message}</h4>

<form method="post" action="${pageContext.request.contextPath}/users/registration">
    <label>Name</label><input type="text" name="name">
    <label>Login</label><input type="text" name="login">
    <label>Password</label><input type="password" name="pwd">
    <label>Repeat password</label><input type="password" name="pwd-repeat">
    <button type="submit">Register!</button>
</form>

</body>
</html>
