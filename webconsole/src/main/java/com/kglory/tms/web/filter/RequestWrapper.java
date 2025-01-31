package com.kglory.tms.web.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read Request Body in Filter Is it possible to intercept HTTP Request in order to read HTTP
 * Request Body (HTTP Request Payload) before it is served by target servlet or JSP?
 * 
 * The answer is yes. You can use Servlet Filter to read, "filter" or modify ServletRequest
 * (HTTPServletRequest) object before the request object is sent to and serve by the final servlet.
 * 
 * What if I read a request body within a servlet filter, is the body still going to be available to
 * be read again by the servlet, or can it be read only once?
 * 
 * Yes, but it is a little bit tricky. Because of the fact that the request body can be read only
 * once. If you read the body in a filter, the target servlet will not be able to re-read it and
 * this will also cause IllegalStateException. You will need ServletRequestWrapper or its child:
 * HttpServletRequestWrapper so that you can read HTTP request body and then the servlet can still
 * read it later.
 * 
 * The first step is to create a class that extends HttpServletRequestWrapper. Then, use the
 * constructor to read HTTP Request body and store it in "body" variable. The final step is to
 * override getInputStream() and getReader() so that the final servlet can read HTTP Request Body
 * without causing IllegalStateException.
 * 
 * @author idess
 * 
 */
public class RequestWrapper extends HttpServletRequestWrapper {
	private final String	BODY;
	private static Logger logger = LoggerFactory.getLogger(RequestWrapper.class);
	
	public RequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
		BODY = cleanXSS(stringBuilder.toString());
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(BODY.getBytes("UTF-8"));
		ServletInputStream servletInputStream = new ServletInputStream() {
			ReadListener readListener;
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				this.readListener = readListener;
			}
		};
		return servletInputStream;
	}
	
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	}
	
	public String getBody() {
		return this.BODY;
	}
	
	public String[] getParameterValues(String parameter) {

	  String[] values = super.getParameterValues(parameter);
	  if (values == null)  {
              return null;
      }
	  int count = values.length;
	  String[] encodedValues = new String[count];
	  for (int i = 0; i < count; i++) {
		  encodedValues[i] = cleanXSS(values[i]);
	  }  
	  return encodedValues; 
	}
	
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null) {
			return null; 
		}
		return cleanXSS(value);
	}
	
	public String getHeader(String name) {
	    String value = super.getHeader(name);
	    if (value == null)
	        return null;
	    return cleanXSS(value);
	}

	private String cleanXSS(String value) {
//		logger.debug("cleanXSS 1>>>>>>>>>>>>>>>>>>> "+ value);
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("/cookie/gi", "cook1e");
		value = value.replaceAll("/document/gi", "d0cument");
		value = value.replaceAll("/script/gi", "scr1pt");
		value = value.replaceAll("/javascript/gi", "javascr1pt");
//		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
//		value = value.replaceAll("'", "&#39;");
//		value = value.replaceAll("\"", "&#34;");
//		value = value.replaceAll("eval\\((.*)\\)", "");
//		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
//		value = value.replaceAll("script", "");
//		logger.debug("cleanXSS 2>>>>>>>>>>>>>>>>>>> "+ value);
		return value;
	}
}