/**
 * 트래픽 분석 > 서비스 > 팝업  
 * 
 */
define(function (require) {
    "use strict";

    // require library
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            Pagination = require('views/common/basicPagination'),
            dataExpression = require('utils/dataExpression'),
            Collection = require('collections/trafficAnalysis/serviceCollection'),
            TbIpRetrieve = require('views/tools/ipRetrievePopup');

    var Tpl = require('text!tpl/trafficAnalysis/protocolPopup.html'),
            ItemTpl = require('text!tpl/trafficAnalysis/protocolPopupItem.html');

    var ProtocolPopupItem = Backbone.View.extend({
        tagName: 'tr',
        template: _.template(ItemTpl),
        initialize: function (options) {
            this.model = options.model;
        },
        events: {
            'click .ipRetrievePopupLink': 'showIpRetrievePopup'
        },
        render: function () {
            this.$el.html(this.template({
                model: this.model.toJSON()
            }));

            return this;
        },
        showIpRetrievePopup: function () {
            var ip = $('.ipRetrievePopupLink', this.el).text();
            Backbone.ModalView.msg({
                size: 'medium-large',
                title: 'IP ' + locale.retrieve,
                body: new TbIpRetrieve({
                    ip: ip
                })
            });
        }
    });

    return Backbone.View.extend({
        className: 'tab-content',
        template: _.template(Tpl),
        initialize: function (options) {
            options = options || {};
            this.searchCondition = options.searchCondition;
            this.collection = new Collection();

            this.pagination = new Pagination({
                evt: this
            });
            this.bind('pagination', this.search, this);
            this.children = [];
            this.listenTo(this.collection, 'add', this.addOne);
        },
        events: {
            'click .ipSearchBtn': 'search'
        },
        render: function () {
            this.$el.html(this.template({
                locale: locale,
                model: this.searchCondition,
                pathName: decodeURIComponent(this.searchCondition.pathName)
            }));
            this.$tbody = $('.table tbody', this.el);
            $('.more-option', this.el).append(this.pagination.render().el);

            this.search();
            return this;
        },
        search: function (paging) {
            var self = this;

            var ipSelectboxValue = $('.ipSelectbox', self.el).val();

            // 최초 팝업 로드시
            if (paging == undefined) {
                self.paging = paging;
                self.paging = self.pagination.getPaginationRowValue();
                _.extend(self.searchCondition, self.paging, {
                    'ipSelectboxValue': ipSelectboxValue
                });
            } else if (paging.type == 'click') {
                _.extend(self.searchCondition, {
                    'ipSelectboxValue': ipSelectboxValue
                });
            } else {
                _.extend(self.searchCondition, paging, {
                    'ipSelectboxValue': ipSelectboxValue
                });
            }
            this.$tbody.empty();

            this.collection.fetch({
                method: 'POST',
                url: 'api/trafficAnalysis/selectProtocolIpTrafficList',
                contentType: 'application/json',
                acync: false,
                data: JSON.stringify(this.searchCondition),
                success: function (collection) {
                    if (self.collection.length == 0) {
                        $(".table", self.el).addClass("nodata");
                        self.pagination.setTotalRowSize(0);
                        $('#totalCount', self.el).text(0);
                    } else {
                        $(".table", self.el).removeClass("nodata");
                        self.pagination.setTotalRowSize(collection.at(0).get('totalRowSize'));
                        $('#totalCount', self.el).text(collection.at(0).get('totalRowSize'));
                    }
                },
                beforeSend: function () {
                    Backbone.Loading.setModalLoading($('body'));
                },
                complete: function () {
                    Backbone.Loading.removeModalLoading($('body'));
                }
            });
        },
        addOne: function (model) {

            model.set({
                bps: dataExpression.getFormatTrafficData(model.get('bps')),
                pps: dataExpression.getFormatTrafficData(model.get('pps'))
            });

            var protocolPopupItem = new ProtocolPopupItem({
                model: model
            });
            this.$tbody.append(protocolPopupItem.el);
            protocolPopupItem.render();
            this.children.push(protocolPopupItem);
        },
        onClose: function () {
            Backbone.Loading.removeLoading($('.progressbar'));
            if (this.children.length > 0) {
                _.each(this.children, function (child) {
                    child.close();
                });
            }
            this.pagination.close();
        },
    });
});