<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"
	href="./themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="./themes/icon.css">
<script type="text/javascript" src="./js/jquery.min.js"></script>
<script type="text/javascript" src="./js/jquery.easyui.min.js"></script>
<style type="text/css">
body {
	height: 100%;
	width: 100%;
	margin: 0px;
	padding: 0px;
	overflow: hidden;
}
</style>
<script type="text/javascript">
var wsdlLocation="http://www.webxml.com.cn/WebServices/WeatherWS.asmx?wsdl";

$(document).ready(function() {
	var json=[{    
	    "id":1,    
	    "text":"Folder1",    
	    "iconCls":"icon-save",    
	    "children":[{    
	        "text":"File1",    
	        "checked":true   
	    },{    
	        "text":"Books",    
	        "state":"open",    
	        "attributes":{    
	            "url":"/demo/book/abc",    
	            "price":100    
	        },    
	        "children":[{    
	            "text":"PhotoShop",    
	            "checked":true   
	        },{    
	            "id": 8,    
	            "text":"Sub Bookds",    
	            "state":"closed"   
	        }]    
	    }]    
	},{    
	    "text":"Languages",    
	    "state":"closed",    
	    "children":[{    
	        "text":"Java"   
	    },{    
	        "text":"C#"   
	    }]    
	}] ;
	$('#wsdlTree').tree({
		//data:json,
		url : './ServiceCallServlet?type=tree&wsdlLocation='+wsdlLocation,
		method : 'POST',
		lines : true,
		loadFilter : function(data, parent) {
			return data;
		},
		onClick : function(node) {
			if (node.children) {//如果服务节点 显示整个服务的详情
				serverName = node.text;
			    console.log(node.doc);
			} else {//如果方法节点  显示这个方法的详情
				methodName = node.text;
				serverName = node.parentName;
			}
			var doc=decodeURIComponent(node.doc);
			if(doc&&doc!="undefined"&&doc!="null"){
				while(doc.indexOf("+")>-1){
					doc=doc.replace("+"," ");
				}
				$("#content").html(doc);
			}
		}
	});
});
</script>
</head>
<body>
	<div class="easyui-layout" style="width: 100%; height: 900px">
		<div region="west" title="WSDL解析" style="width: 20%;">
			<ul id="wsdlTree" class="easyui-tree"></ul>
		</div>
		<div id="content" region="center" title="" style="padding: 0px;">
		</div>
	</div>
</body>
</html>