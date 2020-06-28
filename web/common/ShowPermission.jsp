<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="resoft.common.domain.*" %>
<%@ taglib uri="/webwork" prefix="ww" %>
<html>
    
    
    <script type="text/javascript">
	function doCheckAll( isChecked ){
		var checkboxs = document.getElementsByName("checkbox_modify");
		var size = checkboxs.length;
		for( i = 0 ; i < size; i++ ){
			checkboxs[i].checked = isChecked;
		}
	}
    </script>
    <head>
        <title>用户权限管理</title>
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
                    <div align="center" class="tt"><font color="#FFFFFF">用户权限管理</font></div>
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
        
        <form name="form1" method="post" action="ModifyPermission.action">  
            <input type="hidden" name="code" value="<ww:property value="code"/>" />
            <table width="80%"  border="0" align="center" cellspacing="1" bgcolor="#999999">
                <tr bgcolor="#ffffff">
                    <td width="18%" align="center">选择</td>
                    <td width="82%">菜单名称</td>
                </tr>
                <ww:iterator value="roles" status="status">
                    <tr bgcolor="#ffffff">
                        <td><div align="center">
                                <input type="checkbox" name="checkbox_modify" value="<ww:property value="module.id"/>" <ww:if test="checked == 'true' "> checked </ww:if> >
                            </div></td>
                        <td><ww:property value="module.name"/></td>
                    </tr>
                </ww:iterator>
            </table>
            <table width="19%"  border="0" align="center">
                <tr>
                    <td>&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><div align="center">
                            <input name="imageField" type="image" src="../images/ok.gif"  border="0">
                    </div></td>
                    <td>&nbsp;</td>
                     <td><div align="center"><a href="UserList.action"><img src="../images/return.gif" width="60" height="20" border="0"></a></div></td>
                </tr>
            </table>
            <p>&nbsp;  </p>
        </form>
        <table align="center" width="80%" border="0" cellspacing="0" cellpadding="0" class=page>
            <tr>
                <td><a href="#" onClick="doCheckAll( true )">全选</a> - <a href="#" onClick="doCheckAll( false )">取消</a></td>
            </tr>
        </table>
        <p>&nbsp;</p>
    </body>
</html>
