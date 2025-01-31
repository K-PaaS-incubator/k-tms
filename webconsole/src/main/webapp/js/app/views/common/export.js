/**
 * Export
 */
define(function (require) {

    //"use strict";

    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            dataExpression = require('utils/dataExpression'),
            bootstrap = require('bootstrap'),
            locale = require('i18n!nls/str'),
            LoginStatusModel = require('models/loginStatus');	// session 체크	

    var templateFile = null;
    var docTitle = null;
    var listParam = null;
    var model = null;
    var fetchCollection = null;
    var contentTable = null;
    var totalColomn = null;
    var listKeys = null;
    var cssColumn = null;

    var Export = Backbone.View.extend({
        initialize: function (options) {
            this.menuOptions = options.menuOptions;
            this.templateFile = options.templateFile;
            this.docTitle = options.docTitle;
            this.listParam = options.listParam;
            this.model = options.requireModel;
            this.fetchCollection = options.fetchCollection;
            this.contentTable = options.contentTable;
            this.totalColomn = options.totalColomn;
            this.listKeys = options.listKeys;
            this.cssColumn = options.cssColumn;
            this.items = options.collection;
            this.forMat = options.forMat;
            if (typeof options.pageMode == "undefined") {
                this.pageMode = "vertical";
            } else {
                this.pageMode = options.pageMode;
            }

            this.contentBefore = '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">'
                    + '<html>'
                    + '<head>'
                    + '<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">'
                    + '<title>' + this.docTitle + '</title>'
                    + '<style type="text/css">'
                    + '	*    { PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-LEFT: 0px; PADDING-RIGHT: 0px; PADDING-TOP: 0px; }'
                    + '	HTML { WIDTH: 100%; HEIGHT: 100%; padding:0; margin:0;}'
                    + '	BODY { WIDTH: 100%; HEIGHT: 100%; padding:0; margin:0;}'
                    + '	HTML { OVERFLOW-Y: scroll; OVERFLOW-X: hidden; }'
                    + '	BODY { BACKGROUND-COLOR: #fff; WORD-BREAK: break-all; }'
                    + '	BODY { LINE-HEIGHT: 18px; FONT-FAMILY: "ëì", Dotum, Helvetica, AppleGothic, Sans-serif; COLOR: #626262; FONT-SIZE: 11px; }'
                    + '	h2   { padding:5px;}'
                    + '	td   { mso-number-format:"\@"; border:1px solid #ccc;}'
                    + '	TABLE { width: 100%;cell-padding: 0px; cell-spacing: 0px !important; border: 0px;}'
                    + '	table.data > tbody > tr:nth-child(odd) {background-color:#f1f1f1 !important;}'
                    + '	table.data > tbody > tr:nth-child(odd) > td:nth-child(odd) {background-color:#e9e9e9 !important;}'
                    + '	table.data > tbody > tr:nth-child(even) > td:nth-child(odd) {background-color:#f1f1f1 !important;}'
                    + '	table.data > tbody > tr > td {padding: 10px;}'
                    + ' .ref_box { margin-top: 20px;padding: 10px;border: #ccc 1px solid;}'
                    + ' .export-title {width: 100%;padding: 10px;font-weight: bold;font-size: 18px;text-align: center;background: #333;color: #fff;}'
                    + ' .search-title {width: 100px;font-size: 12px;font-weight:bold;background-color: #e9e9e9;text-align: center;}'
                    + '	.search-label {font-size: 12px;font-weight:bold;padding-right: 20px;}'
                    + '	.search-content {font-size: 12px;}'
                    + '	.total-table-data {width: 100px;margin:0px;font-weight: bold;padding: 4px 3px;text-align: center;line-height: 1.5em;word-break:break-all;}'
                    + '	.content-table-header {text-align: center;font-weight: bold;font-size: 12px;padding: 10px;background-color: #E0E0E0;}'
                    + '	.content-table-data {text-align: center;padding: 10px;word-break:break-all;background-color: #E0E0E;}'
                    + '	.text-align-left { text-align: left; }'
                    + '	.text-align-right { text-align: right; }'
                    + '	.text-align-center { text-align: center;  }'
                    + '	.num { text-align: right;}'
                    + '	.width-100 {width:100px;}'
                    + '</style>'
                    + '</head>'
                    + '<body>';

            this.contentAfter = '</body></html>';
        },
        makeFile: function () {

            var self = this;
            var template = self.getTemplate(self.docTitle, self.listParam, self.fetchCollection);	// 테이블 template
            var div = document.createElement('div');

            $(div).append(fetchCollection).find("input").remove();
            var searchDate = self.listParam.startDateInput + '_' + self.listParam.endDateInput;

            if (self.listParam.startDateInput == undefined) {
                //this.title = encodeURI(self.docTitle, 'UTF-8');
                this.title = (self.docTitle);
            } else {
                //this.title = encodeURI(self.docTitle + '(' + searchDate + ')', 'UTF-8');
                this.title = (self.docTitle + '(' + searchDate + ')');
            }
            /**
             * TODO : content post 전송시 약 1500건 이상부터(특정 물자열 크기부터) jsp getParameter 에서 값을 불러오지 못하는 오류 발생
             * excel.jsp, word.jsp, html.jsp 로 post 전송하는 방법이 아닌 파일생성(script window process 호출하여 사용하는 방식으로 변경)
             * 단 해당 윈도우 프로세스 사용이 불가하면 파일생성하여 다운로드 받도록 makeFileDownload()!! 
             */
            /*
             var content = encodeURI(template + $(div).html(), 'UTF-8');
             
             $(div).append($('<form id=\"hiddenForm\" method=\"POST\" action=\"' + self.templateFile + '\" >' +
             '<input type=\"hidden\" name=\"pageMode\" value=\"' + this.pageMode + '\" />' +
             '<input type=\"hidden\" name=\"title\" id=\"title\" value=\"' + this.title + '\" />' +
             '<input type=\"hidden\" name=\"content\" id=\"content\" value=\"' + content + '\" />'));
             $(div).append($('</form>'));
             $('body').append($(div).html());
             */

            if (self.templateFile == 'js/tpl/common/word.jsp') {
                self.title += '.doc';
            } else if (self.templateFile == 'js/tpl/common/html.jsp') {
                self.title += '.html';
            } else {
                self.title += '.xls';
            }

            var content = this.contentBefore + (template + $(div).html()) + this.contentAfter;
            var loginModel = new LoginStatusModel();
            loginModel.fetch({
                url: "requireLogin",
                async: false,
                method: 'POST',
                contentType: 'application/json',
                success: function (m) {
                    if (loginModel.get('loginYN')) {
                        //    $('body #hiddenForm').submit();
                        self.makeFileDownload(content, self.title, 'application/octet-stream');
                    }
                },
            });
        },
        makeFileDownload: function (data, filename, type) {
            var file = new Blob([data], {type: type});
            if (window.navigator.msSaveOrOpenBlob) // IE10+
                window.navigator.msSaveOrOpenBlob(file, filename);
            else { // Others
                var a = document.createElement("a"),
                        url = URL.createObjectURL(file);
                a.href = url;
                a.download = filename;
                document.body.appendChild(a);
                a.click();
                setTimeout(function () {
                    document.body.removeChild(a);
                    window.URL.revokeObjectURL(url);
                }, 0);
            }
        },
        getTemplate: function (docTitle, listParam, fetchCollection) {
            var self = this;
            var div = document.createElement("div");
            var blankTable1 = document.createElement("table");	// 공백 
            var blankTable2 = document.createElement("table");
            var blankTable3 = document.createElement("table");
            var blankTable4 = document.createElement("table");
            var titleTable = document.createElement("table");	// 타이틀
            var conditionTable = document.createElement("table");	// 검색조건
            var br = '<tr><td></td></tr>';
            var searchDate = self.listParam.startDateInput + " ~ " + self.listParam.endDateInput;

            $(blankTable1).append(br);
            $(titleTable).append('<tr><td colspan=\"' + self.totalColomn + '\" class=\"export-title\" >' + docTitle + '</td></tr>');

            $(blankTable2).append(br);

            if (listParam.startDateInput != undefined) {
                $(conditionTable).attr('class', 'data');
                //검색조건 삭제
                //$(conditionTable).append('<tr><td class=\"search-title\">' + locale.searchCondition + '</td><td></<td></tr>');
                $(conditionTable).append('<tr><td><label class=\"search-label\">' + locale.searchSection + '</label><span class=\"search-content\"><td colspan="3">' + searchDate + '</span></td></tr>');
                if (self.menuOptions == "applicationLayer") {
                    $(conditionTable).append('<tr><td><label class=\"search-label\">' + locale.targetType + '</label><span class=\"search-content\"><td colspan="3">' + self.listParam.searchPath + '</span></td></tr>');
                    $(conditionTable).append('<tr><td><label class=\"search-label\">' + locale.type + '</label><span class=\"search-content\"><td colspan="3">' + self.listParam.strbType + '</span></td></tr>');
                }
                $(blankTable3).append(br);
                $(blankTable4).append(br);
            }

            fetchCollection.each(function (m, index) {
                self.model = m;
                if (self.menuOptions == "applicationLayer") {
                    var totalData = self.model.get('lTotCount');
                    var data = self.model.get('sumCount');
                    var count = dataExpression.getFormatTrafficData(totalData);
                    var percentData = dataExpression.getFormatPercentData(totalData, data);
                    var nType = dataExpression.getbTypeName(self.model.get('nType'));
                    self.model.set({
                        nType: nType,
                        sumCount: percentData,
                        totalCount: count
                    });
                }
                var tdData = '';

                if (self.menuOptions == "application" || self.menuOptions == "fileMeta") {
                    var bIpType = self.model.get('bIpType');
                    if (bIpType == 6) {
                        self.dwSourceIp = self.model.get('strSourceIp');
                        self.deDestinationIp = self.model.get('strDestinationIp');
                    } else {
                        self.dwSourceIp = self.model.get('dwSourceIp');
                        self.deDestinationIp = self.model.get('deDestinationIp');
                    }
                    var bType = self.model.get('bType');
                    var dwFileSize = self.model.get('dwFileSize');
                    switch (bType) {
                        case 1:
                            self.strBtype = "http";
                            break;
                        case 2:
                            self.strBtype = "dns";
                            break;
                        case 3:
                            self.strBtype = "tls";
                            break;
                        case 4:
                            self.strBtype = "smtp";
                            break;
                        case 5:
                            self.strBtype = "ftp";
                            break;
                        default:
                        	self.strBtype = "";
                    }

                    if (self.model.get('nProtocol') == 0) {
                        self.strProtocol = "MPLS";
                    } else if (self.model.get('nProtocol') == 1) {
                        self.strProtocol = "ICMP";
                    } else if (self.model.get('nProtocol') == 2) {
                        self.strProtocol = "ARP";
                    } else if (self.model.get('nProtocol') == 3) {
                        self.strProtocol = "RARP";
                    } else if (self.model.get('nProtocol') == 4) {
                        self.strProtocol = "LLC";
                    } else if (self.model.get('nProtocol') == 6) {
                        self.strProtocol = "TCP";
                    } else if (self.model.get('nProtocol') == 17) {
                        self.strProtocol = "UDP";
                    }

                    if (dwFileSize > 1000) {
                        self.dwFileSize = self.round(dwFileSize / 1024, 0) + ' KB';
                    }

                    self.model.set({
                        dwSourceIp: self.dwSourceIp,
                        deDestinationIp: self.deDestinationIp,
                        bType: self.strBtype,
                        nProtocol: self.strProtocol,
                        dwFileSize: self.dwFileSize
                    });
                }

                if (self.menuOptions == "service") {
                    if (self.model.get('nProtocol') == 0) {
                        self.strProtocol = "MPLS";
                    } else if (self.model.get('nProtocol') == 1) {
                        self.strProtocol = "ICMP";
                    } else if (self.model.get('nProtocol') == 2) {
                        self.strProtocol = "ARP";
                    } else if (self.model.get('nProtocol') == 3) {
                        self.strProtocol = "RARP";
                    } else if (self.model.get('nProtocol') == 4) {
                        self.strProtocol = "LLC";
                    } else if (self.model.get('nProtocol') == 6) {
                        self.strProtocol = "TCP";
                    } else if (self.model.get('nProtocol') == 17) {
                        self.strProtocol = "UDP";
                    }
                    self.model.set({
                        nProtocol: self.strProtocol
                    });
                }

                if (self.menuOptions == "sessionLog") {
                    var networkName = self.model.get('srcNetName') ? self.model.get('srcNetName') : self.model.get('dstNetName') ? self.model.get('dstNetName') : '-';
                    self.model.set({
                        networkName: networkName
                    });
                }

                if (self.menuOptions == "session") {
                    if (self.model.get('serverPort') == 20) {
                        self.strServerPort = "FTP-DATA";
                    } else if (self.model.get('serverPort') == 21) {
                        self.strServerPort = "FTP";
                    } else if (self.model.get('serverPort') == 22) {
                        self.strServerPort = "SSH";
                    } else if (self.model.get('serverPort') == 23) {
                        self.strServerPort = "TELNET";
                    } else if (self.model.get('serverPort') == 25) {
                        self.strServerPort = "SMTP";
                    } else if (self.model.get('serverPort') == 80) {
                        self.strServerPort = "HTTP";
                    } else if (self.model.get('serverPort') == 110) {
                        self.strServerPort = "POP3";
                    } else if (self.model.get('serverPort') == 143) {
                        self.strServerPort = "IMAP";
                    } else if (self.model.get('serverPort') == 443) {
                        self.strServerPort = "HTTPS";
                    } else if (self.model.get('serverPort') == 1863) {
                        self.strServerPort = "MSNP";
                    }
                    self.model.set({
                        serverPort: self.strServerPort
                    });
                }
                if (self.menuOptions == "prevention") {
                    if (self.model.get('luserGroupIndex') == 1) {
                        self.luserGroupIndex = locale.admin;
                    } else if (self.model.get('luserGroupIndex') == 0) {
                        self.luserGroupIndex = locale.user;
                    } /// 1: 관리자, 2: 사용자            
                    if (self.model.get('strAlarmType') == "1000000000") {
                        self.strAlarmType = locale.mailSend;
                    } else if (self.model.get('strAlarmType') == "0100000000") {
                        self.strAlarmType = locale.smsSend;
                    } else if (self.model.get('strAlarmType') == "0010000000") {
                        self.strAlarmType = locale.emergencyAlert;
                    } else if (self.model.get('strAlarmType') == "0001000000") {
                        self.strAlarmType = locale.visualAural;
                    } else if (self.model.get('strAlarmType') == "100000000") {
                        self.strAlarmType = locale.programAlert;
                    }
                    //경고행위타입 0:행위 OFF, 1: 행위 ON 첫 번째 문자:긴급알람,두 번째 문자:메일전송, 세 번째 문자 : SMS 전송
                    // 메일 전송 1000000000
                    // SMS 전송 0100000000
                    // 긴급 알람 0010000000
                    // 가시가청 0001000000

                    // 경보결과 0: 실패, 1: 성공
                    if (self.model.get('nStatus') == 1) {
                        self.nStatus = locale.success;
                    } else if (self.model.get('nStatus') == 0) {
                        self.nStatus = locale.fail;
                    }
                    if (self.model.get('nType') == 1) {
                        self.nType = locale.auditLog;
                    } else if (self.model.get('nType') == 2) {
                        self.nType = locale.anomaly;
                    } else if (self.model.get('nType') == 3) {
                        self.nType = locale.maliciousHost;
                    } else if (self.model.get('nType') == 4) {
                        self.nType = locale.managementHost;
                    } else if (self.model.get('nType') == 5) {
                        self.nType = locale.prevention;
                    } else if (self.model.get('nType') == 6) {
                        self.nType = locale.intrusionDetectionPolicy;
                    } else if (self.model.get('nType') == 100) {
                        self.nType = locale.userDefinedMessage;
                    }
                    self.model.set({
                        luserGroupIndex: self.luserGroupIndex,
                        strAlarmType: self.strAlarmType,
                        nStatus: self.nStatus,
                        nType: self.nType
                    });
                }

                if (self.menuOptions == "originalLog") {
                    var srcNetworkName = self.model.get('srcNetworkName') ? self.model.get('srcNetworkName') : '-';
                    var dstNetworkName = self.model.get('dstNetworkName') ? self.model.get('dstNetworkName') : '-';
                    var vsensorName = self.model.get('vsensorName') ? self.model.get('vsensorName') : '-';
                    var sensorName = self.model.get('sensorName') ? self.model.get('sensorName') : '-';

//                        if (self.model.get('wInbound') == 771) {
//                            self.strBound = "Ingress";
//                        } else if (self.model.get('wInbound') == 770) {
//                            self.strBound = "Egress";
//                        } else if (self.model.get('wInbound') == 768) {
//                            self.strBound = "-";
//                        }
                    self.strBound = dataExpression.getBoundDetectionEvent(self.model.get('wInbound'));
                    if (self.model.get('nProtocol') == 6) {
                        self.strProtocol = "TCP";
                    } else if (self.model.get('nProtocol') == 17) {
                        self.strProtocol = "UDP";
                    } else {
                        self.strProtocol = "ICMP";
                    }
                    self.model.set({
                        srcNetworkName: srcNetworkName,
                        dstNetworkName: dstNetworkName,
                        vsensorName: vsensorName,
                        sensorName: sensorName,
                        wInbound: self.strBound,
                        nProtocol: self.strProtocol
                    });
                }

                if (self.menuOptions == "auditLog") {
                    if (self.model.get('ltype1') == 1) {
                        self.strlType = locale.dashboard.auditAction;
                    } else if (self.model.get('ltype1') == 2) {
                        self.strlType = locale.dashboard.auditError;
                    } else if (self.model.get('ltype1') == 3) {
                        self.strlType = locale.dashboard.auditWarning;
                    }
                    self.model.set({
                        ltype1: self.strlType
                    });
                }

                var num_chk = ["bps", "ingressBps", "egressBps", "pps", "ingressPps", "egressPps", "sumBps", "sumPps"];
                var width_chk = ["nProtocol", "wService"];
                var class_type = "content-table-data";
                var ratio_chk = ["sumBps", "sumPps"];

                // <td>에 데이터 삽입
                for (var i = 1; i < self.totalColomn + 1; i++) {		// 총 건수count 용
                    class_type = "content-table-data";
                    var flag = 'false';
                    for (var j = 0; j < self.forMat.length; j++) {
                        if (self.forMat[j] == self.listKeys[i]) {
                            flag = 'true';
                            break;
                        }
                    }

                    if (self.menuOptions == "originalLog") {
                        if (flag == 'true') {
                            tdData += _.isNull(self.model.get(self.listKeys[i])) ? ' ' : self.model.get(self.listKeys[i]);
                        } else {
                            tdData += _.isNull(self.model.get(self.listKeys[i])) ? ' ' : self.model.get(self.listKeys[i]);
                        }
                    }

                    if (self.menuOptions == "institution") {		// 기관 
                        var tab = "";
                        if (self.model.get('lParentGroupIndex') != -1 && i == 1) {
                            tab = " > ";
                        }
                        if ($.inArray(self.listKeys[i], num_chk) != -1)
                            class_type += " num";
                        tdData += '<td class=\"' + class_type + '\"  name=\"' + self.listKeys[i] + '\" >';
                        if (flag == 'true') {
                            tdData += _.isNull(self.model.get(self.listKeys[i])) ? ' ' : tab + dataExpression.getFormatTrafficData(self.model.get(self.listKeys[i]));
                        } else {
                            tdData += _.isNull(self.model.get(self.listKeys[i])) ? ' ' : tab + self.model.get(self.listKeys[i]);
                        }
                    } else {
                        if ($.inArray(self.listKeys[i], num_chk) != -1)
//                            class_type += " num";
                        if ($.inArray(self.listKeys[i], width_chk) != -1)
                            class_type += " width-100";
                        tdData += '<td class=\"' + class_type + '\"  name=\"' + self.listKeys[i] + '\" data-option=\"' + self.listKeys[i] + '\">';
                        if (flag == 'true') {
                            tdData += _.isNull(self.model.get(self.listKeys[i])) ? ' ' : dataExpression.getFormatTrafficData(self.model.get(self.listKeys[i]));
                        } else {
                            tdData += _.isNull(self.model.get(self.listKeys[i])) ? ' ' : self.renderContent(self.model.get(self.listKeys[i]));
                        }
                    }
                    tdData += '</td>';
                }

                // 유해트래픽은 <tr>을 따로 넣음 
                if (self.menuOptions == 'malicious') {
                    for (var i = 0; i < self.items.length; i++) {
                        if ($.inArray(self.listKeys[i], num_chk) != -1)
                            class_type += " num";
                        $(self.contentTable).attr('class', 'data');
                        $(self.contentTable).append('<tr>' + tdData + '</tr><tr>' + '<td class=\"content-table-data\" >' + locale.maliciousTraffic + '</td>' + '<td class=\"' + class_type + '\" >' + dataExpression.getFormatTrafficData(self.items.at(i).get('malBps')) + '</td>' + '<td class=\"' + class_type + '\" >' + dataExpression.getFormatTrafficData(self.items.at(i).get('malIngressBps')) + '<td class=\"' + class_type + '\" >' + dataExpression.getFormatTrafficData(self.items.at(i).get('malEgressBps')) + '</td>' + '<td class=\"' + class_type + '\" >' + dataExpression.getFormatTrafficData(self.items.at(i).get('malPps')) + '<td class=\"' + class_type + '\" >' + dataExpression.getFormatTrafficData(self.items.at(i).get('malIngressPps')) + '</td>' + '<td class=\"' + class_type + '\" >' + dataExpression.getFormatTrafficData(self.items.at(i).get('malEgressPps')) + '</td>' + '</tr>'); // 유해트래픽 일경우 <tr> 한줄 더 추가
                    }
                } else {
                    $(self.contentTable).attr('class', 'data');
                    $(self.contentTable).append('<tr>' + tdData + '</tr>');
                }
            });

            $(div).append($(blankTable1));			// 공백  <table>
            $(div).append($(titleTable));			// 문서타이틀 <table>
            $(div).append($(blankTable2));			// 공백 <table>
            $(div).append($(conditionTable));		// 조회조건 <table>
            $(div).append($(blankTable3));			// 공백 <table>
            $(div).append($(self.contentTable));	// 데이터
            return $(div).html();
        },
        renderContent: function (str) {
            if (typeof str == "undefined") {
                str = " ";
            }
            var contentArray = str.toString().split(".....");
            var content = '';
            var referenceContent = '';
            var return_str;

            for (var i = 0; contentArray.length > i; i++) {
                if (contentArray.length == (parseInt(i) + parseInt(1))) {
                    var referenceContentArray = contentArray[i].split('..');
                    for (var j = 0; referenceContentArray.length > j; j++) {
                        referenceContent += referenceContentArray[j] + '<br/>';
                    }
                } else {
                    content += ('<br/>' + contentArray[i] + '.');
                }
            }

            if (content != "" && referenceContent != "") {
                return_str = content + "<div class=\"ref_box\"><strong>" + locale.reference + "</strong></br>" + referenceContent + "</div>";
            } else {
                return_str = referenceContent;
            }

            return return_str;
        },
        getStrName: function (networkName, vsensorName) {

            var strName = null;
            if (networkName == null && vsensorName == null) {
                strName = locale.all;
            } else if (networkName != null && vsensorName == null) {
                strName = locale.network + "(" + networkName + ")";
            } else if (networkName == null && vsensorName != null) {
                strName = locale.vsensor + "(" + vsensorName + ")";
            }
            return strName;
        },
        round: function (num, ja) {
            var dataResult;

            ja = Math.pow(10, ja);
            dataResult = Math.round(num * ja) / ja;
            return dataResult;
        },
        numberFormat: function (num) {
            var dataResult;
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
            } else {
                dataResult = num;
            }
            return dataResult;
        }
    });

    return Export;

});
