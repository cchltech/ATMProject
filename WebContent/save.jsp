<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="UserServlet?action=saveMoney" method="post">
账户:<%= session.getAttribute("card") %>
<br>
请放入100元钞票，并少于100张。
<br>
<input type="text" name="money">&nbsp;&nbsp;<input type="submit" value="确定存入">
</form>
</body>
</html>