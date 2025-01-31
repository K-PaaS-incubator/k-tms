/**
 * @author leekyunghee update 18.01.25
 * @title 시스템관리_네트워크_등록정보  
 * @description 네트워크 생성을 위해 등록정보를 설정한다. 
 */
define(function (require) {

    "use strict";

    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            locale = require('i18n!nls/str'),
            errorLocale = require('i18n!nls/error'),
            tpl = require('text!tpl/systemSettings/network.html'),
            dataExpression = require('utils/dataExpression'),
            alertMessage = require('utils/AlertMessage');

    var NetworkModel = require('models/systemSettings/networkModel'),
            NetworkCollection = require('collections/systemSettings/networkCollection'),
            NetworkIpBlockItem = require('views/systemSettings/systemConfig/network/networkIpBlockItem'),
            IpBlockSettingPopup = require('views/systemSettings/systemConfig/network/networkIpBlockSettingPopup');

    return Backbone.View.extend({
        editable: false,
        type: '',
        tagName: 'form',
        className: 'form-inline',
        template: _.template(tpl),
        templateEdit: _.template([
            '<table class="view-table col-xs-12 margin-t5" id="editNetwork">',
            '<tr><th></th><td class="pull-right">* <%= locale.required %></td></tr>',
            '<tr>',
            '<th width="15%"><%= locale.name %> *</th>',
            '<td><input type="text" class="form-control col-xs-12 strName" id="strName" value="<%= networkModel.strName %>" maxlength="32"/></td>',
            '</tr>',
            '<tr>',
            '<th><%= locale.comment %></th>',
            '<td><textarea class="form-control col-xs-12 strDescription" rows="7" id="strDescription"></textarea></td>',
            '</tr>',
            '<tr>',
            '<th><%= locale.type %></th>',
            '<td>',
            '<select class="form-control minwidth-200" id="networkType">',
            /*'<option value="0" <%= networkModel.selected0 %> >ALL</option>',*/
            '<option value="1" <%= networkModel.selected1 %> ><%= locale.ipBlock %></option>',
            /*'<option value="2" <%= networkModel.selected2 %> >VLAN</option>',*/
            /*'<option value="3" <%= networkModel.selected3 %> >MPLS</option>',
             '<option value="4" <%=networkModel.selected4 %> >VPN-IPBLOCK</option>',
             '<option value="5" <%=networkModel.selected5 %> >URL</option>',*/
            '</select>',
            /*'<input type="text" class="form-control margin-l10" id="vLanInput" style="display:none;">',*/
            '</td>',
            '</tr>',
            '<tr>',
            '<th><%= locale.ipBlock %> <%= locale.list %> *</th>',
            '<td class="align-right">',
            '<div style="height:150px; overflow-y:scroll; overflow-x:hidden;" class="border-solid-bottom margin-b5">',
            '<table class="fancyTable table-hover table table-striped table-vertical-line" id="header-fix">',
            '<colgroup>',
            '<col width="8%" />',
            '<col width="40%" />',
            '<col width="40%" />',
            '<col width="12%" />',
            '</colgroup>',
            '<thead>',
            '<tr>',
            '<th class="align-center"></th>',
            '<th class="align-center">From IP</th>',
            '<th class="align-center">To IP</th>',
            '<th class="align-center">Mask</th>',
            '</tr>',
            '</thead>',
            '<tbody id="editIpBlockTbody">',
            '</tbody>',
            '</div>',
            '</table>',
            '</div>',
            '<span class="description">(<%= locale.selectedDeleteMod %>)</span>&nbsp;',
            '<button type="button" class="btn btn-default btn-sm networkInsertBtn" id="networkInsertBtn"><%= locale.add %></button>',
            '<button type="button" class="btn btn-default btn-sm networkDeleteBtn margin-l5 margin-r5" id="networkDeleteBtn"><%= locale.remove %></button>',
            '<button type="button" class="btn btn-default btn-sm networkUpdateBtn" id="networkUpdateBtn"><%= locale.modify %></button>',
            '</td>',
            '</tr>',
            '</table>'
        ].join('')),
        templateInsert: _.template([
            '<table class="view-table col-xs-12 margin-t5" id="insertNetwork">',
            '<tr><th></th><td class="pull-right">* <%= locale.required %></td></tr>',
            '<tr>',
            '<th width="15%"><%= locale.name %> *</th>',
            '<td><input type="text" class="form-control col-xs-12 strName" id="strName" maxlength="32"/></td>',
            '</tr>',
            '<tr>',
            '<th><%= locale.comment %></th>',
            '<td><textarea class="form-control col-xs-12 strDescription" rows="7" id="strDescription"></textarea></td>',
            '</tr>',
            '<tr>',
            '<th><%= locale.type %></th>',
            '<td>',
            '<select class="form-control minwidth-200" id="networkType">',
            /*'<option value="0">ALL</option>',*/
            '<option value="1"><%= locale.ipBlock %></option>',
            /*'<option value="2">VLAN</option>',*/
            /* '<option value="3">MPLS</option>',
             '<option value="4">VPN-IPBLOCK</option>',
             '<option value="5">URL</option>',*/
            '</select>',
            '</td>',
            '</tr>',
            '<tr>',
            '<th><%= locale.ipBlock %> <%= locale.list %> *</th>',
            '<td class="align-right">',
            '<div style="height:150px; overflow-y:scroll; overflow-x:hidden;" class="border-solid-bottom margin-b5">',
            '<table class="fancyTable table-hover table table-striped table-vertical-line" id="header-fix">',
            '<thead>',
            '<tr>',
            '<th class="align-center"></th>',
            '<th class="align-center">From IP</th>',
            '<th class="align-center">To IP</th>',
            '<th class="align-center">Mask</th>',
            '</tr>',
            '</thead>',
            '<tbody id="editIpBlockTbody">',
            '</tbody>',
            '</table>',
            '</div>',
            '<span class="description">(<%= locale.selectedDeleteMod %>)</span>',
            '<button type="button" class="btn btn-default btn-sm networkInsertBtn" id="networkInsertBtn"><%= locale.add %></button>',
            '<button type="button" class="btn btn-default btn-sm networkDeleteBtn" id="networkDeleteBtn"><%= locale.remove %></button>',
            '<button type="button" class="btn btn-default btn-sm networkUpdateBtn" id="networkUpdateBtn"><%= locale.modify %></button>',
            '</td>',
            '</tr>',
            '</table>'
        ].join('')),
        initialize: function (options) {
            this.lnetworkIndex = options.lnetworkIndex;
            this.strName = options.strName;
            this.children = [];
            this.networkModel = new NetworkModel();
            this.ipBlockModel = new NetworkModel();
            this.networkIndexModel = new NetworkModel();
            this.ipBlockCollection = new NetworkCollection();
            this.ipBlockDelCollection = new NetworkCollection();
            this.model = new NetworkModel();
        },
        status: function () {
            return this.editable;
        },
        events: {
            'click #networkInsertBtn': 'showIpBlockPopup',
            'click #networkDeleteBtn': 'deleteIpBlockInfo',
            'click #networkUpdateBtn': 'updateIpBlockPopup',
            'change #networkType': 'showNetworkType',
            'keyup .strName': 'validStrName',
            'keyup .strDescription': 'byteCheck1024'
//			'keyup .strDescription'		: 'validDescription'
                    //'change #bandWidthSelect' : 'setBandWidth',
                    //'blur #dbLbandWidthMbps'  : 'setBandWidthUserDefinition'
                    //'click .changeBtn'		: 'toggle'
        },
        getInsertIndex: function () {
            return this.lnetworkIndex;
        },
        render: function () {
            this.onClose();

            if (this.lnetworkIndex != undefined) {
                this.editable = false;
                this.type = "update";
                this.$el.html(this.template({
                    locale: locale
                }));
                this.getNetworkInfo();
                return this;

            } else {
                this.editable = true;
                this.type = 'insert';
                this.$el.html(this.templateInsert({
                    locale: locale
                }));
                //this.setBandWidth();		// 대역폭 설정 제거 
                // max+1된 네트워크인덱스 조회
//                this.getNextNetworkIndex();

                return this;
            }
        },
        renderEdit: function () {
            this.onClose();
            this.editable = true;
            this.type = 'update';
            this.setStypeModel();
            this.$el.html(this.templateEdit({
                networkModel: this.networkModel.toJSON(),
                locale: locale
            }));
            this.getNetworkInfo();
            this.getNetworkEditableData();
            this.addOneIpBlock();
            // TODO 대역폭 값에 따른 대역폭 범위를 select하기 
            //$('#bandWidthSelect', this.el).find('value', 'this.networkModel.get("dbLbandWidth")').attr("selected");
            return this;
        },
        validStrName: function () {
            var self = this;
            var value = $('.strName', this.el).val();
            var strNameLengthCheck = Backbone.Utils.validation.strByteCheck(value, 32);

            if (strNameLengthCheck != true) {
                alertMessage.infoMessage(strNameLengthCheck, "warn");
                self.resultStrNameVal = Backbone.Utils.Tip.validationTooltip($('.strName'), errorLocale.validation.reInputValue);
                this.valid = false;
                this.strNameValidate = false;
            } else {
                Backbone.Utils.Tip.validationTooltip($('.strName'), true);
                this.strNameValidate = true;
            }
            this.model.set({
                strName: value,
                changeInfo: true
            });
        },
        validDescription: function () {
            var self = this;
            var value = $('.strDescription', this.el).val();
            var strDescLengthCheck = Backbone.Utils.validation.strByteCheck(value, 64);

            if (strDescLengthCheck != true) {
                self.resultStrNameVal = Backbone.Utils.Tip.validationTooltip($('.strDescription'), errorLocale.validation.reInputValue);
                this.valid = false;
                this.strNameValidate = false;
            } else {
                Backbone.Utils.Tip.validationTooltip($('.strDescription'), true);
                this.strNameValidate = true;
            }
            this.model.set({
                strDescription: value,
                changeInfo: true
            });
        },
        toggle: function () {
            if (this.editable) {
                this.mergeNetworkBlockInfo(this.type);
                return this.render();
            } else {
                return this.renderEdit();
            }
        },
        mergeNetworkBlockInfo: function (action) {
            if (this.type == 'update') {
                if (this.editable) {
                    // ipblock 값이 있으면(toIp, fromIp) lValue1값이 0이 아니다.
                    var ipBlockList = [];
                    var ipBlockListDel = [];
                    if (this.ipBlockCollection.size() > 0) {
                        //TODO system_networkipBlock 테이블에서 lid 의 마지막 값을 가져와 +1함, 그값을 lValue1에 담는다.
                        var thisView = this;

                        for (var i = 0; thisView.ipBlockCollection.size() > i; i++) {
                            ipBlockList.push({
                                index: thisView.ipBlockCollection.at(i).get('index'),
                                lId: thisView.ipBlockCollection.at(i).get('lId'),
                                dwToIp: thisView.ipBlockCollection.at(i).get('dwToIp'),
                                dwFromIp: thisView.ipBlockCollection.at(i).get('dwFromIp'),
                                longToIp: Backbone.Utils.Casting.convertIpToLong(thisView.ipBlockCollection.at(i).get('dwToIp')),
                                longFromIp: Backbone.Utils.Casting.convertIpToLong(thisView.ipBlockCollection.at(i).get('dwFromIp'))
                            });
                        }

                        for (var i = 0; thisView.ipBlockDelCollection.size() > i; i++) {
                            ipBlockListDel.push({
                                index: thisView.ipBlockDelCollection.at(i).get('index'),
                                lId: thisView.ipBlockDelCollection.at(i).get('lId'),
                                dwToIp: thisView.ipBlockDelCollection.at(i).get('dwToIp'),
                                dwFromIp: thisView.ipBlockDelCollection.at(i).get('dwFromIp'),
                            });
                        }

                        var setParams = {
                            strName: $('#strName', this.el).val(),
                            lnetworkIndex: this.networkModel.get('lnetworkIndex'),
                            strDescription: $('#strDescription', this.el).val(),
                            sType: $('#networkType', this.el).val(),
                            //dbLbandWidth	: $('#dbLbandWidth', this.el).val(),		// 160503 대역폭 설정 제거
                            lValue1: this.networkModel.get('lValue1'),
                            ipBlockList: ipBlockList,
                            ipBlockListDel: ipBlockListDel
                        };

                        var thisView = this;
                        this.networkModel.fetch({
                            method: 'POST',
                            url: 'api/systemSetting/updateNetworkDetailInfo',
                            contentType: 'application/json',
                            data: JSON.stringify(setParams),
                            dataType: 'text',
                            async: false,
                            success: function (data) {
                                // 커맨드 실행
                            	console.log(data);
                            }
                        });
                        //return this.render();
                    }
//				}else {
//					return this.renderEdit();
//				}
                }
            } else if (this.type == 'insert') {
                // 신규등록
                if (this.editable) {
                    var thisView = this;

                    var ipBlockList = [];
                    if (thisView.ipBlockCollection.size() > 0) {
                        for (var i = 0; thisView.ipBlockCollection.size() > i; i++) {
                            ipBlockList.push({
                                lId: thisView.ipBlockCollection.at(i).get('lId'),
                                dwToIp: thisView.ipBlockCollection.at(i).get('dwToIp'),
                                dwFromIp: thisView.ipBlockCollection.at(i).get('dwFromIp')
                                        //longToIp: Backbone.Utils.Casting.convertIpToLong(thisView.ipBlockCollection.at(i).get('dwToIp')),
                                        //longFromIp: Backbone.Utils.Casting.convertIpToLong(thisView.ipBlockCollection.at(i).get('dwFromIp'))
                            });
                        }


                    } else {
                        this.networkModel.set({
                            lValue1: 0
                        });
                    }
                    var setParams = {
                        strName: $('#strName', this.el).val(),
                        strDescription: $('#strDescription', this.el).val(),
                        sType: $('#networkType', this.el).val(),
                        //dbLbandWidth	: $('#dbLbandWidth',this.el).val(),			// 160503 대역폭 설정 제거 
                        lValue1: this.networkModel.get('lValue1'),
                        ipBlockList: ipBlockList
                    };

                    this.networkModel.fetch({
                        method: 'POST',
                        url: 'api/systemSetting/insertNetworkDetailInfo',
                        contentType: 'application/json',
                        //dataType: 'text', 
                        data: JSON.stringify(setParams),
                        async: false,
                        success: function (model) {
                            thisView.type = "select";
                            thisView.lnetworkIndex = model.get('lnetworkIndex');

                            // 커맨드 실행
                        }
                    });
                }
                //return this.render();
            }
            return this;
        },
        setStypeModel: function () {
            if (this.networkModel.get('sType') == 0) {
                this.selected0 = "selected";
                this.selected1 = "";
                this.selected2 = "";
                this.selected3 = "";
                this.selected4 = "";
                this.selected5 = "";
            } else if (this.networkModel.get('sType') == 1) {
                this.selected0 = "";
                this.selected1 = "selected";
                this.selected2 = "";
                this.selected3 = "";
                this.selected4 = "";
                this.selected5 = "";
            } else if (this.networkModel.get('sType') == 2) {
                this.selected0 = "";
                this.selected1 = "";
                this.selected2 = "selected";
                this.selected3 = "";
                this.selected4 = "";
                this.selected5 = "";
            } else if (this.networkModel.get('sType') == 3) {
                this.selected0 = "";
                this.selected1 = "";
                this.selected2 = "";
                this.selected3 = "selected";
                this.selected4 = "";
                this.selected5 = "";
            } else if (this.networkModel.get('sType') == 4) {
                this.selected0 = "";
                this.selected1 = "";
                this.selected2 = "";
                this.selected3 = "";
                this.selected4 = "selected";
                this.selected5 = "";
            } else if (this.networkModel.get('sType') == 5) {
                this.selected0 = "";
                this.selected1 = "";
                this.selected2 = "";
                this.selected3 = "";
                this.selected4 = "selected";
                this.selected5 = "";
            }
            this.networkModel.set({
                selected0: this.selected0,
                selected1: this.selected1,
                selected2: this.selected2,
                selected3: this.selected3,
                selected4: this.selected4,
                selected5: this.selected5
            });
        },
        getNetworkInfo: function () {
            var thisView = this;

            this.networkModel.fetch({
                method: 'POST',
                url: 'api/systemSetting/selectNetworkDetailList',
                contentType: 'application/json',
                data: JSON.stringify({lnetworkIndex: this.lnetworkIndex}),
                async: false,
                success: function (networkModel) {
                    thisView.getNetworkInfoData(networkModel);
                    thisView.getNetworkIpBlockInfo(thisView.networkModel.get('lnetworkIndex'));
                }
            });
        },
        getNetworkInfoData: function (networkModel) {
            var thisView = this;

            thisView.networkModel.set({
                sTypeName: thisView.getStypeExpression(thisView.networkModel.get('sType'))
                        //dbLbandWidthMbps: thisView.getBandWidthMbpsExpression(thisView.networkModel.get('dbLbandWidth')),		// 대역폭 설정 제거 
                        //dbLbandWidthType: thisView.networkModel.get('dbLbandWidth')
            });
            $('#lnetworkIndex', thisView.el).val(networkModel.get('lnetworkIndex'));
            $('#readStrName', thisView.el).text(_.unescape(thisView.networkModel.get('strName')));
            $('#readLnetworkIndex', thisView.el).text(thisView.networkModel.get('lnetworkIndex'));
            $('#readStrDescription', thisView.el).text(_.unescape(thisView.networkModel.get('strDescription')));
            //$('#readDbLbandWidthMbps', thisView.el).append(thisView.networkModel.get('dbLbandWidthMbps'));			// 대역폭 설정 제거 
            //$('#readDbLbandWidth', thisView.el).append(thisView.networkModel.get('dbLbandWidth'));					// 대역폭 설정 제거 
            $('#readStype', thisView.el).text(thisView.networkModel.get('sTypeName'));
        },
        //<script> alert("script ok"); </script>
        getNetworkEditableData: function (networkModel) {
            var thisView = this;
            $('#strName', thisView.el).text(_.unescape(thisView.networkModel.get('strName')));
            $('#lnetworkIndex', thisView.el).text(thisView.networkModel.get('lnetworkIndex'));
            $('#strDescription', thisView.el).text(_.unescape(thisView.networkModel.get('strDescription')));
            //$('#dbLbandWidthMbps', thisView.el).val(thisView.networkModel.get('dbLbandWidthMbps'));					// 대역폭 설정 제거 
            //$('#dbLbandWidth', thisView.el).val(thisView.networkModel.get('dbLbandWidth'));							// 대역폭 설정 제거 
            $('#lValue1', thisView.el).text(thisView.networkModel.get('lValue1'));
            //$('#bandWidthSelect', thisView.el).val(thisView.getBandWidthValExpression(thisView.networkModel.get('dbLbandWidth')));	// 대역폭 설정 제거 		
            //thisView.setBandWidth();
        },
        getNetworkIpBlockInfo: function (data) {
            var thisView = this;

            this.ipBlockCollection.fetch({
                method: 'POST',
                url: 'api/systemSetting/selectNetworkIpBlockList',
                contentType: 'application/json',
                data: JSON.stringify({lnetworkIndex: data}),
                async: false,
                success: function (collection) {
                    thisView.addOneIpBlock();
                }
            });
        },
        getNextNetworkIndex: function () {
            var self = this;
            this.networkIndexModel.fetch({
                method: 'POST',
                url: 'api/systemSetting/selectNextNetworkIndex',
                contentType: 'application/json',
                data: JSON.stringify({}),
                async: false,
                success: function (model) {
                    $(".lnetworkIndex", self.el).val(model.get('lnetworkIndex'));
                }
            });
        },
        addOneIpBlock: function () {
            var thisView = this;

            if (thisView.editable == false) {
                $('#ipBlockTbody', this.el).empty();

                if (thisView.ipBlockCollection.length > 0) {
                    for (var i = 0; i < thisView.ipBlockCollection.length; i++) {
                        var ipCheck = Backbone.Utils.validation.validateIp(thisView.ipBlockCollection.at(i).get('dwFromIp'));
                        var mask = '';
                        var ipType = 6;
                        if (ipCheck == true) {
                            mask = dataExpression.bitMask(dataExpression.convertIpToBinaryNum(thisView.ipBlockCollection.at(i).get('dwFromIp')), dataExpression.convertIpToBinaryNum(thisView.ipBlockCollection.at(i).get('dwToIp')));
                            ipType = 4;
                        } else {
                            var toIp = thisView.ipBlockCollection.at(i).get('dwToIp');
                            var fromIp = thisView.ipBlockCollection.at(i).get('dwFromIp');
                            Backbone.ajax({
                                method: 'POST',
                                contentType: 'application/json',
                                url: 'api/systemSetting/getIpv6RangeMask',
                                data: JSON.stringify({fromIp: fromIp, toIp: toIp}),
                                dataType: 'json',
                                async: false,
                                success: function (data) {
                                    console.log("mask : " + data);
                                    mask = data;
                                }
                            });
                        }

                        thisView.ipBlockCollection.at(i).set({
                            'ipIndex': i,
                            'mask': mask,
                            'ipType': ipType
                        });

                        var networkIpBlockItem = new NetworkIpBlockItem({
                            model: this.ipBlockCollection.at(i)
                        });

                        this.children.push(networkIpBlockItem);
                        $('#ipBlockTbody', this.el).append(networkIpBlockItem.render().el);
                    }
                } else {
                    $("#header-fix", thisView.el).addClass("nodata");
                }
            } else {
                $('#editIpBlockTbody', this.el).empty();

                for (var i = 0; i < thisView.ipBlockCollection.length; i++) {
                    var ipCheck = Backbone.Utils.validation.validateIp(thisView.ipBlockCollection.at(i).get('dwFromIp'));
                    var mask = '';
                    var ipType = 6;
                    if (ipCheck == true) {
                        mask = dataExpression.bitMask(dataExpression.convertIpToBinaryNum(thisView.ipBlockCollection.at(i).get('dwFromIp')), dataExpression.convertIpToBinaryNum(thisView.ipBlockCollection.at(i).get('dwToIp')));
                    } else {
                        var toIp = thisView.ipBlockCollection.at(i).get('dwToIp');
                        var fromIp = thisView.ipBlockCollection.at(i).get('dwFromIp');
                        Backbone.ajax({
                            method: 'POST',
                            contentType: 'application/json',
                            url: 'api/systemSetting/getIpv6RangeMask',
                            data: JSON.stringify({fromIp: fromIp, toIp: toIp}),
                            dataType: 'json',
                            async: false,
                            success: function (data) {
                                mask = data;
                            }
                        });
                    }
                    thisView.ipBlockCollection.at(i).set({
                        'ipIndex': i,
                        'mask': mask,
                        'ipType': ipType
                    });
                    var networkIpBlockItem = new NetworkIpBlockItem({
                        model: this.ipBlockCollection.at(i)
                    });
                    this.children.push(networkIpBlockItem);
                    $('#editIpBlockTbody', this.el).append(networkIpBlockItem.render().el);
                }
            }
        },
        setBandWidth: function () {

            var selectedVal = $('#bandWidthSelect', this.el).val();
            var result = this.getBandWidthTypeExpression(selectedVal);
            if (selectedVal == 'userDefinition') {
                $('#dbLbandWidthMbps', this.el).removeAttr('disabled');
            } else {
                $('#dbLbandWidthMbps', this.el).attr('disabled', 'disabled');
            }

            $('#dbLbandWidthMbps', this.el).val(result.convertedVal);
            $('#dbLbandWidth', this.el).val(result.resultVal);

        },
        setBandWidthUserDefinition: function () {
            $('#dbLbandWidth').val(this.getBandWidthExpression($('#dbLbandWidthMbps', this.el).val()));
        },
        showNetworkType: function () {
            var sType = $("#networkType option:selected").val();
            if (sType == 2) {
                $("#vLanInput").show();
            } else {
                $("#vLanInput").hide();
            }
        },
        showIpBlockPopup: function () {
            // ipblock popup을 보여주는 fn
            var thisView = this;
            var ipBlockSettingPopup = new IpBlockSettingPopup({
                type: 'insert'
            });
            Backbone.ModalView.msgWithOkCancelBtn({
                size: 'small',
                type: 'search',
                title: 'IP' + locale.setting,
                okLabel: locale.apply,
                body: ipBlockSettingPopup,
                okButtonCallback: function (e) {

                    var popupView = this;
                    //selectbox선택여부에 따라
                    var selectedType = ipBlockSettingPopup.selectedIpType();
                    var toIp, fromIp, mask;
                    ipBlockSettingPopup.valid();
                    if (selectedType == 'range') {
                        if (ipBlockSettingPopup.resultToIp == true && ipBlockSettingPopup.resultFromIp == true) {

                            toIp = $('#toIpInput', popupView.el).val();
                            fromIp = $('#fromIpInput', popupView.el).val();
                            // TODO toIp, fromIp 에 대한 mask 값을 계산해서 넣어줘야함
                            // 임시 방편으로 null값을 넣고, mask 유형일때만 mask값을 넣는다.
                            mask = $('#maskInput', popupView.el).val();

                            thisView.ipBlockCollection.add({
                                //'lId'		: thisView.networkModel.get('lValue1'),
                                'dwToIp': toIp,
                                'dwFromIp': fromIp,
                                'mask': mask
                            });

                            thisView.addOneIpBlock();

                            return true;
                        }
                        alertMessage.infoMessage(errorLocale.validation.invaildIp, "warn");
                        if (ipBlockSettingPopup.resultToIp != true) {
                            Backbone.Utils.Tip.validationTooltip($('#toIpInput', this.el), false);
                        } else {
                            Backbone.Utils.Tip.validationTooltip($('#toIpInput', this.el), true);
                        }
                        if (ipBlockSettingPopup.resultFromIp != true) {
                            Backbone.Utils.Tip.validationTooltip($('#fromIpInput', this.el), false);
                        } else {
                            Backbone.Utils.Tip.validationTooltip($('#fromIpInput', this.el), true);
                        }
                        return false;

                    } else {
                        if (ipBlockSettingPopup.resultIp == true && ipBlockSettingPopup.resultMask == true) {

                            toIp = $('#toIpInput', popupView.el).val();
                            fromIp = $('#fromIpInput', popupView.el).val();
                            mask = $('#maskInput', popupView.el).val();
                            thisView.ipBlockCollection.add({
                                //'lId'		: thisView.networkModel.get('lValue1'),
                                'dwToIp': toIp,
                                'dwFromIp': fromIp,
                                'mask': mask
                            });

                            thisView.addOneIpBlock();
                            return true;
                        }
                        if (ipBlockSettingPopup.resultIp != true) {
                            alertMessage.infoMessage(errorLocale.validation.invaildIp, "warn");
                            Backbone.Utils.Tip.validationTooltip($('#ipInput', this.el), false);
                            return false;
                        } else {
                            Backbone.Utils.Tip.validationTooltip($('#ipInput', this.el), true);
                        }
                        if (ipBlockSettingPopup.resultMask != true) {
                            alertMessage.infoMessage(errorLocale.validation.invaildMaskAndIp, "warn");
                            Backbone.Utils.Tip.validationTooltip($('#maskInput', this.el), false);
                            return false;
                        } else {
                            Backbone.Utils.Tip.validationTooltip($('#maskInput', this.el), true);
                        }
                        return false;
                    }
                }
            });
        },
        updateIpBlockPopup: function () {
            var thisView = this;
            var ipIndex = $('input:radio[name="ipIndex"]:checked', thisView.el).val();
            // 라디오박스를 선택해야지만 수정팝업을 띄운다.
            // ipIndex 는 임의로만든 ipblock의 index이자 collection의 배열을 구분해내는 키로 사용됨, db엔 없는값
            if (ipIndex != undefined) {

                var ipBlockSettingPopup = new IpBlockSettingPopup({
                    type: 'update',
                    model: thisView.ipBlockCollection.at(ipIndex)
                });

                Backbone.ModalView.msgWithOkCancelBtn({
                    size: 'small',
                    type: 'search',
                    title: 'IP' + locale.setting,
                    okLabel: locale.apply,
                    body: ipBlockSettingPopup,
                    okButtonCallback: function (e) {

                        var popupView = this;
                        //selectbox선택여부에 따라
                        var selectedType = ipBlockSettingPopup.selectedIpType();
                        var toIp, fromIp, mask;

                        if (selectedType == 'range') {
                            if (ipBlockSettingPopup.resultToIp == true || ipBlockSettingPopup.resultFromIp == true) {

                                toIp = $('#toIpInput', popupView.el).val();
                                fromIp = $('#fromIpInput', popupView.el).val();
                                // TODO toIp, fromIp 에 대한 mask 값을 계산해서 넣어줘야함
                                // 임시 방편으로 null값을 넣고, mask 유형일때만 mask값을 넣는다.
                                mask = $('#maskInput', popupView.el).val();

                                var fromIpLongValue = Backbone.Utils.Casting.convertIpToLong(fromIp);
                                var toIpLongValue = Backbone.Utils.Casting.convertIpToLong(toIp);

                                if (toIpLongValue < fromIpLongValue) {
                                    return this.resultFrom = Backbone.Utils.Tip.validationTooltip($('#fromIpInput', this.el), errorLocale.validation.isStartIpEndIp);
                                }

                                thisView.ipBlockCollection.at(ipIndex).set({
                                    //'lId'		: thisView.networkModel.get('lValue1'),
                                    'dwToIp': toIp,
                                    'dwFromIp': fromIp,
                                    'mask': mask
                                });

                                thisView.addOneIpBlock();

                                return true;
                            }

                            return false;

                        } else {
                            if (ipBlockSettingPopup.resultIp == true && ipBlockSettingPopup.resultMask == true) {

                                toIp = $('#toIpInput', popupView.el).val();
                                fromIp = $('#fromIpInput', popupView.el).val();
                                mask = $('#maskInput', popupView.el).val();

                                thisView.ipBlockCollection.at(ipIndex).set({
                                    //'lId'		: thisView.networkModel.get('lValue1'),
                                    'dwToIp': toIp,
                                    'dwFromIp': fromIp,
                                    'mask': mask
                                });

                                thisView.addOneIpBlock();
                                return true;
                            }

                            return false;
                        }
                    }
                });
            } else {
                alertMessage.infoMessage(errorLocale.validation.changeSelectedIp, 'info', '', 'small');
            }
        },
        deleteIpBlockInfo: function () {
            // 선택한 ipblock을 삭제하는 fn 
            var thisView = this;

            var ipIndex = $('input:radio[name="ipIndex"]:checked', thisView.el).val();

            if (ipIndex != undefined) {
                var removeModel = thisView.ipBlockCollection.at(ipIndex);
                if (removeModel.get('index') != undefined) {
                    thisView.ipBlockDelCollection.add(removeModel);
                }
                thisView.ipBlockCollection.remove(removeModel);
                thisView.addOneIpBlock();
            } else {
                alertMessage.infoMessage(errorLocale.validation.selectedDeleteIp, 'info', '', 'small');
            }
        },
        getBandWidthMbpsExpression: function (data) {
            return data / 1000;
        },
        getBandWidthExpression: function (data) {
            return data * 1000;
        },
        getStypeExpression: function (data) {

            var result;

            switch (data) {

                case 0:
                    result = 'All';
                    break;
                case 1:
                    result = locale.ipBlock;
                    break;
                case 2:
                    result = 'VLAN';
                    break;
                case 3:
                    result = 'MPLS';
                    break;
                case 4:
                    result = 'VPN-IPBLOCK';
                    break;
                case 5:
                    result = 'URL';
                    break;
                default:
                	result = '';
            }
            return result;
        },
        getBandWidthValExpression: function (data) {

            var type;

            if (data == 1544000) {
                type = 'T1';
            } else if (data == 6312000) {
                type = 'T2';
            } else if (data == 44736000) {
                type = 'T3';
            } else if (data == 274760000) {
                type = 'T4L';
            } else if (data == 2048000) {
                type = 'E1';
            } else if (data == 34064000) {
                type = 'E3';
            } else if (data == 10000000) {
                type = '10M';
            } else if (data == 100000000) {
                type = '100M';
            } else if (data == 1000000000) {
                type = '1G';
            } else if (data == 2500000000) {
                type = '2.5G';
            } else if (data == 10000000000) {
                type = '10G';
            } else {
                type = 'userDefinition';
                // 사용자 정의
            }
            return type;

        },
        getBandWidthTypeExpression: function (selectedVal) {
            var resultVal; //실제값
            var convertedVal; // 변환값
            if (selectedVal == 'T1') {
                resultVal = 1544000;
            } else if (selectedVal == 'T2') {
                resultVal = 6312000;
            } else if (selectedVal == 'T3') {
                resultVal = 44736000;
            } else if (selectedVal == 'T4L') {
                resultVal = 274760000;
            } else if (selectedVal == 'E1') {
                resultVal = 2048000;
            } else if (selectedVal == 'E3') {
                resultVal = 34064000;
            } else if (selectedVal == '10M') {
                resultVal = 10000000;
            } else if (selectedVal == '100M') {
                resultVal = 100000000;
            } else if (selectedVal == '1G') {
                resultVal = 1000000000;
            } else if (selectedVal == '2.5G') {
                resultVal = 2500000000;
            } else if (selectedVal == '10G') {
                resultVal = 10000000000;
            } else if (selectedVal == 'userDefinition') { 	// 사용자 정의
                resultVal = $('#dbLbandWidth', this.el).val();
            }
            convertedVal = this.getBandWidthMbpsExpression(resultVal);	// 환산값

            return ({'resultVal': resultVal, 'convertedVal': convertedVal});
        },
        isValid: function () {
            var self = this;
            this.valid = true;

            this.resultStrNameVal = Backbone.Utils.validation.validateIsNull($('#strName', this.el).val());
            if (this.valid && this.resultStrNameVal != true) {
                alertMessage.infoMessage(this.resultStrNameVal, "warn");
                Backbone.Utils.Tip.validationTooltip($('#strName', this.el), this.resultStrNameVal);
                this.valid = false;
                return false;
            }

            var strNameLengthCheck = Backbone.Utils.validation.strByteCheck($('.strName', this.el).val(), 32);
            if (strNameLengthCheck != true) {
                alertMessage.infoMessage(errorLocale.validation.names + strNameLengthCheck, 'info', '', 'small');
                self.resultStrNameVal = Backbone.Utils.Tip.validationTooltip($('.strName'), errorLocale.validation.reInputValue);
                this.valid = false;
                return false;
            }

            if (this.resultStrNameVal == true)
                Backbone.Utils.Tip.validationTooltip($('#strName', this.el), true);

            if ($('.strDescription', this.el).val() != '') {
                var strDesciptionLengthCheck = Backbone.Utils.validation.strByteCheck($('.strDescription', this.el).val(), 64);
                if (strDesciptionLengthCheck != true) {
                    alertMessage.infoMessage(errorLocale.validation.description + strDesciptionLengthCheck, 'info', '', 'small');
                    self.resultStrDescriptionVal = Backbone.Utils.Tip.validationTooltip($('.strDescription'), errorLocale.validation.reInputValue);
                    this.valid = false;
                    return this.valid;
                }
            }

            if (this.ipBlockCollection.length > 0) {
                this.valid = true;
            } else {
                this.valid = false;
                alertMessage.infoMessage(errorLocale.validation.ipBlockSelected, 'info', '', 'small');
                return this.valid;
            }
            if (this.type == 'insert') {
                // 네트워크명 중복검사 
                var self = this;
                var setParam = {
                    strName: $(".strName", self.el).val()
                };
                self.model.fetch({
                    method: 'POST',
                    url: 'api/systemSetting/isDuplicateNetworkName',
                    contentType: 'application/json',
                    data: JSON.stringify(setParam),
                    async: false,
                    success: function (model) {
                        var strName = model.get('strName');
                        if (strName === null || strName === "") {
                            // 입력필드와 model값이 같고 model값의 중복이 없을 경우
                            self.resultUserIdVal = true;
                        } else {
                            // 입력필드하고 넘겨받은 값과 같지 않으면 
                            if (self.strName != self.$(".strName").val()) {
                                alertMessage.infoMessage(errorLocale.validation.isDuplicateNetworkName, "warn");
                                self.resultUserIdVal = Backbone.Utils.Tip.validationTooltip($('.strName'), errorLocale.validation.isDuplicateNetworkName);
                                self.valid = false;
                            } else {
                                // 업데이트 이전값과 같으면 
                                self.resultUserIdVal = true;
                            }
                        }
                    }
                });
            }

            return this.valid;
        },
        onClose: function () {
            if (this.children.length > 0) {
                _.each(this.children, function (child) {
                    child.close();
                });
            }
        },
        byteCheck1024: function (e) {
            Backbone.Utils.validation.validateTextArea(e, 1024);
        }
    });
});