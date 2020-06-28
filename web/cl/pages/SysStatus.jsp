
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="resoft.common.domain.*" %>
<%@ taglib uri="/webwork" prefix="ww"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>无标题文档</title>
<link href="../stylesheets/common.css" rel="stylesheet" type="text/css" />
</head>

<body>
 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="top"  style="background:url(../images/right_bg.gif) bottom repeat-x"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">系统状态查看</td>
      </tr>
    </table>
      <table width="80%" border="0" cellpadding="0" cellspacing="0" class="list" style="margin-left:33px;margin-top:34px">
        <tr>
          <th width="30%">登录TIPS状态</th>
		          <ww:if test="sysStatus.loginStatus==0">
                        <td bgcolor="ffffff" >已登录</td>
                    </ww:if>
                    <ww:if test="sysStatus.loginStatus==1">
                        <td bgcolor="ffffff">未登录</td>
                    </ww:if>
        </tr>

        <tr>
          <th>工作日期</th>
          <td align="left"><ww:property value="sysStatus.workDate" /> </td>
          <td align="center">&nbsp;</td>
        </tr>
		        <tr>
          <th>当前系统状态</th>
          <ww:if test="sysStatus.SysStatus==0">
                        <td bgcolor="ffffff" >日间</td>
                    </ww:if>
                    <ww:if test="sysStatus.SysStatus==1">
                        <td bgcolor="ffffff">日切</td>
                    </ww:if>
                    <ww:if test="sysStatus.SysStatus==2">
                        <td bgcolor="ffffff">系统维护状态</td>
                    </ww:if>  
          <td>&nbsp;</td>
        </tr>
      </table>
	  </td>
  </tr>
</table>
</body>
</html>
