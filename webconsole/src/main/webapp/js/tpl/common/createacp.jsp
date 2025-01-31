<%@page import="java.io.IOException"%>
<%@ page import="java.io.BufferedOutputStream"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	BufferedOutputStream bos = null;
	try {
		String fileName = request.getParameter("fileName");
		String byteStr = request.getParameter("content");
		char[] charData = byteStr.toUpperCase().toCharArray();
		byte[] bin = new byte[charData.length / 2 + charData.length % 2];
		
		String lengthStr = "";
		int hex = 0xff;
		int first = bin.length & hex;
		lengthStr += String.format("%02x", first).toUpperCase();
		int second = bin.length >> 8 & hex;
		lengthStr += String.format("%02x", second).toUpperCase();
		int third = bin.length >> 16 & hex;
		lengthStr += String.format("%02x", third).toUpperCase();
		int forth = bin.length >> 24 & hex;
		lengthStr += String.format("%02x", forth).toUpperCase();
		
		String globalHeader = "D4C3B2A1020004000000000000000000FFFF000001000000";
		String timeHeader = "89BC91508ED70A00";
		
		out.clear();
		out = pageContext.pushBody();
		response.reset();
		response.setContentType("application/octet-stream");
		String headerData = "attachment; filename=\"" + fileName + ".acp\";";
		headerData = headerData.replaceAll("\n", " ").replaceAll("\r", " ");
		response.setHeader("Content-Disposition", headerData);
		
		String pcapStr = globalHeader + timeHeader + lengthStr + lengthStr + byteStr;
		char[] pcapCharData = pcapStr.toUpperCase().toCharArray();
		byte[] pcapByte = new byte[pcapCharData.length / 2 + pcapCharData.length % 2];
		for (int i = 0; i < pcapCharData.length; i++) {
			byte ch = (byte) (Character.isDigit(pcapCharData[i]) ? pcapCharData[i] - '0' : pcapCharData[i] - 'A' + 10);
			if (i % 2 == 0) {
				pcapByte[i / 2] = (byte) (ch << 4);
			} else {
				pcapByte[i / 2] |= ch;
			}
		}
		
		bos = new BufferedOutputStream(response.getOutputStream());
		bos.write(pcapByte);
	} catch (IOException e) {
		if (bos != null) {
			bos.close();
		}
	} finally {
		if (bos != null) {
			bos.close();
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>packet download</title>
</head>
<body>

</body>
</html>