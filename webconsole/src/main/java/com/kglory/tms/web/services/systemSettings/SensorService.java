package com.kglory.tms.web.services.systemSettings;

import com.kglory.tms.web.common.Constants;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.systemSettings.SensorMapper;
import com.kglory.tms.web.model.dto.IpMonitorDto;
import com.kglory.tms.web.model.dto.SensorInboundDto;
import com.kglory.tms.web.model.dto.SensorIntegrityFileDto;
import com.kglory.tms.web.model.dto.SystemConfigDto;
import com.kglory.tms.web.model.vo.EthoNetVO;
import com.kglory.tms.web.model.vo.SensorInboundVO;
import com.kglory.tms.web.model.vo.SensorIntegrityFileVO;
import com.kglory.tms.web.model.vo.SensorVO;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.NumberUtil;
import com.kglory.tms.web.util.StringUtil;
import com.kglory.tms.web.util.SystemUtil;
import com.kglory.tms.web.util.TableFinder;
import com.kglory.tms.web.util.file.FileUtil;
import javax.servlet.http.HttpSession;

@Service
public class SensorService {

    private static Logger logger = LoggerFactory.getLogger(SensorService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    SensorMapper sensorMapper;
    @Autowired
    AuditLogService auditLogSvc;

    /**
     * 센서 목록을 조회한다.
     *
     * @param
     * @return List<SensorVO> 센서 목록 정보 resultList
     */
    public List<SensorVO> selectSensorSettingInfo() throws BaseException {

        List<SensorVO> resultList = null;
        resultList = sensorMapper.selectSensorSettingInfo();

        return resultList;
    }


    /**
     * 센서 상세 목록을 조회한다.
     *
     * @param SystemConfigDto
     * @return SensorVO 센서 상세 목록 정보 result
     */
    public SensorVO selectSensorDetailInfo() throws BaseException {

        SensorVO result = null;
        result = sensorMapper.selectSensorDetailInfo();
//            result.setlPrivateIp(IpUtil.getServerIp());
        result.setEthoNetList(getEthorNetList());

        return result;
    }
    
    public List<EthoNetVO> getEthorNetList() throws BaseException {
        List<EthoNetVO> rtnList = new ArrayList<>();
        List<String> ethoNetCommand = new ArrayList<>();
        //ifconfig -a | grep Ethernet | awk -F " " '{print $1}'
        ethoNetCommand.add("/bin/sh");
        ethoNetCommand.add("-c");
        ethoNetCommand.add("ifconfig -a | grep Ethernet | awk -F \" \" '{print $1}'");
        List<String> ethorNetList = SystemUtil.execCommand(ethoNetCommand);
        if (ethorNetList != null && ethorNetList.size() > 0) {
            for(String item : ethorNetList) {
                List<String> ipCommand = new ArrayList<>();
                ipCommand.add("/bin/sh");
                ipCommand.add("-c");
                ipCommand.add("ifconfig | grep -A 1 " + "\"" + item + "\" | tail -1 | awk -F \" \" '{print $2}' | awk -F \":\" '{print $2}'");
                List<String> ip = SystemUtil.execCommand(ipCommand);
                EthoNetVO vo = new EthoNetVO();
                vo.setEthoNet(item);
                if (ip != null && ip.size() > 0) {
                    vo.setIp(ip.get(0));
                logger.debug("eth : " + item + ", ip : " + ip.get(0));
                }
                rtnList.add(vo);
            }
        }
        return rtnList;
    }

    /**
     * 센서 상세 정보를 수정한다.
     *
     * @param SystemConfigDto
     * @return
     */
    public void updateSensorDetailInfo(SystemConfigDto dto, HttpSession session) throws BaseException {
		SensorVO sensor = sensorMapper.selectSensorDetailInfo();
		// if (!NumberUtil.longEquals(sensor.getnHyperScanHitCount(),
		// dto.getnHyperScanHitCount())) {
		// sensorMapper.updateSensorHyperscanHitCount(dto);
		// }

		if (sensor.getsUseBlackList() != dto.getsUseBlackList()) {
			sensorMapper.updateSensorUseBlackList(dto);
		}

    }

    /**
     * 센서 상세 정보를 등록한다.
     *
     * @param SystemConfigDto
     * @return
     */
    public long insertSensorDetailInfo(SystemConfigDto dto) throws BaseException {
        long insertLIndex = 0;
        dto.setlPrivateIp(IpUtil.getNetworkByteOrderIpToLong(dto.getlPrivateIpInput()));
        dto.setlPublicIp(IpUtil.getNetworkByteOrderIpToLong(dto.getlPublicIpInput()));

        sensorMapper.insertSensorDetailInfo(dto);

        insertLIndex = dto.getlIndex();

        return insertLIndex;
    }

    /**
     * 센서 inbound 정보 삭제
     *
     * @param dto
     * @throws BaseException
     */
    public void deleteSensorInbound(SystemConfigDto dto) throws BaseException {
        // 센서 inbound 정보 삭제
        SensorInboundDto inboundDto = new SensorInboundDto();
        inboundDto.setLsensorIndex(dto.getlIndex());
        sensorMapper.deleteSensorInboundInfo(inboundDto);
    }

    /**
     * 센서 무결성 정보를 조회한다.
     *
     * @param SystemConfigDto
     * @return SensorVO 센서 상세 목록 정보 result
     */
    public SensorVO selectSensorIntegrityInfo(SystemConfigDto dto) throws BaseException {

        SensorVO result = null;
        result = sensorMapper.selectSensorIntegrityInfo(dto);
        return result;
    }


    /**
     * 센서 무결성검사 정보를 수정한다.
     *
     * @param SystemConfigDto
     * @return
     */
    public void updateSensorIntegrityInfo(SensorIntegrityFileDto dto) throws BaseException {
        //센서 무결성 기본 정보를 수정 한다.
        sensorMapper.updateSensorIntegrityInfo(dto);
    }

    /**
     * 센서 무결성검사 정보를 생성한다.
     *
     * @param SystemConfigDto
     * @return
     */
    public void insertSensorIntegrityInfo(SensorIntegrityFileDto dto) throws BaseException {
        //센서 무결성 기본 정보를 생성 한다.
        sensorMapper.updateSensorIntegrityInfo(dto);
    }

    /**
     * 센서 인바운드 정보를 조회한다.
     *
     * @param SystemConfigDto
     * @return List<SensorInboundVO>
     */
    public SensorInboundVO selectSensorInboundInfo() throws BaseException {

        SensorInboundVO result = null;
        result = sensorMapper.selectSensorInboundInfo();

        if (result == null) {
            return new SensorInboundVO();
        } else {
            return result;
        }
    }

    /**
     * 센서 인바운드 정보를 업데이트한다. (정보 삭제후 신규 등록)
     *
     */
    public void updateSensorInboundInfo(SensorInboundDto dto, HttpSession session) throws BaseException {

        String msg = "NIC:" + StringUtil.stringNullToEmpty(dto.getStrNicInfo()) + ", IP:"+StringUtil.stringNullToEmpty(dto.getStrIpInfo());
        SensorInboundVO vo = sensorMapper.selectSensorInboundInfo();
        if (vo != null) {
            if (!StringUtil.isStringEqualse(vo.getStrNicInfo(), dto.getStrNicInfo()) || !StringUtil.isStringEqualse(vo.getStrIpInfo(), dto.getStrIpInfo())) {
                sensorMapper.updateSensorInboundInfo(dto);
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SENSOR_INBOUND_MOD_SUCCESS, (String) session.getAttribute("Username"), msg);
                writeSensorInbound();
            }
        } else {
            //inbound 감사로그 체크
            if(dto.getStrNicInfo() != null && !dto.getStrNicInfo().isEmpty() && dto.getStrIpInfo() != null && !dto.getStrIpInfo().isEmpty()) {
                sensorMapper.insertSensorInboundInfo(dto);
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SENSOR_INBOUND_MOD_SUCCESS, (String) session.getAttribute("Username"), msg);
                writeSensorInbound();
            }
        }

    }

    /**
     * 센서 인바운드 정보를 신규생성한다.
     *
     */
    public void insertSensorInboundInfo(SensorInboundDto dto) throws BaseException {
        sensorMapper.insertSensorInboundInfo(dto);
    }

    /**
     * 사설IP AND 공인IP 중복 확인
     *
     * @param dto
     * @return
     */
    public SensorVO duplicatePrivateIpAndPublicIp(SystemConfigDto dto) throws BaseException {

        SensorVO resultVo = new SensorVO();
        dto.setlPrivateIp(IpUtil.getNetworkByteOrderIpToLong(dto.getlPrivateIpInput()));
        dto.setlPublicIp(IpUtil.getNetworkByteOrderIpToLong(dto.getlPublicIpInput()));

        resultVo = sensorMapper.duplicatePrivateIpAndPublicIp(dto);
        return resultVo;
    }

    /**
     * 센서 이름 중복검사 SensorVO
     *
     * @param dto
     * @return
     */
    public SensorVO isDuplicateSensorName(SystemConfigDto dto) throws BaseException {
        SensorVO result = new SensorVO();
        result = sensorMapper.isDuplicateSensorName(dto);
        if (result == null) {
            return new SensorVO();
        } else {
            return result;
        }
    }

    /**
     * 센서 IP 모니터링 정보 조회
     *
     * @param dto
     * @return ArrayList<SensorVO>
     */
    public ArrayList<SensorVO> selectSensorIpMonitoringList() throws BaseException {
        ArrayList<SensorVO> selectedList = null;

        selectedList = sensorMapper.selectSensorIpMonitoringList();
        return selectedList;
    }
    
    public void writeSensorDetailInfo() throws BaseException {
        List<SensorVO> monList = sensorMapper.selectSensorIpMonitoringList();
        SensorVO sensor = sensorMapper.selectSensorDetailInfo();
        FileUtil.writeSensorMonitor(monList, sensor);
    }

    /**
     * 센서 IP 모니터링 신규 설정
     *
     * @param dto
     */
    public String insertSensorIpMonitoring(List<IpMonitorDto> dto) throws BaseException {
        StringBuffer rtn = new StringBuffer();
        for (Integer i = 0; i < dto.size(); i++) {
            if (dto != null && dto.get(i) != null && dto.get(i).getlIndex() != null && dto.get(i).getlIndex() > 0) {
                SensorVO ipmon = sensorMapper.selectSensorIpMonitoring(dto.get(i));
                if (ipmon == null) {
                    sensorMapper.insertSensorIpMonitoring(dto.get(i));
                    rtn.append("(fromIP:");
                    rtn.append(dto.get(i).getStrFromIp());
                    rtn.append(", toIP:");
                    rtn.append(dto.get(i).getStrToIp());
                    rtn.append(")");
                } else {
                    if (!ipmon.getStrFromIp().equals(dto.get(i).getStrFromIp()) || !ipmon.getStrToIp().equals(dto.get(i).getStrToIp())) {
                        sensorMapper.updateSensorIpMonitoring(dto.get(i));
                        rtn.append("(fromIP:");
                        rtn.append(dto.get(i).getStrFromIp());
                        rtn.append(", toIP:");
                        rtn.append(dto.get(i).getStrToIp());
                        rtn.append(")");
                    }
                }
            } else {
                sensorMapper.insertSensorIpMonitoring(dto.get(i));
                rtn.append("(fromIP:");
                rtn.append(dto.get(i).getStrFromIp());
                rtn.append(", toIP:");
                rtn.append(dto.get(i).getStrToIp());
                rtn.append(")");
            }
        }
        return rtn.toString();
    }

    public String deleteSensorIpMonitor(List<IpMonitorDto> list) throws BaseException {
        StringBuffer rtn = new StringBuffer();
        if (list != null && list.size() > 0) {
            rtn.append("[delete MonIP ");
        }
        for(IpMonitorDto item : list) {
            sensorMapper.deleteSensorIpMonitor(item);
            rtn.append("(fromIP:");
            rtn.append(item.getStrFromIp());
            rtn.append(", toIP:");
            rtn.append(item.getStrToIp());
            rtn.append(")");
        }
        if (list != null && list.size() > 0) {
            rtn.append("]");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(list.toString()));
        }
        return rtn.toString();
    }
    
    public void updateSensorHyperscanHitCount(SystemConfigDto dto) throws BaseException {
        sensorMapper.updateSensorHyperscanHitCount(dto);
    }
    
    public void updateSensorUseBlackList(SystemConfigDto dto) throws BaseException {
        sensorMapper.updateSensorUseBlackList(dto);
    }
    
    public void writeSensorInbound() throws BaseException {
        FileUtil.writeSensorInbound(selectSensorInboundInfo());
    }
}
