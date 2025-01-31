/*
 * 제품 정보
 */

define(function (require) {

    "use strict";

    var $ = require('jquery'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            tpl = require('text!tpl/preferences/systemInformation.html');

    return Backbone.View.extend({
        template: _.template(tpl),
        initialize: function () {
            this.modelName = "";
            this.version = "";
            this.bildDate = "";
            this.productModel = "";
        },
        render: function () {
            this.getModel();
            var self = this;
            this.$el.html(this.template({
                locale: locale,
                modelName : self.modelName,
                version : self.version,
                bildDate : self.bildDate,
                productModel : self.productModel,
            }));
            return this;
        },
        getModel: function () {
            var self = this;
            Backbone.ajax({
                method: 'POST',
                contentType: 'application/json',
                url: 'api/sensorModel',
                async: false,
                success: function (data) {
                    self.modelName = data.sensorModel;
                    self.version = data.version;
                    self.bildDate = data.bildDate;
                    self.productModel = data.productModel;
                }
            });
        }
    });
});