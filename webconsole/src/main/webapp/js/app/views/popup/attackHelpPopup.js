/**
 * @description 실시간 감시, 원본로그 > 공격 상세 정보 팝업 
 */
define(function (require) {

    "use strict";

    var $ 						= require('jquery'),
        Backbone 				= require('backbone'),
        locale 					= require('i18n!nls/str'),
        dataExpression 			= require('utils/dataExpression'),
        AttackHelpPopupModel 	= require('models/detectionAnalysis/attackHelpPopupModel'),
        TbIpRetrieve 			= require('views/tools/ipRetrievePopup');

    // require template
    var Tpl 					= require('text!tpl/detectionAnalysis/attackDefinition.html');

    return Backbone.View.extend({
        className: 'tab-pane padding-r15 padding-l15',
        template: _.template(Tpl),
        initialize: function (options) {
            this.model 				= new AttackHelpPopupModel();
            this.detectionModel 	= new AttackHelpPopupModel();
            this.searchCondition 	= options.searchCondition;
            this.searchType 		= options.searchType;
            this.listModel 			= options.model;
            this.menuType 			= options.menuType;
            this.strTitle 			= options.strTitle;
            this.bType 				= options.bType;
            
            if (options.searchType == 'attackHelp') {
                // 등록정보만 보여주고자 할때
                this.searchType = 0;
            }
            if (options.searchType == 'attackHelpAllInfo') {
                // 탐지정보 등록정보를 모두보여줄때
                this.searchType = 1;
            }
        },
        events: {
            'click .sipRetrievePopupBtn': 'showsIpRetrievePopup',
            'click .dipRetrievePopupBtn': 'showdIpRetrievePopup'
        },
        render: function() {
            this.$el.html(this.template({
                locale: locale,
                SYSTEM_TYPE: SYSTEM_TYPE
            }));
            if (this.searchType == 0) {
                // 탐지정보를 숨긴다.

                $('#detectionInfoTitle', this.el).empty();
                $('#detectionInfoContent', this.el).empty();
                $('#detectionInfoContent', this.el).removeClass();
            }
//            if (this.menuType == "monitoring") {
//                this.setMonitoringDetectionAttackHelp();
//            } else {
                this.detecteAttackDefinition();
//            }
            // 상관분석일 경우에만 
            this.registerAttackDefinition();

            return this;
        },
        //탐지정보
        detecteAttackDefinition: function() {
            var thisView = this;

            _.extend(this.searchCondition, this.listModel.toJSON(), {
                strDestIp: this.listModel.get('deDestinationIp'),
                strSrcIp: this.listModel.get('dwSourceIp')
            });
            // 공격 정보 조회 
            this.detectionModel.fetch({
                method: 'POST',
                url: 'api/selectDetectionAttackHelpPopupData',
                contentType: 'application/json',
                async: false,	
                data: JSON.stringify(this.searchCondition),
                success: function(model) {
                    thisView.setDetectionAttackHelp(model);
                }
            });
        },
        //등록정보 
        registerAttackDefinition: function() {
            var thisView = this;
            // 도움말 정보 조회 
            this.model.fetch({
                method: 'POST',
                url: 'api/selectAttackHelpPopupData',
                contentType: 'application/json',
                async: false,
                data: JSON.stringify(this.searchCondition),
                success: function(model) {
                	// 도움말 팝업 등록정보 
                    thisView.setRegistrationAttackHelp();
                }
            });
        },
        setMonitoringDetectionAttackHelp: function() {
            // 목록 조회시 가지고 있는 항목은 listModel에서 보여주고, 없는 정보는 새로 조회한다.
            // 실시간 감시 데이터
            $('#attackType', this.el).text(this.listModel.get('bType'));
            $('#attackName', this.el).text(this.listModel.get('strTitle'));
            $('#startDateTime', this.el).text(this.listModel.get('tmStart'));
            $('#endDateTime', this.el).text(this.listModel.get('tmEnd'));
            $('#sip', this.el).text(this.listModel.get('dwSourceIp'));
            $('#dip', this.el).text(this.listModel.get('deDestinationIp'));
            $('#sPort', this.el).text(dataExpression.getPortFormat(this.listModel.get('nSourcePort')));
            $('#dPort', this.el).text(dataExpression.getPortFormat(this.listModel.get('nDestinationPort')));
            $('#sMac', this.el).text(dataExpression.nullToHyphen(this.listModel.get('strSourceMac')));
            $('#dMac', this.el).text(dataExpression.nullToHyphen(this.listModel.get('strDestinationMac')));
            $('#ttl', this.el).text(dataExpression.nullToHyphen(this.listModel.get('nTtl')));
            $('#sourceNetwork', this.el).text(dataExpression.nullToHyphen(this.listModel.get('srcNetworkName')));
            $('#destnationNetwork', this.el).text(dataExpression.nullToHyphen(this.listModel.get('dstNetworkName')));
            $('#packetCount', this.el).text(this.listModel.get('dwPacketCounter') + "개");
            $('#attackCount', this.el).text(this.listModel.get('dwEventNum') + "건");
            $('#bytes', this.el).text(this.listModel.get('dwPktSize'));
        },
        setDetectionAttackHelp: function(model) {
            // 침입탐지분석 > 원본로그 데이터
            // bType은 listModel에만 있음.
            $('#startDateTime', this.el).text(model.get('tmStart'));
            $('#endDateTime', this.el).text(model.get('tmEnd'));
            $('#sip', this.el).text(model.get('dwSourceIp'));
            $('#dip', this.el).text(model.get('deDestinationIp'));
            $('#sPort', this.el).text(dataExpression.getPortFormat(model.get('nSourcePort')));
            $('#dPort', this.el).text(dataExpression.getPortFormat(model.get('nDestinationPort')));
            $('#sMac', this.el).text(dataExpression.nullToHyphen(model.get('strSourceMac')));
            $('#dMac', this.el).text(dataExpression.nullToHyphen(model.get('strDestinationMac')));
            $('#ttl', this.el).text(dataExpression.nullToHyphen(model.get('nTtl')));
            $('#sourceNetwork', this.el).text(dataExpression.nullToHyphen(model.get('srcNetworkName')));
            $('#destnationNetwork', this.el).text(dataExpression.nullToHyphen(model.get('dstNetworkName')));
            $('#packetCount', this.el).text(model.get('dwPacketCounter') + "개");
            $('#attackCount', this.el).text(model.get('dwEventNum') + "건");
            $('#bytes', this.el).text(model.get('dwPktSize'));
            if (this.listModel.get('bType') != null) {
                $('#attackType', this.el).text(this.listModel.get('bType'));
            } else {
                $('#attackType', this.el).text(model.get('bType'));
            }
//            $("#sourceNation", this.el).text(dataExpression.nullToHyphen(model.get('strSrcNationIso')));
//            $("#destinationNation", this.el).text(dataExpression.nullToHyphen(model.get('strDestNationIso')));
            
            var nSrcScore = 0;
            var nDestScore = 0;
            if (model.get('nSrcScore') != null && parseInt(model.get('nSrcScore')) > 0) {
                nSrcScore = parseInt(parseInt(model.get('nSrcScore')) / 12);
            }
            if (nSrcScore > 10) {
                nSrcScore = 10;
            }
            if (nSrcScore == 0 && parseInt(model.get('nSrcScore')) > 0) {
                nSrcScore = 1;
            }

            if (model.get('nDestScore') != null && parseInt(model.get('nDestScore')) > 0) {
                nDestScore = parseInt(parseInt(model.get('nDestScore')) / 12);
            }
            if (nDestScore > 10) {
                nDestScore = 10;
            }
            if (nDestScore == 0 && parseInt(model.get('nDestScore')) > 0) {
                nDestScore = 1;
            }

            var strSrcCategory = "";
            if (model.get('strSrcCategory') != null) {
                strSrcCategory = model.get('strSrcCategory');
            }

            var strDestCategory = "";
            if (model.get('strDestCategory') != null) {
                strDestCategory = model.get('strDestCategory');
            }
            if (model.get('nSrcScore') != null && model.get('nSrcScore') > 0) {
                $("#srcRepute", this.el).show();
                $("#srcRepute", this.el).text(nSrcScore);
                $("#srcReputeDetail", this.el).text(model.get('nSrcScore'));
                $("#srcRepute", this.el).addClass("repute-bg-lv"+nSrcScore);
                $("#srcReputeCategory", this.el).text(strSrcCategory);
            } else {
                $("#srcReputeDetail", this.el).text("-");
                $("#srcReputeCategory", this.el).text("-");
                $("#srcRepute", this.el).hide();
            }
            
            if (model.get('nDestScore') != null && model.get('nDestScore') > 0) {
                $("#destRepute", this.el).show();
                $("#destRepute", this.el).text(nDestScore);
                $("#destReputeDetail", this.el).text(model.get('nDestScore'));
                $("#destRepute", this.el).addClass("repute-bg-lv"+nDestScore);
                $("#destReputeCategory", this.el).text(strDestCategory);
            } else {
                $("#destRepute", this.el).hide();
                $("#destReputeDetail", this.el).text("-");
                $("#destReputeCategory", this.el).text("-");
            }
            
//            var bType = this.menuType == 'original' ? this.bType : this.listModel.get('bType');
//            $('#attackType', this.el).append(bType);
//            $('#attackName', this.el).append(this.listModel.get('strTitle'));
//            if (this.menuType == 'nation') {
//            	$('#attackName', this.el).append(this.strTitle);            	
//            } else {
//            	$('#attackName', this.el).append(this.listModel.get('strTitle'));
//            }
            // SYSTEM_SIGHELP 테이블에 strDescription과 strAtkType이 없을 경우가 있으므로 공격 정보 팝업에서 넘겨받도록 변경.
            var attackName = this.menuType == 'nation' ? this.strTitle : this.listModel.get('strTitle');
            $('#attackName', this.el).append(attackName);
        },
        setRegistrationAttackHelp: function() {
            // 상관분석, 공격TOP5 팝업 등록정보
            //severity-bg-lv
            //$('#severityColor', this.el).addClass('threat-bg-lv' + dataExpression.severityColorData(this.model.get('bSeverity')));
            $('#severityColor', this.el).addClass('severity-bg-lv' + this.model.get('bSeverity'));
            $('#severity', this.el).append(dataExpression.severityData(this.model.get('bSeverity')));
            $('#lCode', this.el).append(this.model.get('lCode'));
            $('#patternInfo', this.el).append(this.model.get('signatureRule'));
            $('#summary', this.el).append(this.model.get('strSummary'));
            $('#detailComment', this.el).append(this.model.get('strDescription'));
            $('#solution', this.el).append(this.model.get('strSolution'));
            $('#reference', this.el).append(this.model.get('strReference'));
            if (this.model.get('strAttackType') != null) {
                $('#attackType', this.el).text(this.model.get('strAttackType'));
            }
            if (this.menuType == 'correlation') {
                $('#attackName', this.el).val(this.model.get('strTitle'));
            }
            //$('#detectionSucceedOrNot', this.el).append(this.model.get());
            //$('#packetSave', this.el).append('');
            //$('#threshold', this.el).append('');
            //$('#trapId', this.el).append('');
        },
        showsIpRetrievePopup: function() {
            Backbone.ModalView.msg({
                size: 'medium-large',
                title: 'IP ' + locale.retrieve,
                body: new TbIpRetrieve({
                    ip: this.listModel.get('dwSourceIp')
                })
            });
        },
        showdIpRetrievePopup: function() {
            Backbone.ModalView.msg({
                size: 'medium-large',
                title: 'IP ' + locale.retrieve,
                body: new TbIpRetrieve({
                    ip: this.listModel.get('deDestinationIp')
                })
            });
        }
    });
});
