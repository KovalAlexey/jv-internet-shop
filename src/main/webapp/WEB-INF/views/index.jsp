<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Internet-shop</title>
</head>
<body>
<h1>Hello Mates!</h1>
<h2>Today is: ${time}</h2>
<a href="${pageContext.request.contextPath}/users/all">All users</a>
<a href="${pageContext.request.contextPath}/products/all">All products</a>
<a href="${pageContext.request.contextPath}/users/cart">User cart</a>
<a href="${pageContext.request.contextPath}/injectData">Inject data to DB</a>
</body>
</html>
