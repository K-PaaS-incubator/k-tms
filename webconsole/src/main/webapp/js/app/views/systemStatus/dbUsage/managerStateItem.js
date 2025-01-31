/** 
 * 매니저 상태 리스트 아이템 
 */
define(function (require) {

    var $ = require('jquery'),
            Backbone = require('backbone'),
            analysisDate = require('utils/analysisDate'),
            dataExpression = require('utils/dataExpression'),
            locale = require('i18n!nls/str');

    var ManagerStateTrendView = require('views/systemStatus/dbUsage/managerStateTrend');

    return Backbone.View.extend({
        template: _.template([
            '<td class="align-right"><%= dwCpuSpeed %> (GHz)</td>',
            '<td class="align-right"><%= dwCpuNum %> (' + locale.unitCount + ')</td>',
            '<td class="align-right"><%= dblcurCpuUsage %> (%)</td>',
            '<td class="align-right"><%= dblcurMemUsed %> (%)</td>',
            '<td class="align-right"><%= dwHddUsed %> (%)</td>',
            '<td class="align-right"><%= dwProcessNum %> (' + locale.unitCount + ')</td>',
            '<td class="align-right"><%= dbUsed %> (%)</td>'
            //'<td class="align-center"><button class="btn-intable-chart graphPopup" type="button"></button></td>'
        ].join('')),
        tagName: 'tr',
        initialize: function () {
            this.model.set({
                dwCpuSpeed: (dataExpression.getMhzUnit(this.model.get('dwCpuSpeed'))),
                dblcurMemUsed: (dataExpression.getFormatPercent(this.model.get("dblcurMemUsed"), this.model.get("dwMemTotal"))),
                dwHddUsed: (dataExpression.getFormatPercent(this.model.get("dwHddUsed"), this.model.get("dwHddTotal")))
            });
            this.listenTo(this.model, "change", this.render());
        },
        events: {
            'click .graphPopup': 'graphPopup'
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },
        graphPopup: function () {
            var tempStartDate;
            var str = this.model.get('tmoccur');
            this.endDate = str.substring(0, 16);

            if (this.endDate == "") {
                var tempDate = analysisDate.getCurrentTime();
                this.endDate = tempDate['year'] + "-" + tempDate['month'] + "-" + tempDate['day'] + " " + tempDate['hour'] + ":" + tempDate['minute'];
            }

            tempStartDate = new Date(analysisDate.convertStrToDate(this.endDate).getTime() - (12 * 60 * 60 * 1000));

            this.startDate = tempStartDate.getFullYear() +
                    "-" + analysisDate.leadingZero((tempStartDate.getMonth() + 1), 2) +
                    "-" + analysisDate.leadingZero(tempStartDate.getDate(), 2) +
                    " " + analysisDate.leadingZero(tempStartDate.getHours(), 2) +
                    ":" + analysisDate.leadingZero(tempStartDate.getMinutes(), 2);

            var searchCondition = {};
            _.extend(searchCondition, {
                simpleTimeSelect: "12h",
                startDateInput: this.startDate,
                endDateInput: this.endDate
            });

            Backbone.ModalView.msg({
                size: 'medium-large',
                title: locale.managerStatusTrend,
                body: new ManagerStateTrendView({
                    type: 'search',
                    model: this.model,
                    searchCondition: searchCondition
                })
            });
        }
    });
});