define(function (require) {

    "use strict";

    var $ = require('jquery'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str');
    var dataExpression = require('utils/dataExpression');

    var tpl = require('text!tpl/securityPolicy/detectionPolicyHelp.html');

    var DetectionPolicyHelp = require('models/securityPolicy/detectionPolicyHelpModel');
    return Backbone.View.extend({
        editable: false,
        template: _.template(tpl),
        templateForm: _.template([
            '<form class="form-inline detectionPolicyHelpEdit" role="form">',
            '<table class="view-table col-xs-12">',
            // 수정/저장시 모든 도움말 내용이 나옴 
            '<tr>',
            '<% if (this.type == "update" && this.lCode >= 1000000) { %>',
            '<th class="col-lg-3">',
            locale.summaryDescripttion,
            '</th>',
            '<td>',
            '<input type="text" id="strSummary" name="strSummary" class="form-control col-xs-12 margin-r5 strSummary" value="<%= model.strSummary %>" maxlength="64"/>',
            //'<textarea id="strSummary" class="form-control col-xs-12" rows="5" value="<%= model.strSummary %>"><%= model.strSummary %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>',
            locale.detailComment,
            '</th>',
            // '<% if (this.type == "update" && this.lCode >= 1000000) { %>',
            '<td>',
            '<textarea id="detailDescription" name="detailDescriptionTextArea" class="form-control col-xs-12" rows="5"><%= model.strDescription %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>',
            locale.solution,
            '</th>',
            '<td>',
            '<textarea id="solution" name="solutionTextArea" class="form-control col-xs-12" rows="5"><%= model.strSolution %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>',
            locale.reference,
            '</th>',
            '<td>',
            '<textarea id="reference" name="referenceTextArea" class="form-control col-xs-12" rows="5"><%= model.strReference %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>BugTrap ID</th>',
            '<td>',
            '<input type="text" id="bugtrapId" name="bugtrapIdInput" class="form-control col-xs-12 margin-r5" value="<%= model.strbId %>" maxlength="64"/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>CVE ID</th>',
            '<td>',
            '<input type="text" id="cveId" name="cveIdInput" class="form-control col-xs-12 margin-r5" value="<%= model.strCveId %>" maxlength="64"/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>Vulnerable</th>',
            '<td>',
            '<input id="strVul" type="text" class="form-control col-xs-12" value="<%= model.strVul %>" maxlength="64"/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>False Positive</th>',
            '<td>',
            '<textarea id="falsePositive" name="falsePositiveTextArea" class="form-control col-xs-12" rows="5"><%= model.strFalsePositive %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<tr>',
            '<th>Invulnerable</th>',
            '<td>',
            '<textarea id="strNotVul" name="invulnerableTextArea" class="form-control col-xs-12" rows="5"><%= model.strNotVul %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>Reliability of IP</th>',
            '<td>',
            '<textarea id="reliabilityOfIp" name="reliabilityOfIpTextArea" class="form-control col-xs-12" rows="5"><%= model.strAddrsPoof %></textarea>',
            '</td>',
            '</tr>',
            '</table>',
            '<% } %>',
            '</form>',
            // 벤더정책 도움말은 수정 불가 
            '<% if (this.type == "update" && this.lCode < 1000000) { %>',
            '<th class="col-lg-3">',
            locale.summaryDescripttion,
            '</th>',
            '<td>',
            '<input type="text" id="strSummary" name="strSummary" class="form-control col-xs-12 margin-r5 strSummary" value="<%= model.strSummary %>" disabled/>',
            '</td>',
            '</tr>',
            '<th>',
            locale.detailComment,
            '</th>',
            '<td>',
            '<textarea id="detailDescription" name="detailDescriptionTextArea" class="form-control col-xs-12" rows="5" disabled><%= model.strDescription %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>',
            locale.solution,
            '</th>',
            '<td>',
            '<textarea id="solution" name="solutionTextArea" class="form-control col-xs-12" rows="5" disabled><%= model.strSolution %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>',
            locale.reference,
            '</th>',
            '<td>',
            '<textarea id="reference" name="referenceTextArea" class="form-control col-xs-12" rows="5" disabled><%= model.strReference %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>BugTrap ID</th>',
            '<td>',
            '<input type="text" id="bugtrapId" name="bugtrapIdInput" class="form-control col-xs-12 margin-r5" value="<%= model.strbId %>" disabled/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>CVE ID</th>',
            '<td>',
            '<input type="text" id="cveId" name="cveIdInput" class="form-control col-xs-12 margin-r5" value="<%= model.strCveId %>" disabled/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>Vulnerable</th>',
            '<td>',
            '<input id="strVul" type="text" class="form-control col-xs-12" value="<%= model.strVul %>" disabled/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>False Positive</th>',
            '<td>',
            '<textarea id="falsePositive" name="falsePositiveTextArea" class="form-control col-xs-12" rows="5" disabled><%= model.strFalsePositive %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<tr>',
            '<th>Invulnerable</th>',
            '<td>',
            '<textarea id="strNotVul" name="invulnerableTextArea" class="form-control col-xs-12" rows="5" disabled><%= model.strNotVul %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>Reliability of IP</th>',
            '<td>',
            '<textarea id="reliabilityOfIp" name="reliabilityOfIpTextArea" class="form-control col-xs-12" rows="5" disabled><%= model.strAddrsPoof %></textarea>',
            '</td>',
            '</tr>',
            '</table>',
            '</form>',
            '<% } %>',
            // 사용자 정의 정책 신규 생성 > 도움말 입력시 공격명, 공격유형, 위험도는 등록정보에서 입력받는다.
            '<% if (this.type == "insert") { %>',
            '<form class="form-inline detectionPolicyHelpEdit" role="form">',
            '<table class="view-table col-xs-12">',
            '<tr>',
            '<th class="col-lg-3">',
            locale.summaryDescripttion,
            '</th>',
            '<td>',
            '<input type="text" id="strSummary" name="strSummary" class="form-control col-xs-12 margin-r5 strSummary" value="<%= model.strSummary %>" maxlength="64"/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>',
            locale.solution,
            '</th>',
            '<td>',
            '<textarea id="solution" name="solutionTextArea" class="form-control col-xs-12" rows="5"><%= model.strSolution %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>',
            locale.reference,
            '</th>',
            '<td>',
            '<textarea id="reference" name="referenceTextArea" class="form-control col-xs-12" rows="5"><%= model.strReference %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>BugTrap ID</th>',
            '<td>',
            '<input type="text" id="bugtrapId" name="bugtrapIdInput" class="form-control col-xs-12 margin-r5" value="<%= model.strbId %>" maxlength="64"/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>CVE ID</th>',
            '<td>',
            '<input type="text" id="cveId" name="cveIdInput" class="form-control col-xs-12 margin-r5" value="<%= model.strCveId %>" maxlength="64"/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>Vulnerable</th>',
            '<td>',
            '<input id="strVul" type="text" class="form-control col-xs-12" value="<%= model.strVul %>" maxlength="64"/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>False Positive</th>',
            '<td>',
            '<textarea id="falsePositive" name="falsePositiveTextArea" class="form-control col-xs-12" rows="5"><%= model.strFalsePositive %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<tr>',
            '<th>Invulnerable</th>',
            '<td>',
            '<textarea id="strNotVul" name="invulnerableTextArea" class="form-control col-xs-12" rows="5"><%= model.strNotVul %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>Reliability of IP</th>',
            '<td>',
            '<textarea id="reliabilityOfIp" name="reliabilityOfIpTextArea" class="form-control col-xs-12" rows="5"><%= model.strAddrsPoof %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>',
            locale.detailComment,
            '</th>',
            '<td>',
            '<textarea id="detailDescription" name="detailDescriptionTextArea" class="form-control col-xs-12" rows="5"><%= model.strhelpDescription %></textarea>',
            '</td>',
            '</tr>',
            '</table>',
            '</form>',
            '<% } %>',
        ].join('')),
        initialize: function (options) {
            this.lCode = options.lCode;
            this.attackName = options.attackName;
            this.model = new DetectionPolicyHelp();
        },
        events: {
            "keyup #detailDescription": "byteCheck20000",
            "keyup #solution": "byteCheck5000",
            "keyup #reference": "byteCheck5000",
            "keyup #falsePositive": "byteCheck5000",
            "keyup #strNotVul": "byteCheck5000",
            "keyup #reliabilityOfIp": "byteCheck5000"
        },
        render: function () {
            if (this.lCode != undefined) {
                this.editable = false;
                this.type = "update";
                this.$el.html(this.template({
                    model: this.model.toJSON(),
                    locale: locale
                }));
                this.getDetectionPolicyHelp();
                return this;

            } else {
                this.editable = true;
                this.type = "insert";
                this.$el.html(this.templateForm({
                    model: this.model.toJSON(),
                    locale: locale
                }));
            }
            this.addDetectionPolicyHelp();
            return this;
        },
        renderEdit: function () {
            this.editable = true;
            this.type = "update";
            this.$el.html(this.templateForm({
                model: this.model.toJSON(),
                locale: locale
            }));
            $('#bSeverity', this.el).append(dataExpression.severityData(this.model.get('sSeverity')));		// edit(수정)화면 위험도 
            return this;
        },
        status: function () {
            return this.editable;
        },
        statusType: function () {
            return this.type;
        },
        toggle: function (insertIndex) {
            if (this.editable) {
                this.mergeDetectionPolicyHelpInfo(insertIndex);
                return this.render();
            } else {
                return this.renderEdit();
            }
        },
        mergeDetectionPolicyHelpInfo: function (insertIndex) {
            if (this.type == "update") {
            	
            	if(this.lCode >= 1000000){//사용자 정의룰 일경우에만 저장
                	
            		var Obj = insertIndex;
            		var self = this;
            		var setParams = {
            				lCode: this.lCode,
            				strTitle: this.attackName,
            				strSummary: this.$("#strSummary").val(),
            				strDescription: this.$("#detailDescription").val(),
            				strSolution: this.$("#solution").val(),
            				strReference: this.$("#reference").val(),
            				strbId: this.$("#bugtrapId").val(),
            				strCveId: this.$("#cveId").val(),
            				strVul: this.$("#strVul").val(),
            				strNotVul: this.$("#strNotVul").val(),
            				strAddrsPoof: this.$("#reliabilityOfIp").val(),
            				strFalsePositive: this.$("#falsePositive").val(),
            				helpYN: Obj['helpYN']
            		};
            		
            		
            		self.model.fetch({
            			method: 'POST',
            			url: 'api/securityPolicy/updateUserSignature',
            			contentType: 'application/json',
            			dataType: 'text',
            			data: JSON.stringify(setParams),
            			async: false,
            			success: function (model) {
            				console.log(model);
            			}
            		});
                }
            } else {
                var Obj = insertIndex;
                var self = this;
                var setParams = {
                    lCode: Obj['lCode'], // 등록정보에서 받아온 lCode
                    strTitle: Obj['strDescription'],
                    strSummary: $("#strSummary").val(),
                    strDescription: $("#detailDescription").val(), // 상세설명
                    strFalsePositive: $("#falsePositive").val(), // FalsePositive
                    strSolution: $("#solution").val(), // 해결방법 
                    strReference: $("#reference").val(), // 참조
                    strbId: $("#bugtrapId").val(), // BId
                    strCveId: $("#cveId").val(), // CVEID
                    strVul: $("#strVul").val(), // 취약점
                    strNotVul: $("#strNotVul").val(), // Invulnerable
                    strAddrsPoof: $("#reliabilityOfIp").val()		// Reliability of IP
                };
                self.model.fetch({
                    method: 'POST',
                    url: 'api/securityPolicy/updateUserSignatureHelp', // lCode를 가지고 insert
                    contentType: 'application/json',
                    dataType: 'text',
                    data: JSON.stringify(setParams),
                    async: false,
                    success: function (model) {
                        self.lCode = Obj['lCode'];
                        self.strTitle = Obj['strDescription'];
                        self.bSeverity = Obj['sSeverity'];
                        self.strSummary = self.$("#strSummary").val();
                        self.strDetect = Obj['strSigRule'];
                    }
                });
            }
        },
        getDetectionPolicyHelp: function () {
            var self = this;
            this.model.fetch({
                method: 'POST',
                url: 'api/securityPolicy/selectDetectionPolicyHelp',
                contentType: 'application/json',
                data: JSON.stringify({lCode: this.lCode}),
                success: function () {
                    self.addDetectionPolicyHelp();
                }
            });
        },
        addDetectionPolicyHelp: function () {
            $("#strSummary", this.el).text(dataExpression.nullToString(this.model.get('strSummary'))); 				// 요약 설명 
            $('#strDescription', this.el).text(dataExpression.nullToString(this.model.get('strDescription')));	// 상세설명 
            $('#strSolution', this.el).text(dataExpression.nullToString(this.model.get('strSolution')));			// 해결 방법
            $('#strReference', this.el).text(dataExpression.nullToString(this.model.get('strReference')));			// 참조 
            $('#strBId', this.el).text(dataExpression.nullToString(this.model.get('strbId')));						// bId
            $('#strCveId', this.el).text(dataExpression.nullToString(this.model.get('strCveId')));					// CveId
            $('#strVul', this.el).text(dataExpression.nullToString(this.model.get('strVul')));						// Vulnerable
            $('#strFalsePositive', this.el).text(dataExpression.nullToString(this.model.get('strFalsePositive')));	// False Positive
            $('#strNotVul', this.el).text(dataExpression.nullToString(this.model.get('strNotVul')));
            $('#reliabilityOfIp', this.el).text(dataExpression.nullToString(this.model.get('strAddrsPoof')));
        },
        isValid: function () {
            this.valid = true;

            this.resultStrNameVal = Backbone.Utils.validation.validateIsNull($('#attackTypeInput').val());
            if (this.valid && this.resultStrNameVal != true) {
                var tooltipResult = Backbone.Utils.Tip.validationTooltip($('#attackTypeInput'), this.resultStrNameVal);
                this.valid = false;
            }
            return this.valid;
        },
        byteCheck20000: function (e) {
            Backbone.Utils.validation.validateTextArea(e, 20000);
        },
        byteCheck5000: function (e) {
            Backbone.Utils.validation.validateTextArea(e, 5000);
        }
    });
});