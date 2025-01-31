define(function (require) {

    var $ = require('jquery'),
            Backbone = require('backbone'),
            dataExpression = require('utils/dataExpression'),
            tpl = require('text!tpl/securityPolicy/registerYaraRulePolicy.html'),
            YaraRulePolicyModel = require('models/securityPolicy/yaraRulePolicyModel'),
            YaraRulePolicyGroupCollection = require('collections/securityPolicy/yaraRulePolicyGroupCollection'),
            alertMessage = require('utils/AlertMessage'),
            locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error');

    return Backbone.View.extend({
        editable: false,
        type: "",
        template: _.template(tpl),
        templateEditForm: _.template([
            '<form class="form-inline detectionPolicyEdit" role="form">',
            '<table class="view-table col-xs-12">',
            '<tr>',
            '<th width="25%"><%=locale.yaraRuleType%></th>',
            '<td>',
            '<select id="yaraRuleGroupSelect" name="yaraRuleGroupSelect" class="form-control margin-r5 col-xs-5" <% if(type == "update" && groupIndex < 99) { %> disabled <% } %> >',
            '</select>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th><%=locale.yaraRulePolicyName%> *</th>',
            '<td>',
            '<input type="text" id="ruleName" name="ruleName" class="form-control col-xs-5 ruleName" value="<%= model.ruleName %>" style="ime-mode:disabled;" maxlength="128"/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th><%=locale.severity%></th>',
            '<td>',
            '<select id="severityLevel" name="sSeverity" class="form-control margin-r5 col-xs-5" <% if(type == "update" && groupIndex < 99) { %> disabled <% } %>>',
            '<option value="5" <%= model.selected0 %> ><%=locale.high%></option>',
            '<option value="3" <%= model.selected1 %> ><%=locale.med%></option>',
            '<option value="1" <%= model.selected2 %> ><%=locale.low%></option>',
            '<option value="0" <%= model.selected3 %> ><%=locale.information%></option>',
            '</select>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th><%=locale.usedStr%></th>',
            '<td>',
            '<input type="checkbox" id="lUsed" name="lUsed" class="form-control lUsed" <% if(model.lUsed == 1) {%> checked <% }%> style="ime-mode:disabled;"/>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>meta *</th>',
//							'<td>',
//							'<textarea id="meta" name="meta" class="form-control col-xs-12 meta" rows="7" <% if(type == "update" && groupIndex < 99) { %> disabled <% } %> ><%= model.meta %></textarea>',
//							'</td>',
//							'</tr>',
//							'<tr>',
//							'<th>strings</th>',
//							'<td>',
//							'<textarea id="strings" name="strings" class="form-control col-xs-12 strings" rows="7" <% if(type == "update" && groupIndex < 99) { %> disabled <% } %> ><%= model.strings %> </textarea>',
//							'</td>',
//							'</tr>',
//							'<tr>',
//							'<th>condition</th>',
//							'<td>',
//							'<textarea id="condition" name="condition" class="form-control col-xs-12 condition" rows="7" <% if(type == "update" && groupIndex < 99) { %> disabled <% } %> ><%= model.condition %> </textarea>',
//							'</td>',
            '<td>',
            '<textarea id="meta" name="meta" class="form-control col-xs-12 meta" rows="7" style="ime-mode:disabled;" ><%= model.meta %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>strings *</th>',
            '<td>',
            '<textarea id="strings" name="strings" class="form-control col-xs-12 strings" rows="7" style="ime-mode:disabled;" ><%= model.strings %></textarea>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th>condition *</th>',
            '<td>',
            '<textarea id="condition" name="condition" class="form-control col-xs-12 condition" rows="7" style="ime-mode:disabled;" ><%= model.condition %></textarea>',
            '</td>',
            '</tr>',
            '</table>',
            '</form>'
        ].join('')),
        initialize: function (options) {
            this.lIndex = options.lIndex;
            this.groupIndex = options.groupIndex;
            this.model = new YaraRulePolicyModel();
            this.yaraRulePolicyGroupCollection = new YaraRulePolicyGroupCollection();
            this.ruleName = options.ruleName;
        },
        events: {
//                            "keypress #ruleName" : "inputCheck"
//                            "keydown #ruleName" : "inputCheck"
            "keyup #ruleName" : "inputKeyUpNonKr",
            "focusout #ruleName" : "inputKeyUpNonKr",
            "keyup #meta": "byteCheck20000",
            "focusout #meta": "inputKeyUpNonKr",
            "keyup #strings": "byteCheck20000",
            "focusout #strings": "inputKeyUpNonKr",
            "keyup #condition": "byteCheck4000",
            "focusout #condition": "inputKeyUpNonKr"
        },
        status: function () {
            return this.editable;
        },
        statusType: function () {
            return this.type;
        },
        getInsertIndex: function () {
            return this.lIndex;
        },
        getInsertGroupIndex: function () {
            return this.groupIndex;
        },
        render: function () {
            if (this.lIndex != undefined) {
                this.editable = false;
                this.type = "update";
                this.$el.html(this.template({
                    model: this.model.toJSON(),
                    groupIndex: this.groupIndex,
                    type: this.type,
                    locale: locale
                }));
                this.getYaraRuleDetailInfo();

            } else {
                this.editable = true;
                this.type = "insert";
                this.$el.html(this.templateEditForm({
                    model: this.model.toJSON(),
                    groupIndex: this.groupIndex,
                    type: this.type,
                    locale: locale
                }));
                $('#sSeverity', this.el).append(dataExpression.severityData(this.model.get('sSeverity')));
                this.getYaraRuleGroupType();
            }
            return this;
        },
        renderEdit: function () {
            this.editable = true;
            this.type = "update";
            this.setSeverityModel();
            this.$el.html(this.templateEditForm({
                model: this.model.toJSON(),
                groupIndex: this.groupIndex,
                type: this.type,
                locale: locale
            }));
            this.getYaraRuleDetailInfo();
            this.getYaraRuleGroupType('update');	// 그룹 유형 조회
            if (this.type == "update" && this.groupIndex < 99) {
                $('.ruleName', this.el).prop('disabled', true);
                $('.meta', this.el).prop('disabled', true);
                $('.strings', this.el).prop('disabled', true);
                $('.condition', this.el).prop('disabled', true);
            }
            return this;
        },
        toggle: function () {
            if (this.editable) {
                this.mergeYaraRulePolicy(this.type);
                return this.render();
            } else {
                return this.renderEdit();
            }
        },
        mergeYaraRulePolicy: function (action) {
            var self = this;

            var url = this.getActionUrl(action);
            var params = this.makeParams(action);
            
            if (action == 'update') {
                this.dataType = 'text';
            } else {
                this.dataType = '';
            }
            
            this.groupIndex = $('#yaraRuleGroupSelect option:selected').val();
            this.model.fetch({
                method: 'POST',
                url: url,
                contentType: 'application/json',
                data: JSON.stringify(params),
                dataType: this.dataType,
                async: false,
                success: function (model) {
                    if (action === 'insert') {
                        self.lIndex = model.get('lIndex');
                        self.type = "update";
                        self.ruleName = self.$("#ruleName").val()
                    }	// 저장후 화면 전환을 위해 필요한 인덱스
                    alertMessage.infoMessage(locale.msg.saveMsg, 'Info');
                }
            });
            this.groupIndex = $('#yaraRuleGroupSelect option:selected').val();
            this.lIndex = this.lIndex;
        },
        getActionUrl: function (action) {
            return action === 'update' ? 'api/securityPolicy/updateYaraRule' : 'api/securityPolicy/insertYaraRule';
        },
        makeParams: function (action) {
            var yaraRuleGroupSelect = $('#yaraRuleGroupSelect option:selected', this.el).val();
            var groupName = $('#yaraRuleGroupSelect option:selected', this.el).text();
            var severitySel = $('#severityLevel option:selected', this.el).val();
            var setParams = {
                groupIndex: yaraRuleGroupSelect,
                groupName: groupName,
                ruleName: this.$("#ruleName").val(),
                sSeverity: severitySel,
                meta: this.$("#meta").val(),
                strings: this.$("#strings").val(),
                condition: this.$("#condition").val(),
                lUsed: (this.$('#lUsed').is(':checked') ? 1 : 0)
            };
            if (action === 'update') {
                setParams.lIndex = this.lIndex;
            }

            return setParams;
        },
        /** 우측 등록 정보 
         * 아코디언 목록의 인덱스를 전달 받아 상세 정보 조회
         */
        getYaraRuleDetailInfo: function () {
            var self = this;
            var setParams = {
                lIndex: this.lIndex,
                groupIndex: this.groupIndex
            };
            this.model.fetch({
                method: 'POST',
                url: 'api/securityPolicy/selectYaraRuleDetailInfo',
                contentType: 'application/json',
                data: JSON.stringify(setParams),
                async: false,
                success: function (model) {
                    self.setYaraRuleDetailData(model);
                }
            });
        },
        setYaraRuleDetailData: function (model) {
            var usedVal = locale.beNotInUse
            if (model.get('lUsed') == 1) {
                usedVal = locale.usedStr
            }
            $('#severityColor', this.el).addClass('threat-bg-lv' + dataExpression.severityColorData(model.get('sSeverity')));
            $('#sSeverity', this.el).append(dataExpression.severityData(model.get('sSeverity')));
            $("#groupName", this.el).text(model.get('groupName'));
            $("#ruleName", this.el).text(_.unescape(model.get('ruleName')));
            $("#meta", this.el).text(_.unescape(model.get('meta')));
            $("#strings", this.el).text(_.unescape(model.get('strings')));
            $("#condition", this.el).text(_.unescape(model.get('condition')));
            $("#lUsed", this.el).text(usedVal);
        },
        getYaraRuleGroupType: function () {
            var self = this;
            this.yaraRulePolicyGroupCollection.fetch({
                method: 'POST',
                url: 'api/securityPolicy/selectYaraRuleGroupType',
                contentType: 'application/json',
                data: JSON.stringify({}),
                async: false,
                success: function (collection) {
                    self.addOneYaraRuleTypeSelect('update');
                }
            });
        },
        addOneYaraRuleTypeSelect: function () {
            var self = this;
            for (var i = 0; i < self.yaraRulePolicyGroupCollection.length; i++) {
                if (this.groupIndex >= 99 || this.groupIndex == undefined) {
                    if (parseInt(self.yaraRulePolicyGroupCollection.at(i).get('groupIndex')) >= 99) {
                        $('#yaraRuleGroupSelect', this.el).append("<option value=" + self.yaraRulePolicyGroupCollection.at(i).get("groupIndex") + ">" + self.yaraRulePolicyGroupCollection.at(i).get("groupName") + "</option>");
                    }
                } else {
                    $('#yaraRuleGroupSelect', this.el).append("<option value=" + self.yaraRulePolicyGroupCollection.at(i).get("groupIndex") + ">" + self.yaraRulePolicyGroupCollection.at(i).get("groupName") + "</option>");
                }
            }
            if (this.type == "update") {
                for (var i = 0; i < self.yaraRulePolicyGroupCollection.length; i++) {
                    // editable 상태일 경우 해당 유형그룹이 selected 상태여야함
                    if (self.model.get('groupIndex') == self.yaraRulePolicyGroupCollection.at(i).get('groupIndex')) {
                        $("#yaraRuleGroupSelect option[value=" + self.model.get('groupIndex') + "]").attr("selected", "selected");
                    }
                }
            }
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
        inputCheck: function (e) {
            var value = $('#ruleName', this.el).val();
            $('#ruleName', this.el).val(value.replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g, ''));
        },
        isValid: function () {
            var self = this;
            this.valid = true;

            var $strStrings = $(".strings", this.el);
            var $strCondition = $(".condition", this.el);
            var $strMeta = $(".meta", this.el);
            
            //NULL 체크
            this.resultYaraRuleNameVal = Backbone.Utils.validation.validateIsNull($('#ruleName').val());
            if (this.valid && this.resultYaraRuleNameVal != true) {
                alertMessage.infoMessage(errorLocale.validation.ruleNameNullValid, "warn", "", "small");
                Backbone.Utils.Tip.validationTooltip($('#ruleName', this.el), this.resultYaraRuleNameVal);
                this.valid = false;
                return this.valid;
            }

            if (this.resultYaraRuleNameVal == true) {
                Backbone.Utils.Tip.validationTooltip($('#ruleName', this.el), true);
                this.valid = true;
            }

            //영문, 숫자 _만 가능
            this.resultYaraRuleEngVal = Backbone.Utils.validation.isEngAndNum($('#ruleName').val());
            if (this.valid && this.resultYaraRuleEngVal != true) {
                alertMessage.infoMessage(errorLocale.validation.ruleNameValueValid, "warn", "", "small");
                Backbone.Utils.Tip.validationTooltip($('#ruleName', this.el), this.resultYaraRuleEngVal);
                this.valid = false;
                return this.valid;
            }

            if (this.resultYaraRuleEngVal == true) {
                Backbone.Utils.Tip.validationTooltip($('#ruleName', this.el), true);
                this.valid = true;
            }
            
            // meta 정보 입력문자 제한
            this.resultMetaVal = Backbone.Utils.validation.validateIsNull($strMeta.val());
            if (this.valid && this.resultMetaVal != true) {
                alertMessage.infoMessage(errorLocale.validation.metaNullValid, "warn", "", "small");
                Backbone.Utils.Tip.validationTooltip($strMeta, this.resultMetaVal);
                this.valid = false;
                return this.valid;
            }
            if (this.resultMetaVal == true) {
                Backbone.Utils.Tip.validationTooltip($strMeta, true);
                this.valid = true;
            }
            var meta = $("#meta", this.el).val();
            if (meta.match(/rule_id|rule_group_id|severity|rType|org_num/) && this.groupIndex >= 99) {
                alertMessage.infoMessage(errorLocale.validation.metaValidation, "warn", "", "small");
                Backbone.Utils.Tip.validationTooltip($strMeta, errorLocale.validation.metaValidation);
                this.valid = false;
                return this.valid;
            } else {
                Backbone.Utils.Tip.validationTooltip($strMeta, true);
                this.valid = true;
            }

            // 필수 입력 strings 
            this.resultStringsVal = Backbone.Utils.validation.validateIsNull($strStrings.val());
            if (this.valid && this.resultStringsVal != true) {
                alertMessage.infoMessage(errorLocale.validation.stringsNullValid, "warn", "", "small");
                Backbone.Utils.Tip.validationTooltip($strStrings, this.resultStringsVal);
                this.valid = false;
                return this.valid;
            }
            if (this.resultStringsVal == true) {
                Backbone.Utils.Tip.validationTooltip($strStrings, true);
                this.valid = true;
            }

            // 필수 입력 condition
            this.resultConditionVal = Backbone.Utils.validation.validateIsNull($strCondition.val());
            if (this.valid && this.resultConditionVal != true) {
                alertMessage.infoMessage(errorLocale.validation.conditionNullValid, "warn", "", "small");
                Backbone.Utils.Tip.validationTooltip($strCondition, this.resultConditionVal);
                this.valid = false;
                return this.valid;
            }
            if (this.resultConditionVal == true) {
                Backbone.Utils.Tip.validationTooltip($strCondition, true);
                this.valid = true;
            }

            var setParam = {
                ruleName: $("#ruleName", self.el).val()
            };
            this.model.fetch({
                method: 'POST',
                url: 'api/securityPolicy/isDuplicateYaraRuleName',
                contentType: 'application/json',
                data: JSON.stringify(setParam),
                async: false,
                success: function (model) {
                    var ruleName = model.get('ruleName');
                    if (ruleName === null || ruleName === "") {
                        self.resultRuleName = true;
                        self.resultYaraRuleNameVal = true;
                        self.resultYaraRuleEngVal = true;
                        self.valid = true;
                    } else {
                        if (self.ruleName != self.$("#ruleName").val()) {
                            alertMessage.infoMessage(errorLocale.validation.duplicateName, 'warn');
                            self.resultRuleName = Backbone.Utils.Tip.validationTooltip($('#ruleName', self.el), errorLocale.validation.duplicateName);
                            self.resultYaraRuleNameVal = false;
                            self.resultYaraRuleEngVal = false;
                            self.valid = false;
                        } else {
                            self.resultRuleName = true;
                        }
                    }
                }
            });

            return this.valid;
        },
        validMeta: function () {
            var meta = $("#meta", this.el).val();
            if (meta.match(/^[rule_id|rule_group_id|severity|org_num]^/)) {
                return false;
            }
            return true;
        },
        byteCheck4000: function (e) {
            Backbone.Utils.validation.keyUpNonKr(e);
            Backbone.Utils.validation.validateTextArea(e, 4000);
        },
        byteCheck20000: function (e) {
            Backbone.Utils.validation.keyUpNonKr(e);
            Backbone.Utils.validation.validateTextArea(e, 20000);
        },
        inputKeyUpNonKr: function(e) {
            Backbone.Utils.validation.keyUpNonKr(e);
        }
    });
});