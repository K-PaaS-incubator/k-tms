/**
 * 
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
            serialize = require('serialize');

    // require locale
    var locale = require('i18n!nls/str'),
        errorLocale = require('i18n!nls/error'),
            alertMessage = require('utils/AlertMessage');

    // require js
    var ProtocolSearchModel = require('models/trafficAnalysis/protocolSearchModel'),
            TabView = require('views/trafficAnalysis/protocol/protocolTab'),
            RangeTabView = require('views/trafficAnalysis/protocol/protocolRangeStatTab');

    // require template
    var tpl = require('text!tpl/trafficAnalysis/protocol.html');

    return Backbone.View.extend({
        searchFormEl: 'form.protocolForm',
        detailSearchDiv: '#detail-search',
        template: _.template(tpl),
        targetView: null,
        periodView: null,
        defaultTabSize: 2,
        initialize: function (options) {

            _.bindAll(this, "search", "renderTab", "renderDefaultTabs", "makeTemplate", "staticView", "rangeStatView", "dynamicView", "showTab", "hideTab", "renderTabs", "removeTab", "getProtocol");
            this.evt = options.evt;

            this.evt.bind("newTab", this.renderTab);
            this.evt.bind("detailTab", this.renderTab);

            this.protocolSearchModel = new ProtocolSearchModel();
            this.tabs = [];
            $('[data-toggle="tooltip"]').tooltip();
        },
        events: {
            'click #searchBtn': 'search',
            "click #scroll-tab ul > li": "showTab",
            "click .closetab": "removeTab",
            "click .cancelBtn": "settings"
        },
        onClose: function () {
            this.evt.unbind("newTab");
            this.evt.unbind("detailTab");
            var tab = null;
            // 탭뷰에 대한 이벤트 clear
            while ((tab = this.tabs.pop()) != undefined) {
                tab.close();
            }
        },
        render: function () {

            this.$el.html(this.template({
                locale: locale
            }));
            this.targetView = Backbone.TargetView.MakeTarget(this.$('#targetType'));
            this.periodView = Backbone.PeriodView.makeUI(this.$('#period'), {periodUnit: 5});

            this.renderDefaultTabs();
            $('#scroll-tab', this.el).scrollTabs();

            this.search();

            return this;
        },
        search: function () {
            var analysisPeriod = this.periodView.getPeriod();
            if (Backbone.Utils.compareMaxDate(analysisPeriod.startDate, analysisPeriod.endDate) == false) {
                alertMessage.infoMessage(errorLocale.validation.periodDate, 'info', '', 'small');

                return false;
            }
            if (Backbone.Utils.compareDate(analysisPeriod.startDate, analysisPeriod.endDate) == false) {
                alertMessage.infoMessage(errorLocale.validation.periodMinDate30, 'info', '', 'small');

                return false;
            }
            // 탭 초기화
            this.initTab();

            var setParams = Backbone.FormSerialize.getData(this.$(this.searchFormEl), this.targetView.currentSelOrgSensor());
            var protocolType = this.getProtocol();
            var searchParams = $.extend(protocolType, setParams);

            this.protocolSearchModel.set(searchParams);
            this.evt.trigger('retrieve', this.protocolSearchModel);

            return true;
        },
        // 검색 조건 초기화
        settings: function () {
            Backbone.FormSerialize.setData(this.$(this.searchFormEl));

            this.targetView = Backbone.TargetView.MakeTarget($('#targetType', this.el));
            this.periodView = Backbone.PeriodView.makeUI($('#period', this.el), {
                periodUnit: 5,
                category: "hour"
            });
            $('#protocolSelect').val('total').attr('selected', 'selected');
            $('#winBoundSelect').val('0').attr('selected', 'selected');
            $('#sortSelect').val('bps').attr('selected', 'selected');
        },
        renderTab: function (model) {
            this.renderTabs(this.dynamicView(model), "custom", model);
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
            } else if (type == "range") {
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
        getProtocol: function () {

            var selProtocol = $('select#protocolSelect', this.el).val();
            var ucType, nProtocol;

            switch (selProtocol) {
                case 'total' :
                    ucType = 10;
                    nProtocol = 0;
                    break;
                case 'arp' :
                    ucType = 11;
                    nProtocol = 2;
                    break;
                case 'rarp' :
                    ucType = 11;
                    nProtocol = 3;
                    break;
                case 'llc' :
                    ucType = 11;
                    nProtocol = 4;
                    break;
                case 'icmp' :
                    ucType = 21;
                    nProtocol = 1;
                    break;
                case 'tcp' :
                    ucType = 21;
                    nProtocol = 6;
                    break;
                case 'udp' :
                    ucType = 21;
                    nProtocol = 17;
                    break;
                case 'Others' :
                    ucType = 11;
                    nProtocol = 255;
                    break;
                    //case 'icmpv6' : break;
                default :
                	ucType = 11;
                	nProtocol = 255;
            }

            return {'ucType': ucType, 'nProtocol': nProtocol};
        }

    });
});