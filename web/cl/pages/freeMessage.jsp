<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="resoft.common.domain.*" %>
<%@ taglib uri="/webwork" prefix="ww"%>

<html>
    <head>
        <title>Untitled Document</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../stylesheets/Normal.css" rel="stylesheet" type="text/css">
		<link  href="../stylesheets/calendar.css" rel="stylesheet" type="text/css" >
		<script language="javascript" type="text/javascript" src="../javascripts/browserSniffer.js"></script>
		<script language="javascript" type="text/javascript" src="../javascripts/Calendar.js"></script>
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
                <div align="center" class="tt"><font color="#FFFFFF">冲正查询</font></div></td>
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
                    <div align="center">所有机构</div></td>
                    <td width="46%" bgcolor="#FFFFFF">
                        <input name="column1" type="hidden" value="">
                        <input name="operator1" type="hidden" value="equals" >
                        <input name="value1" type="text" id="Id" maxlength="18" style="border-width: 1px,1px,1px,1px;border: 1px solid">
                    </td>
                    <td width="28%" bgcolor="#FFFFFF"></td>
                </tr>      
  <tr>
      <td  width="26%" bgcolor="#EEEEEE" class="tt"> 
      <div align="center" class="style2">发送日期</div></td>
            
     <td bgcolor="#EEEEEE">
	      <input name="column2" type="hidden" value="001">
          <input name="operator2" type="hidden" value="equals" >
	      <input name="value2" type="text"  value="" >
            <script language="JavaScript" type="text/javascript">
            <!--
            /**
            * Example callback function
            */
            function oriEntrustDate_callback(date, month, year)
            {
                if (String(month).length == 1) {
                    month = '0' + month;
                }

                if (String(date).length == 1) {
                    date = '0' + date;
                }
                document.forms['form1'].value2.value = year +  month +  date;               
            }
			dynCalendar_setImagesPath("../images/");
            calendar1 = new dynCalendar('calendar1', 'oriEntrustDate_callback');
            
            calendar1.setMonthCombo(true);
            calendar1.setYearCombo(true);
            //-->
            </script>
          
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
