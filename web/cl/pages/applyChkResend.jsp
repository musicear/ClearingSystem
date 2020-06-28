<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="resoft.common.domain.*,java.util.*" %>

<%@ taglib uri="/webwork" prefix="ww"%>
<script language=javascript>
    function go() {
    	if (form1.OriChkDate.value=="") {
        	alert("日期不能为空！");
            document.form1.OriChkDate.focus();
            return false;
        }
    	if (form1.OriChkAcctOrd.value=="") {
        	alert("对账批次不能为空！");
            document.form1.OriChkAcctOrd.focus();
            return false;
        }        
            }
</script>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
    <title>无标题文档</title>
    <link href="../stylesheets/common.css" rel="stylesheet" type="text/css"/>
    <link href="../stylesheets/calendar.css" rel="stylesheet" type="text/css">
		<script language="javascript" type="text/javascript" src="../javascripts/browserSniffer.js"></script>
		<script language="javascript" type="text/javascript" src="../javascripts/Calendar.js"></script>
</head>


<body>
<form name="form1" method="post" action="../action/ApplyChkResend.action" onsubmit="return(go());">
    <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td align="left" valign="top" style="background:url(../images/right_bg.gif) bottom repeat-x">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">
                        申请对账文件重发     </td>
                    </tr>
                </table>
                <table width="75%" border="0" cellpadding="0" cellspacing="0" class="list"
                       style="margin-left:33px;margin-top:34px">
                    <tr>
                        <th>日期</th>
                        <td align="left">
                            <input name="OriChkDate" readonly type="text" id="OriChkDate" maxlength="15">
    			<script language="JavaScript" type="text/javascript">

            function Date_callback(date, month, year)
            {
                if (String(month).length == 1) {
                    month = '0' + month;
                }
                if (String(month).length == 2) {
                    month = '' + month;
                }
                
                if (String(date).length == 1) {
                    date = '0' + date;
                }
                if (String(date).length == 2) {
                    date = '' + date;
                }
                
                
                document.forms['form1'].OriChkDate.value = year +  month +  date;               
            }
			dynCalendar_setImagesPath("../images/");
            calendar1 = new dynCalendar('calendar1', 'Date_callback');
            
            calendar1.setMonthCombo(true);
            calendar1.setYearCombo(true);
      
            </script>
                        </td>
                         <td>*对账日期不能为空</td>
                        <td>&nbsp;</td>
                    </tr>           
                    <tr>
                        <th>对账批次</th>
                        <td align="left">
                            <input name="OriChkAcctOrd" type="text" id="OriChkAcctOrd" maxlength="20">

                        </td>
                           <td>*对账批次不能为空，且输入的对账批次（序号）应为日切时的批次</td>
                        <td>&nbsp;</td>
                    </tr>

                </table>
                <p>
                    <input type="submit" name="Submit" value=" 确 定 " class="submit" style="margin-left:34px">
                    <input type="reset" name="Submit22" value=" 重 置 " class="submit">                    
                    <input type="button" name="Submit22" value=" 返 回 " class="submit"
                           onClick="javascript:self.location.href='../common/welcome.jsp'">
                </p></td>
        </tr>
    </table>
</form>
</body>
</html>
