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
 * Host Byte Order 방식으로 IP를 long type과 String type으로 변환해준다.
 */
@MappedJdbcTypes(JdbcType.NUMERIC)
public class HostByteOrderIpTypeHandler extends BaseTypeHandler<String> {

    private static Logger	logger	= LoggerFactory.getLogger(HostByteOrderIpTypeHandler.class);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter.equals("-5000000000")) {
            ps.setLong(i, Long.parseLong(parameter));
        } else if (parameter.equals("-1")) {
            ps.setLong(i, Long.parseLong(parameter));
        } else {
            ps.setLong(i, IpUtil.getHostByteOrderIpToLong(parameter));
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String rtn = "";
        String ip = rs.getString(columnName);
        if (ip != null) {
            if (IpUtil.isIPv6Address(ip)) {
                rtn = ip;
            } else {
                long longIP = rs.getLong(columnName);
                try {
					rtn = ipv4Conversion(longIP);
				} catch (BaseException e) {
					logger.error(e.getLocalizedMessage());
				}
            }
        }
        return rtn;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String rtn = "";
        String ip = rs.getString(columnIndex);
        if (ip != null) {
            if (IpUtil.isIPv6Address(ip)) {
                rtn = ip;
            } else {
                long longIP = rs.getLong(columnIndex);
                try {
					rtn = ipv4Conversion(longIP);
				} catch (BaseException e) {
					logger.error(e.getLocalizedMessage());
				}
            }
        }
        return rtn;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String rtn = "";
        String ip = cs.getString(columnIndex);
        if (ip != null) {
            if (IpUtil.isIPv6Address(ip)) {
                rtn = ip;
            } else {
                long longIP = cs.getLong(columnIndex);
                try {
					rtn = ipv4Conversion(longIP);
				} catch (BaseException e) {
					logger.error(e.getLocalizedMessage());
				}
            }
        }
        return rtn;
    }

    private String ipv4Conversion(long ip) throws SQLException, BaseException {
        if (ip == -5000000000L) {
            return String.valueOf(ip);
        } else if (ip == -1L) {
            return String.valueOf("TOTAL");
        } else {
            return IpUtil.getHostByteOrderIpToString(ip);
        }
    }

    private String ipv6Conversion(String ip) throws SQLException, BaseException{
        return IpUtil.hexStringToIpv6(ip);
    }
}
