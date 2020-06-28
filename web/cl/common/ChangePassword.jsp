<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/webwork" prefix="ww"%>
<html>
    <head>
        <title>J1201Input</title>
        <link href="../stylesheets/Normal.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312"></head>
    <body bgcolor="#ffffff" leftmargin="0" topmargin="0">
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
                    <div align="center" class="tt"><font color="#FFFFFF">修改密码</font></div>
                </td>
                <td>&nbsp;</td>
                <td>&nbsp;    </td>
            </tr>
        </table>
        <table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
                <td background="../images/obj_8.gif"><img src="../images/obj_8.gif" width="1" height="4"></td>
            </tr>
        </table>
        <script language=javascript>
function go() {
	if(frm.oldPassword.value=="") {
		alert("请输入原密码");
		return false;
	}
	if(frm.newPassword.value=="") {
		alert("请输入新密码");
		return false;
	}
	if(frm.newPassword.value!=frm.confirmPassword.value) {
		alert("确认密码与新密码不符");
		return false;
	}
	return true;
}
        </script>
        <form action="ChangePassword.action" onsubmit="return(go());" name="frm">
            <table width="90%"  border="0" align="center" cellspacing="1" cellpadding="0" bgcolor="#999999">
                <tr>
                    <td width="13%" bgcolor="#EEEEEE" class="tt">
                        <div align="center"><span class="style1">原 密 码</span></div>
                    </td>
                    <td width="59%" bgcolor="#FFFFFF">
                        <input name="oldPassword" type="password" id="oldPassword" maxlength="10" style="border-width: 1px,1px,1px,1px;border: 1px solid">
                    </td>
                    <td width="28%" bgcolor="#FFFFFF">*</td>
                </tr>
                <tr>
                    <td bgcolor="#EEEEEE" class="tt" width="13%">
                        <div align="center"><span class="style1">新 密 码</span></div>
                    </td>
                    <td bgcolor="#FFFFFF" width="59%">
                        <input name="newPassword" type="password" id="newPassword" maxlength="10" style="border-width: 1px,1px,1px,1px;border: 1px solid">
                    </td>
                    <td bgcolor="#FFFFFF" width="28%">*</td>
                </tr>
                <tr>
                    <td bgcolor="#EEEEEE" class="tt" width="13%">
                        <div align="center"><span class="style1">确 认 密 码</span></div>
                    </td>
                    <td bgcolor="#FFFFFF" width="59%">
                        <input name="confirmPassword" type="password" id="confirmPassword" style="border-width: 1px,1px,1px,1px;border: 1px solid" value="" maxlength="10">
                    </td>
                    <td bgcolor="#FFFFFF" width="28%">*</td>
                </tr>
            </table>  
            <div align="center"><br>
                <table width="60" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td height="20" background="../images/obj_9.gif">
                            <div align="center" class="tt">
                                <input name="imageField" type="image" src="../images/ok.gif" width="60" height="20" border="0">
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
        
    </body>
</html>
