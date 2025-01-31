/**
 * ip
 */
define(function (require) {
    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            bootstrap = require('bootstrap'),
            jquerySlimScroll = require('jquerySlimScroll'),
            jqueryScrollTabs = require('jqueryScrollTabs'),
            jqueryMouseWheel = require('jqueryMouseWheel'),
            tmsjs = require('tmsjs'),
            serialize = require('serialize'),
            alertMessage = require('utils/AlertMessage');

    // require locale
    var locale = require('i18n!nls/str'),
        errorLocale = require('i18n!nls/error');
    var sessionManager = require('utils/sessionManager');

    // require js
    var IpSearchModel = require('models/trafficAnalysis/ipSearchModel'),
            TabView = require('views/trafficAnalysis/ip/ipTab'),
            RangeTabView = require('views/trafficAnalysis/ip/ipRangeStatTab');

    // require template
    var tpl = require('text!tpl/trafficAnalysis/ip.html'),
            template = _.template(tpl);

    var resultMask = true, resultIp = true;

    return Backbone.View.extend({
        searchFormEl: 'form.ipForm',
        targetView: null,
        periodView: null,
        resultIp: true,
        resultMask: true,
        defaultTabSize: 2,
        initialize: function (options) {
            $('[data-toggle="tooltip"]').tooltip();

            _.bindAll(this, "render", "search", "renderTab", "renderDefaultTabs", "makeTemplate", "staticView", "rangeStatView", "dynamicView", "showTab", "hideTab", "renderTabs", "removeTab", "validationIp", "validationMask");
            this.evt = options.evt;
            this.evt.bind("newTab", this.renderTab);
            this.evt.bind("detailTab", this.renderTab);

            this.ipSearchModel = new IpSearchModel();

            this.tabs = [];
        },
        events: {
            'click #searchBtn': 'search',
            "click .cancelBtn": "settings",
            'blur #ipInput': 'validationIp', // ip validation
            'blur #maskInput': 'validationMask', // mask validation
            "click #scroll-tab ul > li": "showTab",
            'click .closetab': 'removeTab',
            'click #ipType1': 'changeIpv4Type',
            'click #ipType2': 'changeIpType'
        },
        onClose: function () {
            this.evt.unbind("newTab");
            var tab = null;
            // 탭뷰에 대한 이벤트 clear
            while ((tab = this.tabs.pop()) != undefined) {
                tab.close();
            }
        },
        render: function () {

            this.$el.html(template({locale: locale}));

            this.targetView = Backbone.TargetView.MakeTarget(this.$('#targetType'), {viewType: 2, lvsensorIndex: sessionManager.RefIndex});
            this.periodView = Backbone.PeriodView.makeUI(this.$('#period'), {periodUnit: 5});
            // 정적 탭 호출, 탭 컨텐츠에 들어갈 뷰를 담음
            this.renderDefaultTabs();
            $('#scroll-tab', this.el).scrollTabs();

            this.search();

            return this;
        },
        search: function () {
            this.initTab();

            if (this.resultMask == true && this.resultIp == true) {
                var analysisPeriod = this.periodView.getPeriod();
                if (Backbone.Utils.compareMaxDate(analysisPeriod.startDate, analysisPeriod.endDate) == false) {
                    alertMessage.infoMessage(errorLocale.validation.periodDate, 'info', '', 'small');

                    return false;
                }
                if (Backbone.Utils.compareDate(analysisPeriod.startDate, analysisPeriod.endDate) == false) {
                    alertMessage.infoMessage(errorLocale.validation.periodMinDate30, 'info', '', 'small');

                    return false;
                }
                if ($('#maskInput').val() != "") {
                    if ($('#ipInput').val() == "") {
                        this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), errorLocale.validation.ipInputNullValid);
                        return false;
                    }
                }
                $('#ipListTbody', this.el).empty();

                var setParams = Backbone.FormSerialize.getData(this.$(this.searchFormEl), this.targetView.currentSelOrgSensor());
                this.ipSearchModel.set(setParams);
                this.evt.trigger('retrieve', this.ipSearchModel);
                return true;
            } else {
                return false;
            }
        },
        settings: function () {
            this.targetView = Backbone.TargetView.MakeTarget($('#targetType', this.el));
            Backbone.FormSerialize.setData(this.$(this.searchFormEl));
            this.periodView = Backbone.PeriodView.makeUI($('#period', this.el), {
                periodUnit: 5,
                category: "hour"
            });

            // hidden ip 초기화 
            $("#fromIpInput", this.el).val("");
            $("#toIpInput", this.el).val("");

            // input 유효성(빨간테두리, 툴팁) 초기화
            this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), true);
            this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput'), true);
        },
        renderTab: function (model) {
            this.renderTabs(this.dynamicView(model), 'custom', model);
        },
        renderDefaultTabs: function () {
            this.renderTabs(this.staticView(), 'static');
            this.renderTabs(this.rangeStatView(), 'range');
        },
        makeTemplate: function (title, id, content, useActive) {
            var self = this;
            var tabHeader = '<li class="' + (useActive ? 'active' : '') + '"><a href="#' + id + '" role="tab" data-toggle="tab">' + title + '</a></li>';
            $("#scroll-tab ul", self.el).append(tabHeader);
            var tabContent = $('<div class="tab-pane ' + (useActive ? 'active' : '') + '" id="' + id + '"></div>').append(content);
            $(".tab-content", self.el).append(tabContent);
        },
        staticView: function () {
            // 5분 통계 뷰
            var tabView = new TabView({
                evt: this.evt,
                listType: "default"
            });
            tabView.render();
            this.tabs.push(tabView);
            return tabView.el;
        },
        rangeStatView: function () {
            // 구간별 조회 뷰
            var rangeTabView = new RangeTabView({evt: this.evt});
            rangeTabView.render();
            this.tabs.push(rangeTabView);
            return rangeTabView.el;
        },
        dynamicView: function (model) {
            // 동적탭을 비활성화 한다. 
            this.hideTab();

            // 동적인 탭을 그린다.
            var tabView = new TabView({
                evt: this.evt,
                listType: "tab"
            });
            tabView.render();
            this.tabs.push(tabView);

            // 검색조건으로 조회
            this.evt.trigger('retrieve', model);

            return tabView.el;
        },
        showTab: function (e) {
            var tabIndex = (typeof e === "number" ? e : this.$("#scroll-tab .nav li").index($(e.target).closest("li")));

            if (tabIndex < 0)
                return;
            this.hideTab();
            this.$("#scroll-tab .nav li:eq(" + (tabIndex) + ")").addClass('active');
            this.$(".tab-content > div:eq(" + (tabIndex) + ")").addClass('active');

            // 활성탭에 이벤트를 bind 한다. (window.tmsEvents)
            if (this.tabs[tabIndex] && typeof this.tabs[tabIndex].on === "function") {
                this.tabs[tabIndex].customOn();
            }
        },
        hideTab: function () {
            this.$("#scroll-tab .nav li, .tab-content div").removeClass('active');
            // 모든 탭에 이벤트를 unbind 한다. (window.tmsEvents)
            _.each(this.tabs, function (v) {
                if (v && typeof v.off === "function") {
                    v.customOff();
                }
                ;
            });
        },
        renderTabs: function (view, type, model) {
            if (type == "static") {
                this.makeTemplate(locale.stateSearch, 'tabStatic', view, true);
            }
            else if (type == "range") {
                this.makeTemplate(locale.rangeSearch, 'tabRange', view, false);
            } else {
                var tabSize = $("#scroll-tab .nav li").length - this.defaultTabSize;
                this.$("#scroll-tab ul").append('<li class="active"><span><a href="#tab' + tabSize + '" role="tab" data-toggle="tab">' + model.get('startDateInput') + ' ~ ' + model.get('endDateInput') + '<button type="button" class="icon-tab-btn closetab"></button></a></span></li>');
                var tabContent = $('<div class="tab-pane active" id="tab' + tabSize + '" ></div>').append(view);
                this.$(".tab-content").append(tabContent);
            }
        },
        removeTab: function (e) {
            var totalTabLength = this.$("#scroll-tab .nav > li").length - 1;					// 전체탭 사이즈
            var showTabIndex = this.$("#scroll-tab .nav > li.active").index();					// 활성탭 인덱스
            var delTabIndex = this.$("#scroll-tab .nav > li").index($(e.target).closest("li"));	// 삭제탭 인덱스

            this.$("#scroll-tab .nav li:eq(" + delTabIndex + "), .tab-content > div:eq(" + delTabIndex + ")").remove();
            // 탭 정보를 담은 배열에서 삭제탭을 제거한다.
            var tab = this.tabs.splice(delTabIndex, 1);
            tab[0].close();
            var tabIndex = (showTabIndex == delTabIndex && showTabIndex == totalTabLength) ? delTabIndex - 1
                    : (showTabIndex == delTabIndex && showTabIndex < totalTabLength) ? delTabIndex + 1
                    : (showTabIndex > delTabIndex) ? showTabIndex - 1 : showTabIndex;
            this.showTab(tabIndex);
        },
        initTab: function () {
            // 활성 탭이 있을 경우 분석시작 버튼을 누르면 탭이 제거된다.
            this.$(".closetab").trigger("click");
            // 활성 탭이 없어지면 5분 통계 탭으로 이동한다.
            this.showTab(0);
            // 분석기간을 이용하여 구간기간을 재설정하기 위해 구간 조회 탭도 이벤트 활성화
            this.tabs[1].customOn();
        },
        validationIp: function () {
            // ip 형식 체크 
            var ipValue = $('#ipInput').val();
            var maskValue = $('#maskInput').val();

            this.resultIp = Backbone.Utils.validation.validateIpDualCheck(ipValue);
            this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), this.resultIp);

            if (this.resultIp == true) {
                if (maskValue != '' && this.resultMask) {

                    if (this.resultIp != true) {
                        this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), this.resultIp);
                        return this.resultIp;
                    }
                    this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput'), true);
                    var resultData = Backbone.Utils.Calculation.parseCidrRangeIp(ipValue, maskValue);
                    $('#fromIpInput').val(resultData[0]);
                    $('#toIpInput').val(resultData[1]);
                    return this.resultIp;

                } else {
                    $('#toIpInput').val(ipValue);
                    $('#fromIpInput').val(ipValue);
                    return this.resultIp = true;
                }
            } else {
                if (ipValue == '' && maskValue == '') {
                    this.resultIp = true;
                    this.resultMask = true;
                    Backbone.Utils.Tip.validationTooltip($('#maskInput'), true);
                    Backbone.Utils.Tip.validationTooltip($('#ipInput'), true);
                    $('#toIpInput').val('');
                    $('#fromIpInput').val('');
                }
            }
            if (this.resultIp == false) {
                alertMessage.infoMessage(errorLocale.validation.invaildIp + errorLocale.validation.reInput, 'info', '', 'small');
            }
        },
        validationMask: function () {
            var maskValue = $('#maskInput').val();
            var ipValue = $('#ipInput').val();
            this.resultMask = Backbone.Utils.validation.validateMask(maskValue);
            this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput'), this.resultMask);

            if (this.resultMask == true) {

                if (ipValue != '' && this.resultIp) {
                    if (this.resultMask != true) {
                        this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput'), this.resultMask);
                        return this.resultMask;
                    } else {
                        var resultData = Backbone.Utils.Calculation.parseCidrRangeIp(ipValue, maskValue);
                        this.resultIp = Backbone.Utils.Tip.validationTooltip($('#ipInput'), true);
                        $('#fromIpInput').val(resultData[0]);
                        $('#toIpInput').val(resultData[1]);
                        return this.resultMask = true;
                    }
                }
            } else {
                if (ipValue == '' && maskValue == '') {
                    this.resultIp = true;
                    this.resultMask = true;
                    Backbone.Utils.Tip.validationTooltip($('#maskInput'), true);
                    Backbone.Utils.Tip.validationTooltip($('#ipInput'), true);
                    $('#toIpInput').val('');
                    $('#fromIpInput').val('');
                }
                if (ipValue != '' && this.resultIp) {
                    if (maskValue == '') {
                        this.resultMask = Backbone.Utils.Tip.validationTooltip($('#maskInput'), true);
                        return this.resultMask;
                    }
                }
            }
            if (this.resultMask == false) {
                //mask의 입력 범위는 8~32입니다.
                alertMessage.infoMessage(errorLocale.validation.maskRangeValid + errorLocale.validation.reInput, 'info', '', 'small');
            }
        },
        changeIpv4Type: function () {
            var ipType = $("#ipType1").val();
            var changeIp = ipType == 4 ? false : true;

            $("#maskInput").attr("disabled", changeIp);
        },
        changeIpType: function () {
            var ipType = $("#ipType2").val();
            var changeIp = ipType == 6 ? true : false;

            $("#maskInput").attr("disabled", changeIp);
        }
    });
});