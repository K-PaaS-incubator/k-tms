USE TESS;
-- 메뉴 데이터 추가
-- 주메뉴
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (2,'originalLog','로그분석','Log Analysis',20,'Y',null,1,null);
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (3,'trafficAnalysis','트래픽분석','Traffic Analysis',30,'Y',null,1,null);
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (4,'systemStatus','시스템현황','System Status',40,'Y',null,2,null);
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (5,'securityPolicy','보안정책','Security Policy',50,'Y',null,2,null);
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (6,'systemSettings','시스템관리','System Settings',60,'Y',null,1,null);
                
-- 로그분석
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (21,'detectionLog','침입탐지','Original Log',10,'Y','originalLog',1,'originalLog_detection');
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (24,'fileMetaLog','파일탐지','FileMeta Detection',40,'Y','originalLog',1,'originalLog_filemeta');
-- 트래픽분석
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (31,'protocol','프로토콜','Protocol',10,'Y','trafficAnalysis',1,'trafficAnalysis_protocol');
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (32,'services','서비스','Services',20,'Y','trafficAnalysis',1,'trafficAnalysis_services');
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (35,'ip','IP','IP',50,'Y','trafficAnalysis',1,'trafficAnalysis_ip');
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (36,'maliciousTraffic','유해트래픽','Malicious Traffic',60,'Y','trafficAnalysis',1,'trafficAnalysis_maliciousTraffic');
-- 시스템현황
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (42,'auditLog','감사로그','Audit Log',20,'Y','systemStatus',2,'systemStatus_auditLog');
-- 보안정책
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (51,'intrusionDetectionPolicy','침입탐지정책','Intrusion  Detection Policy',10,'Y','securityPolicy',2,'securityPolicy_intrusionDetectionPolicy');
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (53,'yaraRulePolicy','악성코드','Yara Rule Policy',30,'Y','securityPolicy',2,'securityPolicy_yaraRulePolicy');
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (55,'auditLogPolicy','감사로그 정책','Audit Log Policy',50,'Y','securityPolicy',2,'securityPolicy_auditLogPolicy');
-- 시스템관리
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (61,'systemConfig','시스템 설정','System Configuration',10,'Y','systemSettings',2,'systemSettings_systemConfig');
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (62,'account','계정관리','Account',20,'Y','systemSettings',2,'systemSettings_account');
Insert into TB_MENU (MENU_NO,MENU_KEY,MENU_NAME,MENU_NAME_ENG,DISPLAY_ORDER,ENABLED,UPPER_MENU_KEY,ROLE_NO,URL) values (63,'filterView','필터뷰 설정','FilterView',30,'Y','systemSettings',1,'systemSettings_filterView');