package com.kglory.tms.web.services.systemSettings;

import com.kglory.tms.web.common.Constants;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.systemSettings.VsensorMapper;
import com.kglory.tms.web.model.dto.DetectionExceptionDto;
import com.kglory.tms.web.model.dto.NetworkDto;
import com.kglory.tms.web.model.dto.VsensorDto;
import com.kglory.tms.web.model.vo.DetectionExceptionVO;
import com.kglory.tms.web.model.vo.DetectionPolicyVO;
import com.kglory.tms.web.model.vo.NetworkVO;
import com.kglory.tms.web.model.vo.SessionServiceDataVO;
import com.kglory.tms.web.services.securityPolicy.DetectionPolicyService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.NumberUtil;
import com.kglory.tms.web.util.file.FileUtil;

import javax.servlet.http.HttpSession;

@Service
public class VsensorService {

    private static Logger logger = LoggerFactory.getLogger(VsensorService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    VsensorMapper vsensorMapper;
    @Autowired
    AuditLogService auditLogSvc;
    @Autowired
    DetectionPolicyService detectionSvc;

    /*
     * 세션 감시 서비스 목록 조회(update)
     * @param 
     * @return List<SessionMonitoringVO> 세션 감시 서비스 목록 정보 resultList
     */
    public List<SessionServiceDataVO> selectSessionMonitoringService(VsensorDto dto) throws BaseException {

        List<SessionServiceDataVO> resultList = null;
        resultList = vsensorMapper.selectSessionMonitoringService(dto);
        return resultList;
    }
    /*
     * 가상센서 세션감시 정보를 수정한다.
     * @param VsensorDto
     * @return 
     */

    public void updateSessionMonitoringService(VsensorDto dto) throws BaseException {
        // 세션감시 서비스 목록을 삭제한후 새로 저장한다.  
        if (dto != null && dto.getFileList() != null && dto.getFileList().size() > 0) {
            vsensorMapper.deleteSessionMonitoringService(dto);
            vsensorMapper.insertSessionMonitoringService(dto);
        }
    }

    /*
     * 가상센서 세션감시 정보를 등록한다.
     * @param VsensorDto
     * @return 
     */
    public void insertSessionMonitoringService(VsensorDto dto) throws BaseException {
        // 세션감시 서비스 목록을 삭제한후 새로 저장한다.  
        vsensorMapper.deleteSessionMonitoringService(dto);
        vsensorMapper.insertSessionMonitoringService(dto);
    }
    /*
     * 가상 센서에 할당된 물리센서 목록 조회 
     * @param VsensorDto
     * @return List<VsensorVO> 물리센서 목록 정보 resultList
     */

    /*
     * 세션 감시 서비스 목록 조회(insert)
     * @param 
     * @return List<SessionMonitoringVO> 세션 감시 서비스 목록 정보 resultList
     */
    public List<SessionServiceDataVO> selectSessionMonitoringServiceData() throws BaseException {
        List<SessionServiceDataVO> resultList = null;
        resultList = vsensorMapper.selectSessionMonitoringServiceData();
        return resultList;
    }
    /*
     * 탐지 예외 > 공격유형별 탐지정책 목록조회
     * @return List<DetectionPolicyVO> 공격유형별 정책 목록 result
     */

    public List<DetectionPolicyVO> selectDetectionPolicyGroup() throws BaseException {
        List<DetectionPolicyVO> resultList = null;
        resultList = vsensorMapper.selectDetectionPolicyGroup();
        return resultList;
    }

    /**
     * 시스템 관리 > 시스템 설정 > 가상센서 > 탐지예외 목록 조회
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public List<DetectionExceptionVO> selectDetectionException(DetectionExceptionDto dto) throws BaseException {
        List<DetectionExceptionVO> resultList = null;
        resultList = vsensorMapper.selectDetectionException(dto);
        return resultList;

    }

    /*
     * 가상센서 > 탐지 예외 > 탐지 예외 수정/저장
     * @return List<DetectionExceptionVO> 공격명, 소스IP/포트/네트워크, 대상IP/포트/네트워크, 프로토콜 목록 result
     */
    public void updateDetectionException(List<DetectionExceptionDto> dto, HttpSession session) throws BaseException {
        for (Integer i = 0; i < dto.size(); i++) {
            /*
             * Any 상태 또는 사용자 설정 상태를 구분짓는다. 
             * 0 = Any, 1 = 해당 설정  
             */
            String srcPort = "ANY";
            String dstPort = "ANY";
            if (dto.get(i).getlVioCode() == 0L && dto.get(i).getnClassType() == 0L) {
                // Any 상태
                dto.get(i).setNchkVioCode(1);
            } else {
                dto.get(i).setNchkVioCode(0);
            }

            if (dto.get(i).getLsrcNetworkIndex() == 0) {
                // Any 상태
                dto.get(i).setNchkSrcNetwork(1);
            } else {
                dto.get(i).setNchkSrcNetwork(0);
            }

            if (dto.get(i).getLdstNetworkIndex() == 0) {
                // Any 상태
                dto.get(i).setNchkDstNetwork(1);
            } else {
                dto.get(i).setNchkDstNetwork(0);
            }
            if (dto.get(i).getStrSrcIpFrom() == null || dto.get(i).getStrSrcIpFrom().equals("")) {
                // Any 상태
                dto.get(i).setNchkSrcIp(1);
            } else {
                dto.get(i).setNchkSrcIp(0);
            }

            if (dto.get(i).getStrDstIpFrom() == null || dto.get(i).getStrDstIpFrom().equals("")) {
                dto.get(i).setNchkDstIp(1);
            } else {
                dto.get(i).setNchkDstIp(0);
            }

            if (dto.get(i).getnProtocol() == 0) {
                dto.get(i).setNchkProtocol(1);
            } else {
                dto.get(i).setNchkProtocol(0);
            }

            if (dto.get(i).getnSport() == 0) {
                dto.get(i).setNchkSport(1);
            } else {
                dto.get(i).setNchkSport(0);
                srcPort = String.valueOf(dto.get(i).getnSport());
            }

            if (dto.get(i).getnDport() == 0) {
                dto.get(i).setNchkDport(1);
            } else {
                dto.get(i).setNchkDport(0);
                dstPort = String.valueOf(dto.get(i).getnDport());
            }

            DetectionExceptionVO result = new DetectionExceptionVO();
            result = vsensorMapper.isDuplicateDetectionException(dto.get(i));

            if (result == null) {
                try {
                    String protcol = "";
                    if (dto.get(i).getnProtocol() == 0L) {
                        protcol = "ANY";
                    } else if (dto.get(i).getnProtocol() == 1L) {
                        protcol = "ICMP";
                    } else if (dto.get(i).getnProtocol() == 6L) {
                        protcol = "TCP";
                    } else if (dto.get(i).getnProtocol() == 17L) {
                        protcol = "UDP";
                    }
                    String srcNet = "";
                    if (dto.get(i).getLsrcNetworkIndex() == 0) {
                        srcNet = "ANY";
                    } else {
                        srcNet = dto.get(i).getLsrcNetworkIndexName();
                    }
                    String dstNet = "";
                    if (dto.get(i).getLdstNetworkIndex() == 0) {
                        dstNet = "ANY";
                    } else {
                        dstNet = dto.get(i).getLdstNetworkIndexName();
                    }
                    vsensorMapper.insertDetectionException(dto.get(i));
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_EXCEPTION_SUCCESS,
                            (String) session.getAttribute("Username"), dto.get(i).getlIndex(), dto.get(i).getStrDescriptionValue(), dto.get(i).getStrSrcIpFrom(), dto.get(i).getStrSrcIpTo(), srcPort,
                            srcNet, dto.get(i).getStrDstIpFrom(), dto.get(i).getStrDstIpTo(), dstPort, dstNet, protcol, MessageUtil.getMessage("str.used" + dto.get(i).getnDetect()));
                    writePolicyException();
                } catch (BaseException ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_EXCEPTION_FAIL,
                            (String) session.getAttribute("Username"), result.getlIndex() + "(" + dto.get(i).getStrDescriptionValue() + ")");
                    throw ex;
                } catch (Exception ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_EXCEPTION_FAIL,
                            (String) session.getAttribute("Username"), result.getlIndex() + "(" + dto.get(i).getStrDescriptionValue() + ")");
                    throw new BaseException(ex);
                }
            } else {
                try {
                    if (!NumberUtil.longEquals(result.getnDetect(), dto.get(i).getnDetect())) {
                        dto.get(i).setlIndex((int) result.getlIndex());
                        vsensorMapper.updatePolicyExceptionByIndex(dto.get(i));
                        auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_EXCEPTION_MOD_SUCCESS,
                                (String) session.getAttribute("Username"), result.getlIndex() + "(" + dto.get(i).getStrDescriptionValue() + ")", MessageUtil.getMessage("str.used" + dto.get(i).getnDetect()));
                        writePolicyException();
                    }
                } catch (BaseException ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_EXCEPTION_MOD_FAIL,
                            (String) session.getAttribute("Username"), result.getlIndex() + "(" + dto.get(i).getStrDescriptionValue() + ")");
                    throw ex;
                } catch (Exception ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_EXCEPTION_MOD_FAIL,
                            (String) session.getAttribute("Username"), result.getlIndex() + "(" + dto.get(i).getStrDescriptionValue() + ")");
                    throw new BaseException(ex);
                }
            }
        }
    }

    /*
     * 가상센서에 매핑되어 있는 네트워크 목록 조회
     */
    public List<NetworkVO> selectVsensorTargetNetworkList(NetworkDto dto) throws BaseException {
        List<NetworkVO> resultList = null;
        resultList = vsensorMapper.selectVsensorTargetNetworkList(dto);
        return resultList;
    }

    /*
     * 예외 목록 삭제 
     */
    public void deleteDetectionExceptionList(List<DetectionExceptionDto> dto, HttpSession session) throws BaseException {
        for (int i = 0; i < dto.size(); i++) {
            dto.get(i).setlIndex(dto.get(i).getDeletelIndex());
            DetectionExceptionVO vo = vsensorMapper.selectDetectionExceptionDetail(dto.get(i));
            String msg = MessageUtil.getbuilMessage("audit.policy.detection.exception.del", dto.get(i).getDeletelIndex() ,vo.getStrDescriptionValue());
            try {
                vsensorMapper.deleteDetectionExceptionList(dto.get(i));
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_EXCEPTION_DEL_SUCCESS, (String) session.getAttribute("Username"), msg);
            } catch(BaseException e) {
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_EXCEPTION_DEL_FAIL, (String) session.getAttribute("Username"), msg);
            }
        }
        if (dto.size() > 0) {
            writePolicyException();
        }
    }

    /*
     * 예외 신규 생성
     */
    public void insertDetectionExceptionList(List<DetectionExceptionDto> dto) throws BaseException {
        for (Integer i = 0; i < dto.size(); i++) {
            dto.get(i).setlVioCode(dto.get(i).getlVioCode());
            dto.get(i).setLsrcNetworkIndex(dto.get(i).getLsrcNetworkIndex());
            dto.get(i).setLdstNetworkIndex(dto.get(i).getLdstNetworkIndex());
            dto.get(i).setLvsensorIndex(dto.get(i).getLvsensorIndex());
            dto.get(i).setnDport(dto.get(i).getnDport());
            dto.get(i).setnSport(dto.get(i).getnSport());
            dto.get(i).setnProtocol(dto.get(i).getnProtocol());
            dto.get(i).setnDetect(dto.get(i).getnDetectValue());
            vsensorMapper.insertDetectionExceptionList(dto.get(i));
        }
        writePolicyException();
    }

    /*
     * 실시간 감시 > 침입탐지 탐지예외 추가 
     */
    public void updateMonitorIntrusionDetectionException(DetectionExceptionDto dto, String userId) throws BaseException {
        /*
         * Any 상태 또는 사용자 설정 상태를 구분짓는다. 
         * 0 = Any, 1 = 해당 설정  
         */
        if (dto.getlVioCode() == 0) {
            // Any 상태
            dto.setNchkVioCode(1);
        } else {
            dto.setNchkVioCode(0);
        }

        if (dto.getLsrcNetworkIndex() == 0) {
            // Any 상태
            dto.setNchkSrcNetwork(1);
        } else {
            dto.setNchkSrcNetwork(0);
        }

        if (dto.getLdstNetworkIndex() == 0) {
            // Any 상태
            dto.setNchkDstNetwork(1);
        } else {
            dto.setNchkDstNetwork(0);
        }
        if (dto.getStrSrcIpFrom().equals("")) {
            // Any 상태
            dto.setNchkSrcIp(1);
        } else {
            dto.setNchkSrcIp(0);
        }

        if (dto.getStrDstIpFrom().equals("")) {
            dto.setNchkDstIp(1);
        } else {
            dto.setNchkDstIp(0);
        }

        if (dto.getnProtocol() == 0) {
            dto.setNchkProtocol(1);
        } else {
            dto.setNchkProtocol(0);
        }

        if (dto.getnSport() == 0) {
            dto.setNchkSport(1);
        } else {
            dto.setNchkSport(0);
        }

        if (dto.getnDport() == 0) {
            dto.setNchkDport(1);
        } else {
            dto.setNchkDport(0);
        }

        DetectionExceptionVO result = new DetectionExceptionVO();
        result = vsensorMapper.isDuplicateDetectionException(dto);

        if (result == null) {
            try {
                String protcol = "";
                if (dto.getnProtocol() == 0L) {
                    protcol = "ANY";
                } else if (dto.getnProtocol() == 1L) {
                    protcol = "ICMP";
                } else if (dto.getnProtocol() == 6L) {
                    protcol = "TCP";
                } else if (dto.getnProtocol() == 17L) {
                    protcol = "UDP";
                }
                vsensorMapper.insertDetectionException(dto);
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_EXCEPTION_SUCCESS,
                        userId, dto.getlIndex(), dto.getStrDescriptionValue(), dto.getStrSrcIpFrom(), dto.getStrSrcIpTo(), dto.getnSport(),
                        dto.getNchkSrcNetwork(), dto.getStrDstIpFrom(), dto.getStrDstIpTo(), dto.getnDport(), dto.getNchkDstNetwork(),
                        protcol, MessageUtil.getMessage("str.used" + dto.getnDetect()));
                writePolicyException();
            } catch (BaseException ex) {
                logger.error(ex.getLocalizedMessage(), ex);
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_EXCEPTION_FAIL,
                        userId, dto.getStrDescriptionValue());
                throw ex;
            } catch (Exception ex) {
                logger.error(ex.getLocalizedMessage(), ex);
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_EXCEPTION_FAIL,
                        userId, dto.getStrDescriptionValue());
                throw new BaseException(ex);
            }
        } else {
            if (result.getnDetect().intValue() != dto.getnDetectValue().intValue()) {
                try {
                    dto.setlIndex((int) result.getlIndex());
                    vsensorMapper.updatePolicyExceptionByIndex(dto);
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_EXCEPTION_MOD_SUCCESS,
                            userId, result.getlIndex() + "(" + dto.getStrDescriptionValue() + ")", MessageUtil.getMessage("str.used" + dto.getnDetect()));
                    writePolicyException();
                } catch (BaseException ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_EXCEPTION_MOD_FAIL,
                            userId, result.getlIndex() + "(" + dto.getStrDescriptionValue() + ")");
                    throw ex;
                } catch (Exception ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_EXCEPTION_MOD_FAIL,
                            userId, result.getlIndex() + "(" + dto.getStrDescriptionValue() + ")");
                    throw new BaseException(ex);
                }
            }
        }
    }

    public DetectionExceptionVO isDuplicateDetectionException(DetectionExceptionDto dto) throws BaseException {

        DetectionExceptionVO result = new DetectionExceptionVO();
        result = vsensorMapper.isDuplicateDetectionException(dto);
        return result;
    }
    
    public void writePolicyException() throws BaseException {
        FileUtil.writePolicyException(vsensorMapper.selectDetectionException(new DetectionExceptionDto()));
    }
}
