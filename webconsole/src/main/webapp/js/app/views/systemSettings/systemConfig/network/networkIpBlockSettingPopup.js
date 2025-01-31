define(function (require) {

    "use strict";

    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error'),
            dataExpression = require('utils/dataExpression'),
            alertMessage = require('utils/AlertMessage');

    return Backbone.View.extend({
        resultIp: false, //ip의 Validation 에대한 true false
        resultMask: false, // mask Validation에 대한 true false
        resultToIp: false,
        resultFromIp: false,
        popupResult: false, // 모든 Validation에 대해 true/false 이며 
        className: 'tab-content',
        template: _.template([
            '<div class="modal-body modal-body-gray-padding">',
            '<form class="form-inline" role="form">',
            '<div class="col-xs-12 padding-clear">',
            '<label class="control-label col-xs-3 padding-clear"><%=locale.inputType%></label>',
            '<select class="form-control col-xs-9" id="ipTypeSelect">',
            '<option value="range"><%=locale.scoping%></option>',
            '<option value="mask"><%=locale.mask%></option>',
            '</select>',
            '</div>',
            '</form>',
            '</div>',
            '<div class="modal-body-white-padding border-solid-top overflow-hidden">',
            '<form class="form-inline" role="form">',
            '<div class="col-xs-12 padding-clear" id="rangeIpType">',
            '<label class="control-label col-xs-3 padding-clear"><%=locale.start%> IP</label>',
            '<input class="form-control col-xs-9" type="text" id="fromIpInput">',
            '<label class="control-label col-xs-3 margin-t10 padding-clear"><%=locale.end%> IP</label>',
            '<input class="form-control margin-t10 col-xs-9" type="text" id="toIpInput">',
            '</div>',
            '<div class="col-xs-12 padding-clear" id="maskType">',
            '<label class="control-label col-xs-3 padding-clear">IP <%=locale.address%></label>',
            '<input type="text" class="form-control col-xs-9" id="ipInput"/>',
            '<label class="control-label col-xs-3 margin-t10 padding-clear">MASK</label>',
            '<input type="text" class="form-control margin-t10 col-xs-4" id="maskInput"/>',
            '</div>',
            '<input type="hidden" class="form-control margin-t10 col-xs-4" id="popupIndex"/>',
            '</form>',
            '</div>',
            '</div>'
        ].join('')),
        events: {
            'change #ipTypeSelect': 'selectedIpType',
            'blur #ipInput': 'validateIp',
            'blur #maskInput': 'validateMask',
            'blur #toIpInput': 'validateToIp',
            'blur #fromIpInput': 'validateFromIp',
        },
        initialize: function (options) {
            options = options || {};
            this.type = options.type;
        },
        render: function () {
            this.$el.append(this.template({locale: locale}));
            $('#maskType', this.el).hide();

            if (this.type == 'insert') {
                $('#resultInput').val('false');
            } else if (this.type == 'update') {
                $('#resultInput').val('true');
                // 선택한 수정하코자하는 정보를 팝업에 나타내 줌
                this.setIpBlockInfo();
            } else {
                $('#resultInput').val('false');
            }
            return this;
        },
        selectedIpType: function () {
            var selectedType = $('#ipTypeSelect', this.el).val();
            if (selectedType == 'range') {
                $('#rangeIpType', this.el).show();
                $('#maskType', this.el).hide();
            } else {
                $('#rangeIpType', this.el).hide();
                $('#maskType', this.el).show();
            }
            return selectedType;
        },
        setIpBlockInfo: function () {
            $('#toIpInput', this.el).val(this.model.get('dwToIp'));
            $('#fromIpInput', this.el).val(this.model.get('dwFromIp'));
            $('#ipInput', this.el).val(this.model.get('dwFromIp'));
            $('#maskInput', this.el).val(this.model.get('mask'));
            $('#popupIndex', this.el).val(this.model.get('ipIndex'));

        },
        valid: function () {
            var selectedType = $('#ipTypeSelect', this.el).val();
            if (selectedType == 'range') {
                if ($('#toIpInput', this.el).val() == '' || $('#fromIpInput', this.el).val() == '') {
                    this.resultToIp = false;
                    this.resultFromIp = false;
                    return false;
                }
                this.resultToIp = this.validateToIp();
                this.resultFromIp = this.validateFromIp();
            } else {
                if ($('#ipInput', this.el).val() == '' || $('#maskInput', this.el).val() == '') {
                    this.resultIp = false;
                    this.resultMask = false;
                    return false;
                }
                this.resultIp = this.validateIp();
                this.resultMask = this.validateMask();
            }
        },
        validateToIp: function () {
            var ipValue = $('#toIpInput', this.el).val();

            if (ipValue == "" || ipValue == null) {
                // ip값없고 mask값도 없을때
                $('#toIpInput', this.el).val('');
                $('#fromIpInput', this.el).val('');
                //this.popupResult = false;
                return  this.resultToIp = Backbone.Utils.Tip.validationTooltip($('#toIpInput', this.el), true);
            }
            this.resultToIp = Backbone.Utils.validation.validateIpDualCheck(ipValue);

            if (this.resultToIp == true) {
                var isV6 = Backbone.Utils.validation.validateIpv6(ipValue);
                // fromIp 보다 작으면 false / 크면 true
                if (this.resultFromIp == true) {
                    var toIpLongValue = Backbone.Utils.Casting.convertIpToLong(ipValue);
                    var fromIpValue = $('#fromIpInput', this.el).val();
                    var fromIpLongValue = Backbone.Utils.Casting.convertIpToLong(fromIpValue);

                    if (toIpLongValue < fromIpLongValue) {
                        return this.resultToIp = Backbone.Utils.Tip.validationTooltip($('#toIpInput', this.el), errorLocale.validation.isStartIpEndIp);
                    }
                    // TODO toIp, fromIp를 이용한 mask값 구하기
                    // 1. ip를 "."으로 split ㅇ ㅋ
                    // 2. 각자리 값을 2진수로 변환 ㅇㅋ
                    // 3. 0빠진 자리 채우기 oㅋ ==> convertIpToBinaryNum 
                    // 4. 맨앞자리부터 비교하기, 비교해서 다른값이 나오기전까지의 count가 mask 값임  
                    var maskValue = dataExpression.bitMask(dataExpression.convertIpToBinaryNum(ipValue), dataExpression.convertIpToBinaryNum(fromIpValue));
                    $('#maskInput', this.el).val(maskValue);
                    return this.resultToIp = Backbone.Utils.Tip.validationTooltip($('#toIpInput', this.el), this.resultToIp);

                } 
            } else {
                alertMessage.infoMessage(this.resultToIp);
                return this.resultToIp = Backbone.Utils.Tip.validationTooltip($('#toIpInput', this.el), this.resultToIp);
            }
        },
        validateFromIp: function () {
            var ipValue = $('#fromIpInput', this.el).val();
            if (ipValue == "" || ipValue == null) {
                // ip값없고 mask값도 없을때
                $('#toIpInput', this.el).val('');
                $('#fromIpInput', this.el).val('');
                this.popupResult = false;
                this.resultFromIp = Backbone.Utils.Tip.validationTooltip($('#ipInput', this.el), true);
                return  this.resultFromIp;
            }
            this.resultFromIp = Backbone.Utils.validation.validateIpDualCheck(ipValue);
            if (this.resultFromIp == true) {
                // fromIp 보다 작으면 false/ 크면 true
                if (this.resultToIp == true) {
                    var fromIpLongValue = Backbone.Utils.Casting.convertIpToLong(ipValue);
                    var toIpValue = $('#toIpInput', this.el).val();
                    var toIpLongValue = Backbone.Utils.Casting.convertIpToLong(toIpValue);

                    if (toIpLongValue < fromIpLongValue) {
                        return this.resultFrom = Backbone.Utils.Tip.validationTooltip($('#fromIpInput', this.el), errorLocale.validation.isStartIpEndIp);
                    }
                    // 마스크값 구하기
                    var maskValue = dataExpression.bitMask(dataExpression.convertIpToBinaryNum(toIpValue), dataExpression.convertIpToBinaryNum(ipValue));
                    $('#maskInput', this.el).val(maskValue);

                    return this.resultFromIp = Backbone.Utils.Tip.validationTooltip($('#fromIpInput', this.el), this.resultFromIp);
                }
            } else {
                alertMessage.infoMessage(this.resultFromIp);
                return this.resultFromIp = Backbone.Utils.Tip.validationTooltip($('#fromIpInput', this.el), this.resultFromIp);
            }
        },
        validateIp: function () {
            // ip 형식 체크 
            var ipValue = $('#ipInput', this.el).val();
            var maskValue = $('#maskInput', this.el).val();

            if (ipValue == "" || ipValue == null) {
                // ip값없고 mask값도 없을때
                $('#toIpInput', this.el).val('');
                $('#fromIpInput', this.el).val('');
                this.popupResult = false;
                return  this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput', this.el), true);
            }

            this.resultIp = Backbone.Utils.validation.validateIpDualCheck(ipValue);

            if (this.resultIp == true) {
                Backbone.Utils.Tip.validationTooltip($('#ipInput', this.el), true);
                // mask에 값이 있을때

//                //mask값에 맞는 ip형식인지 판단
//                this.resultIp = Backbone.Utils.validation.validateIpAndMask(ipValue, maskValue);
//                // 1. 형식이틀리다면
//                if (this.resultIp != true) {
//                    this.popupResult = false;
//                    return this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput', this.el), this.resultIp);
//                }
//                // 2. 형식이 맞다면
//                if ($('#maskInput', this.el).val() != '') {
//                    this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput', this.el), true);
//                    var resultData = Backbone.Utils.Calculation.parseCidrRangeIp(ipValue, maskValue);
//                    $('#fromIpInput', this.el).val(resultData[0]);
//                    $('#toIpInput', this.el).val(resultData[1]);
//                }

                this.popupResult = true;
                return this.resultIp = true;
            } else {
                alertMessage.infoMessage(this.resultIp);
                this.popupResult = false;
                this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput', this.el), this.resultIp);
            }

        },
        validateMask: function () {
            // mask 형식 체크 
            var maskValue = $('#maskInput', this.el).val();
            var ipValue = $('#ipInput', this.el).val();

            if (maskValue == "" || maskValue == null) {
                $('#toIpInput', this.el).val(ipValue);
                $('#fromIpInput', this.el).val(ipValue);
                return this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput', this.el), true);
            }
            var isV6 = Backbone.Utils.validation.validateIpv6(ipValue);
            if (isV6 != true) {
                this.resultMask = Backbone.Utils.validation.validateMask(maskValue);
                if (this.resultMask != true) {
                    alertMessage.infoMessage(this.resultMask, "warn");
                    this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput', this.el), this.resultMask);
                } else {
                    Backbone.Utils.Tip.validationTooltip($('#maskInput', this.el), true);
                }

                if (this.resultMask == true) {

                    if (ipValue != null || ipValue != '') {
                        var resultData = Backbone.Utils.Calculation.parseCidrRangeIp(ipValue, maskValue);
                        this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput', this.el), true);
                        $('#fromIpInput', this.el).val(resultData[0]);
                        $('#toIpInput', this.el).val(resultData[1]);

                        this.popupResult = true;
                        return this.resultMask = true;
                        //                    }
                    }
                } else {
                    this.popupResult = false;
                }
            } else {
                this.resultMask = Backbone.Utils.validation.validateIpv6Mask(maskValue);
                if (this.resultMask != true) {
                    alertMessage.infoMessage(this.resultMask, "warn");
                    this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput', this.el), this.resultMask);
                    return false;
                } else {
                    Backbone.Utils.Tip.validationTooltip($('#maskInput', this.el), true);
                }
                this.getIpv6Mask();
//                this.sleep(1000);
                if (this.resultMask == true && this.popupResult == true) {
                    return true;
                }
            }
        },
        getIpv6Mask: function () {
            var self = this;
            var path = $('#ipInput', this.el).val() + "/" + $('#maskInput', this.el).val();
            Backbone.ajax({
                method: 'POST',
                contentType: 'application/json',
                url: 'api/systemSetting/getIpv6SubnetMask',
                data: path,
                dataType: 'json',
                async: false,
                success: function (data) {
                    self.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput', self.el), true);
                    $('#fromIpInput', this.el).val(data.start);
                    $('#toIpInput', this.el).val(data.end);
                    self.popupResult = true;
                    self.resultMask = true;
                }
            });
        },
        getIpv6RangeMask: function () {
            var self = this;
            var roIp = $('#toIpInput', this.el).val('');
            var fromIp = $('#fromIpInput', this.el).val('');
            Backbone.ajax({
                method: 'POST',
                contentType: 'application/json',
                url: 'api/systemSetting/getIpv6SubnetMask',
                data: path,
                dataType: 'json',
                async: false,
                success: function (data) {
                    self.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput', self.el), true);
                    $('#fromIpInput', this.el).val(data.start);
                    $('#toIpInput', this.el).val(data.end);
                    self.popupResult = true;
                    self.resultMask = true;
                }
            });
        },
        sleep: function(time) {
            var start = $.now();
            var chk = true;
            while (chk) {
                if (($.now() - start) > time) {
                    chk = false;
                }
            }
        },
    });
});