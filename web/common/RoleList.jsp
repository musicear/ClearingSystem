<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/webwork" prefix="ww" %>
<html>
    <head>
        <title>角色列表</title>
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
                <div align="center" class="tt"><font color="#FFFFFF">角色管理</font></div></td>
                <td>&nbsp;</td>
                <td>
                <div align="right" class="tt"></div></td>
            </tr>
        </table>
        <table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
                <td background="../images/obj_8.gif"><img src="../images/obj_8.gif" width="1" height="4"></td>
            </tr>
        </table>
        <form name="form1" method="post" action="RemoveRole.action">
            <table width="90%"  border="0" align="center" cellspacing="1" bgcolor="#999999">
                <tr bgcolor="#eeeeee">
                    <td><div align="center">选择</div></td>
                    <td><div align="center">角色代码</div></td>
                    <td><div align="center">角色名称</div></td>
                    <td><div align="center">权限管理</div></td>
                    <td><div align="center">修改</div></td>
                </tr>
                <ww:iterator value="roleList">
                    <tr bgcolor="#ffffff">
                        <td><div align="center">
                                <input type="checkbox" name="id<ww:property value="id" />" value="checkbox">
                        </div></td>
                        <td><ww:property value="id" /></td>
                        <td><ww:property value="name" /></td>
                        <td><div align="center"><a href="ShowRolePermission.action?id=<ww:property value="id" />">权限管理</a></div></td>
                        <td><div align="center"><a href="ToModifyRole.action?id=<ww:property value="id" />">修改</div></td>
                    </tr>
                </ww:iterator>
            </table>
            
            <table width="90%"  border="0" align="center">
                <tr>
                    <td width="20%">&nbsp;</td>
                    <td width="73%">&nbsp;</td>
                    <td width="7%">&nbsp;</td>
                </tr>
                <tr>
                    <td><a href="ToAddRole.action"><img src="../images/add.gif" width="60" height="20" border="0"></a></td>
                    <td><input name="imageField" type="image" src="../images/remove.gif" width="60" height="20" border="0"></td>
                    <td>&nbsp;</td>
                </tr>
            </table>
        </form>
    </body>
</html>
