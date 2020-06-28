<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="ww" uri="/webwork" %>
<html>
<head>
    <script src="../javascripts/prototype.js" type="text/javascript"></script>
    <link href="../stylesheets/Normal.css" rel="stylesheet" type="text/css">
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0" background="../images/obj_6.gif">
    <tr>
        <td height="26" class="tt"><font color="#666666">
        </font></td>
    </tr>
</table>
<table border=0>
    <tr>
        <td height="26"></td>
    </tr>
</table>
<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
    <tr>
        <td bgcolor="#3685CB" width="158">
            <div align="center" class="tt"><font color="#FFFFFF">修改权限</font></div></td>
        <td width="378">
            <div align="right" class="tt"></div></td>
    </tr>
</table>
<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
    <tr>
        <td background="../images/obj_8.gif"><img src="../images/obj_8.gif" width="1" height="4"></td>
    </tr>
</table>
<br>
<table width="90%" border="0" align="center" cellspacing="1" bgcolor="#CCCCCC">
    <tr bgcolor="#FAFAFA">
        <td>选中</td>
        <td>功能名称</td>
    </tr>
    <ww:iterator value="grantedModules" id="top">
        <ww:iterator value="top.childModules">
            <tr bgcolor="#FFFFFF">
                <td><input type="checkbox" name="a" value=""/></td>
                <td><ww:property value="name"/></td>
            </tr>
        </ww:iterator>
    </ww:iterator>
</table>
</body>
</html>