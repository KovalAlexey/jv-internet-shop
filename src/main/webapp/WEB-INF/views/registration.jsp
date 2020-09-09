<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<h1>Please, provide you user details</h1>

<h4 style="color: blue">${message}</h4>

<form method="post" action="${pageContext.request.contextPath}/users/registration">
    Login<input type="text" name="login">
    Password<input type="password" name="pwd">
    Repeat password<input type="password" name="pwd-repeat">


    <button type="submit">Register!</button>
</form>

</body>
</html>
