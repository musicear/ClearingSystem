<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html>
<head>
<title>ȨО¹݀뺯title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../stylecss/Normal.css" rel="stylesheet" type="text/css">
</head>

<body>
<%@  include file="header.jsp" %>
<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td bgcolor="#3685CB" width="100">
      <div align="center" class="tt"><font color="#FFFFFF">ȨО¹݀뺯font></div>
    </td>
    <td>&nbsp;</td>
    <td>
      <div align="right" class="tt"></div>
    </td>
  </tr>
</table>
<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td background="../images/obj_8.gif"><img src="../images/obj_8.gif" width="1" height="4"></td>
  </tr>
</table>
<br>
<table width="80%"  border="0" align="center" cellspacing="1" bgcolor="#999999">
  <tr bgcolor="#eeeeee">
    <td><div align="center">µȂ¼´�div></td>
    <td><div align="center">ԃ»§ѕĻ</div></td>
    <td><div align="center">̹˴»�div></td>
    <td><div align="center">̹˴ة</div></td>
  </tr>
  <tr bgcolor="#ffffff">
    <td><c:out value="${oper.id}" /></td>
    <td><c:out value="${oper.name}" /></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>

<form name="form1" method="post" action="OperatorMng.do?method=savePerm">
  <input type="hidden" name="operId" value="<c:out value="${oper.id}" />">
  <table width="80%"  border="0" align="center" cellspacing="1" bgcolor="#999999">
    <tr bgcolor="#ffffff">
      <td width="6%">&nbsp;</td>
      <td width="94%">&nbsp;</td>
    </tr>
	<c:forEach items="${allModules}" var="module"  varStatus="status">
    <tr bgcolor="#ffffff">
      <td><div align="center">
        <input type="checkbox" name="id<c:out value="${module.id}" />" value="checkbox" <c:if test="${perms[status.index ]=='yes'}">checked</c:if> <c:out value="${perms[status.index - 1]}" /> >
      </div></td>
      <td><c:out value="${module.name}" /></td>
    </tr>
	</c:forEach>
  </table>
  <table width="50%"  border="0" align="center">
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td><div align="center">
        <input name="imageField" type="image" src="../images/ok.gif" width="60" height="20" border="0">
      </div></td>
      <td>&nbsp;</td>
      <td><div align="center"><a href="javascript:history.back();"><img src="../images/cancel.gif" width="60" height="20" border="0"></a></div></td>
    </tr>
  </table>
  <p>&nbsp;  </p>
</form>
<p>&nbsp;</p>
</body>
</html>
