define(function (require) {

    var $ = require('jquery'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            alertMessage = require('utils/AlertMessage'),
            RawPacketModel = require('models/detectionAnalysis/rawPacketModel'),
            PacketEncodingStrModel = require('models/detectionAnalysis/packetEncodingStrModel'), // 패킷 데이터 character Model
            ACPExport = require('views/common/acpExport');							// 내보내기 기능 추가
    var rawPacketView = require('text!tpl/popup/rawPacketPopupTemplate.html');

    return Backbone.View.extend({
        className: 'tab-content',
        rawPacketTemplate: _.template(rawPacketView), // 팝업 뷰	
        packetHexaTable: [], // packet 테이블
        eventTarget: "",
        chooseClipBoardText: "", // 선택 영역 클립보드 
        macHeaderChildTemplate: _.template([
            '<li class="macTarget">' +
                    '<a class="macTag">' +
                    '- <a class="dstMac" id="dstMacHighLight"></a>' +
                    '</a>' +
                    '</li>' +
                    '<li class="macTarget">' +
                    '<a class="macTag">' +
                    '- <a class="srcMac" id="srcMacHighLight"></a>' +
                    '</a>' +
                    '</li>' +
                    '<li class="macTarget">' +
                    '<a class="macTag">' +
                    '- <a class="etherType" id="etherTypeHighLight">' +
                    '</a>' +
                    '</a>' +
                    '</li>'].join('')),
        ipv4HeaderChildTemplate: _.template(['<li class="ipv4header" >' +
                    '<a>- <%=locale.lowerRank%> </a>' +
                    '</li>'].join('')),
        ipv4HeaderParentTemplate: _.template(['<li class="ipv4Target">' +
                    '<a class="ipv4Tag">' +
                    '- <a class="ipv4Version" id="ipv4Version"></a>' +
                    '</a>' +
                    '</li>',
            '<li class="ipv4Target">' +
                    '<a class="ipv4Tag">' +
                    '- <a class="ipHdrLength" id="ipHdrLength"></a>' +
                    '</a>' +
                    '</li>',
            '<li class="ipv4ParentHeader">' +
                    '<a id="typeOfService">' +
                    '<span class="icon-plus-sign"></span>- Type of Service = <a class="typeOfService" ></a>' +
                    '</a>' +
                    '<ul>' +
                    '<li>' +
                    '<a id="typeOfServicePrecedence">- <a class="typeOfServicePrecedence" id="typeOfServicePrecedence" />. .... = <a class="typeOfServicePrecedenceText" id="typeOfServicePrecedenceText"/> <a class="typeOfServiceValue" id="typeOfServiceValue" /></a>' +
                    '</li>' +
                    '<li>' +
                    '<a id="typeOfServiceDelay">- ...<a class="typeOfServiceDelay" id="typeOfServiceDelay"></a> .... = <a class="typeOfServiceDelayText" id="typeOfServiceDelayText" /> <a class="typeOfServiceDelay" /> </a>' +
                    '</li>' +
                    '<li>' +
                    '<a id="typeOfServiceThrt">- .... <a class="typeOfServiceThrt" id="typeOfServiceThrt"></a>... = <a class="typeOfServiceThrtText" id="typeOfServiceThrtText" /> <a class="typeOfServiceThrt" /> </a>' +
                    '</li>' +
                    '<li>' +
                    '<a id="typeOfServiceRelty">- .... .<a class="typeOfServiceRelty" id="typeOfServiceRelty"></a>.. = <a class="typeOfServiceReltyText" id="typeOfServiceReltyText" /> <a class="typeOfServiceRelty" /> </a>' +
                    '</li>' +
                    '<li>' +
                    '<a id="typeOfServiceFuture">- .... ..<a class="typeOfServiceFuture" id="typeOfServiceFuture"></a>. = <a class="typeOfServiceFutureText" id="typeOfServiceFutureText"  /> <a class="typeOfServiceFuture" /> </a>' +
                    '</li>' +
                    '</ul>' +
                    '</li>',
            '<li>' +
                    '<a>' +
                    '- <a class="totalLength" id="totalLength"></a>' +
                    '</a>' +
                    '</li>',
            '<li>' +
                    '<a>' +
                    '- <a class="identification" id="identification"></a>' +
                    '</a>' +
                    '</li>',
            '<li class="ipv4ParentHeader">' +
                    '<a id="ipFlags">' +
                    '<span class="icon-plus-sign"></span>- Flags = <a class="ipFlags" id="ipFlags"></a>' +
                    '</a>' +
                    '<ul>' +
                    '<li>' +
                    '<a id="mustFlag">- 0... ... = must be 0 </a>' +
                    '</li>' +
                    '<li>' +
                    '<a id="dontFragFlagAll">- .<a class="dontFragFlag" id="dontFragFlag"/><a id="dontFragFlag">.. ...</a> = <a class="dontFragFlagText" id="dontFragFlagText" /></a>' +
                    '</li>' +
                    '<li>' +
                    '<a id="moreFragFlagAll">- ..<a class="moreFragFlag" id="moreFragFlag"/><a id="moreFragFlag">. ...</a> = <a class="moreFragFlagText" id="moreFragFlagText" /></a>' +
                    '</li>' +
                    '</ul>' +
                    '</li>',
            '<li>' +
                    '<a class="fragOffset" id="fragOffset">- </a>' +
                    '</li>' +
                    '<li>' +
                    '<a class="timeTolive" id="timeTolive">- </a>' +
                    '</li>' +
                    '<li>' +
                    '<a class="protocol" id="protocol">- </a>' +
                    '</li>' +
                    '<li>' +
                    '<a class="checkSum" id="checkSum">- </a>' +
                    '</li>' +
                    '<li>' +
                    '<a class="srcAddress" id="srcAddress">- </a>' +
                    '</li>' +
                    '<li>' +
                    '<a class="destAddress" id="destAddress">- </a>' +
                    '</li>'].join('')),
        ipv6HeaderParentTemplate: _.template(['<li class="ipv6Target">' +
                    '<a class="ipv6Tag">' +
                    '- <a class="ipv6Version" id="ipv6Version"></a>' +
                    '</a>' +
                    '</li>' +
                    '<li class="ipv6Target">' +
                    '<a class="ipv6Tag">' +
                    '- <a class="trafficClass" id="trafficClass"></a>' +
                    '</a>' +
                    '</li>' +
                    '<li class="ipv6Target">' +
                    '<a class="ipv6Tag">' +
                    '- <a class="flowLabel" id="flowLabel"></a>' +
                    '</a>' +
                    '</li>' +
                    '<li class="ipv6Target">' +
                    '<a class="ipv6Tag">' +
                    '- <a class="payloadLength" id="payloadLength"></a>' +
                    '</a>' +
                    '</li>' +
                    '<li class="ipv6Target">' +
                    '<a class="ipv6Tag">' +
                    '- <a class="nextHeader" id="nextHeader"></a>' +
                    '</a>' +
                    '</li>' +
                    '<li class="ipv6Target">' +
                    '<a class="ipv6Tag">' +
                    '- <a class="hopLimit" id="hopLimit"></a>' +
                    '</a>' +
                    '</li>' +
                    '<li class="ipv6Target">' +
                    '<a class="ipv6Tag">' +
                    '- <a class="ipv6SourceIp" id="ipv6SourceIp"></a>' +
                    '</a>' +
                    '</li>' +
                    '<li class="ipv6Target">' +
                    '<a class="ipv6Tag">' +
                    '- <a class="ipv6DestIp" id="ipv6DestIp"></a>' +
                    '</a>' +
                    '</li>'].join('')),
        // ICMP
        icmpHeaderParentTemplate: _.template(['<li class="icmpTarget"><a class="icmpTag">- <a class="icmpType" 		id="icmpType"></a> </a></li>',
            '<li class="icmpTarget"><a class="icmpTag">- <a class="icmpCode" 		id="icmpCode"></a> </a></li>',
            '<li class="icmpTarget"><a class="icmpTag">- <a class="icmpChecksum" 	id="icmpChecksum"></a> </a></li>',
            '<li class="icmpTarget"><a class="icmpTag">- <a class="icmpIdentifier"	id="icmpIdentifier"></a> </a></li>',
            '<li class="icmpTarget"><a class="icmpTag">- <a class="icmpSequence"	id="icmpSequence"></a> </a></li>'].join('')),
        icmpHeaderChildTemplate: _.template('<li class="icmpheader" id="icmpheader"><a>- <%=locale.lowerRank%> </a></li>'),
        // TCP
        tcpHeaderParentTemplate: _.template(['<li class="tcpTarget"><a class="tcpTag">- <a class="tcpSrcPort"		id="tcpSrcPort" ></a> </a></li>',
            '<li class="tcpTarget"><a class="tcpTag">- <a class="tcpDstPort"		id="tcpDstPort" ></a> </a></li>',
            '<li class="tcpTarget"><a class="tcpTag">- <a class="seqNo"			id="seqNo" ></a> </a></li>',
            '<li class="tcpTarget"><a class="tcpTag">- <a class="ackNo"			id="ackNo" ></a> </a></li>',
            '<li class="tcpTarget"><a class="tcpTag">- <a class="tcpHeaderLen"		id="tcpHeaderLen" ></a> </a></li>',
            '<li class="tcpTarget"><a class="tcpTag">- <a class="reserved"			id="reserved" ></a> </a></li>',
            '<li class="tcpParentHeader">' +
                    '<a id="tcpFlags"><span class="icon-plus-sign"></span>- Flags = <a class="tcpFlags" id="tcpFlags"></a></a>' +
                    '<ul>' +
                    '<li><a id="urgFlagAll">- ..<a class="urgFlag" id="urgFlag"/>. .... = <a class="urgFlagText" id="urgFlagText" /> </a></li>' +
                    '<li><a id="ackFlagAll">- ...<a class="ackFlag" id="ackFlag"/> .... = <a class="ackFlagText" id="ackFlagText" /> </a></li>' +
                    '<li><a id="pshFlagAll">- .... <a class="pshFlag" id="pshFlag"/>... = <a class="pshFlagText" id="pshFlagText" /> </a></li>' +
                    '<li><a id="rstFlagAll">- .... .<a class="rstFlag" id="rstFlag"/>.. = <a class="rstFlagText" id="rstFlagText" /> </a></li>' +
                    '<li><a id="synFlagAll">- .... ..<a class="synFlag" id="synFlag"/>. = <a class="synFlagText" id="synFlagText" /> </a></li>' +
                    '<li><a id="finFlagAll">- .... ...<a class="finFlag" id="finFlag"/> = <a class="finFlagText" id="finFlagText" /> </a></li>' +
                    '</ul>' +
                    '</li>' +
                    '<li><a class="windowSize"	id="windowSize" >- </a></li>' +
                    '<li><a class="tcpChecksum" id="tcpChecksum" >- </a></li>' +
                    '<li><a class="urgentPtr" id="urgentPtr" >- </a></li>' +
                    '<li><a class="options" id="options" >- </a></li>'
        ].join('')),
        tcpHeaderChildTemplate: _.template('<li class="tcpHeader" class="tcpHeader" ><a>- <%=locale.lowerRank%> </a></li>'),
        // UDP
        udpHeaderParentTemplate: _.template(['<li class="udpTarget"><a class="udpTag">- <a class="udpSrcPort"	id="udpSrcPort"	 ></a> </a></li>',
            '<li class="udpTarget"><a class="udpTag">- <a class="udpDstPort"	id="udpDstPort"	 ></a> </a></li>',
            '<li class="udpTarget"><a class="udpTag">- <a class="udpLength"	id="udpLength"	 ></a> </a></li>',
            '<li class="udpTarget"><a class="udpTag">- <a class="udpChecksum"	id="udpChecksum" ></a> </a></li>'
        ].join('')),
        udpHeaderChildTemplate: _.template('<li class="udpHeader" id="udpHeader"><a>- <%=locale.lowerRank%> </a></li>'),
        initialize: function (options) {
            options = options || {};
            this.model = options.model;
            this.searchCondition = options.searchCondition;
            this.rawPacketModel = new RawPacketModel();
            this.packetEncodingStrModel = new PacketEncodingStrModel();
            this.menuType = options.menuType;
        },
        events: {
            'mouseup .drag': 'getMouseup',
            'click .btnAcp': 'callMakeAcp',
            "click a": "setHighLightTarget",
            "change #packetCharacterSet": "setStrDataEncodingChange",
            "click #drag-button": "getDragCopyBinary",
            "click #copy-button": "getPacketCopyAscii",
        },
        getMouseup: function (e) {
            var self = this;
            var temp = "";
            if (document.getSelection) {
                temp = document.getSelection();
            } else if (document.selection) {
                temp = document.seletion.createRange().text;
            }
            self.chooseClipBoardText = temp.toString();
        },
        getDragCopyBinary: function (event) {
            if (this.chooseClipBoardText == '') {
                alertMessage.infoMessage('복사 영역을 선택해주세요.', 'info', '', 'small');
                return false;
            }
            document.execCommand("Copy");
            alertMessage.infoMessage('복사 되었습니다.', 'info', '', 'small');
        },
        callMakeAcp: function () {
            var self = this;
            new ACPExport({
                fileName: self.$('.security-view-title').text(), // 공통
                content: self.rawPacketModel.get('binaryString')  // file 명 (출력메뉴)

            }).makeFile();
        },
        render: function () {
            var self = this;

            // template 바인딩
            self.$el.html(self.rawPacketTemplate({
                locale: locale,
                model: self.model.toJSON(),
                rawPacketModel: self.rawPacketModel.toJSON(),
                ipType: self.searchCondition.ipType
            }));

            self.$(".tree ul.macHeader").append(self.macHeaderChildTemplate());
            self.$(".tree ul.ipv4Header").append(self.ipv4HeaderParentTemplate());
            self.$(".tree ul.ipv6Header").append(self.ipv6HeaderParentTemplate());

            // type 별 트리 생성
            if (self.model.get('nProtocol') == 1) {
                // ICMP
                self.$(".tree ul.icmpHeader").append(self.icmpHeaderParentTemplate());
            } else if (self.model.get('nProtocol') == 6) {
                // TCP
                self.$(".tree ul.tcpHeader").append(self.tcpHeaderParentTemplate());
            } else if (self.model.get('nProtocol') == 17) {
                // UDP
                self.$(".tree ul.udpHeader").append(self.udpHeaderParentTemplate());
            } 
            $(".modal-body .modal-sub").removeClass('nodata');

            $.when(self.getRawPacketData()).done(function () {
            	console.log('getRawPacketData');
            });

            return self;
        },
        getRawPacketData: function () {
            var self = this;

            self.rawPacketModel.fetch({
                method: 'POST',
                url: 'api/selectRawPacketPopup',
                contentType: 'application/json',
                data: JSON.stringify(self.searchCondition),
                //async: false,
                beforeSend: function () {
                    Backbone.Loading.setLoading($('#rawPacketPopup', self.el));
                },
                success: function (model) {
                    self.packetEncodingStrModel = self.rawPacketModel;
                    // 패킷데이터 . 치환
                    self.packetEncodingStrModel.set({
                        strData: self.rawPacketModel.get('strData').replace(/[^\u0021-\u007E]/g, '.') // 기본 - US-ASCII
                    });
                    self.setBinaryData();
                    self.setRawPacketData();
                    self.setBinaryStrData();
                },
                complete: function () {
                    Backbone.Loading.removeLoading($('#rawPacketPopup', self.el));
                }
            });
        },
        getPacketCopyAscii: function () {
            var el = document.getElementById('clipboard-text');
            var range = document.createRange();
            range.selectNodeContents(el);
            var sel = window.getSelection();
            sel.removeAllRanges();
            sel.addRange(range);
            document.execCommand('copy');
            alertMessage.infoMessage('복사 되었습니다.', 'info', '', 'small');
        },
        // strData 인코딩 변경
        setStrDataEncodingChange: function (e) {
            var self = this;
            _.extend(self.searchCondition, {
                'encodingType': e.target.value
            });
            self.packetEncodingStrModel.fetch({
                method: 'POST',
                url: 'api/selectRawPacketPopup',
                contentType: 'application/json',
                async: false,
                data: JSON.stringify(self.searchCondition),
                success: function (model) {
                    // 패킷데이터 . 치환
                    self.packetEncodingStrModel.set({
                        strData: self.packetEncodingStrModel.get('strData').replace(/[^\u0021-\u007E]/g, '.') // 기본 - US-ASCII
                    });
                    self.setBinaryStrData();
                    self.setHighLight();
                }
            });
        },
        setRawPacketData: function () {
            var self = this;
            // 이더넷 타입
            var frameType = self.rawPacketModel.get('datalink').frameType.toString(16).length < 4 ?
                    "0" + self.rawPacketModel.get('datalink').frameType.toString(16) + "h" :
                    self.rawPacketModel.get('datalink').frameType.toString(16) + "h";

            //////////////////////////////////////////////-- 공통 시작 -- /////////////////////////////////////////////
            // Mac Header 
            $(".dstMac", self.el).append('Destination=' + self.rawPacketModel.get('datalink').strDstMac);
            $(".srcMac", self.el).append('Source=' + self.rawPacketModel.get('datalink').strSrcMac);
            $(".etherType", self.el).append('Ethertype=' + frameType);

            // IPv4 Header
            $(".ipv4Version", self.el).append('Version = ' + self.rawPacketModel.get('version'));
            $(".ipHdrLength", self.el).append('Header Length = ' + self.rawPacketModel.get('ipHdrLength') + ' bytes');

            if (self.rawPacketModel.get('version') == 4) {
                // TOS Hexa value
                var typeOfServiceHex = "";
                if (self.rawPacketModel.get('ipv4Header').typeOfService == '0') {
                    typeOfServiceHex = self.rawPacketModel.get('ipv4Header').typeOfService.toString(16) + "0";
                } else {
                    typeOfServiceHex = self.rawPacketModel.get('ipv4Header').typeOfService.toString(16);
                }
                $(".typeOfService", self.el).append(typeOfServiceHex + 'h');
                /*								
                 000 - Routine
                 111 - Network Control
                 110 - Internetwork Control
                 101 - CRITIC/ECP
                 100 - Flash Override
                 011 - Flash
                 010 - Immediate
                 001 - Priority
                 */
                var typeOfServicePrecedence = self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence < 10 ? "00" + self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence :
                        self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence < 100 ? "0" + self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence :
                        self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence;
                var typeOfServicePrecedenceText = "";
                if (self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '0') {
                    typeOfServicePrecedenceText = "Routine";
                } else if (self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '10' || self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '12' || self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '14') {
                    typeOfServicePrecedenceText = "Priority";
                } else if (self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '18' || self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '20' || self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '22') {
                    typeOfServicePrecedenceText = "Immediate";
                } else if (self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '26' || self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '28' || self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '30') {
                    typeOfServicePrecedenceText = "Flash";
                } else if (self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '34' || self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '36' || self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '38') {
                    typeOfServicePrecedenceText = "Flash Override";
                } else if (self.rawPacketModel.get('ipv4Header').typeOfServicePrecedence == '46') {
                    typeOfServicePrecedenceText = "CRITIC/ECP";
                } else {
                    typeOfServicePrecedenceText = "default";
                }
                $(".typeOfServicePrecedence", self.el).append(typeOfServicePrecedence);
                $(".typeOfServicePrecedenceText", self.el).append(typeOfServicePrecedenceText);
                $(".typeOfServiceValue", self.el).append(self.rawPacketModel.get('ipv4Header').typeOfService);

                var typeOfServiceDelay = self.rawPacketModel.get('ipv4Header').typeOfServiceDelay;
                var typeOfServiceThrt = self.rawPacketModel.get('ipv4Header').typeOfServiceThrt;
                var typeOfServiceRelty = self.rawPacketModel.get('ipv4Header').typeOfServiceRelty;
                var typeOfServiceFuture = self.rawPacketModel.get('ipv4Header').typeOfServiceFuture;
                var typeOfServiceDelayText = "";
                var typeOfServiceThrtText = "";
                var typeOfServiceReltyText = "";
                var typeOfServiceFutureText = "Reserved for Future Use.";

                if (typeOfServiceDelay == '0') {
                    typeOfServiceDelayText = "Normal Delay";
                } else {
                    typeOfServiceDelayText = "Low Delay";
                }
                $(".typeOfServiceDelay", self.el).append(typeOfServiceDelay);
                $(".typeOfServiceDelayText", self.el).append(typeOfServiceDelayText);

                // typeOfServiceThrt
                if (typeOfServiceThrt == '0') {
                    typeOfServiceThrtText = "Normal Throughput";
                } else {
                    typeOfServiceThrtText = "High Throughput";
                }
                $(".typeOfServiceThrt", self.el).append(typeOfServiceThrt);
                $(".typeOfServiceThrtText", self.el).append(typeOfServiceThrtText);

                // typeOfServiceRelty
                if (typeOfServiceRelty == '0') {
                    typeOfServiceReltyText = "Normal Relibility";
                } else {
                    typeOfServiceReltyText = "High Relibility.";
                }
                $(".typeOfServiceRelty", self.el).append(typeOfServiceRelty);
                $(".typeOfServiceReltyText", self.el).append(typeOfServiceReltyText);

                // typeOfServiceFuture
                $(".typeOfServiceFuture", self.el).append(typeOfServiceFuture);
                $(".typeOfServiceFutureText", self.el).append(typeOfServiceFutureText);

                $(".totalLength", self.el).append('Total Length = ' + self.rawPacketModel.get('ipv4Header').ipLength + ' bytes');
                $(".identification", self.el).append('Identification = ' + self.rawPacketModel.get('ipv4Header').identification);
                $(".ipFlags", self.el).append(self.rawPacketModel.get('ipv4Header').ipFlags + "h");

                var dontFragFlag = self.rawPacketModel.get('ipv4Header').dontFragFlag == true ? 1 : 0;
                $(".dontFragFlag", self.el).append(dontFragFlag);
                if (dontFragFlag == 1) {
                    $(".dontFragFlagText", self.el).append("Don't Fragment");
                } else {
                    $(".dontFragFlagText", self.el).append('May Fragment');
                }

                var moreFragFlag = self.rawPacketModel.get('ipv4Header').moreFragFlag == true ? 1 : 0;
                $(".moreFragFlag", self.el).append(moreFragFlag);
                if (moreFragFlag == 1) {
                    $(".moreFragFlagText", self.el).append("More Fragments");
                } else {
                    $(".moreFragFlagText", self.el).append('Last Fragment');
                }

                $(".fragOffset", self.el).append('Fragment offset = ' + self.rawPacketModel.get('ipv4Header').fragOffset + "bytes");
                $(".timeTolive", self.el).append('Time to live = ' + self.rawPacketModel.get('ipv4Header').ipTtl + ' seconds/hops');
                $(".protocol", self.el).append('Protocol = ' + self.rawPacketModel.get('ipv4Header').ipProtocol);
                $(".checkSum", self.el).append('Header Checksum = ' + self.rawPacketModel.get('ipv4Header').ipChecksum);
                $(".srcAddress", self.el).append('Source address = ' + self.rawPacketModel.get('ipv4Header').strSrcIp);
                $(".destAddress", self.el).append('Destination address = ' + self.rawPacketModel.get('ipv4Header').strDstIp);
            } else if (self.rawPacketModel.get('version') == 6) {
                $(".ipv6Version", self.el).append('Version = ' + self.rawPacketModel.get('version'));
                $(".trafficClass", self.el).append('Traffic Class = ' + self.rawPacketModel.get('ipv6Header').strTrafficClass);
                $(".flowLabel", self.el).append('Flow Label = ' + self.rawPacketModel.get('ipv6Header').flowLabel);
                $(".payloadLength", self.el).append('Payload Length = ' + self.rawPacketModel.get('ipv6Header').payloadLength);
                $(".nextHeader", self.el).append('Next Header = ' + self.rawPacketModel.get('ipv6Header').nextHeader);
                $(".hopLimit", self.el).append('Hop Limit = ' + self.rawPacketModel.get('ipv6Header').hopLimit);
                $(".ipv6SourceIp", self.el).append('Source address = ' + self.rawPacketModel.get('ipv6Header').strSrcIp);
                $(".ipv6DestIp", self.el).append('Destination address = ' + self.rawPacketModel.get('ipv6Header').strDstIp);

            }
            ////////////////////////////////////////////// -- 공통 끝 -- /////////////////////////////////////////////

            // ICMP Header
            if (self.model.get('nProtocol') == 1) {
                var icmpType = self.rawPacketModel.get('icmpType') < 10 ? "0" + self.rawPacketModel.get('icmpType') : self.rawPacketModel.get('icmpType');
                var icmpCode = self.rawPacketModel.get('icmpCode') < 10 ? "0" + self.rawPacketModel.get('icmpCode') : self.rawPacketModel.get('icmpCode');
                var icmpTypeStr = "";
                if (self.rawPacketModel.get('icmpTypeDesc') != null || self.rawPacketModel.get('icmpTypeDesc') != '') {
                    icmpTypeStr = self.rawPacketModel.get('icmpTypeDesc');
                }
                icmpTypeStr += " (" + icmpType + ")";

                var icmpCodeStr = "";
                if (self.rawPacketModel.get('icmpCodeDesc') != null || self.rawPacketModel.get('icmpCodeDesc') != '') {
                    icmpCodeStr = self.rawPacketModel.get('icmpCodeDesc');
                }
                icmpCodeStr += " (" + icmpCode + ")";

                $(".icmpType", self.el).append('Type = ' + icmpTypeStr);
                $(".icmpCode", self.el).append('Code = ' + icmpCodeStr);
                $(".icmpChecksum", self.el).append('Checksum = ' + self.rawPacketModel.get('icmpChecksum').toString(16));
                $(".icmpIdentifier", self.el).append('Identifier = ' + self.rawPacketModel.get('icmpIdentifier'));
                $(".icmpSequence", self.el).append('Sequence Number = ' + self.rawPacketModel.get('icmpSequence'));
            }

            // TCP Header
            if (self.model.get('nProtocol') == 6) {

                $(".tcpSrcPort", self.el).append('Source Port = ' + self.rawPacketModel.get('tcpSrcPort'));
                $(".tcpDstPort", self.el).append('Destination Port = ' + self.rawPacketModel.get('tcpDstPort'));
                $(".seqNo", self.el).append('Sequence number = ' + self.rawPacketModel.get('seqNo'));
                $(".ackNo", self.el).append('Acknowledgement number = ' + self.rawPacketModel.get('ackNo'));
                $(".tcpHeaderLen", self.el).append('Header length = ' + self.rawPacketModel.get('tcpHeaderLen') + ' bytes');
                $(".reserved", self.el).append('Reserved = ' + self.rawPacketModel.get('reserved') + 'h');
                $(".tcpFlags", self.el).append(self.rawPacketModel.get('tcpFlags') + 'h');

                var urgFlag = self.rawPacketModel.get('urgFlag') == true ? 1 : 0;
                var ackFlag = self.rawPacketModel.get('ackFlag') == true ? 1 : 0;
                var pshFlag = self.rawPacketModel.get('pshFlag') == true ? 1 : 0;
                var rstFlag = self.rawPacketModel.get('rstFlag') == true ? 1 : 0;
                var synFlag = self.rawPacketModel.get('synFlag') == true ? 1 : 0;
                var finFlag = self.rawPacketModel.get('finFlag') == true ? 1 : 0;

                var urgFlagText = "";
                var ackFlagText = "";
                var pshFlagText = "";
                var rstFlagText = "";
                var synFlagText = "";
                var finFlagText = "";

                if (urgFlag == 0) {
                    urgFlagText = "No urgent pointer";
                } else {
                    urgFlagText = "urgent pointer";
                }
                if (ackFlag == 0) {
                    ackFlagText = "No Acknowledgement";
                } else {
                    ackFlagText = "Acknowledgement";
                }
                if (pshFlag == 0) {
                    pshFlagText = "No Push";
                } else {
                    pshFlagText = "Push";
                }
                if (rstFlag == 0) {
                    rstFlagText = "No Reset";
                } else {
                    rstFlagText = "Reset";
                }
                if (synFlag == 0) {
                    synFlagText = "No SYN";
                } else {
                    synFlagText = "SYN";
                }
                if (finFlag == 0) {
                    finFlagText = "No FIN";
                } else {
                    finFlagText = "FIN";
                }

                $(".urgFlag", self.el).append(urgFlag);
                $(".urgFlagText", self.el).append(urgFlagText);

                $(".ackFlag", self.el).append(ackFlag);
                $(".ackFlagText", self.el).append(ackFlagText);

                $(".pshFlag", self.el).append(pshFlag);
                $(".pshFlagText", self.el).append(pshFlagText);

                $(".rstFlag", self.el).append(rstFlag);
                $(".rstFlagText", self.el).append(rstFlagText);

                $(".synFlag", self.el).append(synFlag);
                $(".synFlagText", self.el).append(synFlagText);

                $(".finFlag", self.el).append(finFlag);
                $(".finFlagText", self.el).append(finFlagText);

                $(".windowSize", self.el).append('Window = ' + self.rawPacketModel.get('windowSize'));
                $(".tcpChecksum", self.el).append('Checksum = ' + self.rawPacketModel.get('tcpChecksum') + 'h');
                $(".urgentPtr", self.el).append('Urgent pointer = ' + self.rawPacketModel.get('urgentPtr'));

                if (self.rawPacketModel.get('options') > 0) {
                    $(".options", self.el).append('Options = ' + self.rawPacketModel.get('options') + ' bytes');
                }
            }

            // UDP Header
            if (self.model.get('nProtocol') == 17) {
                $(".udpSrcPort", self.el).append('Source Port = ' + self.rawPacketModel.get('udpSrcPort'));
                $(".udpDstPort", self.el).append('Destination Port = ' + self.rawPacketModel.get('udpDstPort'));
                $(".udpLength", self.el).append('Message Length = ' + self.rawPacketModel.get('udpLength'));
                $(".udpChecksum", self.el).append('Checksum = ' + self.rawPacketModel.get('udpChecksum') + "h");
            }

            $(function () {
                $('.tree li > a', self.el).addClass('cursor-pointer');
                $('.tree li li', self.el).css("display", "none");
                $('.tree li:has(ul)', self.el).addClass('parent_li').find(' > a');
                $('.tree li.parent_li > a > span', self.el).on('click', function (e) {
                    var children = $(this).closest('li.parent_li').find(' > ul > li');
                    if (children.is(":visible")) {
                        children.hide('fast');
                        $(this).addClass('icon-plus-sign').removeClass('icon-minus-sign');
                    } else {
                        children.show('fast');
                        $(this).addClass('icon-minus-sign').removeClass('icon-plus-sign');
                    }
                    e.stopPropagation();
                });
            });
        },
        // hexa type line number
        getHexaLineNumber: function (idx) {
            var no = "";
            var count = idx / 16;
            if (count.toString(16).length == 3)
                no = "" + String(count.toString(16));
            if (count.toString(16).length == 2)
                no = "0" + String(count.toString(16));
            if (count.toString(16).length == 1)
                no = "00" + String(count.toString(16));
            no += "0";
            return no;
        },
        setBinaryData: function () {
            var self = this;
            $('#hexaTable > tbody:last', this.el).empty();
            var hexaData = self.rawPacketModel.get('binaryString');
            self.packetHexaTable = [];
            for (var i = 0, j = 0; i < (hexaData.length) - 1; i = i + 2, j++) {		// i:hexa index, j:HexaTable index
                self.packetHexaTable[j] = hexaData.substring(i, i + 2);
            }

//			for( var k=0; k < self.packetHexaTable.length; k++){
//				// 행의 위치
//				if( (k%16) == 0 ){
//					var count = k/16;
//					var no = "";
//					if ( count.toString(16).length == 3) 	no = "" + String(count.toString(16));
//					if ( count.toString(16).length == 2) 	no = "0" + String(count.toString(16));
//					if ( count.toString(16).length == 1)	no = "00" + String(count.toString(16));
//					no += "0";	
//					$('#hexaTable', self.el).append('<div class="padding-clear packetInfo-row pull-left"  >'+no+'</div>');
//				}
//				// Hexa 데이터 세팅
//				$('#hexaTable', self.el).append('<div id="hexaItem'+k+'" class="packetInfo-cell pull-left">'+self.packetHexaTable[k]+'</div>');
//			}

            var tr = '<tr class="drag" >';
            for (var k = 0; k < self.packetHexaTable.length; k++) {
                // 행의 위치
                if ((k % 16) == 0) {
                    if (k != 0) {
                        tr += '</tr><tr class="drag" >';
                        tr += '<td class="packetInfo-cell3 pull-left drag"  >' + self.getHexaLineNumber(k) + '&nbsp;</td>';
                    } else {
                        tr += '<td class="packetInfo-cell3 pull-left drag"  >' + self.getHexaLineNumber(k) + '&nbsp;</td>';
                    }
                }
                tr += '<td class="packetInfo-cell pull-left drag" id="hexaItem' + k + '" >' + self.packetHexaTable[k] + '&nbsp;</td>';
            }
            tr += '</tr>';
            $('#hexaTable > tbody', self.el).append(tr);
        },
        setBinaryStrData: function () {
            var self = this;
            self.$('#clipboard-text', self.el).html("");
            for (var l = 0; l < self.packetEncodingStrModel.get('strData').length; l++) {
                if (l != 0 && l % 16 == 0) {
                    self.$('#clipboard-text', self.el).append('\n').append('<span id=\"clipboardItem' + l + '" class="drag" >' + self.packetEncodingStrModel.get('strData').substr(l, 1) + '</span>');
                } else {
                    self.$('#clipboard-text', self.el).append('<span id=\"clipboardItem' + l + '" class="drag" >' + self.packetEncodingStrModel.get('strData').substr(l, 1) + '</span>');
                }
            }
        },
        // 기본 font 색상 초기화
        setOnColorInit: function () {
            // 색상 초기화
            //$('#hexaTable div').each(function() {
            $('#hexaTable tr td').each(function () {
                $(this).css('color', '#333').css("font-weight", ""); 				// 폰트
            });

            $('#clipboard-text span').each(function () {
                $(this).css('color', '#333').css("font-weight", ""); 				// 폰트
            });
        },
        // 선택영역 외의 다른영역 font 색상 초기화
        setOffColorInit: function () {
            // 색상 초기화
            //$('#hexaTable div').each(function() {
            $('#hexaTable tr td').each(function () {
                $(this).css('color', '#a9a9a9').css("font-weight", ""); 				// 폰트
            });

            $('#clipboard-text span').each(function () {
                $(this).css('color', '#a9a9a9').css("font-weight", ""); 				// 폰트
            });
        },
        // 선택시 hexatable 선택영역 bold
        setHexaTableFontColor: function (idx) {
            $("#hexaTable").find("#hexaItem" + idx).css("color", "#0000cd").css("font-weight", "bold");
        },
        // 선택시 text 선택영역 bold
        setHexaTextFontColor: function (idx) {
            $("#clipboard-text").find("#clipboardItem" + idx).css("color", "#0000cd").css("font-weight", "bold");
        },
        // highlight 타겟 세팅
        setHighLightTarget: function (e) {
            var self = this;
            self.eventTarget = e.target.id;
            self.setHighLight();
        },
        setHighLight: function () {
            var self = this;
            var clipboardText = self.$('#clipboard-text').text();
            self.setOnColorInit();
            //////////////////////////////////////////////-- 공통 시작 -- /////////////////////////////////////////////	
            if (self.eventTarget == "macHighLight") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('macHeaderIndex').macStartIdx; idx <= self.rawPacketModel.get('macHeaderIndex').macEndtIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "dstMacHighLight") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('macHeaderIndex').dstMacStartIdx; idx <= self.rawPacketModel.get('macHeaderIndex').dstMacEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "srcMacHighLight") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('macHeaderIndex').srcMacStartIdx; idx <= self.rawPacketModel.get('macHeaderIndex').srcMacEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "etherTypeHighLight") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('macHeaderIndex').etherTypeStartIdx; idx <= self.rawPacketModel.get('macHeaderIndex').etherTypeEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "ipHighLight") {
                self.setOffColorInit();
                if (self.rawPacketModel.get('version') == 4) {
                    for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').ipv4HeaderStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').ipv4HeaderEndIdx; idx++) {
                        self.setHexaTableFontColor(idx);
                        self.setHexaTextFontColor(idx);
                    }
                } else if (self.rawPacketModel.get('version') == 6) {
                    for (var idx = self.rawPacketModel.get('ipv6HeaderIndex').ipv6HeaderStartIdx; idx <= self.rawPacketModel.get('ipv6HeaderIndex').ipv6HeaderEndIdx; idx++) {
                        self.setHexaTableFontColor(idx);
                        self.setHexaTextFontColor(idx);
                    }
                }
            }
            if (self.eventTarget == "ipv4Version") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').ipv4VersionStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').ipv4VersionEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "ipHdrLength") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').ipHdrLengthStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').ipHdrLengthEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "typeOfService") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').typeOfServiceStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').typeOfServiceEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "typeOfServicePrecedence" || self.eventTarget == "typeOfServicePrecedenceText"
                    || self.eventTarget == "typeOfServiceDelay" || self.eventTarget == "typeOfServiceDelayText"
                    || self.eventTarget == "typeOfServiceThrt" || self.eventTarget == "typeOfServiceThrtText"
                    || self.eventTarget == "typeOfServiceRelty" || self.eventTarget == "typeOfServiceReltyText"
                    || self.eventTarget == "typeOfServiceFuture" || self.eventTarget == "typeOfServiceFutureText") {

                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').typeOfServiceStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').typeOfServiceEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "totalLength") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').totalLengthStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').totalLengthEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "identification") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').identificationStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').identificationEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "ipFlags" || self.eventTarget == "mustFlag"
                    || self.eventTarget == "dontFragFlagAll" || self.eventTarget == "dontFragFlag" || self.eventTarget == "dontFragFlagText"
                    || self.eventTarget == "moreFragFlagAll" || self.eventTarget == "moreFragFlag" || self.eventTarget == "moreFragFlagText"
                    ) {

                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').ipFlagsStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').ipFlagsEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "fragOffset") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').fragOffsetStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').fragOffsetEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "timeTolive") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').timeToliveStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').timeToliveEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "protocol") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').protocolStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').protocolEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "checkSum") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').checkSumStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').checkSumEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "srcAddress") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').srcAddressStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').srcAddressSumEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "destAddress") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv4HeaderIndex').destAddressStartIdx; idx <= self.rawPacketModel.get('ipv4HeaderIndex').destAddressEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "ipv6Version") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv6HeaderIndex').ipv6VersionStartIdx; idx <= self.rawPacketModel.get('ipv6HeaderIndex').ipv6VersionEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "trafficClass") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv6HeaderIndex').trafficStartIdx; idx <= self.rawPacketModel.get('ipv6HeaderIndex').trafficEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "flowLabel") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv6HeaderIndex').flowLabelStartIdx; idx <= self.rawPacketModel.get('ipv6HeaderIndex').flowLabelEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "payloadLength") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv6HeaderIndex').payloadLengthStartIdx; idx <= self.rawPacketModel.get('ipv6HeaderIndex').payloadLengthEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "nextHeader") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv6HeaderIndex').nextHeaderStartIdx; idx <= self.rawPacketModel.get('ipv6HeaderIndex').nextHeaderEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "hopLimit") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv6HeaderIndex').hopLimitStartIdx; idx <= self.rawPacketModel.get('ipv6HeaderIndex').hopLimitEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "ipv6SourceIp") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv6HeaderIndex').srcAddressStartIdx; idx <= self.rawPacketModel.get('ipv6HeaderIndex').srcAddressSumEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "ipv6DestIp") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('ipv6HeaderIndex').destAddressStartIdx; idx <= self.rawPacketModel.get('ipv6HeaderIndex').destAddressEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            //////////////////////////////////////////////-- 공통 끝 -- /////////////////////////////////////////////			

            //////////////////////////////////////////////-- ICMP 시작 -- /////////////////////////////////////////////
            if (self.eventTarget == "icmpHighLight") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('icmpHeaderIndex').icmpHeaderStartIdx; idx <= self.rawPacketModel.get('icmpHeaderIndex').icmpHeaderEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "icmpType") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('icmpHeaderIndex').icmpTypeStartIdx; idx <= self.rawPacketModel.get('icmpHeaderIndex').icmpTypeEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "icmpCode") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('icmpHeaderIndex').icmpCodeStartIdx; idx <= self.rawPacketModel.get('icmpHeaderIndex').icmpCodeEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "icmpChecksum") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('icmpHeaderIndex').icmpChecksumStartIdx; idx <= self.rawPacketModel.get('icmpHeaderIndex').icmpChecksumEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "icmpIdentifier") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('icmpHeaderIndex').icmpIdentifierStartIdx; idx <= self.rawPacketModel.get('icmpHeaderIndex').icmpIdentifierEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "icmpSequence") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('icmpHeaderIndex').icmpSequenceStartIdx; idx <= self.rawPacketModel.get('icmpHeaderIndex').icmpSequenceEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            //////////////////////////////////////////////-- ICMP 끝 -- /////////////////////////////////////////////

            //////////////////////////////////////////////-- TCP 시작 -- /////////////////////////////////////////////
            if (self.eventTarget == "tcpHighLight") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').tcpHeaderStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').tcpHeaderEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "tcpSrcPort") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').tcpSrcPortStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').tcpSrcPortEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "tcpDstPort") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').tcpDstPortStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').tcpDstPortEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "seqNo") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').seqNoStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').seqNoEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "ackNo") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').ackNoStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').ackNoEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "tcpHeaderLen") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').tcpHeaderLenStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').tcpHeaderLenEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "reserved") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').reservedStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').reservedEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "tcpFlags") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').tcpFlagsStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').tcpFlagsEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "urgFlagAll" || self.eventTarget == "urgFlag" || self.eventTarget == "urgFlagText") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').urgFlagStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').urgFlagEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "ackFlagAll" || self.eventTarget == "ackFlag" || self.eventTarget == "ackFlagText") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').ackFlagStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').ackFlagEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "pshFlagAll" || self.eventTarget == "pshFlag" || self.eventTarget == "pshFlagText") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').pshFlagStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').pshFlagEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "rstFlagAll" || self.eventTarget == "rstFlag" || self.eventTarget == "rstFlagText") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').rstFlagStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').rstFlagEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "synFlagAll" || self.eventTarget == "synFlag" || self.eventTarget == "synFlagText") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').synFlagStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').synFlagEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "finFlagAll" || self.eventTarget == "finFlag" || self.eventTarget == "finFlagText") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').finFlagStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').finFlagEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "windowSize") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').windowSizeStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').windowSizeEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "tcpChecksum") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').tcpChecksumStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').tcpChecksumEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "urgentPtr") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').urgentPtrStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').urgentPtrEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "options" && self.rawPacketModel.get('tcpHeaderIndex').optionsEndIdx > 0) {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('tcpHeaderIndex').optionsStartIdx; idx <= self.rawPacketModel.get('tcpHeaderIndex').optionsEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            //////////////////////////////////////////////-- TCP 끝 -- /////////////////////////////////////////////			

            //////////////////////////////////////////////-- UDP 시작 -- /////////////////////////////////////////////
            if (self.eventTarget == "udpHighLight") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('udpHeaderIndex').udpHeaderStartIdx; idx <= self.rawPacketModel.get('udpHeaderIndex').udpHeaderEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "udpSrcPort") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('udpHeaderIndex').udpSrcPortStartIdx; idx <= self.rawPacketModel.get('udpHeaderIndex').udpSrcPortEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "udpDstPort") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('udpHeaderIndex').udpDstPortStartIdx; idx <= self.rawPacketModel.get('udpHeaderIndex').udpDstPortEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "udpLength") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('udpHeaderIndex').udpLengthStartIdx; idx <= self.rawPacketModel.get('udpHeaderIndex').udpLengthEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "udpChecksum") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('udpHeaderIndex').udpChecksumStartIdx; idx <= self.rawPacketModel.get('udpHeaderIndex').udpChecksumEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            //////////////////////////////////////////////-- UDP 끝 -- /////////////////////////////////////////////	
            if (self.eventTarget == "dataHighLight") {
                self.setOffColorInit();
                for (var idx = self.rawPacketModel.get('dataIndex').dataStartIdx; idx <= self.rawPacketModel.get('dataIndex').dataEndIdx; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
            if (self.eventTarget == "patternDetected") {
                self.setOffColorInit();
                var startOffset = self.rawPacketModel.get('patternDetectedIndex').dwMaliciousSrvFrame;
                var offsetLength = self.rawPacketModel.get('patternDetectedIndex').dwMaliciousSrvByte;
                for (var idx = startOffset; idx < startOffset + offsetLength; idx++) {
                    self.setHexaTableFontColor(idx);
                    self.setHexaTextFontColor(idx);
                }
            }
        }
    });
});
