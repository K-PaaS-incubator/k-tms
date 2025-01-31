define(function (require) {

    var $ = require('jquery'),
            spin = require('spin');

    return {
        setLoading: function (target) {
            target.append('<div class="loading-backdrop in" id="loading"></div>');
            target.addClass('loading');
            target.spin('large');
        },
        setModalLoading: function (target) {
            target.append('<div id="loading"></div>');
            target.addClass('loading');
            target.spin('large');
        },
        removeLoading: function (target) {
            $("#loading").remove();
            target.removeClass('loading');
            $('.spinner').remove();
        },
        removeModalLoading: function (target) {
            $("#loading").remove();
            target.removeClass('loading');
            $('.loading-backdrop').remove();
            $('.spinner').remove();
        },
        setModalBackup: function (target) {
            target.append('<div id="modal_loading_custom"></div>');
        },
        removeModalBackup: function (target) {
            $("#modal_loading_custom").remove();
        },
    };
});
