define(function (require) {
    var $ = require('jquery'),
            _ = require('underscore'),
            Backbone = require('backbone'),
            Loading = require('utils/Loading'),
            aes = require('utils/security/aes'),
            pbkdf2 = require('utils/security/pbkdf2'),
            spin = require('spin');
    
    var AesOption = {
    		keySize : 128 / 32,
            iterationCount : 10000,
            iv : "F27D5C9927726BCEFE7510B1BDD3D137",
            passPhrase : ""
    };
    
    Backbone.ajax({
		method: 'POST',
		contentType: 'application/json',
		url: '/api/common/getAesKey',
		async: false,
		success: function (data) { 
			AesOption.passPhrase = data.AES_SESSION_KEY;
		}
	});
            
    return {
        generateKey : function (salt) {
        	console.log('AesOption.passPhrase', AesOption.passPhrase);
            var key = CryptoJS.PBKDF2(
            		AesOption.passPhrase,
                    CryptoJS.enc.Hex.parse(salt),
                    {keySize: AesOption.keySize, iterations: AesOption.iterationCount});
            return key;
        },
        encrypt : function (salt, plainText) {
            var key = this.generateKey(salt);
            var encrypted = CryptoJS.AES.encrypt(
                    plainText,
                    key,
                    {iv: CryptoJS.enc.Hex.parse(AesOption.iv)});
            return encrypted.ciphertext.toString(CryptoJS.enc.Base64);
        },
        decrypt : function (salt, cipherText) {
            var key = this.generateKey(salt);
            var cipherParams = CryptoJS.lib.CipherParams.create({
                ciphertext: CryptoJS.enc.Base64.parse(cipherText)
            });
            var decrypted = CryptoJS.AES.decrypt(
                    cipherParams,
                    key,
                    {iv: CryptoJS.enc.Hex.parse(AesOption.iv)});
            return decrypted.toString(CryptoJS.enc.Utf8);
        },
        hexEncode : function(str) {
            var hex, i;
            var result = "";
            for (i=0; i<str.length; i++) {
                hex = str.charCodeAt(i).toString(16);
                result += (hex).slice(-4);
            }

            return result
        }
    }
});

var AesUtil = function () {
    this.keySize = 128 / 32;
    this.iterationCount = 10000;
    this.iv = "F27D5C9927726BCEFE7510B1BDD3D137";
    this.passPhrase = "Kglory passPhrase AES";
};

AesUtil.prototype.generateKey = function (salt) {
    var key = CryptoJS.PBKDF2(
        this.passPhrase,
        CryptoJS.enc.Hex.parse(salt),
        {keySize: this.keySize, iterations: this.iterationCount});
    return key;
}

AesUtil.prototype.encrypt = function (salt, plainText) {
    var key = this.generateKey(salt);
    var encrypted = CryptoJS.AES.encrypt(
        plainText,
        key,
        {iv: CryptoJS.enc.Hex.parse(this.iv)});
    return encrypted.ciphertext.toString(CryptoJS.enc.Base64);
}

AesUtil.prototype.decrypt = function (salt, cipherText) {
    var key = this.generateKey(salt);
    var cipherParams = CryptoJS.lib.CipherParams.create({
        ciphertext: CryptoJS.enc.Base64.parse(cipherText)
    });
    var decrypted = CryptoJS.AES.decrypt(
        cipherParams,
        key,
        {iv: CryptoJS.enc.Hex.parse(this.iv)});
    return decrypted.toString(CryptoJS.enc.Utf8);
}

String.prototype.hexEncode = function(){
    var hex, i;

    var result = "";
    for (i=0; i<this.length; i++) {
        hex = this.charCodeAt(i).toString(16);
        result += (hex).slice(-4);
    }

    return result
}