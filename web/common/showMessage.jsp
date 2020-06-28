<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="ww" uri="/webwork" %>
<html>
<link href="../stylesheets/Normal.css" rel="stylesheet" type="text/css">
<style type="text/css">
    <!--
    body, td, th {
        font-family: 宋 体;
    }

    -->
</style>
<script type="text/javascript" language="javascript">
    function init() {
        var img = document.getElementById("aImg");
        img.focus();
    }
</script>

<body onload="init()">
<DIV align=center>
    <p>&nbsp;</p>

    <TABLE cellSpacing=0 cellPadding=0 width=448 border=0>
        <TBODY>
            <TR>
                <TD width=382 background="../images/tishi_4.gif" height="31">
                    <div align="center">信息提示</div>
                </TD>
            </TR>
            <TR>
                <TD background=../images/tishi_5.gif height="182">
                    <DIV align=center>
                        <table width="90%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td align="center"><ww:property value="message"/></td>
                            </tr>
                            <tr>
                                <td><p>&nbsp;</p>

                                    <p>&nbsp;</p>

                                    <p>&nbsp;</p>

                                    <p>&nbsp;</p></td>
                            </tr>
                            <tr>
                                <td>
                                    <div align="center">
                                        <img src="../images/return.gif" alt="return" onClick="history.back();">
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </DIV>
                </TD>
            </TR>
        </TBODY>
    </TABLE>
    </DIV>
</body>
</html>

