/**
 * malicious Traffic main view 
 * @author leekyunghee
 * @since 2015-08-17
 * @description 유해트래픽 화면 구현 
 */

define(function (require) {

    "use strict";

    var $ = require('jquery'),
            Backbone = require('backbone'),
            jquerySlimScroll = require('jquerySlimScroll'),
            jqueryScrollTabs = require('jqueryScrollTabs'),
            jqueryMouseWheel = require('jqueryMouseWheel'),
            tmsjs = require('tmsjs'),
            serialize = require('serialize'),
            alertMessage = require('utils/AlertMessage');

    var locale = require('i18n!nls/str'),
        errorLocale = require('i18n!nls/error');
    var tpl = require('text!tpl/trafficAnalysis/maliciousTraffic.html'),
            MaliciousTrafficSearchModel = require('models/trafficAnalysis/maliciousTrafficSearchModel'),
            TabView = require('views/trafficAnalysis/maliciousTraffic/maliciousTrafficStat'), // 최종 5분 통계 탭
            RangeTabView = require('views/trafficAnalysis/maliciousTraffic/maliciousTrafficRange'); 			// 구간별 조회 탭 	

    return Backbone.View.extend({
        formEl: "form.maliciousTrafficForm",
        template: _.template(tpl),
        targetView: null,
        periodView: null,
        defaultTabSize: 2,
        initialize: function (options) {
            this.maliciousTrafficSearchModel = new MaliciousTrafficSearchModel();
            _.bindAll(this, "search", "renderTab", "renderDefaultTabs", "makeTemplate", "staticView", "rangeStatView", "dynamicView", "showTab", "hideTab", "renderTabs", "removeTab");
            this.evt = options.evt;

            this.evt.bind("newTab", this.renderTab);
            this.evt.bind("detailTab", this.renderTab);

            this.tabs = [];
        },
        events: {
            "click .searchBtn": "search",
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
            this.$el.html(this.template({locale: locale}));

            // 조회 대상 검색 조건 
            this.targetView = Backbone.TargetView.MakeTarget(this.$('#targetType'));
            // 분석 기간 검색 조건   
            this.periodView = Backbone.PeriodView.makeUI(this.$('#period'), {periodUnit: 5});

            // 최초 페이지 로딩시 그려야할 tab 호출 
            this.renderDefaultTabs();
            // 최초 데이터 조회 
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
            this.initTab();
            var setParams = Backbone.FormSerialize.getData(this.$(this.formEl), this.targetView.currentSelOrgSensor());
            this.maliciousTrafficSearchModel.set(setParams);
            this.evt.trigger('retrieve', this.maliciousTrafficSearchModel);

            return true;
        },
        renderTab: function (model) {
            this.renderTabs(this.dynamicView(model), 'custom', model);
        },
        renderDefaultTabs: function () {
            this.renderTabs(this.staticView(), "static");
            this.renderTabs(this.rangeStatView(), "range");
        },
        makeTemplate: function (title, id, content, useActive) {
            var self = this;
            // 탭 헤더와 컨텐츠를 생성하고 컨텐츠에 뷰를 담는다.
            var tabHeader = '<li class="' + (useActive ? 'active' : '') + '"><a href="#' + id + '" role="tab" data-toggle="tab">' + title + '</a></li>';
            $("#scroll-tab ul", self.el).append(tabHeader);
            var tabContent = $('<div class="tab-pane ' + (useActive ? 'active' : '') + '" id="' + id + '"></div>').append(content);
            $(".tab-content", self.el).append(tabContent);
        },
        staticView: function () {
            var tabView = new TabView({
                targetView: this.targetView,
                periodView: this.periodView,
                evt: this.evt,
                listType: "default"
            });
            tabView.render();
            this.tabs.push(tabView);
            return tabView.el;
        },
        rangeStatView: function () {
            var rangeTabView = new RangeTabView({
                evt: this.evt
            });
            rangeTabView.render();
            this.tabs.push(rangeTabView);
            return rangeTabView.el;
        },
        dynamicView: function (model) {
            // 동적탭을 비활성화 한다. 
            this.hideTab();

            // 동적 탭 뷰
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
        removeTab: function (e) {
            var totalTabLength = this.$("#scroll-tab .nav > li").length - 1;					// 전체탭 사이즈
            var showTabIndex = this.$("#scroll-tab .nav > li.active").index();					// 활성탭 인덱스
            var delTabIndex = this.$("#scroll-tab .nav > li").index($(e.target).closest("li"));	// 삭제탭 인덱스

            this.$("#scroll-tab .nav li:eq(" + delTabIndex + "), .tab-content > div:eq(" + delTabIndex + ")").remove();
            // 탭 정보를 담은 배열에서 삭제탭을 제거한다.
            var tab = this.tabs.splice(delTabIndex, 1);
            tab[0].close();

            /**
             *  활성화 탭을 선택한다.
             *	1. 활성탭과 삭제탭이 동일하며 마지막탭일 경우에는 삭제탭인덱스 - 1 
             *  2. 활성탭과 삭제탭이 동일하면 마지막탭이 아닐 경우에는 삭제탭인덱스 + 1
             *  3. 활성탭이 삭제탭보다 크면 활성탭인덱스 - 1
             *  4. 그 외는 활성탭인덱스   
             */
            var tabIndex = (showTabIndex == delTabIndex && showTabIndex == totalTabLength) ? delTabIndex - 1 : (showTabIndex == delTabIndex && showTabIndex < totalTabLength) ? delTabIndex + 1 : (showTabIndex > delTabIndex) ? showTabIndex - 1 : showTabIndex;
            this.showTab(tabIndex);
        },
        /** 
         * 타입에 따라 정적인 tab 과 dynamic tab으로 그려준다. 
         */
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
        initTab: function () {
            // 활성 탭이 있을 경우 분석시작 버튼을 누르면 탭이 제거된다.
            this.$(".closetab").trigger("click");
            // 활성 탭이 없어지면 5분 통계 탭으로 이동한다.
            this.showTab(0);
            // 분석기간을 이용하여 구간기간을 재설정하기 위해 구간 조회 탭도 이벤트 활성화
            this.tabs[1].customOn();
        },
        // 검색 조건 초기화
        settings: function () {
            Backbone.FormSerialize.setData(this.$(this.formEl));
            this.targetView = Backbone.TargetView.MakeTarget($('#targetType', this.el));
            this.periodView = Backbone.PeriodView.makeUI($('#period', this.el), {
                periodUnit: 5,
                category: "hour"
            });
            $('#sortSelect').val('bps').attr('selected', 'selected');
        }
    });
});