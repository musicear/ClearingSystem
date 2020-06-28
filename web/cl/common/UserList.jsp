<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="resoft.common.domain.*" %>
<%@ taglib uri="/webwork" prefix="ww"%>

<html>
    <head>
        <title>Untitled Document</title>
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
        <br/>
        <table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
                <td bgcolor="#3685CB" width="100">
                <div align="center" class="tt"><font color="#FFFFFF">用户管理</font></div></td>
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
        <form name="form1" method="post" action="removeUser.action">
            <table width="90%"  border="0" align="center" cellspacing="1" bgcolor="#999999">
                <tr bgcolor="#eeeeee">
                    <td width="13%"><div align="center">选择</div></td>
                    <td width="11%"><div align="center">登录代码</div></td>
                    <td width="14%"><div align="center">用户姓名</div></td>
                    <td width="18%"><div align="center">所属角色</div></td>
                    <td width="23%"><div align="center">所属机构</div></td>
                    <td width="10%"><div align="center">用户权限</div></td>
                    <td width="11%"><div align="center">修改</div></td>
                </tr>    
                <ww:iterator value="userList">
                    <tr bgcolor="ffffff" align="center">
                        <td><div align="center">
                                <input type="checkbox" name="id<ww:property value="code" />" value="checkbox">
                        </div></td>
                        <td><ww:property value="code" /></td>
                        <td><ww:property value="name" /></td>
                        <td><ww:property value="role.name" /></td>
                        <td><ww:property value="org.orgName" /></td>
                        <td align="center">	
                            <a href="ShowPermission.action?code=<ww:property value="code" />">修改</a>	
                        </td>
                        <td align="center">	
                            <a href="ToModifyUser.action?code=<ww:property value="code" />">修改</a>	
                        </td>
                    </tr>
                </ww:iterator>
            </table>  
            <table width="90%"  border="0" align="center">
                <tr>
                    <td width="20%">&nbsp;</td>
                    <td width="73%">&nbsp;</td>  
                </tr>
                <tr>
                    <td><a href="ToAddUser.action"><img src="../images/add.gif" width="60" height="20" border="0"></a></td>
                    <td><input name="imageField" type="image" src="../images/remove.gif" width="60" height="20" border="0"></td>
                    <td>&nbsp;</td>
                </tr>
            </table>
        </form>
    </body>
</html>
