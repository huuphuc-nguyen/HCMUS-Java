<%@ page import="com.example.demo1.PhanSo" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="hello-servlet">Hello Servlet</a>

<br/>
Ngày: <%= new java.util.Date()%>
<br/>
Server: <%= application.getServerInfo()%>
<br/>
Phan so: <%= new PhanSo(2, 1)%>
<br/>
<%--<form action="hello-servlet" method="post">--%>
<%--    <input type="submit" name="bt" value="Phát sinh"/>--%>
<%--</form>--%>
</body>
</html>