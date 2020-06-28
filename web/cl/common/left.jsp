<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../stylesheets/menuHTC.css" rel="stylesheet" type="text/css">
<script>

	
	function ToggleNav()
	{
		if(divCollapsedNav.style.display == "none")
		{
			divCollapsedNav.style.display = "";
			menuContainer.style.display = "none";
			parent.frmstOuter.cols = "18,*"
			parent.document.all ('leftFrame').noResize = true;
parent.document.all ('leftFrame').framespacing= 0;
		}
		else
		{
			menuContainer.style.display = "";
			divCollapsedNav.style.display = "none";
			parent.frmstOuter.cols = "200,*"
			parent.document.all ('leftFrame').noResize = false;
		}
	}
var firstId=null;
</script>

</head>
<body ondblclick="ToggleNav();">

<div id=menuContainer><!---第1个菜单---> 

	<ww:iterator value="grantedModules" id="top" >
	<dl>
		<dt>
		<div><ww:property value="name" /></div>
		<span></span></dt>
		<dd>
		<ul>
			<ww:iterator value="top.childModules">
					<li><a href='<ww:property value="location" />'
						target="mainFrame"><ww:property value="name" /></a></li>
			</ww:iterator>
		</ul>
		</dd>
	</dl>
	</ww:iterator>
</div>

<div id="divCollapsedNav" class="navTocColor"
	style="display:none;width:100%;height:100%;"><a
	href="javascript:ToggleNav();" title="展开导航框架" id="linkNavClosed"><img
	src="../images/toc2.gif" alt="展开导航框架" border="0"></a></div>
</body>
</html>
