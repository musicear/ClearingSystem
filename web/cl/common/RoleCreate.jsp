<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/webwork" prefix="ww" %>
<html>
    <head>
        <title>角色创建</title>
        <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
        <link href="../stylesheets/Normal.css" rel="stylesheet" type="text/css">
    </head>
    
    <body>
         <table width="100%" border="0" cellspacing="0" cellpadding="0" background="../images/obj_6.gif">
            <tr>
                <td height="26" class="tt"><font color="#666666"></font></td>
                <td class="tt">
                    <div align="right"><font color="#666666">	                     
                    </font></div>
                </td>
            </tr>
        </table>
        <br>
        <table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
                <td bgcolor="#3685CB" width="100">
                    <div align="center" class="tt"><font color="#FFFFFF">新增角色</font></div>
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
        <form name="form1" method="post" action="AddRole.action">
            <table width="90%"  border="0" align="center" cellspacing="1" cellpadding="0" bgcolor="#999999">
                <tr>
                    <td width="26%" bgcolor="#EEEEEE" class="tt">
                    <div align="center">角 色 id</div></td>
                    <td width="46%" bgcolor="#FFFFFF">
                        <input name="id" type="text" id="id" maxlength="18" style="border-width: 1px,1px,1px,1px;border: 1px solid">
                    </td>
                    <td width="28%" bgcolor="#FFFFFF">*</td>
                </tr>
                <tr>
                    <td bgcolor="#EEEEEE" class="tt" width="26%">
                    <div align="center">角 色 名 称</div></td>
                    <td bgcolor="#FFFFFF" width="46%">
                        <input name="name" type="text" id="name" maxlength="10" style="border-width: 1px,1px,1px,1px;border: 1px solid">
                    </td>
                    <td bgcolor="#FFFFFF" width="28%">* </td>
                </tr>
            </table>
            <div align="center"><br>
                <table width="50%"  border="0">
                    <tr>
                        <td><div align="center"><span class="tt">
                                    <input name="imageField" type="image" src="../images/ok.gif" width="60" height="20" border="0">
                        </span></div></td>
                        <td><div align="center"><a href="RoleList.action"><img src="../images/return.gif" width="60" height="20" border="0"></a></div></td>
                    </tr>
                </table>
            </div>
        </form>
    </body>
</html>
