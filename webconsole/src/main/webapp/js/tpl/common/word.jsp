<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String title = request.getParameter("title");
	String content = request.getParameter("content");
	String pageMode = request.getParameter("pageMode");
	
	String strTitle = URLDecoder.decode(title,"UTF-8");
	String strTitle2 = new String(strTitle.getBytes("KSC5601"),"8859_1");
	
	String strContent = URLDecoder.decode(content,"UTF-8");
	String strContent2 = new String(strContent.getBytes("KSC5601"),"8859_1");
        
        String strWidth = "595.35pt";
        String strHeight = "841.95pt";
//        page mode horizontal , vertical
        if (pageMode != null && pageMode.equals("horizontal")) {
            strHeight = "595.35pt";
            strWidth = "841.95pt";
        }
	
	response.reset();
	response.setContentType("application/octet-stream");
	String headerData = "attachment; filename=\"" + strTitle2 + ".doc\";";
	headerData = headerData.replaceAll("\n", " ").replaceAll("\r", " ");
	response.setHeader("Content-Disposition", headerData);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Html</title>
<style type="text/css">
        
	@page PageOrientation {
		size: <%=strWidth%> <%=strHeight%>;mso-page-orientation: landscape;
	}
	div.PageOrientation {page: PageOrientation;}	
        
	*    { PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-LEFT: 0px; PADDING-RIGHT: 0px; PADDING-TOP: 0px; }
	HTML { WIDTH: 100%; HEIGHT: 100%; padding:0; margin:0;}
	BODY { WIDTH: 100%; HEIGHT: 100%; padding:0; margin:0;}
	HTML { OVERFLOW-Y: scroll; OVERFLOW-X: hidden; }
	BODY { BACKGROUND-COLOR: #fff; WORD-BREAK: break-all; }
	BODY { LINE-HEIGHT: 18px; FONT-FAMILY: "돋움", Dotum, Helvetica, AppleGothic, Sans-serif; COLOR: #626262; FONT-SIZE: 11px; }
	h2   { padding:5px;}
	TABLE { width: 100%;cell-padding: 0px; cell-spacing: 0px !important; border: 0px;}
	
	
	table.data > tbody > tr:nth-child(odd) {background-color:#f1f1f1 !important;}
	table.data > tbody > tr:nth-child(odd) > td:nth-child(odd) {background-color:#e9e9e9 !important;}
	table.data > tbody > tr:nth-child(even) > td:nth-child(odd) {background-color:#f1f1f1 !important;}
	tr {border-bottom:1px solid #ccc;}
	table.data > tbody > tr > td {border:1px solid #ccc;}
	table.data > tbody > tr > td {padding: 10px;}
	
	.ref_box {
		margin-top: 20px;
		padding: 10px;
		border: #ccc 1px solid;
	}
	
	
	.export-title {
		width: 100%;
		padding: 10px;
		font-weight: bold;
		font-size: 18px;
		text-align: center;
		background: #333;
		color: #fff;
		}
						
	.search-title { 
		width: 100px;
		font-size: 12px;
		font-weight:bold;
		background-color: #e9e9e9;
		text-align: center;
		}							
	
	.search-label { 
		font-size: 12px;
		font-weight:bold;
		padding-right: 20px;
		}								
	
	.search-content { 
		font-size: 12px;
		}
					
							
	/* 리스트 변화량 */	
	.total-table-data {  
		width: 100px;
		margin:0px; 
		font-weight: bold; 
		padding: 4px 3px; 
		text-align: center; 
		line-height: 1.5em;  
		word-break:break-all;
		}
	
	/* 리스트 테이블 헤더 */	
	.content-table-header { 
		text-align: center;
		font-weight: bold;
		font-size: 12px;
		padding: 10px;
		background-color: #E0E0E0;
		}
							
	.content-table-data {
		padding: 10px;
		word-break:break-all; 
		background-color: #E0E0E;
		border-bottom: #ccc 1px solid;
		border-left: #eee 1px solid;
		}
							
	.align-left 	{ text-align: left; }
	.align-right 	{ text-align: right; }
	.align-center 	{ text-align: center;  }
	.num { text-align: right;}
	.width-100 {width:100px;}

</style>
</head>
<body>
<div class="PageOrientation">
	<%=strContent2.replaceAll("<","") %>
</div>
	
</body>
</html>

