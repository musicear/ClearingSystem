<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="resoft.common.domain.*" %>
<%@ taglib uri="/webwork" prefix="ww"%>
<html>
<head>
  <script language=javascript>
    function go() {
    var payFlag=form1.payFlag.value;
    var payAcct=form1.payAcct.value;
    if(payFlag=="0")
        document.form1.payFlag.value=="";
    else{
       if(payAcct==""){
          alert("选择转帐时，帐号不能为空！");
          document.form1.payAcct.focus();
          return false;
       }
    }
    }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>申报查询</title>
<link href="../stylesheets/common.css" rel="stylesheet" type="text/css" />
<link  href="../stylesheets/calendar.css" rel="stylesheet" type="text/css" >
<script language="javascript" type="text/javascript" src="../javascripts/browserSniffer.js"></script>
<script language="javascript" type="text/javascript" src="../javascripts/Calendar.js"></script>
</head>
<body>
<form name="form1" method="post" action="../action/DeclarePayConfirm.action" onsubmit="return(go());">
<input type="hidden" name="taxVouNo" value="<ww:property  value="taxVouNo" />" />
<input type="hidden" name="triTraNo" value="<ww:property  value="traNo" />" />
<input type="hidden" name="oriEntrustDate" value="<ww:property  value="entrustDate" />" />
<input type="hidden" name="oriTaxOrgCode" value="<ww:property  value="taxOrgCode" />" />
<input type="hidden" name="declareFlag" value="1" />
 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="top"  style="background:url(../images/right_bg.gif) bottom repeat-x"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi">银行申报查询返回信息</td>
      </tr>
    </table>
      <table width="80%" border="0" cellpadding="0" cellspacing="0" class="list" style="margin-left:33px;margin-top:34px">
        <tr>
         <td align="left" width="50%">纳税人编码:<ww:property value="taxPayCode" /></td>
          <td align="left" width="50%">征收机关代码<ww:property value="taxOrgCode" /> </td>
        </tr> 
        <tr>
          <td align="left" width="50%">付款单位名称<ww:property value="handOrgName" /></td>
          <td align="left" width="50%">&nbsp;
          </td>
        </tr> 
        <tr>
          <td align="left" width="50%">付款方式<select name=payFlag>
                                              <option value="0">现金</option>
                                              <option value="1">转帐</option>
                                              </select>
          </td>
          <td align="left" width="50%">付款帐号<input name="payAcct" type="text" id="TaxPayCode" maxlength="30" value=""></td>
        </tr> 
        <ww:iterator value="typeList">
        <tr>
         <td align="left" width="50%">税款所属启始日期:<ww:property value="taxEndDate" /></td>
          <td align="left" width="50%">税款所属截止日期<ww:property value="taxEndDate" /> </td>
        </tr>
        <tr>
         <td align="left" width="50%">税款类型:<ww:property value="taxType" /></td>
          <td align="left" width="50%">税款名称<ww:property value="taxTypeName" /> </td>
        </tr>
        <tr><td>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list" >
        <tr><td width="20%">税种</td><td width="20%">税目</td><td width="20%">课税数量</td><td width="20%">计税金额</td><td width="20%">实缴金额</td></tr>
        <ww:iterator value="taxSubjectList">
        <tr>
        <td><ww:property value="taxTypeCode" /></td>
        <td><ww:property value="taxSubjectCode" /></td>
        <td><ww:property value="taxNumber" /></td>
        <td><ww:property value="taxAmt" /></td>
        <td><ww:property value="factTaxAmt" /></td>
        </tr>
        </ww:iterator>
        </table>
        </td>
        </tr>
      </ww:iterator>
      </table>
                <p>
                    <input type="submit" name="Submit" value=" 确 定 "  class="submit" style="margin-left:34px">                    
                    <input type="button" name="Submit22" value=" 返 回 " class="submit"
                           onClick="javascript:self.location.href='../common/welcome.jsp'">
                </p>
	  </td>
  </tr>
</table>
</form>
</body>
</html>