<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>取钱e</title>
</head>
<body>
<form action="UserServlet?action=outMoney" method="post">
取出金额为100的整数倍
<br>
<input type="text" name="money">&nbsp;&nbsp;<input type="submit" value="确定取出">
</form>
</body>
</html>