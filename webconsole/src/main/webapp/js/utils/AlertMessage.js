define(function (require) {
    var $ = require('jquery'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str');

    return{
        infoMessage: function (msg, title, type, size) {
            //메지지 공통으로 변경
            if (title == null || title == '') {
                title = 'Message';
            }
            if (type == null || type == '') {
                type = 'info';
            }
            if (size == null || size == '') {
                size = 'small';
            }
            var ModalContent = Backbone.View.extend({
                render: function () {
                    this.$el.html('<p class="modal-body-white-padding">' + msg + '</p>');
                    return this;
                }
            });
            Backbone.ModalView.msg({
                type: type,
                size: size,
                title: title,
                body: new ModalContent()
            });
            this.valid = false;
        },
        confirmMessage: function (callback, msg, title, type, size) {
            //메지지 공통으로 변경
            if (title == null || title == '') {
                title = 'Message';
            }
            if (type == null || type == '') {
                type = 'info';
            }
            if (size == null || size == '') {
                size = 'small';
            }
            var ModalContent = Backbone.View.extend({
                    render: function() {
                            this.$el.html('<p class="modal-body-white-padding">' + msg + '</p>');
                            return this;
                    }
            });
            Backbone.ModalView.msgWithOkCancelBtn({
                    size: 'small',
                    type: 'info',
                    title: 'Message',
                    body: new ModalContent(),
                    okButtonCallback: callback
            });
            this.valid = false;
        }
    };
});