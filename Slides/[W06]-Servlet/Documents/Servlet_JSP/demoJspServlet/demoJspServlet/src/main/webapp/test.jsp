<%--
  Created by IntelliJ IDEA.
  User: tpl
  Date: 30/03/2023
  Time: 13:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<center>
    <form name="frm" method="get">
        Nhập n:<input type="text" name="soLuong"/><br/>
        <input type="submit" name="bt" value="Phát sinh"/>
    </form>
    <%
        String s = request.getParameter("soLuong");
        if (s != null) {
            int n = Integer.parseInt(s);
            for (int i = 0; i < n; i++) {
                out.println("<b>" + i + "</b>");
                out.println("<br/>");
            }
        }
    %>
</center>
</body>
</html>
