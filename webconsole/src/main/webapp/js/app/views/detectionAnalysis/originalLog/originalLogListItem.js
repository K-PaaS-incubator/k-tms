/**
 * originalLog table item view  
 * @author lee kyunghee
 * @since 2014-12-09
 * @description 원본로그 최종5분 통계 조회 item
 */
define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error'),
            AttackHelpPopupView = require('views/popup/attackHelpPopup'),
            dataExpression = require('utils/dataExpression'),
            RawPacketPopupView = require('views/popup/rawPacketPopup'),
            TbIpRetrieve = require('views/tools/ipRetrievePopup'),
            DetectionPolicyModel = require('models/securityPolicy/detectionPolicyModel'),
            DetectionExceptionModel = require('models/systemSettings/detectionExceptionModel'),
            alertMessage = require('utils/AlertMessage'),
            sessionManager = require('utils/sessionManager');

    // require template
    var tpl = require('text!tpl/detectionAnalysis/originalLogListItem.html');

    return  Backbone.View.extend({
        tagName: 'tr',
        template: _.template(tpl),
        events: {
            'click .attackName': 'attackHelpPopup',
            'click .rawPacketBtn': 'rawPacketPopup',
            // 'click .exceptionBtn': 'detectionExceptionPopup',
            // "click .responseBtn": 'responsePopup',
            'click .ipRetrieveAttackPopupLink': 'showAttackIpRetrievePopup',
            'click .ipRetrieveVictimPopupLink': 'showVictimIpRetrievePopup'
        },
        initialize: function (options) {
            this.model = options.model;
            this.searchCondition = options.searchCondition;
            this.detectionPolicyModel = new DetectionPolicyModel();
            this.detectionExceptionModel = new DetectionExceptionModel();
            this.detectionExceptionValidateModel = new DetectionExceptionModel();
            this.role = sessionManager.role;
            this.listenTo(this.model, "change", this.render());
        },
        render: function () {

            var networkName = this.model.get('srcNetworkName') ? this.model.get('srcNetworkName') : this.model.get('dstNetworkName') ? this.model.get('dstNetworkName') : '-';
            var vsensorName = this.model.get('vsensorName') ? this.model.get('vsensorName') : '-';
            var sensorName = this.model.get('sensorName') ? this.model.get('sensorName') : '-';

            /*service info 추가*/
            var tempSrcService, tempDstService, srcService, dstService, srcApplication, dstApplication;
            var srcServiceBox, dstServiceBox;
            tempSrcService = this.model.get('srcService');

            if (tempSrcService != null) {
                var temp = tempSrcService.split('|');
                srcService = temp[0];
                srcApplication = temp[1];
                srcServiceBox = "list-box";
            } else {
                srcService = dataExpression.getProtocolName(this.model.get('nProtocol'));
                srcApplication = "";
                srcServiceBox = "";
            }
            tempDstService = this.model.get('dstService');

            if (tempDstService != null) {
                var temp = tempDstService.split('|');
                dstService = temp[0];
                dstApplication = temp[1];
                dstServiceBox = "list-box";
            } else {
                dstService = dataExpression.getProtocolName(this.model.get('nProtocol'));
                dstApplication = "";
                dstServiceBox = "";
            }
            /*service info 추가*/

            var nSourcePortView, nDestinationPortView;
            if (this.model.get('nSourcePort') == 0 || this.model.get('nDestinationPort') == 0) {
                nSourcePortView = "-";
                nDestinationPortView = "-";
            } else {
                nSourcePortView = this.model.get('nSourcePort');
                nDestinationPortView = this.model.get('nDestinationPort');
            }

            var strSrcNationIsoView, strDestNationIsoView;
            if (this.model.get('strSrcNationIso') == null) {
                strSrcNationIsoView = "-";
            } else {
                strSrcNationIsoView = this.model.get('strSrcNationIso');
            }
            if (this.model.get('strDestNationIso') == null) {
                strDestNationIsoView = "-";
            } else {
                strDestNationIsoView = this.model.get('strDestNationIso');
            }

            var nSrcScore = 0;
            var nDestScore = 0;
            if (this.model.get('nSrcScore') != null && parseInt(this.model.get('nSrcScore')) > 0) {
                nSrcScore = parseInt(parseInt(this.model.get('nSrcScore')) / 12);
            }
            if (nSrcScore > 10) {
                nSrcScore = 10;
            }
            if (nSrcScore == 0 && parseInt(this.model.get('nSrcScore')) > 0) {
                nSrcScore = 1;
            }

            if (this.model.get('nDestScore') != null && parseInt(this.model.get('nDestScore')) > 0) {
                nDestScore = parseInt(parseInt(this.model.get('nDestScore')) / 12);
            }
            if (nDestScore > 10) {
                nDestScore = 10;
            }
            if (nDestScore == 0 && parseInt(this.model.get('nDestScore')) > 0) {
                nDestScore = 1;
            }

            var strSrcCategory = "";
            if (this.model.get('strSrcCategory') != null) {
                strSrcCategory = this.model.get('strSrcCategory');
            }

            var strDestCategory = "";
            if (this.model.get('strDestCategory') != null) {
                strDestCategory = this.model.get('strDestCategory');
            }


            this.$el.html(this.template({
                rNum: this.model.get('rNum'),
                bSeverity: this.model.get('bSeverity'),
                strTitle: this.model.get('strTitle'),
                bType: this.model.get('bType'),
                dwSourceIp: this.model.get('dwSourceIp'),
                nSourcePort: this.model.get('nSourcePort'),
                nSourcePortView: nSourcePortView,
                //strSrcNationIso: this.model.get('strSrcNationIso'),
                strSrcNationIsoView: strSrcNationIsoView,
                nProtocol: dataExpression.getProtocolName(this.model.get('nProtocol')),
                srcService: srcService,
                dstService: dstService,
                srcServiceBox: srcServiceBox,
                dstServiceBox: dstServiceBox,
                srcApplication: srcApplication,
                dstApplication: dstApplication,
                dwDestinationIp: this.model.get('deDestinationIp'),
                nDestinationPort: this.model.get('nDestinationPort'),
                nDestinationPortView: nDestinationPortView,
                strDestNationIsoView: strDestNationIsoView,
                //strDestNationIso: this.model.get('strDestNationIso'),
                dwpktsize: this.model.get('dwpktsize'),
                startDate: this.model.get('startDate'),
                wInbound: dataExpression.getBoundDetectionEvent(this.model.get('wInbound')),
                dweventnum: this.model.get('dweventnum'),
                networkName: networkName,
                vsensorName: vsensorName,
                sensorName: sensorName,
                lsensorIndex: this.model.get('lsensorIndex'),
                rawdata: this.model.get('rawdata'),
                role: this.role,
                ucCreateLogType: this.model.get('ucCreateLogType'),
                strSrcCategory: strSrcCategory,
                strDestCategory: strDestCategory,
                nSrcScore: nSrcScore,
                nDestScore: nDestScore
            }));

            return this;
        },
        attackHelpPopup: function (e) {
            var bType = $(e.currentTarget).data('btype');
            _.extend(this.searchCondition, {
                'lCode': +this.model.get('lCode')
            });
            Backbone.ModalView.msg({
                size: 'medium-large',
                type: 'info',
                title: locale.attack + locale.detailInfo, // 공격등록정보
                body: new AttackHelpPopupView({
                    searchType: 'attackHelpAllInfo',
                    model: this.model,
                    searchCondition: this.searchCondition,
                    bType: bType,
                    menuType: "original"
                })
            });
        },
        rawPacketPopup: function () {
            var searchCondition = {};
            _.extend(searchCondition, this.model.toJSON(), {
                startDateInput: this.model.get('startDate'),
                endDateInput: this.model.get('endDate'),
                ipType: parseInt($("input:radio[name='ipType']:checked").val())
            });
            Backbone.ModalView.msg({
                size: 'medium-large',
                type: 'info',
                title: locale.rawPacket,
                body: new RawPacketPopupView({
                    model: this.model,
                    searchCondition: searchCondition,
                    menuType: "original"
                })
            });
        },
/*        detectionExceptionPopup: function () {
            var thisView = this;
            var searchCondition = {};
            _.extend(searchCondition, this.model.toJSON(), {
                startDateInput: this.model.get('startDate'),
                endDateInput: this.model.get('endDate')
            });
            var detectionExceptionSetting = new DetectionExceptionSetting({
                model: this.model,
                searchCondition: searchCondition,
                lCode: this.model.get('lCode')
            });
//			console.log(JSON.stringify(this.model));
            Backbone.ModalView.msgWithOkCancelBtn({
                size: 'large',
                type: 'info',
                title: '탐지예외 추가',
                body: detectionExceptionSetting,
                okButtonCallback: function (e) {
                    var self = this;
                    var strDescriptionValue = $("#strDescriptionValue", self.el).val(),
                            lVioCode = $("#lVioCode", self.el).val(),
                            strSrcIpFrom = $("#strSrcIpFrom", self.el).val(),
                            nSport = $("#nSport", self.el).val(),
                            lsrcNetworkIndex = $("#networkSrcSelect option:selected", self.el).val(),
                            lsrcNetworkIndexName = $("#networkSrcSelect option:selected", self.el).text(),
                            //lsrcNetworkIndex 		= $("#lsrcNetworkIndex", self.el).val(),
                            strDstIpFrom = $("#strDstIpFrom", self.el).val(),
                            nDport = $("#nDport", self.el).val(),
                            ldstNetworkIndex = $("#networkDstSelect option:selected", self.el).val(),
                            ldstNetworkIndexName = $("#networkDstSelect option:selected", self.el).text(),
                            //ldstNetworkIndex 		= $("#ldstNetworkIndex", self.el).val(),
                            nProtocol = $("#nProtocolSelect option:selected", self.el).val(),
                            nClassType = $("#nClassType", self.el).val(),
                            lvsensorIndex = thisView.model.get('lvsensorIndex');
//						lCode = thisView.model.get('lCode'),
//						lIndex = thisView.model.get('lIndex');
                    if ($('.nDetectCheck', self.el).prop("checked")) {
                        self.nDetectValue = 1;
                    } else {
                        self.nDetectValue = 0;
                    }
                    thisView.detectionExceptionModel.set({
                        'lvsensorIndex': lvsensorIndex,
                        'strDescriptionValue': strDescriptionValue,
                        'lVioCode': lVioCode,
                        'strSrcIpFrom': strSrcIpFrom,
                        'strSrcIpTo': strSrcIpFrom,
                        'nSport': nSport,
                        'lsrcNetworkIndex': lsrcNetworkIndex,
                        'lsrcNetworkIndexName': lsrcNetworkIndexName,
                        'strDstIpFrom': strDstIpFrom,
                        'strDstIpTo': strDstIpFrom,
                        'nDport': nDport,
                        'ldstNetworkIndex': ldstNetworkIndex,
                        'ldstNetworkIndexName': ldstNetworkIndexName,
                        'nProtocol': nProtocol,
                        'nClassType': nClassType,
                        'nDetectValue': self.nDetectValue
//						'lCode': lCode,
//						'lIndex': lIndex
                    });
                    //console.log(JSON.stringify(thisView.detectionExceptionModel));
//					var detectionExceptionModel = thisView.detectionExceptionModel.toJSON();
//					var isValid = thisView.isValid(detectionExceptionModel);
//					if(isValid == true) {
                    var setParams = {
                        lCode: thisView.model.get('lCode'),
                        lvsensorIndex: thisView.model.get('lvsensorIndex')
                    };
                    var updateParam = $.extend(setParams, thisView.detectionExceptionModel.toJSON());
                    //console.log(JSON.stringify(updateParam));
                    thisView.detectionExceptionModel.fetch({
                        method: 'POST',
                        url: 'api/systemSetting/updateMonitorIntrusionDetectionException',
                        contentType: 'application/json',
                        dataType: 'text',
                        //						data: JSON.stringify(thisView.detectionPolicyModel.toJSON()),
                        data: JSON.stringify(updateParam),
                        async: false,
                        beforeSend: function() {
                            Backbone.Loading.setLoading($('.modal-content', thisView.el));
                        },
                        success: function () {
                            alertMessage.infoMessage(locale.msg.applied, 'info', '', 'small');
                        },
                        complete: function () {
                            Backbone.Loading.removeLoading($('.modal-content', thisView.el));
                        }
                    });
//					}
                }
            });

        },
        responsePopup: function () {
            var thisView = this;
            var searchCondition = {};
            _.extend(searchCondition, this.model.toJSON(), {
                startDateInput: this.model.get('startDate'),
                endDateInput: this.model.get('endDate'),
                lCode: this.model.get('lCode')
            });
            var responseSetting = new ResponseSetting({
                model: this.model,
                searchCondition: searchCondition,
                lCode: this.model.get('lCode')
            });

            Backbone.ModalView.msgWithOkCancelBtn({
                size: 'medium-large',
                type: 'info',
                title: locale.detectionSettingTitle,
                okLabel: locale.apply,
                body: responseSetting,
                okButtonCallback: function (e) {
                    if (responseSetting.resultlThresholdNum == true && responseSetting.resultlThresholdTime == true) {
                        Backbone.Loading.setLoading($('.modal'));
                        setTimeout(function () {
                            var lThresholdNumValue = $("#lThresholdNum", this.el).val();
                            var lThresholdTimeValue = $("#lThresholdTime", this.el).val();
                            if ($('.nDetectCheck', this.el).prop("checked")) {
                                this.lUsedValue = 1;
                            } else {
                                this.lUsedValue = 0;
                            }
                            //var lResponseValue = $("#lResponseSelect option:selected", self.el).val();
                            if ($('.lResponseCheck', this.el).prop("checked") && $('.lBlockCheck', this.el).prop("checked")) {
                                thisView.strBlock = "checked";
                                thisView.strResponse = "checked";
                                thisView.lBlockBool = true;
                                thisView.lResponseBool = true;
                            } else if (!$('.lResponseCheck', this.el).prop("checked") && !$('.lBlockCheck', this.el).prop("checked")) {
                                thisView.strResponse = "";
                                thisView.strBlock = "";
                                thisView.lBlockBool = false;
                                thisView.lResponseBool = false;
                            } else if ($('.lResponseCheck', this.el).prop("checked") && !$('.lBlockCheck', this.el).prop("checked")) {
                                thisView.strResponse = "checked";
                                thisView.strBlock = "";
                                thisView.lBlockBool = false;
                                thisView.lResponseBool = true;
                            } else if (!$('.lResponseCheck', this.el).prop("checked") && $('.lBlockCheck', this.el).prop("checked")) {
                                thisView.strBlock = "checked";
                                thisView.strResponse = "";
                                thisView.lBlockBool = true;
                                thisView.lResponseBool = false;
                            }

                            thisView.detectionPolicyModel.set({
                                lCode: thisView.model.get('lCode'),
                                lvsensorIndex: thisView.model.get('lvsensorIndex'),
                                lThresholdNumValue: lThresholdNumValue,
                                lThresholdTimeValue: lThresholdTimeValue,
                                lUsedValue: this.lUsedValue,
                                //lResponseValue : lResponseValue
                                strBlock: thisView.strBlock,
                                strResponse: thisView.strResponse,
                                lBlockBool: thisView.lBlockBool,
                                lResponseBool: thisView.lResponseBool,
                                strDescription: thisView.model.get('strTitle'),
                            });
                            var setParams = {
                                lCode: thisView.model.get('lCode'),
                                lvsensorIndex: thisView.model.get('lvsensorIndex')
                            };

                            var updateParam = $.extend(setParams, thisView.detectionPolicyModel.toJSON());
                            thisView.detectionPolicyModel.fetch({
                                method: 'POST',
                                url: 'api/securityPolicy/updateIntrusionDetectionResponse',
                                contentType: 'application/json',
                                dataType: 'text',
//						data: JSON.stringify(thisView.detectionPolicyModel.toJSON()),
                                data: JSON.stringify(updateParam),
                                async: false,
                                beforeSend: function() {
                                    Backbone.Loading.setLoading($('.modal-content', thisView.el));
                                },
                                success: function (collection) {
                                    alertMessage.infoMessage(locale.msg.applied, 'info', '', 'small');
                                },
                                complete: function () {
                                    Backbone.Loading.removeLoading($('.modal-content', thisView.el));
                                }
                            });
                            Backbone.Loading.removeLoading($('.modal'));
                            return true;
                        });
                    }
                    return false;
                }
            });
        },*/
        showAttackIpRetrievePopup: function () {
            var ip = $('.ipRetrieveAttackPopupLink', this.el).text();
            Backbone.ModalView.msg({
                size: 'medium-large',
                title: 'IP ' + locale.retrieve,
                body: new TbIpRetrieve({
                    ip: ip
                })
            });
        },
        showVictimIpRetrievePopup: function () {
            var ip = $('.ipRetrieveVictimPopupLink', this.el).text();
            Backbone.ModalView.msg({
                size: 'medium-large',
                title: 'IP ' + locale.retrieve,
                body: new TbIpRetrieve({
                    ip: ip
                })
            });
        },
        isValid: function (detectionExceptionModel) {
            var self = this;
            this.valid = true;
//			var setParams = self.detectionPolicyModel.toJSON();
            var setParams = detectionExceptionModel;
//			console.log(JSON.stringify(self.detectionPolicyModel.toJSON()));
//			console.log(JSON.stringify(setParams));

            this.detectionExceptionValidateModel.fetch({
                method: 'POST',
                url: 'api/systemSetting/isDuplicateDetectionException',
                contentType: 'application/json',
                data: JSON.stringify(setParams),
                async: false,
                success: function () {
//					var lVioCode = self.detectionExceptionCollection.get('lVioCode');
//					console.log(JSON.stringify(self.detectionExceptionModel));
//					console.log(self.detectionExceptionModel.get('strSrcIpTo'));
//					console.log(self.detectionExceptionModel.get('strDstIpFrom'));
//					console.log(self.detectionExceptionModel.get('lVioCode'));
                    if (self.detectionExceptionValidateModel.get('strSrcIpTo') == null && self.detectionExceptionValidateModel.get('strSrcIpFrom') == null && self.detectionExceptionValidateModel.get('strDstIpFrom ') == null && self.detectionExceptionValidateModel.get('strDstIpTo ') == null) {
                        self.valid = true;
                    } else {
                        alertMessage.infoMessage(errorLocale.validation.duplicateSourceIpDestnationIp + errorLocale.validation.reInputValue, 'info', '', 'small');
                        self.valid = false;
                    }
                }
            });
//			console.log(self.valid);
            return this.valid;
        }
    });
});