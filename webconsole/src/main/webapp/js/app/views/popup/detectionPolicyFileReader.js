/*
 * 
 */
define(function (require) {

    "use strict";

    var $ 				= require('jquery'),
        Backbone 		= require('backbone'),
        locale 			= require('i18n!nls/str'),
        dataExpression 	= require('utils/dataExpression'),
        alertMessage 	= require('utils/AlertMessage');

    // require template
    var Tpl = require('text!tpl/popup/detectionPolicyFileReadPopup.html');

    return Backbone.View.extend({
        className: 'tab-pane padding-r15 padding-l15',
        template: _.template(Tpl),
        events: {
        },
        render: function () {
            this.$el.html(this.template({
                locale: locale,
            }));
            return this;
        },
    });
});