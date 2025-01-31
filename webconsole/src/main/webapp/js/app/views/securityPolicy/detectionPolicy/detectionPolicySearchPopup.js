define(function (require) {

    "use strict";

    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            serialize = require('serialize'),
            dataExpression = require('utils/dataExpression'),
            locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error');

    return Backbone.View.extend({
        className: 'detectionPolicyContent',
        formEl: "form.detectionPolicyPopupForm",
        template: _.template([
            '<div class="modal-body padding-b10">',
            '<form class="form-inline detectionPolicyPopupForm" role="form">',
            '<div class="form-group col-xs-12 margin-t10">',
            '<label class="control-label col-xs-2"><%=locale.attackType%></label>',
            '<select name="attackTypeSelect" class="form-control col-xs-10">',
            '<option value=0><%=locale.attackTypeAll%></option>',
            '<option value=1>Access_Attempt</option>',
            '<option value=2>Backdoor/Trojan</option>',
            '<option value=3>DOS/DDOS</option>',
            '<option value=4>Information</option>',
            '<option value=5>Policy_Violation</option>',
            '<option value=6>Probe</option>',
            '<option value=7>Protocol_Vulnerability</option>',
            '<option value=8>Web_Access</option>',
            '<option value=9>Worm/Virus</option>',
            '<option value=99>User_Signature</option>',
            '</select>',
            '</div>',
            '<div class="form-group col-xs-12 margin-t10">',
            '<label class="control-label col-xs-2"><%=locale.attackName%></label>',
            '<input type="text" name="attackNameInput" class="form-control col-xs-10" placeholder="<%=errorLocale.validation.attackNameInput%>"/>',
            '</div>',
            '<div class="form-group col-xs-12 margin-t10">',
            '<label class="control-label col-xs-2"><%=locale.severity%></label>',
            '<select name="severityLevel" class="form-control col-xs-4">',
            '<option value=""><%=locale.noCondition%></option>',
            '<option value="5"><%=locale.high%></option>',
            '<option value="3"><%=locale.med%></option>',
            '<option value="1"><%=locale.low%></option>',
            '<option value="0"><%=locale.information%></option>',
            '</select>',
            '<label class="control-label col-xs-2"><%=locale.protocol%></label>',
            '<select name="protocol" class="form-control col-xs-4">',
            '<option value="0"><%=locale.noCondition%></option>',
            '<option value="1">ICMP</option>',
            '<option value="2">UDP</option>',
            '<option value="3">IP</option>',
            '</select>',
            '</div>',
            '<div class="form-group col-xs-12 margin-t10">',
            '<label class="control-label col-xs-2"><%=locale.thresholdValue%></label>',
            '<div class="col-xs-2 padding-clear">',
            '<input type="text" name="thresholdNum" class="form-control col-xs-12" placeholder="개"/>',
            '</div>',
            '<div class="col-xs-2">',
            '<input type="text" name="thresholdTime" class="form-control col-xs-12" placeholder="초"/>',
            '</div>',
            '<label class="control-label col-xs-2"><%=locale.packetSaveNoSapce%></label>',
            '<select class="form-control col-xs-4 font-size-12">',
            '<option value="0"><%=locale.packetSaveNoSapce%></option>',
            '<option value="1"><%=locale.packetSaveNoSapce%>1</option>',
            '<option value="2"><%=locale.packetSaveNoSapce%>2</option>',
            '</select>',
            '</div>',
            '<div class="form-group col-xs-12 margin-t10">',
            '<label class="control-label col-xs-2"><%=locale.isBlock%></label>',
            '<select name="intrusionCheck" class="form-control col-xs-4">',
            '<option><%=locale.noCondition%></option>',
            '<option value="1">Log without raw</option>',
            '<option value="2">Log with raw</option>',
            '<option value="0">Off</option>',
            '</select>',
            '<label class="control-label col-xs-2">CVE ID</label>',
            '<input type="text" name="cveId" class="form-control col-xs-4" placeholder=""/>',
            '</div>',
            '<div class="form-group col-xs-12 margin-t10 align-center border-dotted-top padding-t10">',
            '<div class="checkbox">',
            '<label>',
            '<input type="checkbox" name="resultSearch">',
            '<%=locale.resultInside%>',
            '</label>',
            '</div>',
            '<div class="checkbox margin-l5">',
            '<label>',
            '<input type="checkbox" name="useCheck">',
            '<%=locale.dbUsing%>',
            '</label>',
            '</div>',
            '<div class="checkbox margin-l5">',
            '<label>',
            '<input type="checkbox" name="unUsedCheck">',
            '<%=locale.unused%>',
            '</label>',
            '</div>',
            '</div>',
            '</form>',
            '</div>'
        ].join('')),
        events: {
            "click #detailSearchBtn": "getDetectionPopupData", // 검색 버튼 클릭 // 조회 되고나서 팝업이 닫혀야 하는지 OR 클릭하자 마자 닫혀야 하는지			
        },
        initialize: function (options) {
            this.collection = options.collection;
        },
        render: function () {
            this.$el.html(this.template({
                locale: locale,
                errorLocale: errorLocale
            }));
            return this;
        },
        getDetectionPopupData: function () {
            var self = this;
            var setParams = Backbone.FormSerialize.getData(this.$(this.formEl));
            self.collection.set(setParams);
            self.collection.fetch({
                method: 'POST',
                data: JSON.stringify(setParams),
                url: '/api/securityPolicy/selectDetectionPolicy',
                contentType: 'application/json',
                success: function (collection) {
                    $(".policy-list").empty();
                    self.collection.each(function (model) {
                        $(".policy-list").append('<li><a href="#securityPolicy_intrusionDetectionPolicy" class="overflow-ellipsis" data-lcode="' + model.get('lCode') + '"><span class="threat-color threat-bg-lv' + dataExpression.severityColorData(model.get('sSeverity')) + '"></span>' + model.get('strDescription') + '</a></li>');
                    }, self);
                }
            });
        }
    });
});