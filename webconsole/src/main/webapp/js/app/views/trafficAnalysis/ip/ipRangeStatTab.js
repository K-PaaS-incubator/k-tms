/**
 * ip 구간조회 Tab View
 */
define(function (require) {
    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            analysisDate = require('utils/analysisDate');

    // require locale
    var locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error'),
            alertMessage = require('utils/AlertMessage');

    // require template
    var tpl = require('text!tpl/common/rangeTab.html');
    var IpSearchModel = require('models/trafficAnalysis/ipSearchModel');

    return Backbone.View.extend({
        sections: 6,
        template: _.template(tpl),
        initialize: function (options) {
            this.evt = options.evt;
            _.bindAll(this, "retrieve", "customOn", "customOff", "renderTab");

            this.customOn();
        },
        onClose: function () {
            this.customOff();
        },
        events: {
            "click .section1Btn": "renderTab",
            "click .section2Btn": "renderTab",
            "click .section3Btn": "renderTab",
            "click .section4Btn": "renderTab",
            "click .section5Btn": "renderTab",
            "click .section6Btn": "renderTab",
            "click .rangeBtn": "renderTabDetail"
        },
        getSectionData: function () {
            var startDateString = $('.startDateInput').val();
            var endDateString = $('.endDateInput').val();
            var sections = analysisDate.divideTimeToArray(startDateString, endDateString);

            for (var i = 0; i < sections.length; i++) {
                var index = (i + 1);
                sections[i]["cls"] = "section" + index + "Btn";
                sections[i]["title"] = locale.last + " " + index + locale.range;
                sections[i]["rangeDate"] = sections[i]["tmStart"] + " ~ " + sections[i]["tmEnd"];
            }
            return sections;
        },
        render: function () {
            this.$el.html(this.template({
                data: this.getSectionData(),
                startDate: $('.startDateInput').val(),
                endDate: $('.endDateInput').val(),
                locale: locale
            }));
            Backbone.Calendar.setDateTimepicker($('.startDateInput', this.el));
            Backbone.Calendar.setDateTimepicker($('.endDateInput', this.el));

            return this;
        },
        customOn: function () {
            this.evt.bind("retrieve", this.retrieve);
        },
        customOff: function () {
            this.evt.unbind("retrieve");
        },
        retrieve: function (model) {
            this.model = model;
            this.render();
        },
        renderTab: function (e) {
            // 구간탭의 버튼 이벤트에 따라 기간 조회를 한다.
            var range = $(e.target).closest("button").data("range");
            var rangeDate = range.split("~");
            // 구간별 기간을 셋팅할 모델 생성
            var model = new IpSearchModel(this.model.toJSON());
            model.set({"startDateInput": rangeDate[0].trim(), "endDateInput": rangeDate[1].trim()});
            // 모델을 이용하여 새탭을 출력하는 이벤트 호출
            this.evt.trigger('newTab', model);
        },
        renderTabDetail: function () {
            // 상세 구간 검색에 필요한 모델에 값을 셋팅한다.  
            var model = new IpSearchModel(this.model.toJSON());
            model.set({
                "startDateInput": this.$('.startDateInput').val(),
                "endDateInput": this.$('.endDateInput').val()
            });
            // 분석기간 선택된 값이 시작시간 보다 종료시간이 이후 시간일 경우 경고 메세지를 팝업한다. 
            if (Backbone.Utils.compareMaxDate(model.get('startDateInput'), model.get('endDateInput')) == false) {
                return alertMessage.infoMessage(errorLocale.validation.startEndTimeValid, 'info', '', 'small');
            }
            if (Backbone.Utils.compareDate(model.get('startDateInput'), model.get('endDateInput')) == false) {
                return alertMessage.infoMessage(errorLocale.validation.grapeMinValid, 'info', '', 'small');
            }
            // 전달받은 이벤트 객체를 이용하여 모델을 전달해야 결과가 출력된다.
            this.evt.trigger('detailTab', model);
        }
    });

});