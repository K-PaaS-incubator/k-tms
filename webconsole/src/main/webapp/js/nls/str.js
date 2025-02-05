// 최초 개발은 한국어로 한다.
define({
    "root": {
        "id": "아이디",
        "password": "비밀번호",
        "sign_in": "로그인",
        "sign_in_id": "아이디 로그인",
        "certifiedCertificateLogin":"공인인증서 로그인",
        "certifiedCertificateAdd":"인증서 등록",
        "logout": "로그아웃",
        "close": "닫기",
        "cancel": "취소",
        "ok": "확인",
        "edit": "수정",
        "content": "내용",
        "requireUsername": "아이디를 넣어주십시오.",
        "requirePassword": "비밀번호를 넣어주십시오.",
        "requireRole": "역할을 선택해 주십시오.",
        "numberOfPresenceUser": "접속자수",
        "control": "제어권",
        "controller": "제어권자",
        "releaseController": "제어권 놓기",
        "acquireController": "제어권 획득",
        "controlAllowedTime": "제어권 남은 시간",
        "invalidListDataNull": "더이상 조회할 데이터가 없습니다.",
        "globalThreatConLevel1": "글로벌 위협수준 1단계",
        "globalThreatConLevel2": "글로벌 위협수준 2단계",
        "globalThreatConLevel3": "글로벌 위협수준 3단계",
        "globalThreatConLevel4": "글로벌 위협수준 4단계",
        "globalThreatConLevel5": "글로벌 위협수준 5단계",
        "fiveMin": "5분마다",
        "refresh":"새로고침",
        "dashboard": {
            "target": "전체",
            "targetOrg": "기관별",
            "serviceTopN": "서비스 Top5",
            "serviceType": "서비스",
            "serviceTraffic": "트래픽 (bps)",
            "eventTopN": "공격 Top5",
            "eventType": "공격명",
            "eventCount": "공격건수 (건)",
            "eventCountPer": "공격건수",
            "victimIpTopN": "피해자IP Top5",
            "victimIpType": "피해자IP",
            "etc": "기타",
            "auditTopN": "감사로그",
            "auditTarget": "최근",
            "auditAction": "행위",
            "auditError": "오류",
            "auditWarning": "경고",
            "totalTrafficEventTraffic": "전체트래픽/유해트래픽",
            "eventCountEventTraffic": "공격건수/유해트래픽",
            "total": "전체",
            "trafficEventCount": "트래픽/공격횟수",
            "count": "건",
            "malicious": "유해",
            "event": "공격",
            "traffic": "트래픽",
            "totalTraffic": "전체 트래픽",
            "maliciousTraffic": "유해 트래픽",
            "totalEventCount": "전체 공격건수",
            "attackCount": "공격건수",
            "eventCountOrder": "공격건수 순위",
            "applicationTopN": "어플리케이션 Top5",
        },
        "search": {
            "period": "분석기간",
            "detailPeriod": "상세 기간 입력",
            "selectPeriod": "기간 선택",
            "recent5Minute": "최근 5분",
            "recent10Minute": "최근 10분",
            "recent30Minute": "최근 30분",
            "recent1Hour": "최근 1시간",
            "recent3Hour": "최근 3시간",
            "recent6Hour": "최근 6시간",
            "recent12Hour": "최근 12시간",
            "recent1Day": "최근 1일",
            "recent7Day": "최근 7일",
            "recent30Day": "최근 30일",
            "recent3Month": "최근 3개월",
            "search": "검색",
            "detailSearch": "상세검색",
            "inbound": "탐지방향",
            "sum": "합계",
            "inOutbound": "양방향",
            "option": "선택 항목",
            "generalInfo": "일반 정보",
            "dataInfo": "데이터 정보",
            "ipSearch": "IP 조회",
            "simpleSearch": "간편검색",
        },
        "sampleDataDate": {
            "day1": "1일",
            "day3": "3일",
            "weark1": "1주",
            "weark2": "2주",
            "weark3": "3주",
            "weark4": "4주",
        },
        "btn": {
            "accept": "반영",
        },
        "msg":{
            "insertMsg":"등록 되었습니다.",
            "saveMsg":"저장 되었습니다.",
            "saveFail":"저장이 실패되었습니다.",
            "isSaved":"저장하시겠습니까?",
            "isDelete":" (을)를 삭제하시겠습니까?",
            "isDeleteDefault":"삭제하시겠습니까?",
            "isVsensorDelete":"가상센서를 삭제하시겠습니까?",
            "isNetworkDelete":"네트워크를 삭제하시겠습니까?",
            "deleteMsg":"삭제 되었습니다.",
            "isYaraRuleGroupType":"악성코드 유형을 등록 하시겠습니까?",
            "saveYaraGroup":"악성코드 유형이 저장 되었습니다.",
            "isDeleteYaraRuleGroupType":"악성코드 유형을 삭제하시겠습니까?",
            "deleteYaraRuleGroup":"악성코드 유형이 삭제 되었습니다.",
            "isDeleteYaraRule":"악성코드를 삭제하시겠습니까?",
            "deleteYaraRule":"악성코드가 삭제 되었습니다.",
            "deleteSelected":"삭제할 항목을 선택해주세요.",
            "isDeleteSelected":"선택 한 항목을 삭제 하시겠습니까?",
            "deleteFilterViewList":"필터뷰 목록이 삭제 되었습니다.",
            "insertServiceMsg":"서비스가 등록되었습니다.",
            "deleteService":"서비스가 삭제 되었습니다.",
            "isDeleteAnomaliesPolicy":"이상징후탐지를 삭제하시겠습니까?",
            "deleteAnomaliesPolicy":"이상징후탐지가 삭제 되었습니다.",
            "isDeleteDetectionList":"침입탐지 목록을 삭제하시겠습니까?",
            "deleteDetection":"침입탐지 목록이 삭제 되었습니다.",
            "saveAttackType":"공격유형이 저장 되었습니다.",
            "deleteAttackType":"공격유형이 삭제 되었습니다.",
            "isDeleteAttackType":"공격유형을 삭제하시겠습니까?",
            "applied":"적용 되었습니다.",
            "updateAlertMsg":"업데이트 되었습니다.",
            "ruleImportSuccess":"정책 가져오기가 정상처리 되었습니다.",
            "ruleImportFail":"정책 가져오기가 실패 되었습니다.",
            "patternUpdateSuccess":"패턴 업데이트가 성공 되었습니다.",
            "patternUpdateFail":"패턴 업데이트가 실패 되었습니다.",
            "reportListDeleteSuccess":"보고서 목록이 삭제 되었습니다.",
            "isSelectedDeploy":"선택한 정책을 배포하시겠습니까?",
            "deploySuccess":"배포 완료 되었습니다.",
            "dbbackupSaveSuccess":"백업 요청 완료 하였습니다.",
            "stopSuccess":"성공적으로 중지 되었습니다.",
            "loginFail":"로그인에 실패하였습니다.",
            "filterNameInput":"필터명을 입력하세요",
            "updateDataDelete":"업데이트시 기존 데이터 삭제 후 등록 됩니다.",
            "loggingMsg1":"적어도 하나의 제한은 설정되어야 함.",
            "loggingMsg2":"입력하지 않을 경우 해당 항목은 적용되지 않음.",
            "loggingMsg3":"2일간 이력만 유지 됩니다.",
            "certifiedLoginMsg":"인증서 관련 문의는 교육과학기술부 전자서명인증센터로 연락해주시기 바랍니다.",
            "isDetectionExceptionUpdate": "탐지예외 정책을 수정 하시겠습니까?",
            "isDetectionExceptionDelete": "선택된 탐지예외 정책을 삭제 하시겠습니까?",
        },
        "threatconLevel":{
            "level1":"1단계(정상)",
            "level2":"2단계(관심)",
            "level3":"3단계(주의)",
            "level4":"4단계(경계)",
            "level5":"5단계(심각)",
        },
        "threatcon":{
            "level1":"정상",
            "level2":"관심",
            "level3":"주의",
            "level4":"경계",
            "level5":"심각",
        },
        "patternExemple":{
            "generalInfo":"일반사항",
            "desc1":'하나의 룰은 룰헤더와 룰옵션 부분으로 구성됩니다.',
            "desc2":'룰헤더와 룰옵션은 "(", ")"으로 구분됩니다.',
            "desc3":'룰헤더',
            "desc4":'룰헤더는 총 7개의 필드로 구성됩니다.',
            "desc5":'필드와 필드는 공백 문자로 구분합니다.',
            "desc6":'룰헤더는 "룰타입", "프로토콜", "Src주소", "Src포트", "방향", "Dst주소", "Dst포트" 필드로 구성됩니다.',
            "desc7":'"룰타입"- "alert"을 지정합니다.',
            "desc8":'"프로토콜"- 다음 4가지 프로토콜을 지정할 수 있습니다.',
            "desc9":'"ip" : "tcp" 또는 "udp" 또는 "icmp"를 의미합니다.',
            "desc10":'"Src주소", "Dst주소',
            "desc11":'임의 호스트 : "any',
            "desc12":'특정 IP : "x.x.x.x", 1≤x≤255',
            "desc13":' IP/Netbit : x.x.x.x{/y}?, 1≤x≤255, 1≤y≤32- 예: 211.11.1.1/32, !211.1.1.1/32"!"를 붙여서 exeption IP 주소를 지정할 수 있습니다.',
            "desc14":' "Src포트", "Dst포트"',
            "desc15":'임의 포트',
            "desc16":'특정 포트',
            "desc17":'포트 범위',
            "desc18":' - "!"를 이용하여 exeption 포트를 지정할 수 있습니다. ',
            "desc19":' - ":"를 이용하여 포트를 범위로 지정할 수 있습니다. ',
            "desc20":' - 예: !80  -> 80포트 제외 ',
            "desc21":'방향 구별있음',
            "desc22":'양방향 구분 없음',
            "desc23":'룰 옵션',
            "desc24":' 룰은 0개 이상의 옵션으로 구성됩니다. ',
            "desc25":' 옵션은 ";"으로 종료합니다. 즉, 옵션과 옵션을 ";"으로 구분합니다. ',
            "desc26":' 옵션은 옵션키와 옵션값으로 구성됩니다. ',
            "desc27":' 옵션키와 옵션값의 구분은 ":"을 이용합니다. ',
            "desc28":' 옵션 값은 없을 수도 있습니다. ',
            "desc29":' 지원되는 옵션은 다음과 같습니다.',
            "desc30":'정규표현식',
            "desc31":'구문',
            "desc32":'고정된 문자열 지정',
            "desc33":'content에 지정하는 문자열에서 16진수는 시작과 끝에  "|"를 써서 표현합니다. ',
            "desc34":'사용예: content:!"GET"; -> 문자열 "GET"이 없는 경우를 표현 content:"|5C 00|"; -> hex 코드로 0x5C, 0x00인 패턴 표현',
            "desc35":'지정',
            "desc36":'사용예: flags:SF,12;  -> reserved bit1, bit2는 무시하고 SYN, FIN비트 설정 여부를 검사합니다.',
            "desc37":'사용예제',
            "desc38":'80포트로 전송되는 모든 TCP 패킷을 검사',
            "desc39":'주소가 211.11.1.1인 근원지 호스트에서 임의 호스트의 25번 포트로 전송되는 모든 TCP 패킷을 검사',
            "desc40":'- 주의: "<-"는 지원하지 않습니다',
        },
        "makeGraph": "그래프생성",
        "makeFile": "파일생성",
        "rank": "순위",
        "anomaliRank":"위",
        "lastValue": "최종값",
        "last": "최종",
        "inPeriodView": "기간내 조회",
        "ThereIsNoSelectedItem": "선택한 항목이 없습니다.",
        "ExceededTheNumberOfItems": "선택 할 수 있는 항목의 개수를 초과하였습니다.",
        "unit": "단위",
        "unitCount":"개",
        "attacker": "공격자",
        "detectionCount": "탐지건수",
        "maliciousTraffic": "유해 트래픽",
        "maliciousTrafficBps": "유해 트래픽 bps",
        "count": "건수",
        "rate": "비율",
        "accordionOpen": "모두펼치기",
        "accordionClose": "모두닫기",
        "occurrenceTrend": "발생 추이",
        "attackEvent": "공격 이벤트",
        "attackName": "공격명",
        "attackNameSearchInput": "공격명 검색어 입력",
        "attackType": "공격유형",
        "attackTypeCount": "공격유형 건수",
        "attackTypeAll": "전체 공격유형",
        "severity": "위험도",
        "severityWeight": "위험도 가중치",
        "weight":"가중치",
        "victimPort": "피해포트",
        "attackPort": "공격포트",
        "threshold": "임계치",
        "noSelect": "선택안함",
        "attackCount": "공격횟수",
        "sort": "정렬기준",
        "attackerIp": "공격자",
        "victimIp": "피해자",
        "targetType": "조회대상",
        "institution": "기관",
        "sensor": "센서",
        "vsensor": "가상센서",
        "network": "네트워크",
        "sourceNetwork": "출발 네트워크",
        "destnationNetwork": "대상 네트워크",
        "all": "전체",
        "victimPortAll": "전체 피해포트",
        "attack": "공격",
        "serviceType": "서비스 유형",
        "serviceName":"서비스 명",
        "searchType": "조회 유형",
        "ipType": "IP 체계",
        "registrationInfo": "등록정보",
        "code": "코드",
        "detectionSucceedOrNot": "탐지 여부",
        "packetSave": "패킷 저장",
        "packetSaveNoSapce": "패킷저장",
        "patternInfo": "패턴 정보",
        "summary": "요약",
        "detailComment": "상세설명",
        "solution": "해결방법",
        "reference": "참조",
        "dbUsageList": "DB 사용량",
        "dbUsing": "사용중",
        "dbSpace": "남은 공간",
        "protocol": "프로토콜",
        "totalTraffic": "전체 트래픽",
        "traffic": "트래픽",
        "time": "시간",
        "originalLog": "원본로그",
        "rawPacket": "원본패킷",
        "information": "정보",
        "eventNum": "개수",
        "frameSize": "프레임 크기",
        "tcpFlag": "TCP 플래그",
        "liveMonitor": "실시간 감시",
        "pause": "잠시 멈춤",
        "resume": "멈춤 해제",
        "interest": "관심 감시",
        "topicsOfInterest": "관심항목",
        "address": "주소",
        "fontColor": "글자색상",
        "backgroundColor": "배경색상",
        "start": "시작",
        "end":"끝",
        "theEnd":"종료",
        "remove": "삭제",
        "unRemove": "삭제안함",
        "add": "추가",
        "detectionTime": "탐지시간",
        "service": "서비스",
        "serviceList":"서비스 목록",
        "startDateTime": "시작 일시",
        "endDateTime": "종료 일시",
        "packetCount": "패킷 개수",
        "detectionInfo": "탐지 정보",
        "detectionDescription":"탐지 내용",
        "global": "글로벌",
        "modificationHistory": "변경이력",
        "modificationHistoryDetailSearch": "변경이력 상세 조회",
        "detailSearch":"상세조회",
        "title": "제목",
        "registrar": "등록자",
        "dateOfRegistration": "등록일",
        "totalCount": "전체건수",
        "anomaly": "이상징후",
        "anomalyRule":"이상징후정책",
        "occurrence": "발생",
        "occurrenceTime": "발생 시간",
        "type": "유형",
        "detectionValue": "탐지값",
        "thresholdValue": "임계값",
        "standardValue": "기준값",
        "variation": "변화량",
        "thresholdExceedRate": "임계초과량",
        "target": "대상",
        "applyTarget": "적용대상",
        "fixed": "고정",
        "detectionKind": "탐지 종류",
        "detectionName": "탐지명",
        "searchWord": "검색어",
        "weaknessType": "취약유형",
        "retrieve": "조회",
        "name": "이름",
        "comment": "설명",
        "bandWidth": "대역폭",
        "block": "차단",
        "allow": "허용",
        "blockReason": "차단이유",
        "blockedSignature": "차단 허용된 정책",
        "ipBlock": "IP 블럭",
        "ipMonitor": "IP 모니터링",
        "list": "목록",
        "setting": "설정",
        "allSetting": "일괄설정",
        "modify": "변경",
        "modifyCompleted": "변경완료",
        "regist": "등록",
        "registCompleted": "등록완료",
        "registInfo": "등록정보",
        "dbManagement": "DB관리",
        "dbPassword": "DB패스워드 변경",
        "timeSynchronization": "시간동기화",
        "syslogWorks": "Syslog 연동",
        "ipsSyslogWorks": "IPS Syslog 연동",
        "backup": "백업",
        "dbBackup": "DB백업",
        "integrityCheck": "무결성검사",
        "excuteIntegrityCheck": "즉시 무결성검사",
        "webConsoleIntegrityCheck": "웹콘솔무결성검사",
        "sensorIntegrityCheck": "센서무결성검사",
        "webConsole":"웹콘솔",
        "sensorStatus": "센서 현황",
        "inboundSetting": "Inbound설정",
        "sessionMonitoring": "세션감시",
        "detectionPolicy": "탐지정책",
        "exception": "예외",
        "detectionException": "탐지예외",
        "detectionExceptionSet": "탐지예외 설정",
        "detectionExceptionAdd": "탐지예외 추가",
        "createNewItem": "신규생성",
        "create": "생성",
        "networkGroup": "네트워크 그룹",
        "manager": "매니저",
        "privateIp": "사설 IP",
        "publicIp": "공인 IP",
        "communicationMethod": "통신방식",
        "SSLCertificates": "SSL 인증",
        "inspectionPeriodSetting": "검사 주기 설정",
        "inspectionPeriod": "검사 주기",
        "setUpAutomaticIntegrityTestCycleForTheConfigurationFilesAndExecutables": "설정파일과 실행파일들에 대한 자동 무결성 검사 주기를 설정합니다.",
        "integrityWebConsoleInfo":"웹콘솔에 대한 자동 무결성 검사 주기를 설정 합니다.",
        "integrityInfo":"무결성 검사 주기 정보.",
        "performServicesAtTheBeginningOfTheIntegrityCheck": "서비스 시작시 무결성 검사 수행",
        "checkFileSettings": "검사 파일 설정",
        "usage": "사용",
        "logType": "로그 유형",
        "fileName": "파일명",
        "performAutomaticIntegrityCheck": "분 마다 자동 무결성 검사 수행",
        "doNotAutomaticallyPerformIntegrityCheck": "자동 무결성 검사 수행 안함",
        "configurationManagerConnections": "매니저 연결 설정",
        "setTheManagerPortAndCommunicationMethodUsedToConnectToTheSensor": "센서와의 연결에 사용될 매니저 포트 및 통신 방식을 설정 합니다.",
        "setSensorInfo" : "센서의 설정 정보",
        "setSensorEthInfo" : "센서 이더넷 정보",
        "port": "포트",
        "DBMSSettings": "DBMS 설정",
        "setTheInformationForConnectingToTheDBServer": "DB 서버에 연결하기 위한 내용을 설정합니다.",
        "setIPMonitor": "센서 IP 모니터링 내용을 설정합니다.",
        "DBType": "DB 종류",
        "oracle": "오라클",
        "userName": "사용자 이름",
        "TMSName": "TMS 이름",
        "patternInformationUpdateSettings": "패턴정보 업데이트 설정",
        "patternInformationUpdatedContactInformationAndReservations": "패턴정보 업데이트 접속 정보 및 예약을 설정합니다.",
        "currentRevision": "현재 버젼",
        "updateServer": "업데이트 서버",
        "emailServerSettings": "이메일 서버 설정",
        "setUpAnEmailServerForSendingEmailAlerts": "이메일 경보 발송을 위한 이메일 서버를 설정합니다.",
        "sftpComment": "통합보안 보안 관리제품과 연동시 사용되는 악송코드 파일 전송용 SFTP 서버를 설정합니다.",
        "emailServer": "이메일 서버",
        "emailPort":"이메일 포트",
        "emailAccount":"이메일 계정",
        "emailPwd":"이메일 비밀번호",
        "externalProgramSettings": "외부 프로그램 설정",
        "setAnExternalApplicationToInteractInTheAuditPolicy.": "감사정책에서 연동할 외부 프로그램을 설정합니다.",
        "ExternalProgram": "외부 프로그램",
        "setOverTraffic": "트래픽 이상 설정",
        "setTheStandardReceivesMoreTraffic": "트래픽 수신 이상 기준을 설정합니다.",
        "MinbasedOnTraffic": "Min 기준 트래픽",
        "MinbasedOnTimes": "Min 기준 시간",
        "MaxbasedOnTraffic": "Max 기준 트래픽",
        "MaxbasedOnTimes": "Max 기준 시간",
        "updatePatternInformation": "즉시 업데이트",
        "daily": "매일",
        "everySunday": "매주 일요일",
        "everyMonday": "매주 월요일",
        "everyTuesday": "매주 화요일",
        "everyWednesday": "매주 수요일",
        "everyThursday": "매주 목요일",
        "everyFriday": "매주 금요일",
        "everySaturday": "매주 토요일",
        "am": "오전",
        "pm": "오후",
        "offTimerSetting": "예약 설정 안함",
        "onTimerSetting": "예약 설정 함",
        "runAt": "에 실행",
        "anomaliesDetection": "이상 징후 탐지",
        "prevention": "예경보",
        "setTheTableMaintenancePeriod": "테이블 유지 기간 설정",
        "table": "테이블",
        "byTable": "테이블별",
        "maintenancePeriod": "유지 기간",
        "nonStatistical1Day": "1일 비 통계",
        "daysLog": "1일 이벤트 로그",
        "disk":"디스크",
        "diskUsed":"디스크 사용량 삭제",
        "diskWarn":"디스크 사용량 경고",
        "statistics5Minutes": "5분 통계",
        "statistics1Hour": "1시간 통계",
        "statistics1Day": "1일 통계",
        "auditLog": "감사로그",
        "auditContents": "감사 내용",
        "auditTime": "감사 시간",
        "tableSpace": "테이블 스페이스",
        "days": "일",
        "setTheTableSpaceMaintenancePeriod": "테이블 스페이스 유지 기간 설정",
        "setDiskUsed": "디스크 사용량 설정",
        "tableSpaceUsage": "테이블 스페이스 사용량",
        "IfMoreThanThisPercentDropInTheOldOrder": "이상 되면 오래된 순서로 Drop",
        "diskWarnMailSend": "이상 되면 경고 메일을 발송 합니다.",
        "maliciousHost": "유해호스트",
        "managementHost": "관리호스트",
        "admin": "관리자",
        "user": "사용자",
        "general":"일반",
        "beNotInUse": "사용안함",
        "hostMapping": "호스트 도메인 조회",
        "maxHops": "최대 Hop 수",
        "timeout": "최대 응답 대기 시간",
        "sunday": "일요일",
        "monday": "월요일",
        "tuesday": "화요일",
        "wednesday": "수요일",
        "thursday": "목요일",
        "friday": "금요일",
        "saturday": "토요일",
        "working": "작동 함",
        "notWorking": "작동 안 함",
        "whetherItWorks": "작동 여부",
        "logSec": "로그/초",
        "log": "로그",
        "logSave":"로그저장",
        "rawDataLogSave":"Raw Data와 함께 로그 저장",
        "numberOfSessions": "세션 개수",
        "numberOfProcess": "프로세스",
        "packetLoss": "패킷 손실률",
        "cpuUsedRate": "CPU 사용률",
        "memUsedRate": "Memory 사용률",
        "hddUsedRate": "HDD 사용률",
        "dbUsedRate": "DB 사용률",
        "processCount": "Process 개수",
        "cpuUsedRateTrendGraph": "CPU 사용률 추이 그래프",
        "memUsedRateTrendGraph": "메모리 사용률 추이 그래프",
        "hddUsedRateTrendGraph": "HDD 사용률 추이 그래프",
        "processRateTrendGraph":"프로세스 사용률 추이 그래프",
        "reservationList": "예약 항목",
        "dailyBackupSettings": "일별 백업 설정",
        "monthlyBackupSettings": "월별 백업 설정",
        "automaticBackupHistory": "자동 백업 내역",
        "whetherItBackup": "백업 여부",
        "itemSetting": "항목 설정",
        "item": "항목",
        "detection":"탐지",
        "intrusionDetection": "침입탐지",
        "intrusionDetectionStatistics": "침입탐지 통계",
        "intrusionDetectionPolicy": "침입탐지 정책",
        "trafficStatistics": "트래픽 통계",
        "securityAudit": "보안 감사",
        "session": "세션",
        "reservationTime": "예약 시간",
        "backupPeriodRanges": "백업 기간 범위",
        "backupFolderLocation": "백업 폴더 위치",
        "backupFileCreationScheme": "백업 파일 생성 방식",
        "backupTableDrop": "백업 테이블 삭제",
        "low": "낮음",
        "med": "중간",
        "high": "높음",
        "noCondition":"조건없음",
        "export": "내보내기",
        "undefinedAttackType": "공격유형 없음",
        "noOperation": "작업 없음",
        "checkForNewUpdates": "신규업데이트 있는지 확인중",
        "newUpdatefileDownloading": "신규업데이트 파일을 다운로드중",
        "updateWorking": "업데이트 작업중",
        "completedUpdate": "업데이트 완료",
        "undefinedNewUpdate": "신규업데이트가 존재하지않음",
        "updatedFailure": "업데이트 실패",
        "patternVersionInfoIsIncorrect": "입력된 패턴 버전정보가 올바르지 않음",
        "updateResult": "업데이트 결과",
        "result": "결과",
        "recentUpdateTime": "최근 업데이트 시간",
        "userDefinedMessage": "사용자 정의 메세지",
        "userDefined":"사용자 정의",
        "success": "성공",
        "fail": "실패",
        "complete":"완료",
        "stop":"중지",
        "loggingColumnStop":"정지",
        "mailSend": "메일전송",
        "smsSend": "SMS전송",
        "emergencyAlert":"긴급알람",
        "emergencyAlertDisp":"긴급 알람 표시",
        "visualAural": "가시가청",
        "visualAuralAlert": "가시/가청 알림",
        "programAlert": "프로그램 알림",
        "profile": "프로파일",
        "editingProfile": "프로파일 편집",
        "clear": "지우기",
        "snmpTrap": "SNMP Trap",
        "fixedThreshold": "고정임계값",
        "itemSelection": "항목 선택",
        "itemHasAlreadyBeenAdded": "이미 추가된 항목입니다.",
        "allUsage": "전체 사용량",
        "protocolUsage": "프로토콜별 사용량",
        "serviceUsage": "서비스별 사용량",
        "separation": "구분",
        "detectionMethod": "탐지방법",
        "emergencyNotification": "긴급알람표시",
        "visualAuralAlertStr": "가시가청알림",
        "apply": "적용",
        "applyTarket":"적용대상",
        "version": "버전",
        "madeUser": "만든사람",
        "madeDate": "만든날짜",
        "recentModify": "최근수정",
        "makeExcel": "EXCEL",
        "makeWord": "WORD",
        "makeHtml": "HTML",
        "attackIp": '공격자IP',
        "detailInfo": "상세 정보",
        "logComment": "로그 설명",
        "packetAnalysis": "패킷 분석",
        "dailyTrendReport": "일간경향분석보고서",
        "weeklyTrendReport": "주간경향분석보고서",
        "monthlyTrendReport": "월간경향분석보고서",
        "dailyTrendReportipv6" : "일간경향분석 보고서(IPv6)",
	"weeklyTrendReportipv6" : "주간경향분석 보고서(IPv6)",
	"monthlyTrendReportipv6" : "월간경향분석 보고서(IPv6)",
        "detectionAtteckReport": "공격 Top10 탐지분석 보고서",
        "detectionAtteckIpReport": "공격자IP Top10 탐지분석 보고서",
        "detectionVictimIppReport": "피해자IP Top10 탐지분석 보고서",
        "detectionAtteckTypeStatReport": "공격형태 통계 보고서",
        "detectionAtteckIpStatReport": "공격자IP 통계 보고서",
        "detectionVictimIpStatReport": "피해자IP 통계 보고서",
        "protocolTrafficReport": "프로토콜 트래픽분석 보고서",
        "serviceTop10TrafficReport": "서비스 Top10 트래픽분석 보고서",
        "frameSizeTrafficReport": "프레임 크기 트래픽분석 보고서",
        "trendReport":"경향 분석 보고서",
        "detectionReport":"침입 탐지 보고서",
        "networkReport":"네트워크 사용량 보고서",
        "reset": "초기화",
        "searching": "검색",
        "lowerRankNetwork": "하위 네트워크",
        "lowerRank": "하위",
        "sumBps": "합계 (bps)",
        "sumPps": "합계 (pps)",
        "ppsSum": "pps 합계",
        "bpsSum": "bps 합계",
        "sessionDetection": "세션탐지",
        "anomalyPolicy": "이상징후탐지",
        "constitution": "구성",
        "offlinePatternInfoUpdate": "오프라인 업데이트",
        "update": "업데이트",
        "correlationAnalysis": "상관분석",
        "share": "점유율",
        "managerStatus": "시스템 상태",
        "TNSName": "TNS 이름",
        "correlationSearch": "상관검색",
        "victimPortCount": "피해포트수",
        "victimIP": "피해자 IP",
        "winbound": "In/Out",
        "localThreat": "로컬 위협",
        "local":"로컬",
        "globalThreat": "글로벌 위협",
        "anomaliesThreat": "이상징후위협",
        "applicationDetection": "어플리케이션탐지",
        "fileMetaDetection": "파일탐지",
        "fileMetaName": "파일분석",
        "srcIpList": "출발IP 목록",
        "dwSourceIp": "출발 IP",
        "dwSourceIpTitle": "출발IP",
        "nSourcePort": "출발 포트",
        "destIpList":"대상IP 목록",
        "destinationIp": "대상 IP",
        "destinationIpTiele": "대상IP",
        "destinationPort": "대상 포트",
        "fileType": "파일 타입",
        "fileNaming": "파일 이름",
        "fileSize": "파일 크기",
        "fileMeta": "파일분석 검색",
        "application": "어플리케이션분석 검색",
        "applicationName": "어플리케이션",
        "index": "인덱스",
        "fileInfo": "파일 정보",
        "number": "번호",
        "detailSetting": "상세 설정",
        "whole": "TOTAL",
        "searchSection": "조회기간",
        "download": "다운로드",
        "currentPage": "현재 페이지",
        "currentPageNoSpace": "현재페이지",
        "httpHost": "Http Host",
        "fileHash": "파일 Hash",
        "sourceIpCount": "출발IP 수",
        "destinationIpCount": "대상IP 수",
        "packet": "패킷",
        "useRate": "사용률",
        "waterLeak": "누수",
        "bps": "(bps)",
        "pps": "(pps)",
        "event": "이벤트",
        "countPerMinute": "단위 : 저장 개수/분",
        "system": "시스템",
        "progress": "추이",
        "activity": "활성",
        "inactive": "비활성",
        "normal": "정상",
        "cpuSpeed": "CPU 클럭",
        "doneExecutionTime": "수행 시점으로 부터",
        "everyMonth": "매월",
        "everyDay": "매일",
        "classifiedByTableBackup": "테이블 별 백업",
        "classifiedByDayIntegrated": "일별 통합",
        "classifiedByDayIntegratedBackup": "일별 통합 백업",
        "classifiedByMonthIntegratedBackup": "월별 통합 백업",
        "immediateBackup": "즉시 백업",
        "nationInformationUpdate": "국가정보 업데이트",
        "reputeInformationUpdate": "평판정보 업데이트",
        "snmpServerIp": "SNMP 서버 IP",
        "snmpVersion": "SNMP 버전",
        "sendToSnmpServerAboutLog": "SNMP 서버로 로그 내용을 전달합니다.",
        "transmissionMode": "전송 형태",
        "certificationType": "인증 종류",
        "certificationKey": "인증 키값",
        "encryptionType": "암호화 종류",
        "encryptionKey": "암호화 키값",
        "transmissionItem": "전송 항목",
        "transmission": "전송",
        "trafficThreshold": "트래픽 임계",
        "eventChanges": " 이벤트 변화",
        "eventCountChanges": "이벤트 건수 변화",
        "eventTrafficChanges": "이벤트 트래픽 변화",
        "eventRankChanges": "이벤트 순위 변화",
        "rankChanges": "순위 변화",
        "countChanges": "건수 변화",
        "syslogServer": "Syslog 서버",
        "syslogServerIp": "Syslog 서버 IP",
        "setServerToSyslogProtocol": "Syslog 프로토콜로 데이터를 연동 할 서버 설정을 합니다.",
        "chooseInterlockedData": "연동 데이터 선택",
        "intrusionDetectionLog": "침입탐지 로그",
        "managerSystemLog": "매니저 시스템 로그",
        "sensorSystemLog": "센서 시스템 로그",
        "sensorInfo": "센서 정보",
        "anomalyDetectionLog": "이상징후 탐지 로그",
        "ipsSyslogServer": "IPS Syslog 서버",
        "sensorSyslogServer": "센서별 Syslog 서버",
        "sensorSyslogServerIp": "센서별 Syslog 서버 IP",
        "setServerToSensorSyslogProtocol": "센서 별 Syslog 프로토콜로 데이터를 연동할 서버 설정을 합니다.",
        "includeRawPacketData": "Raw packet 데이터 포함",
        "deployTarget": "배포대상",
        "virtualSensorName": "가상센서",
        "yaraRulePolicy": "악성코드",
        "yaraRulePolicyName": "악성코드명",
        "yaraRuleType":"악성코드 유형",
        "yaraRuleDetailSearch": "악성코드 상세검색",
        "yaraRuleNameSearch":"악성코드명 검색어 입력",
        "detectionPolicyDetailSearch": "침입탐지 상세검색",
        "detectionPolicyRuleInUse": "현재 사용 중인 정책",
        "yaraRuleInUse": "현재 사용 중인 악성코드",
        "kind": "종류",
        "process": "진행",
        "ruleName": "룰 이름",
        "thresholdCountPerSecond": "임계값(개/초)",
        "allDistribution": "모두 배포",
        "selectDistribution": "선택 배포",
        "abnormality": "이상",
        "protocolPerProgress": "프로토콜 추이",
        "loginConfig": "로그인 설정",
        "loginConfigInfo": "로그인 관련 정보를 설정합니다.",
        "sessionTime": "세션 시간",
        "lockFailCount": "로그인 실패",
        "lockTime": "계정 잠김 시간",
        "loginAuthIp" : "로그인 설정 IP",
        "timeHour": "시",
        "timeMin": "분",
        "timeSec": "초",
        "loginCount": "회",
        "settingErrorMsg": "설정 정보를 다시 확인해 주십시오.",
        "basicInformation": "기본 정보",
        "basicInformationChange": "기본 정보를 변경해 주세요.",
        "encryption": "암호",
        "encryptionCheck": "암호확인",
        "additionalInformation": "추가 정보",
        "affiliation": "소속",
        "telephone": "전화",
        "cellphone": "휴대폰",
        "email": "이메일",
        "save": "저장",
        "step": "단계",
        "selectColor": "선택색상",
        "settingNewTab": "새 탭에 적용",
        "startTime": "시작시간",
        "endTime": "종료시간",
        "processTime":"경과시간",
        "searchCondition": "검색조건",
        "immediate": "즉시",
        "reservation": "예약",
        "report": "보고서",
        "amountOfUsage": "사용량",
        "analysisOftendency": "경향 분석",
        "cycle": "주기",
        "reservationName": "예약명",
        "method": "방법",
        "fileformat": "파일형식",
        "term": "기간",
        "yesterday": "어제",
        "toDay":"오늘",
        "pass": "지난",
        "twoday": "2일",
        "threeday": "3일",
        "oneweek": "7일",
        "twoweek": "14일",
        "definition": "정의",
        "everyWeek": "매주",
        "once": "한번",
        "detectionAnalysis": "탐지분석",
        "statistics": "통계",
        "formOfattack": "공격형태",
        "creationTime": "생성시간",
        "criteria": "기준",
        "thedaybefore": "전일",
        "lastWeek": "전주",
        "lastMonth": "전월",
        "nowValue": "현재값",
        "prevValue": "이전값",
        "about": "제품정보",
        "direction": "방향",
        "filter":"필터",
        "filterviewName": "필터뷰명",
        "outline": "개요",
        "attackNation": "공격 국가",
        "startNation": "출발 국가",
        "destinationNation": "대상 국가",
        "sourceMac": "출발 MAC",
        "destinationMac": "대상 MAC",
        "preProcessCount": "전처리기 히트수",
        "setPreProcessCount" : "센서 전처리기 히트수 설정을 합니다.",
        "setColumn": "컬럼 설정",
        "repute":"평판",
        "reputeInfo":"평판 정보",
        "applicationLog":"어플리케이션 로그",
        "stateSearch":"통계 조회",
        "rangeSearch":"구간별 조회",
        "rangeSearch2":"구간조회",
        "filterViewSelect":"필터뷰 선택",
        "liveFilterViewSave":"실시간 감시 - 필터뷰 저장",
        "isBlock":"차단여부",
        "strType":"타입",
        "selectedDeleteMod":"선택 후 삭제, 변경",
        "monitorringIpBlakcList":"모니터링 IP를 블랙리스트로 사용",
        "inputType":"입력방식",
        "scoping":"범위지정",
        "mask":"마스크",
        "selectedDelete":"선택항목 삭제",
        "sessionInfoSend":"세션정보 전송",
        "threatRule":"위협수준정책",
        "precedence":"상위",
        "reportReservDelete":"보고서 예약 삭제",
        "accountGroupName":"계정 그룹명",
        "account":"계정",
        "required":"필수입력사항",
        "threatLevel":"위협 수준",
        "nation":"국가",
        "nationAttackerIp":"국가(공격자IP)",
        "nationAttacker":"국가(공격)",
        "helpMsg":"도움말",
        "detectionImport":"침입탐지 가져오기",
        "import":"가져오기",
        "proceeding":"진행중",
        "updateSuccess":"업데이트 완료",
        "updateFail":"업데이트 실패",
        "isLogReponse":"로그남김",
        "used":"사용여부",
        "usedStr":"사용",
        "threholdCountTime":"임계값 개수/임계 시간",
        "range":"구간",
        "allRange":"전체구간",
        "rangeTarget":"범위 지정",
        "detectionSettingTitle":"탐지/대응 설정 변경",
        "classification":"분류",
        "accountStatus":"계정 상태",
        "loginAuth":"로그인 허용",
        "accountBlock":"계정 잠금",
        "trafficUnit":"트래픽단위",
        "standard":"표준",
        "summaryDescripttion":"요약설명",
        "resultInside":"결과 내",
        "unused":"미사용",
        "signatureInput":"시그니처 입력",
        "signatureDetailDescription":"시그니처 입력",
        "backupStart":"백업 시작",
        "startExecut":"수행 시점으로부터",
        "prevDay":"일 전",
        "prevMonth":"개월 전",
        "deleted":"삭제 함",
        "unDeleted":"삭제 안 함",
        "noActionSet":"작업 설정 안 함",
        "timeSyncOption":"시간 동기화 옵션",
        "standardTimeSyncUsed":"표준시와 시간 동기화 사용",
        "standardTimeServer":"표준시간 서버",
        "server":"서버",
        "unspecified":"미지정",
        "startIp":"시작 IP",
        "sourceIp":"소스 IP",
        "endIp":"끝 IP",
        "autoIntegrityCheck":"분마다 자동 무결성 검사 수행",
        "managerStatusTrend":"시스템 상태 추이",
        "prevenTime":"예/경보시간",
        "alarmType":"경보종류",
        "alarmUsed":"경보여부",
        "serviceStatus":"자산현황",
        "trafficStopsThreshold":"트래픽 고정 임계값",
        "trafficSampleThreshold":"트래픽 표본 임계값",
        "trafficChange":"트래픽 변화",
        "eventThreshold":"이벤트 임계값",
        "globalThreatCon":"글로벌 ThreatCon",
        "capacityLimit":"용량제한",
        "timeLimit":"시간제한",
        "packetCountLimit":"패킷개수제한",
        "trafficGeneratedIp":"트래픽 발생 IP",
        "total":"총합",
        "accountInfo":"계정 정보",
        "DefaultaccountInfo":"기본 계정 정보 변경",
        "easyRangeSearch":"간편 구간조회",
        "detailRangeSearch":"상세 구간조회",
        "rangeComment":"빠른 조회를 원하시면 짧은 조회 시간 간격을 설정해주세요.",
        "more":"더보기",
        "attackInfo":"공격정보",
        "ipSearch":"ip조회",
        "response":"대응",
        "logging":"로깅",
        "file":"파일",
        "loggingFile":"로깅 파일",
        "nationIpInfoTable":"국가별  IP 정보 테이블",
        "recodeCount":"레코드 수",
        "updateDay":"최종 업데이트 날짜",
        "nationInfoFile":"국가 정보 파일",
        "upperLimitValue":"상한값",
        "howToSetUp":"설정방법",
        "assignmentFixedValue":"고정값할당",
        "batchIncreas":"일괄증감",
        "batchRate":"일괄비율",
        "settingValue":"설정값",
        "minValue":"하한값",
        "avgTraffic":"평균트래픽",
        "maxTraffic":"최대트래픽",
        "minTraffic":"최소트래픽",
        "profileMax":"프로파일상한",
        "profileMin":"프로파일하한",
        "lowTraffic":"하한 트래픽",
        "upperTraffic":"상한 트래픽",
        "selectedCopy":"선택복사",
        "allCopy":"전체복사",
        "reputIpInfoTable":"평판별  IP 정보 테이블",
        "reputeInfoFile":"평판 정보 파일",
        "accountGroup":"계정그룹",
        "anomalyFilterviewSetting":"이상징후 필터뷰 설정",
        "applicationFilterviewSetting":"어플리케이션 필터뷰 설정",
        "detectionFilterviewSetting":"침입탐지 필터뷰 설정",
        "filemetaFilterviewSetting":"파일탐지 필터뷰 설정",
        "sessionFilterviewSetting":"세션탐지 필터뷰 설정",
        "adminInfo":"관리자 기본 정보",
        "generalUserInfo":"일반 사용자 기본 정보",
        "detectionFilterview":"침입탐지 필터뷰",
        "anomalyFilterview":"이상징후 필터뷰",
        "applicationFilterview":"어플리케이션 필터뷰",
        "filemetaFilterview":"파일탐지 필터뷰",
        "sessionFilterview":"세션탐지 필터뷰",
        "productInfo":"제품 정보",
        "copyrightInfo":"저작권 정보",
        "companyName":"회사명",
        "productName":"제품명",
        "build":"빌드",
        "contentClassification":"내용구분",
        "console":"콘솔",
        "duplicateCheck":"중복 검사",
        "typeAdd":"유형추가",
        "minOccurrenceCount":"최소발생건수",
        "hostRegistInfo": "유해 / 관심 호스트 등록정보",
        "profileName":"프로파일 이름",
        "profileDesc":"프로파일 설명",
        "profileUnit":"프로파일 단위",
        "viewReferenceData":"참조데이터보기",
        "referenceData":"참조데이터",
        "protocolTraffic":"프로토콜별 트래픽",
        "serviceTraffic":"서비스별 트래픽",
        "servicePort":"서비스 포트",
        "allSecect":"전체선택",
        "globalLocalSettingInfo":"글로벌/로컬 설정정보",
        "startThreatCon":"시작 ThreatCon",
        "endThreatCon":"종료 ThreatCon",
        "threatLevelDetectionList":"위협수준탐지 목록",
        "threatLevelDetectionSetting":"위협수준탐지 설정",
        "threatDetectionThreathole":"위협수준별 탐지 위협 지수 임계치",
        "levelStr":"레벨",
        "detectionThreatIndex":"탐지 위협 지수",
        "hightDetectionCount":"높음 탐지건수",
        "midDetectionCount":"중간 탐지건수",
        "lowDetectionCount":"낮음 탐지건수",
        "configurationList":"구성 목록",
        "fileMaxHdd":"파일 최대 저장용량",
        "excess":"초과 시",
        "overWrite":"덮어쓰기",
        "sleepSessionSetting":"휴면 세션 설정",
        "sleepSessionEndTime":"휴면 세선 종료시간",
        "typeClass":"유형구분",
        "auditContent":"감사내용",
        "logTime":"로그 시간",
        "prevTime12":"이전 12시간",
        "nextTime12":"이후 12시간",
        "loggingLimit":"로깅제한",
        "loggingDataSizeLimit":"용량으로 제한",
        "loggingPacketCountLimit":"패킷 개수로 제한",
        "loggingTimeLimit":"시간으로 제한",
        "saveData":"저장된 용량",
        "savePacket":"저장된 패킷",
        "sessionDirection":"세션방향",
        "sessionType":"세션종류",
        "isSaveAppData":"어플리케이션 페이로드 저장",
        "tableBackup":"테이블별 백업",
        "daysAllBackup":"일별 통합 백업",
        "securePolicy":"보안정책",
        "loginOptimizedMessage" : "권장 해상도  1280x1024, 권장 브라우저 Chrome.",
        "backupFile": "백업파일",
        "backupRange": "백업기간",
        "backupDate": "백업일자",
        "backupTable": "백업테이블",
        "backupTableDel": "테이블삭제",
        "detailVersion":"세부버전",
        "modelName":"모델명",
        "productModelName":"제품모델명",
        "sftpSetting":"SFTP 설정",
        "sftpId":"SFTP 아이디",
        "sftpPwd":"SFTP 비밀번호",
        "currentPassword" : "현재 비밀번호",
    },
    "en-us": true,
    "ja-jp": true,
});
