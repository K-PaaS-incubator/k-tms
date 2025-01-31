/**
 * 탐지정책 > 등록 정보 
 */
define(function (require) {

    "use strict";

    var $ = require('jquery'),
            Backbone = require('backbone'),
            dataExpression = require('utils/dataExpression'),
            serialize = require('serialize'),
            errorLocale = require('i18n!nls/error'),
            locale = require('i18n!nls/str'),
            alertMessage = require('utils/AlertMessage');

    var tpl = require('text!tpl/securityPolicy/registerDetectionPolicy.html'),
            PatternExemplePopup = require('views/securityPolicy/detectionPolicy/patternExemplePopup');

    var DetectionPolicyModel = require('models/securityPolicy/detectionPolicyModel'),
            AttackTypeCollection = require('collections/securityPolicy/attackTypeCollection'),
            SignatureClassTypeCollection = require('collections/securityPolicy/signatureClassTypeCollection');

    return Backbone.View.extend({
        editable: false,
        type: "",
        template: _.template(tpl),
        // 벤더시그니처 일때 라벨로 표시 
        // 사용자정의 시그니처 일때 입력폼으로 표시
        // 공격유형에 따라 INSERT, UPDATE가 다르도록 한다.  
        templateForm: _.template([
            '<form class="form-inline detectionPolicyEdit" role="form">',
            '<table class="view-table col-xs-12">',
            '<tr>',
            '<% if (this.type == "insert") { %>',
            '<th width="25%"><%= locale.attackType %></th>',
            '<% } %>',
            '<% if (this.type == "update") { %>',
            '<th width="25%"><%= locale.attackType %></th>',
            '<% } %>',
            '<% if (this.type == "insert") { %>',
            '<td>',
            '<select id="signatureClassTypeSelect" name="signatureClassTypeSelect" class="form-control margin-r5 col-xs-5">',
            '</td>',
            '<% } %>',
            '<% if (this.type == "update" && this.lCode >= 1000000) { %>',
            '<td>',
            '<select id="signatureClassTypeSelect" name="signatureClassTypeSelect" class="form-control margin-r5 col-xs-5">',
            '</td>',
            '<% } %>',
            '<% if (this.type == "update" && this.lCode < 1000000) { %>',
            '<td>',
            '<span id="attackType"></span>',
            '</td>',
            '<% } %>',
            '</tr>',
            '<% if (this.type == "update" && this.lCode >= 1000000) { %>',
            '<tr>',
            '<th><%= locale.attackName %> *</th>',
            '<td>',
            '<input type="text" id="attackNameInput" name="attackNameInput" class="form-control col-xs-5 attackNameInput" value="<%= model.strDescription %>" maxlength="256"/>',
            '</td>',
            '</tr>',
            '<% } %>',
            '<% if (this.type == "update" && this.lCode < 1000000) { %>',
            '<tr>',
            '<th><%= locale.attackName %> *</th>',
            '<td>',
            '<%= model.strDescription %>',
            '</td>',
            '</tr>',
            '<% } %>',
            '<% if (this.type == "insert") { %>',
            '<tr>',
            '<th><%= locale.attackName %> *</th>',
            '<td>',
            '<input type="text" id="attackNameInput" name="attackNameInput" class="form-control col-xs-5 margin-r5 attackNameInput" maxlength="256"/>',
            '</td>',
            '</tr>',
            '<% } %>',
            '<tr>',
            '<th><%=locale.severity%></th>',
            '<% if (this.type == "insert") { %>',
            '<td>',
            '<select id="severityLevel" name="bSeverity" class="form-control margin-r5 col-xs-5">',
            '<option value="5"><%= locale.high %></option>',
            '<option value="3"><%=locale.med %></option>',
            '<option value="1"><%= locale.low %></option>',
            '<option value="0"><%= locale.information %></option>',
            '</select>',
            '</td>',
            '<% } %>',
            '<% if (this.type == "update" && this.lCode >= 1000000) { %>',
            '<td>',
            '<select id="severityLevel" name="bSeverity" class="form-control margin-r5 col-xs-5">',
            '<option value="5" <%= model.selected0 %> ><%= locale.high %></option>',
            '<option value="3" <%= model.selected1 %> ><%= locale.med %></option>',
            '<option value="1" <%= model.selected2 %> ><%= locale.low %></option>',
            '<option value="0" <%= model.selected3 %> ><%= locale.information %></option>',
            '</select>',
            '</td>',
            '<% } %>',
            '<% if (this.type == "update" && this.lCode < 1000000) { %>',
            '<td id="bSeverity"></td>',
            '<% } %>',
            '</tr>',
            '<tr><th>',
            '<%= locale.usage %>',
            '</th>',
            '<td>',
            '<input type="checkbox" value="<%= model.lUsed %>" id="lUsed" class="lUsed" name="lUsedCheck" <%= this.strValue %> />',
            '</td>',
            '</tr>',
/*
            '<tr><th>',
            '<%= locale.block %>',
            '</th>',
            '<td>',
            '<input type="checkbox" id="lBlockCheck" class="lBlockCheck" name="lBlockCheck" value="<%= model.lBlock %>" <%= this.strBlock %> />',
            '</td>',
            '</tr>',
*/
            '<tr><th>',
            '<%= locale.packet %>',
            '</th>',
            '<td>',
            '<input type="checkbox" id="lResponseCheck" class="lResponseCheck" name="lResponseCheck" value="<%= model.lResponse %>" <%= this.strResponse %> />',
            '</td>',
            '</tr>',
            '<tr><th>',
            '<%= locale.thresholdCountPerSecond %>',
            '</th>',
            '<td>',
            '<input type="text" id="lThresholdNum" name="lThresholdNum" value="<%= model.lThresholdNum %>" class="form-control col-xs-5 lThresholdNumValue" placeholder="<%= locale.unitCount %>"/>',
            '<input type="text" id="lThresholdTime" name="lThresholdTime" value="<%= model.lThresholdTime %>" class="form-control col-xs-5 margin-l10 lThresholdTimeValue" placeholder="<%= locale.timeSec %>"/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>',
            '<%= locale.signatureInput %> *&nbsp;',
            '<button type="button" id="signaturePatternBtn" class="btn btn-default btn-sm">?</button>',
            '</th>',
            '<td width="75%" id="strDetect" style="word-break: break-all;">',
            '<% if (this.type == "insert") { %>',
            '<textarea id="strSigRule" class="form-control col-xs-12 strSigRule" rows="5"><%= model.strSigRule %></textarea>',
            '<% } %>',
            '<% if (this.type == "update" && this.lCode < 1000000) { %>',
            '<%= model.strSigRule %>',
            '<% } %>',
            '<% if (this.type == "update" && this.lCode >= 1000000) { %>',
            '<textarea id="strSigRule" class="form-control col-xs-12 strSigRule" rows="5"><%= model.strSigRule %></textarea>',
            '<% } %>',
            '</td>',
            '</tr>',
            '</table>',
            '</form>'
        ].join('')),
        initialize: function (options) {
            this.attackName = options.attackName; 	// 공격명 중복 유효성 검사를 위해 목록에서 넘겨 받아야함.
            this.lCode = options.lCode;		// 목록에서 해당하는 상세정보를 위해 전달 받아야함.
            this.sClassType = options.sClassType;	// 목록에서 선택한 시그니처의 그룹 정보로 수정 저장 및 등록 로직을 분기함
            this.model = new DetectionPolicyModel();
            this.collection = new AttackTypeCollection();
            this.signatureClassTypeCollection = new SignatureClassTypeCollection();
            this.resultlThresholdNum = true;
            this.resultlThresholdTime = true;
        },
        events: {
            "click #signaturePatternBtn": "patternHelpPopup",
            "keyup #strSigRule": "byteCheck4000",
            'change .lThresholdNumValue': 'changeThresholdNum',
            'change .lThresholdTimeValue': 'changeThresholdTime',
            'change .lUsed': 'changelUsed',
            'change .lResponseCheck': 'changelResponse',
            'change .lBlockCheck': 'changelBlock',
            "keydown #lThresholdNum": "numberInput",
            "keydown #lThresholdTime": "numberInput",
            "blur #lThresholdNum": "validatelThresholdNum",
            "blur #lThresholdTime": "validatelThresholdTime",
        },
        render: function () {
            // index 번호를 가지고 있으면 update(수정), 없으면 insert(신규생성)
            this.resultlThresholdNum = true;
            this.resultlThresholdTime = true;
            if (this.lCode != undefined) {
                this.editable = false;
                this.type = "update";
                this.getDetectionPolicyDetailList();
                this.$el.html(this.template({
                    model: this.model.toJSON(),
                    locale: locale
                }));
                return this;

            } else {
                this.editable = true;
                this.type = "insert";
                this.$el.html(this.templateForm({
                    model: this.model.toJSON(),
                    locale: locale
                }));
                // 공격유형 SCLASSTYPE == 99
                $('#bSeverity', this.el).append(dataExpression.severityData(this.model.get('sSeverity')));
                this.getAttackTypeList();		// 공격유형 추가 
                this.getSignatureClassType();	// 공격유형 조회

                return this;
            }
        },
        renderEdit: function () {
            this.resultlThresholdNum = true;
            this.resultlThresholdTime = true;
            this.editable = true;
            this.type = "update";
            // 변경 버튼 클릭시 저장되어 있는 위험도 값을 선택하여 보여주도록 화면에 셋팅 
            this.setSeverityModel();
            this.$el.html(this.templateForm({// update
                model: this.model.toJSON(),
                locale: locale
            }));
            this.getDetectionPolicyDetailList();
            this.getSignatureClassType('update');	// 공격 유형 조회 

            return this;
        },
        status: function () {
            return this.editable;
        },
        statusType: function () {
            return this.type;
        },
        getInsertIndex: function () {
            return {
                lCode: this.lCode,
                strDescription: this.strDescription,
                sSeverity: this.sSeverity,
                strSigRule: this.strSigRule,
                strSummary: this.strSummary,
                helpYN: this.helpYN
            };
        },
        toggle: function () {
            if (this.editable) {
                this.mergeDetectionPolicyInfo(this.type);
                return this.render();
            } else {
                return this.renderEdit();
            }
        },
        mergeDetectionPolicyInfo: function (action) {
            var lUsedValue = 0;
            var url = "";
            if (this.type == "update") {
                var setParams = "";
                console.log("this.sClassType : " + this.sClassType);
                if (this.sClassType >= 99) {
                    url = "api/securityPolicy/updateUserSignatureDetail";
                    var self = this;
                    var sClassValue = $('#signatureClassTypeSelect option:selected', this.el).val();
                    var sClassName = $('#signatureClassTypeSelect option:selected', this.el).text();
                    var severitySel = $('#severityLevel option:selected', this.el).val();
                    if (this.$('.lUsed', this.el).prop('checked')) {
                        lUsedValue = 1;
                    }
                    setParams = {
                        lCode: this.lCode,
                        sAlive: 1,
                        sSeverity: severitySel,
                        sClassType: sClassValue,
                        sClassName: sClassName,
                        strDescription: this.$("#attackNameInput").val(), // 공격명 
                        strTitle: this.$("#strSigRule").val(),
                        lResponseBool: this.$('.lResponseCheck', this.el).prop('checked'),
                        lBlockBool: this.$('.lBlockCheck', this.el).prop('checked'),
                        lUsed: lUsedValue,
                        lThresholdNumValue: $('.lThresholdNumValue', this.el).val(),
                        lThresholdTimeValue: $('.lThresholdTimeValue', this.el).val()
                    };
                } else {
                    url = "api/securityPolicy/updateDetectionPolicy";
                    if (this.$('.lUsed', this.el).prop('checked')) {
                        lUsedValue = 1;
                    }
                    setParams = {
                        lCode: this.lCode,
                        strDescription: this.model.get("strDescription"), // 공격명 
                        lResponseBool: this.$('.lResponseCheck', this.el).prop('checked'),
                        lBlockBool: this.$('.lBlockCheck', this.el).prop('checked'),
                        lUsed: lUsedValue,
                        lThresholdNumValue: $('.lThresholdNumValue', this.el).val(),
                        lThresholdTimeValue: $('.lThresholdTimeValue', this.el).val()
                    };
                }
                this.model.fetch({
                    method: 'POST',
                    url: url,
                    contentType: 'application/json',
                    dataType: 'text',
                    data: JSON.stringify(setParams),
                    async: false,
                    success: function (model) {
                    	console.log(model);
                    }
                });
            } else {
                var self = this;
                var severitySel = $('#severityLevel option:selected', this.el).val();
                var sClassValue = $('#signatureClassTypeSelect option:selected', this.el).val();
                if (this.$('.lUsed', this.el).prop('checked')) {
                    lUsedValue = 1;
                }
                var setParams = {
                    sClassType: sClassValue, // 공격유형
                    strDescription: this.$("#attackNameInput").val(), // 공격명
                    sSeverity: severitySel, // 위험도
                    strTitle: this.$("#strSigRule").val(), // 패턴입력
                    sAlive: 1,
                    lResponseBool: this.$('.lResponseCheck', this.el).prop('checked'),
                    lBlockBool: this.$('.lBlockCheck', this.el).prop('checked'),
                    lUsed: lUsedValue,
                    lThresholdNumValue: $('.lThresholdNumValue', this.el).val(),
                    lThresholdTimeValue: $('.lThresholdTimeValue', this.el).val()
                };
                Backbone.ajax({
                    method: 'POST',
                    url: 'api/securityPolicy/insertUserSignature',
                    contentType: 'application/json',
                    dataType: 'text',
                    data: JSON.stringify(setParams),
                    async: false,
                    success: function (data) {
                        self.lCode = data;
                        self.strDescription = self.$("#attackNameInput").val();
                        self.sSeverity = self.$('#severityLevel option:selected').val();
                        self.strSigRule = self.$("#strSigRule").val();
                        self.strSummary = self.$("#strSummary").val();
                        self.type = "update";
                        self.sClassType = sClassValue;
                    }
                });
            }
        },
        // 상세정보 조회 
        getDetectionPolicyDetailList: function () {
            var self = this;
            this.model.fetch({
                method: 'POST',
                url: 'api/securityPolicy/selectDetectionPolicyDetail',
                contentType: 'application/json',
                data: JSON.stringify({lCode: this.lCode}),
                success: function () {
                    self.addDetectionPolicyDetail();
                    self.viewResponseData();
                }
            });
        },
        // 공격유형 전체 select box로 조회 
        getAttackTypeList: function () {
            var self = this;
            this.collection.fetch({
                method: 'POST',
                data: JSON.stringify({}),
                async: false,
                url: 'api/securityPolicy/selectAttackTypeSelect',
                contentType: 'application/json',
                success: function () {
                    self.addAttackType();
                }
            });
        },
        setSeverityModel: function () {
            if (this.model.get('sSeverity') == 5) {
                this.selected0 = "selected";
                this.selected1 = "";
                this.selected2 = "";
                this.selected3 = "";
            } else if (this.model.get('sSeverity') == 3) {
                this.selected0 = "";
                this.selected1 = "selected";
                this.selected2 = "";
                this.selected3 = "";
            } else if (this.model.get('sSeverity') == 1) {
                this.selected0 = "";
                this.selected1 = "";
                this.selected2 = "selected";
                this.selected3 = "";
            } else if (this.model.get('sSeverity') == 0) {
                this.selected0 = "";
                this.selected1 = "";
                this.selected2 = "";
                this.selected3 = "selected";
            }
            this.model.set({
                selected0: this.selected0,
                selected1: this.selected1,
                selected2: this.selected2,
                selected3: this.selected3
            });
        },
        addDetectionPolicyDetail: function () {
            if (this.editable == true) {
                $('#strTitle', this.el).html(this.model.get('strDescription'));
                $('#attackType', this.el).text(dataExpression.nullToString(this.model.get('strAttackType')));
                $('#severityColor', this.el).addClass('threat-bg-lv' + dataExpression.severityColorData(this.model.get('sSeverity')));
                $('#bSeverity', this.el).append(dataExpression.severityData(this.model.get('sSeverity')));
                $('#lCode', this.el).text(this.model.get('lCode'));
                if (this.type == "update" && this.lCode < 1000000) {
                    $('#strDetect', this.el).html(this.model.get('strSigRule'));
                }
                $('#strSummary', this.el).text(dataExpression.nullToString(this.model.get('strSummary')));
                $('#lThresholdNum', this.el).text(this.model.get('lThresholdNum'));
                $('#lThresholdTime', this.el).text(this.model.get('lThresholdTime'));
            } else {
                $('#strTitle', this.el).html(this.model.get('strDescription'));
                $('#attackType', this.el).text(dataExpression.nullToString(this.model.get('strAttackType')));
                $('#severityColor', this.el).addClass('threat-bg-lv' + dataExpression.severityColorData(this.model.get('sSeverity')));
                $('#severityColor', this.el).addClass('rect margin-r5');
                $('#bSeverity', this.el).append(dataExpression.severityData(this.model.get('sSeverity')));
                $('#lCode', this.el).text(this.model.get('lCode'));
                $('#strDetect', this.el).html(this.model.get('strSigRule'));
                $('#strSummary', this.el).text(dataExpression.nullToString(this.model.get('strSummary')));
                $('#lThresholdNum', this.el).text(this.model.get('lThresholdNum'));
                $('#lThresholdTime', this.el).text(this.model.get('lThresholdTime'));
            }
        },
        viewResponseData: function () {
            //사용 여부
            var strUsed = locale.usedStr;
            var strNonUsed = locale.beNotInUse;
            if (this.model.get("lUsed") == 1) {
                this.strValue = "checked";
                $('#lUsed', this.el).text(strUsed);
            } else {
                this.strValue = "";
                $('#lUsed', this.el).text(strNonUsed);
            }
            //
            if (this.model.get("lResponse") == 131074 || this.model.get("lResponse") == 196610) {
                //차단안함
                $('#packet', this.el).text(strUsed);
                $('#block', this.el).text(strNonUsed);
                this.strResponse = "checked";
                this.strBlock = "";
                this.lBlockBool = false;
                this.lResponseBool = true;
            } else if (this.model.get("lResponse") == 131073 || this.model.get("lResponse") == 196609) {
                //차단안함
                $('#packet', this.el).text(strNonUsed);
                $('#block', this.el).text(strNonUsed);
                this.strResponse = "";
                this.strBlock = "";
                this.lBlockBool = false;
                this.lResponseBool = false;
            } else if (this.model.get("lResponse") == 268566529) {
                //차단 268566529
                $('#packet', this.el).text(strNonUsed);
                $('#block', this.el).text(strUsed);
                this.strResponse = "";
                this.strBlock = "checked";
                this.lBlockBool = true;
                this.lResponseBool = false;
            } else if (this.model.get("lResponse") == 268566530) {
                //차단
                $('#packet', this.el).text(strUsed);
                $('#block', this.el).text(strUsed);
                this.strResponse = "checked";
                this.strBlock = "checked";
                this.lBlockBool = true;
                this.lResponseBool = true;
            }
        },
        // 공격유형 정보 append
        addAttackType: function () {
            var self = this;
            for (var i = 0; i < self.collection.length; i++) {
                if (this.type == "update") {
                    $('#attackTypeSelect', this.el).text(this.model.get('strAttackType'));
                } else if (this.type == "insert") {
                    $('#attackTypeSelect', this.el).append("<input type='text' name='attackNameSelect' class='form-control col-xs-8 margin-r5' value='<%=model.strDescription %>' />'");
                }
            }
        },
        // 시그니처 도움말 팝업 
        patternHelpPopup: function () {
            var patternExemplePopup = new PatternExemplePopup();
            Backbone.ModalView.msg({
                size: 'medium-large',
                type: 'info',
                title: locale.signatureDetailDescription,
                body: patternExemplePopup
            });
        },
        // 공격유형 정보 
        getSignatureClassType: function () {
            var self = this;
            this.signatureClassTypeCollection.fetch({
                method: 'POST',
                url: 'api/securityPolicy/selectSignatureClassType',
                contentType: 'application/json',
                data: JSON.stringify({}),
                async: false,
                success: function () {
                    self.addOneSignatureTypeSelect('update');
                }
            });
        },
        addOneSignatureTypeSelect: function () {
            var self = this;
            for (var i = 0; i < self.signatureClassTypeCollection.length; i++) {
                if (parseInt(self.signatureClassTypeCollection.at(i).get('nClassType')) >= 99) {
                    $('#signatureClassTypeSelect', self.el).append('<option value="' + self.signatureClassTypeCollection.at(i).get('nClassType') + '">' + self.signatureClassTypeCollection.at(i).get('strName') + '</option>');
                }
            }
            if (this.type == "update") {
                for (var i = 0; i < self.signatureClassTypeCollection.length; i++) {
                    if (self.model.get('sClassType') == self.signatureClassTypeCollection.at(i).get('nClassType')) {
                        $("#signatureClassTypeSelect option[value=" + self.model.get('sClassType') + "]").attr("selected", "selected");
                    }
                }
            }
        },
        isValid: function () {
            var self = this;
            this.valid = true;

            var $strSigName = $(".attackNameInput", this.el);		// 공격명	 
            var $strSigRule = $(".strSigRule", this.el);			// 룰   

            this.resultAttackNameVal = Backbone.Utils.validation.validateIsNull($strSigName.val());
            if (this.valid && this.resultAttackNameVal != true) {
                alertMessage.infoMessage(this.resultAttackNameVal, "warn");
                Backbone.Utils.Tip.validationTooltip($strSigName, this.resultAttackNameVal);
                this.valid = false;
            }

            if (this.resultAttackNameVal == true)
                Backbone.Utils.Tip.validationTooltip($strSigName, true);

            // 공격명 중복 검사 
            var setParam = {
                strDescription: $(".attackNameInput", self.el).val()
            };
            if ((this.lCode == undefined || this.lCode >= 1000000) && this.type == 'insert') {
                if (this.resultAttackNameVal == true) {
                    this.model.fetch({
                        method: 'POST',
                        url: 'api/securityPolicy/isDuplicateSignatureName',
                        contentType: 'application/json',
                        data: JSON.stringify(setParam),
                        async: false,
                        success: function (model) {
                            var signatureName = model.get('strDescription');
                            if (signatureName === null || signatureName === "") {
                                self.resultAttackNameVal = true;
                                self.valid = true;
                            } else {
                                if (self.attackName != self.$(".attackNameInput").val()) {
                                    var resultMessage = errorLocale.validation.isDuplicateAttackName;
                                    self.resultAttackNameVal = Backbone.Utils.Tip.validationTooltip($('.attackNameInput', self.el), resultMessage);
                                    self.resultAttackNameVal = false;
                                    self.valid = false;
                                    alertMessage.infoMessage(resultMessage, "warn");
                                } else {
                                    self.resultAttackNameVal = true;
                                    self.valid = true;
                                }
                            }
                        }
                    });
                }
            }
            if (this.resultlThresholdNum != true) {
                this.valid = false;
            }
            if (this.resultlThresholdTime != true) {
                this.valid = false;
            }

            // 룰 null 체크
            this.resultStrSigRuleVal = Backbone.Utils.validation.validateIsNull($strSigRule.val());
            if (this.valid && this.resultStrSigRuleVal != true) {
                alertMessage.infoMessage(this.resultStrSigRuleVal, "warn");
                Backbone.Utils.Tip.validationTooltip($strSigRule, this.resultStrSigRuleVal);
                this.valid = false;
            }
            if (this.resultStrSigRuleVal == true)
                Backbone.Utils.Tip.validationTooltip($strSigRule, true);

            // 룰 패턴 검사 
            if (typeof this.lCode == 'undefined' || parseInt(this.lCode) >= 1000000) {
                var signatureRule = this.$(".strSigRule").val();
                var setParam = {
                    strSigRule: encodeURIComponent(signatureRule.replace(/^\s\s*/, '').replace(/\s\s*$/, ''))
                };
                if (this.valid && this.resultStrSigRuleVal == true) {
                    this.model.fetch({
                        method: 'POST',
                        url: 'api/securityPolicy/signatureRuleCheck',
                        contentType: 'application/json',
                        data: JSON.stringify(setParam),
                        async: false,
                        success: function (response) {
                            if (self.model.get('sigRuleYn') === 0) {
                                self.resultStrSigRuleVal = true;
                                self.valid = true;
                            } else {
                                var resultRuleMessage = errorLocale.validation.invaildSignature;
                                alertMessage.infoMessage(self.model.get('errorMessage'), 'warn', '', 'small');
                                self.resultStrSigRuleVal = Backbone.Utils.Tip.validationTooltip($('.strSigRule', self.el), resultRuleMessage);
                                self.resultStrSigRuleVal = true;
                                self.valid = false;
                            }
                        }
                    });
                }
            }
            return this.valid;
        },
        changeThresholdNum: function () {
            var value = $('.lThresholdNum', this.el).val();
            this.model.set({
                lThresholdNumValue: value,
                changing: true
            }, {validate: true, attribute: 'lThresholdNumValue'});
        },
        changeThresholdTime: function () {
            var value = $('.lThresholdTime', this.el).val();
            this.model.set({
                lThresholdTimeValue: value,
                changing: true
            }, {validate: true, attribute: 'lThresholdTimeValue'});
        },
        changelUsed: function () {
            var lUsedValue;
            var strValue;
            if (this.$('.lUsed', this.el).prop('checked')) {
                lUsedValue = 1;
                strValue = 'checked';
            } else {
                lUsedValue = 0;
                strValue = '';
            }
            this.model.set({
                lUsedValue: lUsedValue,
                strValue: strValue,
                changing: true
            });
        },
        changelResponse: function () {
            var lResponseValue;
            var strResponse;
            var lResponseBool;
            if (this.$('.lResponseCheck', this.el).prop('checked')) {
                lResponseValue = 131074;
                lResponseBool = true;
                strResponse = 'checked';
            } else {
                lResponseValue = 131073;
                lResponseBool = false;
                strResponse = '';
            }
            this.model.set({
                lResponseValue: lResponseValue,
                lResponseBool: lResponseBool,
                strResponse: strResponse,
                changing: true
            });
        },
        changelBlock: function () {
            var lBlockValue;
            var strBlock;
            var lBlockBool;
            if (this.$('.lBlockCheck', this.el).prop('checked')) {
                lBlockValue = 268566530;
                lBlockBool = true;
                strBlock = 'checked';
            } else {
                lBlockValue = 268566529;
                lBlockBool = false;
                strBlock = '';
            }
            this.model.set({
                lBlockValue: lBlockValue,
                lBlockBool: lBlockBool,
                strBlock: strBlock,
                changing: true
            });
        },
        validatelThresholdNum: function () {
            var lThresholdNum = this.$('#lThresholdNum', this.el).val();
            this.resultlThresholdNum = false;
            if (_.isNull(lThresholdNum) && lThresholdNum == "") {
                return this.resultlThresholdNum = Backbone.Utils.Tip.validationTooltip($('#lThresholdNum', this.el), true);
            }
            this.resultlThresholdNum = Backbone.Utils.validation.validateIThresholdRangeNum(lThresholdNum);
            Backbone.Utils.Tip.validationTooltip($('#lThresholdNum'), this.resultlThresholdNum);

            if (this.resultlThresholdNum == true) {
                return this.resultlThresholdNum = Backbone.Utils.Tip.validationTooltip($('#lThresholdNum', this.el), this.resultlThresholdNum);
            } else {
                alertMessage.infoMessage(this.resultlThresholdNum, "warn");
                return this.resultlThresholdNum = Backbone.Utils.Tip.validationTooltip($('#lThresholdNum', this.el), this.resultlThresholdNum);
            }
            return this.resultlThresholdNum = true;
        },
        validatelThresholdTime: function () {
            var lThresholdTime = this.$('#lThresholdTime', this.el).val();
            this.resultlThresholdTime = false;
            if (_.isNull(lThresholdTime) && lThresholdTime == "") {
                return this.resultlThresholdTime = Backbone.Utils.Tip.validationTooltip($('#lThresholdTime', this.el), true);
            }
            this.resultlThresholdTime = Backbone.Utils.validation.validateIThresholdRangeTime(lThresholdTime);
            Backbone.Utils.Tip.validationTooltip($('#lThresholdTime'), this.resultlThresholdTime);

            if (this.resultlThresholdTime == true) {
                return this.resultlThresholdTime = Backbone.Utils.Tip.validationTooltip($('#lThresholdTime', this.el), this.resultlThresholdTime);
            } else {
                alertMessage.infoMessage(this.resultlThresholdTime, "warn");
                return this.resultlThresholdTime = Backbone.Utils.Tip.validationTooltip($('#lThresholdTime'), this.resultlThresholdTime);
            }
            return this.resultlThresholdTime = true;
        },
        byteCheck4000: function (e) {
            Backbone.Utils.validation.validateTextArea(e, 4000);
        },
        numberInput: function (e) {
            return Backbone.Utils.validation.keyDownNumber(e);
        },
    });
});