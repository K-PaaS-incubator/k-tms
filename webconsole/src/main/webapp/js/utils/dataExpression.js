define(function (require) {

    var $ = require('jquery'),
            d3 = require('d3'),
            locale = require('i18n!nls/str');

    return {
        //프로토콜 표기
        getProtocolName: function (nProtocol) {
            var strProtocol;

            switch (nProtocol) {
                case 0:
                    strProtocol = "MPLS";
                    break;
                case 1:
                    strProtocol = "ICMP";
                    break;
                case 2:
                    strProtocol = "ARP";
                    break;
                case 3:
                    strProtocol = "RARP";
                    break;
                case 4:
                    strProtocol = "LLC";
                    break;
                case 6:
                    strProtocol = "TCP";
                    break;
                case 17:
                    strProtocol = "UDP";
                    break;
                case 58:
                    strProtocol = "ICMPv6";
                    break;
                default:
                	strProtocol = "";
            }
            return strProtocol;
        },
        getbTypeName: function (bType) {
            var strBtype;

            switch (bType) {
                case 1:
                    strBtype = "http";
                    break;
                case 2:
                    strBtype = "dns";
                    break;
                case 3:
                    strBtype = "tls";
                    break;
                case 4:
                    strBtype = "smtp";
                    break;
                case 5:
                    strBtype = "ftp";
                    break;
                default:
                	strBtype = "";
            }
            return strBtype;
        },
        // ICMP는 포트번호 "-" 로 변환 처리	
        getPortFormat: function (port) {
            var strPort;
            if (port == 0) {
                strPort = '-';
            } else {
                strPort = port;
            }
            return strPort;
        },
        // 프로토콜 숫자타입
        getProtocolNum: function (protocol) {
            var protocolNum;

            switch (protocol) {
                case "TCP":
                    protocolNum = 6;
                    break;
                case "UDP":
                    protocolNum = 17;
                    break;
                default:
                	protocolNum = 0;
            }
            return protocolNum;
        },
        //트래픽량 표기
        getFormatTrafficData: function (data) {
            var dataResult = data;
            var len = this.round(dataResult, 0).toString().length;

            if (len > 3 && len <= 6) {
                dataResult = this.round(dataResult / 1024, 2);
                dataResult = this.changeDecimalLength(dataResult, 2) + "K";
            } else if (len > 6 && len <= 9) {
                dataResult = this.round(dataResult / (1024*1024), 2);
                dataResult = this.changeDecimalLength(dataResult, 2) + "M";
            } else if (len > 9) {
                dataResult = this.round(dataResult / (1024*1024*1024), 2);
                dataResult = this.changeDecimalLength(dataResult, 2) + "G";
            } else {
                if (dataResult >= 1) {
                    dataResult = this.changeDecimalLength(dataResult, 2) + "";
                } else if (dataResult == 0) {
                    dataResult = this.numberFormat(dataResult);
                } else {
                    dataResult = this.changeDecimalLength(dataResult, 2);
                }
            }
            return dataResult;
        },
        //트래픽량 표기
        getFormatByteData: function (data) {
            var dataResult = data;
            var len = this.round(dataResult, 0).toString().length;

            if (len > 3 && len <= 6) {
                dataResult = this.round(dataResult / 1024, 2);
                dataResult = this.changeDecimalLength(dataResult, 2) + "K";
            } else if (len > 6 && len <= 9) {
                dataResult = this.round(dataResult / (1024*1024), 2);
                dataResult = this.changeDecimalLength(dataResult, 2) + "M";
            } else if (len > 9) {
                dataResult = this.round(dataResult / (1024*1024*1024), 2);
                dataResult = this.changeDecimalLength(dataResult, 2) + "G";
            } else {
                if (dataResult >= 1) {
                    dataResult = this.changeDecimalLength(dataResult, 2) + "";
                } else if (dataResult == 0) {
                    dataResult = this.numberFormat(dataResult);
                } else {
                    dataResult = this.changeDecimalLength(dataResult, 2);
                }
            }
            return dataResult;
        },
        //소수점 자리수 지정
        changeDecimalLength: function (data, len) {
            //기본 2자리 설정
            if (typeof len == 'undefined' || len == null) {
                len = 2;
            }
            if (typeof data == 'undefined' || data == null || data == '') {
                return '';
            }
            return data.toFixed(len);
        },
        // 트래픽량 표기 2  값과 단위, selectbox의 옵션값 를 리턴한다
        getFormatTrafficDataUnit: function (data, type) {
            var result = [];
            if (type == 1) {
                var len = this.round(data, 0).toString().length;
                var dataResult = null, dataUnit = null, dataUnitValue = null;
                if (len > 3 && len <= 6) {
                    result.push({
                        'data': data / 1000,
                        'unit': [{'unit': '', 'unitValue': 0, 'unitSelected': ''},
                            {'unit': 'K', 'unitValue': 1, 'unitSelected': 'selected'},
                            {'unit': 'M', 'unitValue': 2, 'unitSelected': ''},
                            {'unit': 'G', 'unitValue': 3, 'unitSelected': ''}
                        ]
                    });
                } else if (len > 6 && len <= 9) {
                    result.push({
                        'data': data / 1000000,
                        'unit': [{'unit': '', 'unitValue': 0, 'unitSelected': ''},
                            {'unit': 'K', 'unitValue': 1, 'unitSelected': ''},
                            {'unit': 'M', 'unitValue': 2, 'unitSelected': 'selected'},
                            {'unit': 'G', 'unitValue': 3, 'unitSelected': ''}
                        ]
                    });
                } else if (len > 9) {
                    result.push({
                        'data': data / 1000000000,
                        'unit': [{'unit': '', 'unitValue': 0, 'unitSelected': ''},
                            {'unit': 'K', 'unitValue': 1, 'unitSelected': ''},
                            {'unit': 'M', 'unitValue': 2, 'unitSelected': ''},
                            {'unit': 'G', 'unitValue': 3, 'unitSelected': 'selected'}
                        ]
                    });
                } else {
                    if (data >= 1) {
                        dataResult = data;
                    } else if (data == 0) {
                        dataResult = this.numberFormat(data);
                    } else {
                        dataResult = data;
                    }
                    result.push({
                        'data': dataResult,
                        'unit': [{'unit': '', 'unitValue': 0, 'unitSelected': 'selected'},
                            {'unit': 'K', 'unitValue': 1, 'unitSelected': ''},
                            {'unit': 'M', 'unitValue': 2, 'unitSelected': ''},
                            {'unit': 'G', 'unitValue': 3, 'unitSelected': ''}
                        ]
                    });
                }
            } else {
                result.push({
                    'data': data,
                    'unit': [{'unit': '', 'unitValue': 0, 'unitSelected': ''},
                        {'unit': 'K', 'unitValue': 1, 'unitSelected': ''},
                        {'unit': 'M', 'unitValue': 2, 'unitSelected': ''},
                        {'unit': 'G', 'unitValue': 3, 'unitSelected': ''}
                    ]
                });
            }

            return result;
        },
        // 세션의 서비스 목록 
        getServicesName: function (serverPort) {
            var strServerPort;

            switch (serverPort) {
                case 20:
                    strServerPort = "FTP-DATA";
                    break;
                case 21:
                    strServerPort = "FTP";
                    break;
                case 22:
                    strServerPort = "SSH";
                    break;
                case 23:
                    strServerPort = "TELNET";
                    break;
                case 25:
                    strServerPort = "SMTP";
                    break;
                case 80:
                    strServerPort = "HTTP";
                    break;
                case 110:
                    strServerPort = "POP3";
                    break;
                case 143:
                    strServerPort = "IMAP";
                    break;
                case 443:
                    strServerPort = "HTTPS";
                    break;
                case 1863:
                    strServerPort = "MSNP";
                    break;
                default:
                    strServerPort = locale.userDefined;
                    break;
            }
            return strServerPort;
        },
        //이벤트량 표기
        getFormatEventData: function (data) {
            var dataResult = data;
            var len = this.round(dataResult, 0).toString().length;

            if (len > 5 && len <= 7) {
                dataResult = this.round(dataResult / 1000, 0);
                dataResult = dataResult + "K";
            } else if (len > 7 && len <= 9) {
                dataResult = this.round(dataResult / 1000000, 0);
                dataResult = dataResult + "M";
            } else if (len > 9) {
                dataResult = this.round(dataResult / 1000000000, 0);
                dataResult = dataResult + "G";
            } else {
                dataResult = this.numberFormat(dataResult);
            }

            return dataResult;
        },
        //그래프용 트래픽량 Y축 
        bytesToString: function (bytes) {
            // One way to write it, not the prettiest way to write it.
            var fmt = d3.format('.f');

            if (bytes < 0) {
                return "";
            } else if (bytes < 1000) {
                return fmt(bytes);
            } else if (bytes < 1000 * 1000) {
                return fmt(bytes / 1000) + 'K';
            } else if (bytes < 1000 * 1000 * 1000) {
                return fmt(bytes / 1000 / 1000) + 'M';
            } else {
                return fmt(bytes / 1000 / 1000 / 1000) + 'G';
            }
        },
        //그래프용 이벤트건수 Y축 
        countToString: function (count) {
            // One way to write it, not the prettiest way to write it.
            var fmt = d3.format('d');

            if (count < 0) {
                return "";
            } else if (count < 1000) {
                return fmt(count);
            } else if (count < 1000 * 1000) {
                return fmt(count / 1000) + 'K';
            } else if (count < 1000 * 1000 * 1000) {
                return fmt(count / 1000 / 1000) + 'M';
            } else {
                return fmt(count / 1000 / 1000 / 1000) + 'G';
            }
        },
        //전체대비 백분율 구하기 % 스트링 포함
        getFormatPercentData: function (data, totalData) {
            var dataResult;
            if (totalData > 0) {
                dataResult = 100000 * data.toFixed(2) / totalData.toFixed(2);
                dataResult = Math.ceil(dataResult);
                dataResult = dataResult / 1000;
                dataResult = Math.round(dataResult * 100) / 100;
                if (dataResult == 0) {
                    dataResult = "0.00%";
                } else {
                    dataResult = this.changeDecimalLength(dataResult, 2) + "%";
                }
            } else {
                dataResult = (this.changeDecimalLength(0, 2)) + "%";
            }

            return dataResult;
        },
        //전체대비 백분율 구하기 % 스트링 제외 데이터만
        getFormatPercent: function (data, totalData) {
            var dataResult;

            if (totalData > 0) {
                dataResult = data / totalData * 100;
                dataResult = this.floatFormat(dataResult, 2)
            } else {
                dataResult = this.floatFormat(0, 2);
            }

            return dataResult;
        },
        floatFormat: function (value, fixed) {
            if (typeof fixed == "undefined") {
                fixed = 2;
            }
            return parseFloat(value).toFixed(fixed);
        },
        //CPU 속도 단위를 GHz로 변환
        //마지막 자리수는 반올림 한다. 
        getMhzUnit: function (data) {
            var dataResult = data;

            if (data > 1000) {
                dataResult = this.round(data / 1000, 1);
            }
            return dataResult;
        },
        getByteUnit: function (data) {
            var dataResult = data;

            if (data > 1000) {
                dataResult = this.round(data / 1024, 0) + ' KB';
            }
            return dataResult;
        },
        /*계산 함수*/

        //반올림 함수
        round: function (num, ja) {
            var dataResult;

            ja = Math.pow(10, ja);
            dataResult = Math.round(num * ja) / ja;
            return dataResult;
        },
        //숫자 format 함수 
        numberFormat: function (num) {
            var dataResult;
            var chk = 1;
            if (num < 0) {
                chk = -1;
            }
            num = num * chk;
            if (!/[^0-9]/.test(num)) {
                num = "" + num;

                var ret = [];
                var len = num.length;

                var ind = len - (parseInt(len / 3) * 3) || 3;

                for (var i = 0; i < len; i++) {
                    ret.push(num.charAt(i));
                    ind--;
                    if (ind <= 0) {
                        ret.push(",");
                        ind = 3;
                    }
                }
                dataResult = ret.join("").replace(/(^,|,$)/g, "");
                if (chk == -1) {
                    dataResult = "-" + dataResult;
                }
            } else {
                dataResult = num;
            }
            return dataResult;
        },
        //숫자 format 함수 3자리마다 ',' 표기 
        numberFormat2: function (num) {
            if (num == 0)
                return num;

            var reg = /(^[+-]?\d+)(\d{3})/;
            var chk = 1;
            if (num < 0) {
                chk = -1;
            }
            var n = ((num * chk) + '');

            while (reg.test(n))
                n = n.replace(reg, '$1' + ',' + '$2');
            if (chk == -1) {
                n = "-" + n;
            }
            return n;
        },
        // 감사로그 유형의 숫자 타입 문자로 변경
        auditTypeCheck: function (data) {
            if (data == 1) {
                return locale.dashboard.auditAction;
            } else if (data == 2) {
                return locale.dashboard.auditError;
            } else if (data == 3) {
                return locale.dashboard.auditWarning;
            }
        },
        // 감사로그 유형의 숫자 타입을 영문으로 변경(이미지 클래스명) 
        auditEngCheck: function (data) {
            if (data == 1) {
                return 'action';
            } else if (data == 2) {
                return 'error';
            } else if (data == 3) {
                return 'warning';
            }
        },
        // 위험도 숫자값 text로 변경 
        severityData: function (data) {
            if (data == 5) {
                return 'High';
            } else if (data == 3) {
                return 'Medium';
            } else if (data == 1) {
                return 'Low';
            } else if (data == 0) {
                return 'Info ';
            } else {
                return 'Absence';
            }
        },
        // 위험도 숫자값을 칼라로 표현할때  
        severityColorData: function (data) {
            if (data == 5) {
                return 5;
            } else if (data == 3) {
                return 4;
            } else if (data == 1) {
                return 3;
            } else if (data == 0) {
                return 2;
            } else {
                return 1;
            }
        },
        preventionTypeCheck: function (data) {
            if (data == 1) {
                return locale.auditLog;	// 감사로그 
            } else if (data == 2) {
                return locale.anomaly;	// 이상징후 
            } else if (data == 3) {
                return locale.maliciousHost;	// 유해호스트
            } else if (data == 4) {
                return locale.managementHost; 	// 관리호스트 
            } else if (data == 5) {
                return locale.prevention;		// 예경보 
            } else if (data == 6) {
                return locale.intrusionDetectionPolicy;		// 침입탐지 정책
            } else if (data == 100) {
                return locale.userDefinedMessage;
            }
        },
        preventionStatus: function (data) {
            if (data == 1) {
                return locale.success;	// 성공
            } else if (data == 0) {
                return locale.fail;	// 실패
            } else {
                return locale.fail;	// 실패
            }
        },
        preventionAlarmType: function (data) {
            if (data == "1000000000") {
                return locale.mailSend;
            } else if (data == "0100000000") {
                return locale.smsSend;
            } else if (data == "0010000000") {
                return locale.emergencyAlert;
            } else if (data == "0001000000") {
                return locale.visualAural;
            } else {
                return locale.programAlert;
            }
        },
        preventionTarget: function (data) {
            if (data == 1) {
                return locale.admin;	//  관리자
            } else {
                return locale.user;		// 사용자
            }
        },
        getDayOfWeek: function (data) {
            if (data == 0) {
                return locale.daily;
            } else if (data == 1) {
                return locale.everySunday;
            } else if (data == 2) {
                return locale.everyMonday;
            } else if (data == 3) {
                return locale.everyTuesday;
            } else if (data == 4) {
                return locale.everyWednesday;
            } else if (data == 5) {
                return locale.everyThursday;
            } else if (data == 6) {
                return locale.everyFriday;
            } else if (data == 7) {
                return locale.everySaturday;
            }
        },
        getAmPmTime: function (data) {
            var ampm;
            var hour;
            var strTime;

            if (data < 12) {
                ampm = locale.am;
                if (data < 10) {
                    hour = "0" + data + ":00";
                } else {
                    hour = data + ":00";
                }
            } else {
                ampm = locale.pm;
                if (data == 12) {
                    hour = data + ":00";
                } else {
                    if ((data % 12) < 10) {
                        hour = "0" + (data % 12) + ":00";
                    } else {
                        hour = (data % 12) + ":00";
                    }

                    hour = (data % 12) + ":00";
                }

            }
            strTime = ampm + " " + hour;
            return strTime;
        },
        /**
         * TMS Console 기준으로 트래픽 임계와 이벤트 변화에 대한 type 별 값
         * (이상징후 메뉴에 해당함)
         */
        getAnomaliesThreatConsoleType: function (data) {

            var unit = '',
                    ucTypeName = '',
                    tableName = '',
                    nProtocol = '';
            var params = null;

            if (data.ucType != undefined) {
                if (data.ucType == 1 || data.ucType == 2) {

                    // 트래픽단위
                    if (data.ucSubType2 == 1) {
                        unit = 'bps';
                    } else if (data.ucSubType2 == 2) {
                        unit = 'pps';
                    } else {
                        unit = '-';
                    }

                    if (data.ucSubType1 == 10) {
                        ucTypeName = locale.allUsage;
                        tableName = "PROTOCOL";
                        nProtocol = 0;
                        // wObject 값 불필요
                    } else if (data.ucSubType1 == 11 || data.ucSubType1 == 21) {

                        tableName = "PROTOCOL";

                        if (data.wObject == 11) {
                            ucTypeName = locale.protocolUsage + ' (Protocol1)';
                        } else if (data.wObject == 6) {
                            ucTypeName = locale.protocolUsage + ' (TCP)';
                        } else if (data.wObject == 17) {
                            ucTypeName = locale.protocolUsage + ' (UDP)';
                        } else if (data.wObject == 1) {
                            ucTypeName = locale.protocolUsage + ' (ICMP)';
                        } else if (data.wObject == 2) {
                            ucTypeName = locale.protocolUsage + ' (ARP)';
                        } else if (data.wObject == 3) {
                            ucTypeName = locale.protocolUsage + ' (RARP)';
                        } else if (data.wObject == 4) {
                            ucTypeName = locale.protocolUsage + ' (LLC)';
                        } else {
                            ucTypeName = locale.protocolUsage;

                        }
                    } else if (data.ucSubType1 == 40) {
                        ucTypeName = locale.serviceUsage;
                        // wObject 값 불필요
                    } else if (data.ucSubType1 == 6 || data.ucSubType1 == 17) {
                        ucTypeName = locale.serviceUsage + ' (' + data.wObject + ')';
                        tableName = "SERVICE";
                    } else {
                        ucTypeName = '-';
                        tableName = "";
                    }

                } else if (data.ucType == 12) {

                    ucTypeName = locale.eventChanges + ' (' + data.attackName + ")";
                    tableName = 'STAT_EP';

                    if (data.ucSubType1 == 1) {
                        unit = locale.count; // 건수
                    } else if (data.ucSubType1 == 2) {
                        unit = locale.traffic; // 트래픽
                    } else if (data.ucSubType1 == 3) {
                        unit = locale.rank; // 순위
                    } else {
                        unit = '';
                    }
                }
            }
            return params = {
                'unit': unit,
                'ucTypeName': ucTypeName,
                'tableName': tableName
            };
        },
        /**
         * /**
         * TMS Web기준으로 트래픽 임계와 이벤트 변화에 대한 type 별 값
         * (이상징후 관련 메뉴에 해당함)
         *
         * getAnomaliesThreatType에서 
         * 넘겨받는 parameter 'data'는 json 타입이다.
         * data 는 다음과 같은 4가지 데이터를 담고있다. 
         * data = { 'ucType' 		: 숫자타입, // 탐지타입
         * 			'ucSubType2' 	: 숫자타입, // 탐지 종류
         * 			'ucSubType1 	: 숫자타입, // 이벤트종류
         * 			'wObject 		: 숫자타입  // 프로토콜/ 서비스 포트번호
         * 		   }
         */
        getAnomaliesThreatType: function (data) {
            var unit = '',
                    ucTypeName = '',
                    tableName = '',
                    nProtocol = '';
            var params = null;

            if (data.ucType != undefined) {
                if (data.ucType >= 1 && data.ucType <= 3) {
                    if (data.ucSubType2 != undefined) {
                        if (data.ucSubType2 == 1) {
                            unit = "bps";
                        } else if (data.ucSubType2 == 2) {
                            unit = "pps";
                        } else if (data.ucSubType2 == 3) {
                            unit = "cps";
                        }
                    } 
                    if (data.ucSubType1 != undefined) {
                        if (data.ucSubType1 == 10) {
                            ucTypeName = "Band Width"; // 트래픽분석의 프로토콜의 total 의 조건값과 같다 
                            tableName = "PROTOCOL";
                            nProtocol = 0;
                        } else if (data.ucSubType1 == 11) {
                            tableName = "PROTOCOL";
                        } else if (data.ucSubType1 == 21) {
                            tableName = "PROTOCOL";

                            if (data.wObject != undefined) {
                                if (data.wObject == 1) {
                                    ucTypeName = "ICMP";
                                    nProtocol = 1;
                                } else if (data.wObject == 6) {
                                    ucTypeName = "TCP";
                                    nProtocol = 6;
                                } else if (data.wObject == 17) {
                                    ucTypeName = "UDP";
                                    nProtocol = 17;
                                } else {
                                    ucTypeName = '';
                                    nProtocol = '';
                                }
                            }
                        } else if (data.ucSubType1 == 6) {
                            tableName = "SERVICE";
                            if (data.wObject != undefined) {
                                ucTypeName = 'TCP Service ' + data.wObject('wObject');
                            } else {
                                ucTypeName = 'TCP Service';
                            }
                        } else if (data.ucSubType1 == 17) {
                            tableName = "SERVICE";
                            if (data.wObject != undefined) {
                                ucTypeName = 'UDP Service ' + data.wObject('wObject');
                            } else {
                                ucTypeName = 'UDP Service';
                            }
                        } else {
                            tableName = '';
                            ucTypeName = '';
                        }
                    } else {
                        ucTypeName = "";
                        tableName = "";
                        nProtocol = "";
                    }
                } else if (data.ucType >= 11 && data.ucType <= 12) {

                    if (data.ucSubType1 != undefined) {
                        if (data.ucSubType1 == 1) {
                            unit = locale.count; // 건수
                        } else if (data.ucSubType1 == 2) {
                            unit = locale.traffic; // 트래픽
                        } else if (data.ucSubType1 == 3) {
                            unit = locale.rank; // 순위
                        } else {
                            unit = '';
                        }
                    }
                    ucTypeName = 'General';
                    tableName = 'STAT_EP';
                }
            }
            return params = {
                'unit': unit,
                'ucTypeName': ucTypeName,
                'tableName': tableName,
                'nProtocol': nProtocol
            };
        },
        detectionUseCheck: function (data) {
            if (data == 0) {
                return locale.beNotInUse;
            } else {
                return locale.usedStr;
            }
        },
        detectionLogCheck: function (data) {
            if (data == 0) {
                return locale.logSave;
            } else if (data == 131073) {
                return locale.logSave;
            } else if (data == 131074) {
                return locale.rawDataLogSave;
            }
        },
        nullToString: function (data) {
            if (data == null) {
                return "";
            } else {
                return data;
            }
        },
        nullToHyphen: function (data) {
            if (data == null) {
                return "-";
            } else {
                return data;
            }
        },
        getDayOfWeekStr: function (data) {
            if (data == 1) {
                return locale.sunday;
            } else if (data == 2) {
                return locale.monday;
            } else if (data == 3) {
                return locale.tuesday;
            } else if (data == 4) {
                return locale.wednesday;
            } else if (data == 5) {
                return locale.thursday;
            } else if (data == 6) {
                return locale.friday;
            } else if (data == 7) {
                return locale.saturday;
            }
        },
        // 10진수를 각각 다른 진수로 변환, data는 변환하고자하는 10진수, convert에는 2, 8, 16 등등 진수를 입력 
        bitConvertor: function (data, convert) {
            var result = parseInt(data).toString(convert);
            return result;
        },
        // ip를 8bit 이진수로 변경하기 
        convertIpToBinaryNum: function (ipValue) {
            var splitValue = ipValue.split(".");

            var ipBitValue = '';
            for (var j = 0; j < 4; j++) {
                var convertValue = this.bitConvertor(splitValue[j], 2);
                if (convertValue.length < 8) {
                    var deVal = 8 - parseInt(convertValue.length);
                    var addZero = '';
                    for (var i = 0; i < deVal; i++) {
                        addZero += '0';
                    }
                    ipBitValue = ipBitValue + addZero + convertValue;
                } else {
                    // convertVal가 255일경우 
                    ipBitValue = ipBitValue + convertValue;
                }
            }
            return ipBitValue;
        },
        // 비트값을 이용한 mask 구하기 (비교 ipbit1, 비교ipbit2)
        // 맨앞자리부터 비교, 비교해서 다른값이 나오기전까지의 count가 mask 임
        bitMask: function (toIpBitValue, fromIpBitValue) {
            var count = 0;
            for (var i = 0; i < 32; i++) {
                if (toIpBitValue.charAt(i) == fromIpBitValue.charAt(i)) {
                    count++;
                } else {
                    break;
                }
            }

            return count;
        },
        getAttackTypeName: function (data) {
            var dataName = null;
            if (data == 1) {
                dataName = 'Access_Attempt';
            } else if (data == 2) {
                dataName = 'Backdoor/Trojan';
            } else if (data == 3) {
                dataName = 'DOS/DDOS';
            } else if (data == 4) {
                dataName = 'Information';
            } else if (data == 5) {
                dataName = 'Policy_Violation';
            } else if (data == 6) {
                dataName = 'Probe';
            } else if (data == 7) {
                dataName = 'Protocol_Vulnerability';
            } else if (data == 8) {
                dataName = 'Web_Access';
            } else if (data == 9) {
                dataName = 'Worm/Virus';
            } else if (data >= 99) {
                dataName = 'User Signature';
            }
            return dataName;
        },
        // 매니저 패턴정보 업데이트 결과 변환
        getPatternUpdateInfo: function (data) {
            var result;
            if (data == 0) {
                result = locale.noOperation;
            } else if (data == 1) {
                result = locale.checkForNewUpdates;
            } else if (data == 2) {
                result = locale.newUpdatefileDownloading;
            } else if (data == 3) {
                result = locale.updateWorking;
            } else if (data == 4) {
                result = locale.completedUpdate;
            } else if (data == 5) {
                result = locale.undefinedNewUpdate;
            } else if (data == 6) {
                result = locale.updatedFailure;
            } else {
                // data == 7 
                result = locale.patternVersionInfoIsIncorrect;
            }
            return result;
        },
        /**
         * bps, kbps, mbps, gbps 또는 pps, kpps, mpps, gpps 단위를 제외한 값으로 변경  
         * parameter로 단위 unit과 단위에대한 값 val를 받는다.
         * 
         * bps, pps = n
         * kbps, kpps = k
         * mbps, mpps = m
         * gbps, gpps = g
         * 
         * 단위를 제외한 값을 리턴함
         */
        getFormatExcludingTrafficUnit: function (unit, val) {
            var returnVal = 0;
            if (unit == 'k') {
                returnVal = val * 1000;
            } else if (unit == 'm') {
                returnVal = val * 1000 * 1000;
            } else if (unit == 'g') {
                returnVal = val * 1000 * 1000 * 1000;
            } else {
                returnVal = val;
            }
            return returnVal;
        },
        //탐지이벤트 방향성 표기
        getBoundDetectionEvent: function (wInbound) {
            var strBound;
            var modResult;

            modResult = wInbound % 256;
            switch (modResult) {
                case 3:
                    strBound = "Inbound";
                    break;
                case 2:
                    strBound = "Outbound";
                    break;
                case 0:
                    strBound = "Outbound";
                    break;
                default:
                    strBound = "-";
                    break;
            }
            return strBound;
        },
        // null 일때 'ANY' 텍스트로 변경 
        getNullFormatAny: function (data) {
            if (data == "" || data == null) {
                return data = 'Any';
            } else {
                return data;
            }
        },
        getNullFormatAnyNprotocol: function (data) {
            if (data == "" || data == null) {
                return strValue = 'Any';
            } else {
                if (data == 6) {
                    strValue = 'TCP';
                } else if (data == 17) {
                    strValue = 'UDP';
                } else if (data == 1) {
                    strValue = 'ICMP';
                } else {
                    strValue = 'Any';
                }
                return strValue;
            }
        },
        getZeroFormatAny: function (data) {

            if (data == 0) {
                return data = 'Any';
            } else {
                return data;
            }
        },
        getUrl: function (locationHref) {
            var link = locationHref;
            var location = link.split("#");
            return location[1];
        }
    };
});
