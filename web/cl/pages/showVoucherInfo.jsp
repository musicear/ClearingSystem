
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="resoft.common.domain.*" %>
<%@ taglib uri="/webwork" prefix="ww"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>无标题文档</title>
<link href="../stylesheets/common.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.STYLE2 {
	font-family: "宋体";
	font-size: 14.25px;
}
-->
</style>
</head>

<body>
 <form name="frm" method="post"  action="../action/SubmitProveInfo.action" >
 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="top"  style="background:url(../images/right_bg.gif) bottom repeat-x"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="43" style="background:url(../images/title.gif) left no-repeat" class="title_zi"><p>付款回单打印 </p>
          <p align="center">XXXX 银行（信用社）电子缴税付款凭证 <br>
            转帐日期 2006年06月28日 凭证字号：0123456789</p>          </td>
      </tr>
    </table>
      <table width="743" border="1">
        <tr>
          <td colspan="5" class="STYLE2">纳税人全称及纳税人识别号：<ww:property   value="TaxPayCode" /> &nbsp;<ww:property   value="TaxPayName" /></td>
          </tr>
        <tr>
          <td width="177" class="STYLE2">付款人全称 </td>
          <td width="112">&nbsp;<ww:property   value="HandOrgName"/></td>
          <td colspan="2" class="STYLE2">征收机关名称</td>
          <td>&nbsp;<ww:property   value="TaxOrgName" /></td>
        </tr>
        <tr>
          <td class="STYLE2">付款人帐号</td>
          <td>&nbsp;<ww:property   value="PayAcct" /></td>
          <td colspan="2" class="STYLE2">收款国库（银行）名称</td>
          <td width="206">&nbsp;<ww:property   value="PayeeName" /></td>
        </tr>
        <tr>
          <td class="STYLE2">小写金额（合计）</td>
          <td>&nbsp;<ww:property   value="TraAmt" /> </td>
          <td colspan="2" class="STYLE2">缴款书交易流水号</td>
          <td>&nbsp;<ww:property   value="TraNo" /></td>
        </tr>
        <tr>
          <td class="STYLE2">大写金额（合计）</td>
          <td>&nbsp;<ww:property   value="TraAmtChinese" /> </td>
          <td colspan="2" class="STYLE2">税票号码</td>
          <td>&nbsp;<ww:property   value="TaxVouNo" /> </td>
        </tr>
        <tr>
          <td class="STYLE2">税（费）种名称</td>
          <td colspan="2" class="STYLE2">所属日期 </td>
          <td colspan="2" class="STYLE2">实缴金额</td>
        </tr>
        <tr>
          <td>&nbsp;	<ww:property   value="TaxTypeName" /></td>
          <td>&nbsp;<ww:property   value="TaxStartDate" /> </td>
          <td width="133">&nbsp;<ww:property   value="TaxEndDate" /> </td>
          <td colspan="2">&nbsp;<ww:property   value="TaxTypeAmt" /></td>
          </tr>
        <tr>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td colspan="2">&nbsp; </td>
          </tr>
        <tr>
          <td colspan="5" class="STYLE2">第 次打印                     打印时间</td>
          </tr>
      </table>
      <p>
        <input type="submit" name="Submit" value=" 确 定 " class="submit" style="margin-left:34px" >
      </p> </td>
  </tr>
</table>
</form>
</body>
</html>
