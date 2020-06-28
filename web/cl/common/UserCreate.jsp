<%@ page contentType="text/html; charset=UTF-8" %>
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
                    <div align="center" class="tt"><font color="#FFFFFF">新增用户</font></div>
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
        <form name="form1" method="post" action="AddUser.action">
            
            <table width="90%"  border="0" align="center" cellspacing="1" cellpadding="0" bgcolor="#999999">
                
                <tr>
                    <td width="26%" bgcolor="#EEEEEE" class="tt">
                    <div align="center">登 录 代 码</div></td>
                    <td width="46%" bgcolor="#FFFFFF">
                        <input name="code" type="text" id="Id" maxlength="18" style="border-width: 1px,1px,1px,1px;border: 1px solid">
                    </td>
                    <td width="28%" bgcolor="#FFFFFF">*</td>
                </tr>
                <tr>
                    <td bgcolor="#EEEEEE" class="tt" width="26%">
                    <div align="center">用 户 姓 名</div></td>
                    <td bgcolor="#FFFFFF" width="46%">
                        <input name="name" type="text" id="Name" maxlength="18" style="border-width: 1px,1px,1px,1px;border: 1px solid">
                    </td>
                    <td bgcolor="#FFFFFF" width="28%">* </td>
                </tr>
                <tr>
                    <td bgcolor="#EEEEEE" class="tt" width="26%">
                    <div align="center">登 录 密 码</div></td>
                    <td bgcolor="#FFFFFF" width="46%">
                        <input name="password" type="password" id="password" maxlength="18" style="border-width: 1px,1px,1px,1px;border: 1px solid">
                    </td>
                    <td bgcolor="#FFFFFF" width="28%">* </td>
                </tr>   
                <tr>
                    <td bgcolor="#EEEEEE" class="tt" width="26%">
                    <div align="center">所 属 角 色</div></td>
                    
                    <td bgcolor="#FFFFFF" width="46%"><select name="id"><ww:iterator value="roleList">
                                <option value="<ww:property value="id" />"><ww:property value="name" /></option>
                    </ww:iterator>  </select></td>
                    
                    <td bgcolor="#FFFFFF" width="28%">* </td>
                </tr>
                
                <tr>
                    <td bgcolor="#EEEEEE" class="tt" width="26%">
                    <div align="center">所 属 机 构</div></td>
                    <td bgcolor="#FFFFFF" width="46%"><select name="orgCode"><ww:iterator value="orgList">
                                <option value="<ww:property value="orgCode" />"><ww:property value="orgName" /></option>
                    </ww:iterator>  </select></td>                    
                    <td bgcolor="#FFFFFF" width="28%">* </td>
                </tr>
                
            </table>
            <div align="center"><br>
                <table width="50%"  border="0">
                    <tr>
                        <td><div align="center"><span class="tt">
                                    <input name="imageField" type="image" src="../images/ok.gif" width="60" height="20" border="0">
                        </span></div></td>
                        <td><div align="center"><a href="UserList.action"><img src="../images/return.gif" width="60" height="20" border="0"></a></div></td>
                    </tr>
                </table>
            </div>
        </form>
    </body>
</html>