/**
 * 트래픽분석 > service
 * table 내의 값을 append해주는 js
 * 
 */
define(function (require) {
    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            dataExpression = require('utils/dataExpression'),
            locale = require('i18n!nls/str'),
            ProtocolPopupView = require('views/trafficAnalysis/service/protocolPopup'),
            ServiceTrend = require('views/trafficAnalysis/service/serviceTrend'),
            alertMessage = require('utils/AlertMessage');

    // require template
    var tpl = require('text!tpl/trafficAnalysis/serviceItem.html');

    return Backbone.View.extend({
        tagName: 'tr',
        template: _.template(tpl),
        events: {
            'click .bpsPopup': 'bpsPopup',
            'click .ppsPopup': 'ppsPopup',
            'click #graphItemCheck': 'checkGraphItemCount',
            'click .protocolPopup': 'protocolPopup'
        },
        initialize: function (options) {
            this.serviceSearchModel = options.searchModel;

            this.model.set({
                indexType: this.serviceSearchModel.get('indexType'),
                nProtocol: this.model.get("nProtocol"),
                nProtocolName: dataExpression.getProtocolName(this.model.get("nProtocol")),
                bpsView: dataExpression.getFormatTrafficData(this.model.get("bps")),
                ingressBpsView: dataExpression.getFormatTrafficData(this.model.get("ingressBps")),
                egressBpsView: dataExpression.getFormatTrafficData(this.model.get("egressBps")),
                ppsView: dataExpression.getFormatTrafficData(this.model.get("pps")),
                ingressPpsView: dataExpression.getFormatTrafficData(this.model.get("ingressPps")),
                egressPpsView: dataExpression.getFormatTrafficData(this.model.get("egressPps")),
            });

            this.listenTo(this.model, "change", this.render());

            this.parentEl = options.parentEl;
        },
        render: function () {
            this.$el.html(this.template({
                locale: locale,
                model: this.model.toJSON(),
                highLightEl: this.serviceSearchModel.get('sortSelect'),
                isHighLight: Backbone.TemplateHelper.isHighLight
            }));
            $('span', this.el).tooltip();

            return this;
        },
        bpsPopup: function () {
            var searchCondition = {};

            this.model.get('nProtocol');
            _.extend(searchCondition, this.serviceSearchModel.toJSON(), {
                nProtocol: this.model.get('nProtocol'),
                wService: this.model.get('wService'),
            });

            Backbone.ModalView.msg({
                size: 'medium-large',
                title: dataExpression.getProtocolName(this.model.get('nProtocol')) + ' ' + this.model.get('wService') + ' ' + locale.occurrenceTrend, // 프로토콜명 포트값 발생추이
                body: new ServiceTrend({
                    searchType: 'bpsTrafficChart',
                    model: this.model,
                    searchCondition: searchCondition
                })
            });
        },
        ppsPopup: function () {
            var searchCondition = {};

            _.extend(searchCondition, this.serviceSearchModel.toJSON(), {
                nProtocol: this.model.get('nProtocol'),
                wService: this.model.get('wService'),
            });

            Backbone.ModalView.msg({
                size: 'medium-large',
                title: dataExpression.getProtocolName(this.model.get('nProtocol')) + ' ' + this.model.get('wService') + ' ' + locale.occurrenceTrend, // 프로토콜명 포트값 발생추이
                body: new ServiceTrend({
                    searchType: 'ppsTrafficChart',
                    model: this.model,
                    searchCondition: searchCondition
                })
            });
        },
        checkGraphItemCount: function (event) {
            var checkedItem = [];
            $("#graphItemCheck:checked", this.parentEl).each(function (i) {
                checkedItem[i] = ($(this).val());
            });

            if (checkedItem.length > 5) {
                alertMessage.infoMessage(locale.ExceededTheNumberOfItems, 'info', '', 'small');
                $(event.currentTarget).prop('checked', false);
            }
        },
        protocolPopup: function () {

            var searchCondition = {};
            _.extend(searchCondition, this.serviceSearchModel.toJSON(), {
                nProtocol: this.model.get('nProtocol'),
                wService: this.model.get('wService'),
            });
            Backbone.ModalView.msg({
                size: 'medium-large',
                type: 'info',
                title: this.model.get('nProtocolName') + "(" + this.model.get('wService') + ") " + locale.trafficGeneratedIp,
                body: new ProtocolPopupView({
                    model: this.model,
                    searchCondition: searchCondition
                })
            });
        }
    });
});