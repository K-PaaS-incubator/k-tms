/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(function (require) {

    "use strict";

    // require library
    var $ = require('jquery'),
            Backbone = require('backbone');

    return Backbone.Model.extend({
        defaults: {
            'lIndex': '',
            'tmFrom': '',
            'tmTo': '',
            'strFileName': '',
            'nTableDel': '',
            'nTableCheckValue': '',
            'tmregDate': '',
            'totalRow': '',
        }
    });
});