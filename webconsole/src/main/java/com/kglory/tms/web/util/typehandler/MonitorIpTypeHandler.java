/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util.typehandler;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.util.IpUtil;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
@MappedJdbcTypes(JdbcType.NUMERIC)
public class MonitorIpTypeHandler extends BaseTypeHandler<String> {
    private static Logger	logger	= LoggerFactory.getLogger(MonitorIpTypeHandler.class);

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
        try {
            String ip = rs.getString(columnName);
            if (IpUtil.isIPv6Address(ip)) {
                rtn = ipv6Conversion(ip);
            } else {
                long longIP = rs.getLong(columnName);
                rtn = ipv4Conversion(longIP);
            }
        } catch(BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch(Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return rtn;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String rtn = "";
        try {
            String ip = rs.getString(columnIndex);
            if (IpUtil.isIPv6Address(ip)) {
                rtn = ipv6Conversion(ip);
            } else {
                long longIP = rs.getLong(columnIndex);
                rtn = ipv4Conversion(longIP);
            }
        } catch(BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch(Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return rtn;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String rtn = "";
        try {
            String ip = cs.getString(columnIndex);
            if (IpUtil.isIPv6Address(ip)) {
                rtn = ipv6Conversion(ip);
            } else {
                long longIP = cs.getLong(columnIndex);
                rtn = ipv4Conversion(longIP);
            }
        } catch(BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch(Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return rtn;
    }

    private String ipv4Conversion(long ip) throws SQLException, BaseException {
        if (ip == -5000000000L) {
            return String.valueOf(ip);
        } else {
            return IpUtil.getHostByteOrderIpToString(ip);
        }
    }

    private String ipv6Conversion(String ip) throws SQLException {
        return ip;
    }
}
