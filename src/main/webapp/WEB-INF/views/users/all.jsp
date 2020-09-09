<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>All Users</title>
</head>
<body>
<h1> All users here: </h1>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Option</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>
                <c:out value="${user.id}"/>
            </td>
            <td>
                <c:out value="${user.name}"/>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
