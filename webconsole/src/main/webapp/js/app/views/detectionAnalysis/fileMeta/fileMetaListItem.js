/**
 * @author lee kyunghee
 * @since 2015-12-24
 * @description FileMeta 리스트 items 
 */
define(function (require) {

    "use strict";

    var $ = require('jquery'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            dataExpression = require('utils/dataExpression'),
            tpl = require('text!tpl/detectionAnalysis/fileMetaListItem.html');

    var FileMetaHelpPopupView = require('views/popup/fileMetaHelpPopup'),
            TbIpRetrieve = require('views/tools/ipRetrievePopup');

    return Backbone.View.extend({
        tagName: 'tr',
        template: _.template(tpl),
        initialize: function (options) {
            this.searchCondition = options.searchCondition;
            this.listenTo(this.model, "change", this.render());
        },
        events: {
            'click .fileMetaName': 'fileMetaHelpPopup',
            'click .ipRetrieveAttackPopupLink': 'showAttackIpRetrievePopup',
            'click .ipRetrieveVictimPopupLink': 'showVictimIpRetrievePopup'
        },
        render: function () {

            var vsensorName = this.model.get('vsensorName') ? this.model.get('vsensorName') : '-';
            var sensorName = this.model.get('sensorName') ? this.model.get('sensorName') : '-';

            this.$el.html(this.template({
                rNum: this.model.get('rNum'),
                strMagic: this.model.get('strMagic'),
                tmLogTime: this.model.get('tmLogTime'),
                dwSourceIp: this.model.get('bIpType') == 4 ? this.model.get('dwSourceIp') : this.model.get('strSourceIp'),
                nSourcePort: this.model.get('nSourcePort'),
                nProtocol: dataExpression.getProtocolName(this.model.get('nProtocol')),
                deDestinationIp: this.model.get('bIpType') == 4 ? this.model.get('deDestinationIp') : this.model.get('strDestinationIp'),
                nDestinationPort: this.model.get('nDestinationPort'),
                vsensorName: vsensorName,
                sensorName: sensorName,
                lsensorIndex: this.model.get('lsensorIndex'),
                strFileName: this.model.get('strFileName'),
                //dwFileSize: dataExpression.getByteUnit(this.model.get('dwFileSize')),
                dwFileSize: dataExpression.getFormatByteData(this.model.get('dwFileSize')),
                strStoreFileName: this.model.get('strStoreFileName'),
                lCode: this.model.get('lCode'),
                nGrpIndex: this.model.get('nGrpIndex'),
                bSeverity: this.model.get('bSeverity'),
                strRuleName: this.model.get('strRuleName'),
                strGrpName: this.model.get('strGrpName')
            }));

            return this;
        },
        fileMetaHelpPopup: function () {

            _.extend(this.searchCondition, {
                'lIndex': this.model.get('lIndex')
            });

            Backbone.ModalView.msg({
                size: 'medium-large',
                type: 'info',
                title: locale.fileMetaName + ' ' + locale.detailInfo, // FileMeta 등록정보
                body: new FileMetaHelpPopupView({
                    menuType: "FileMetaDetection",
                    model: this.model,
                    searchCondition: this.searchCondition
                })
            });
        },
        showAttackIpRetrievePopup: function () {
            var ip = $('.ipRetrieveAttackPopupLink', this.el).text();
            Backbone.ModalView.msg({
                size: 'medium-large',
                title: 'IP ' + locale.retrieve,
                body: new TbIpRetrieve({
                    ip: ip
                })
            });
        },
        showVictimIpRetrievePopup: function () {
            var ip = $('.ipRetrieveVictimPopupLink', this.el).text();
            Backbone.ModalView.msg({
                size: 'medium-large',
                title: 'IP ' + locale.retrieve,
                body: new TbIpRetrieve({
                    ip: ip
                })
            });
        }
    });
});