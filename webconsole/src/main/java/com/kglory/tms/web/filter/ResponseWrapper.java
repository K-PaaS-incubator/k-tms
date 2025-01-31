package com.kglory.tms.web.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResponseWrapper extends HttpServletResponseWrapper {
	private final String	BODY;
	private static Logger logger = LoggerFactory.getLogger(ResponseWrapper.class);
	
	public ResponseWrapper(HttpServletResponse response) throws IOException {
		super(response);
		OutputStream outputStream = response.getOutputStream();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byteArrayOutputStream.writeTo(outputStream);
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		BODY = new String(byteArray, StandardCharsets.UTF_8);
	}
	
	@Override
	public ServletOutputStream getOutputStream() {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			byteArrayOutputStream.write(BODY.getBytes());
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
		ServletOutputStream servletOutputStream = new ServletOutputStream() {
			WriteListener writeListener;
			@Override
			public void write(int b) throws IOException {
				byteArrayOutputStream.write(b);
			}

			@Override
			public void setWriteListener(WriteListener writeListener) {
				// TODO Auto-generated method stub
				this.writeListener = writeListener;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		return servletOutputStream;
	}
	
	public String getBody() {
		return BODY;
	}
}
