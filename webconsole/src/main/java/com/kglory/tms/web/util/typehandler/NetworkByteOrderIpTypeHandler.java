package com.kglory.tms.web.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.util.IpUtil;

/**
 * Network Byte Order 방식으로 IP를 long type과 String type으로 변환해준다.
 */
@MappedJdbcTypes(JdbcType.NUMERIC)
public class NetworkByteOrderIpTypeHandler extends BaseTypeHandler<String> {
	private static Logger	logger	= LoggerFactory.getLogger(NetworkByteOrderIpTypeHandler.class);
	
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
			throws SQLException {
		try {
			if (parameter.equals("-5000000000")) {
				ps.setLong(i, Long.parseLong(parameter));
			} else {
				ps.setLong(i, IpUtil.getNetworkByteOrderIpToLong(parameter));
			}
		} catch (BaseException e) {
			logger.error(e.getLocalizedMessage());
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}
	
	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
		long longIP = rs.getLong(columnName);
		try {
			if (longIP == -5000000000L) {
				return String.valueOf(longIP);
			} else {
				return IpUtil.getNetworkByteOrderIpToString(longIP);
			}
		} catch (BaseException e) {
			logger.error(e.getLocalizedMessage());
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return "";
	}
	
	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		try {
			long longIP = rs.getLong(columnIndex);
			if (longIP == -5000000000L) {
				return String.valueOf(longIP);
			} else {
					return IpUtil.getNetworkByteOrderIpToString(longIP);
			}
		} catch (BaseException e) {
			logger.error(e.getLocalizedMessage());
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return "";
	}
	
	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		long longIP = cs.getLong(columnIndex);
		try {
			if (longIP == -5000000000L) {
				return String.valueOf(longIP);
			} else {
				return IpUtil.getNetworkByteOrderIpToString(longIP);
			}
		} catch (BaseException e) {
			logger.error(e.getLocalizedMessage());
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return "";
	}
	
}
