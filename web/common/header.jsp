<%@ page import="resoft.common.domain.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>head</title>
        <link rel="stylesheet" href="../stylesheets/top.css">
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
    </head>
    <%
    String username = null;
    String orgname = null;
    String rolename = null;
    String workdate = null;
    String clrbatch = null;
    String state=null;
    User user = ((User) session.getAttribute("user"));
    username = user.getName() + ",";
    rolename = user.getRole().getName();
    orgname = user.getOrg().getOrgName();
    
    %>   
    <body bgcolor="#ffffff" marginheight="0" marginwidth="0" leftmargin="0"
          rightmargin="0" topmargin="0" bottommargin="0">
        <table width="100%" height="100%" border="0" cellpadding="0"
               cellspacing="0" class=top>
            <tr>
                <td class="logo">
                    <div class=subNav><%=rolename%>:<%=username%>  机构:<%=orgname%> 
                        | <a href='../index.jsp' target="_parent">
                    退出</a></div>
                </td>
            </tr>
        </table>  
    </body>
</html>