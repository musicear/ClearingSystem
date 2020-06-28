<html>
<body>
<#assign request = ${reportSheet.getProperty("request")}>
<table width="100%"  border="0">
  <#list headParagraphs as p>
  <tr>
    <td align="$p.style.align"  style="font-family:'${p.style.fontFamily}';font-size=${p.style.fontSize}px ">$p.text</td>
  </tr>
  </#list>
</table>

</body>
</html>
