<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.io.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String originFileName = request.getParameter("fileName");
	String originContent = request.getParameter("content");
	
	String fileName1 = URLDecoder.decode(originFileName,"UTF-8");
	String fileName = new String(fileName1.getBytes("KSC5601"),"8859_1");
	
	String content1 = URLDecoder.decode(originContent,"UTF-8");
	String content = new String(content1.getBytes("KSC5601"),"8859_1");
	
	response.reset();
	response.setContentType("app/octet-stream");
	String headerData = "attachment; filename=\"" + fileName + ".csv\";";
	headerData = headerData.replaceAll("\n", " ").replaceAll("\r", " ");
	response.setHeader("Content-Disposition", headerData);
	
	PrintWriter outx = response.getWriter();
	outx.println(content.replaceAll("<",""));
	outx.flush();
	outx.close();

%>



