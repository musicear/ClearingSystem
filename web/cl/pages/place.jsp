<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="resoft.common.domain.*" %>
<%@ taglib uri="/webwork" prefix="ww"%>

<html>
    <head>
        <title>Untitled Document</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
                <div align="center" class="tt"><font color="#FFFFFF">查询</font></div></td>
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
        <form name="form1" method="post" action="../dofile/ProcessCondition.do">
            <table width="90%"  border="0" align="center" cellspacing="1" cellpadding="0" bgcolor="#999999">

                <tr>
                    <td width="26%" bgcolor="#EEEEEE" class="tt">
                    <div align="center">代 码</div></td>
                    <td width="46%" bgcolor="#FFFFFF">
                        <input name="column1" type="hidden" value="001">
                        <input name="operator1" type="hidden" value="equals" >
                        <input name="value1" type="text" id="Id" maxlength="18" style="border-width: 1px,1px,1px,1px;border: 1px solid">
                    </td>
                    <td width="28%" bgcolor="#FFFFFF"></td>
                </tr>       
            </table>
            <table width="90%"  border="0" align="center">
                <tr>
                    <td width="20%">&nbsp;</td>
                    <td width="73%">&nbsp;</td>
                </tr>
                <tr>
                    <td><input type="image" src="../images/ok.gif"></td>
                    <td></td>
                    <td>&nbsp;</td>
                </tr>
            </table>
        </form>
    </body>
</html>
