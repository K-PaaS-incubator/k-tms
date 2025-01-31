/**
 * validation : 입력값 유효성
 * ipv4, ipv6, port, email, 패스워드, telephone, 숫자와.체크, mask, mask에 대한 ip 유효성,
 * 숫자만입력, 1~500까지숫자,
 *
 * casting : 형변환이 필요할때
 * encoding, decoding,
 *
 * DataExpression : 데이터 변환
 *
 * calculation : 값을 구할때
 * 문자바이트계산, mask의ip범위구하기,
 *
 * tip : table이나 input에 사용자에게 필요한 메세지를  (툴팁, alert 등등을 이용해)띄우고자할때
 * table tooltip 나타내기,
 *
 */
define(function (require) {

    //"use strict";

    var $ 			= require('jquery');

    // require i18n
    var errorLocale = require('i18n!nls/error'),
        Backbone 	= require('backbone');

    return {
        validation: {
            // ipv4 검증
            // refs #21402 - 브로드캐스팅 IP 등 추가 검증 로직 추가(x.x.x.255, x.x.x.0)
            validateIp: function(data) {
                if (!data.match(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/) ||
                    data.match("0.0.0.0") || data.match("255.255.255.255") ||
                    data.match(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.0$/) ||
                    data.match(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.255$/)
                ) {
                    return errorLocale.validation.invaildIp;
                }
                if (data.match("0.0.0.0")) {
                    return errorLocale.validation.invaildIp;
                }
                return true;
            },
            // ipv6 검증 
            validateIpv6: function(data) {
                var checkStr = /^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$/;
                if (!checkStr.test(data)) {
                    return errorLocale.validation.invaildIpv6;
                }
                return true;
            },
            validateIpDualCheck: function(data) {
                var chk = this.validateIp(data);
                // IPv6 허용 X
                // if (chk != true) {
                //     chk = this.validateIpv6(data);
                // }
                if (chk != true) {
                    chk = errorLocale.validation.invaildIp;
                }
                return chk;
            },
            // refs #21354 - 로그인 IP 검증 메소드 추가
            validateLoginIpCheck: function(data) {
                var chk = this.validateIp(data);
                if (chk != true) {
                    chk = errorLocale.validation.invaildIp;
                }
                return chk;
            },
            validateIpAndNull: function(data) {
                if (data == null || data == "") {
                    return true;
                }
                if (!data.match(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/)) {
                    return errorLocale.validation.invaildIp;
                }
                return true;
            },
            // port 검증, port범위  1 ~ 65535
            validatePort: function(data) {
                if (!data.match(/^(6553[0-5]|655[0-2]\d|65[0-4]\d\d|6[0-4]\d{3}|[1-5]\d{4}|[1-9]\d{0,3}|1)$/)) {
                    return errorLocale.validation.invaildPort;
                }
                return true;
            },
            validatePortAndNull: function(data) {
                if (data == null || data == "") {
                    return true;
                }
                if (!data.match(/^(6553[0-5]|655[0-2]\d|65[0-4]\d\d|6[0-4]\d{3}|[1-5]\d{4}|[1-9]\d{0,3}|0)$/)) {
                    return errorLocale.validation.invaildPort;
                }
                return true;
            },
            // email 검증
            validateEmail: function(data) {
                if (!data.match(/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/)) {
                    return errorLocale.validation.invaildEmail;
                }
                return true;
            },
            // 입력 바이트 수를 계산하는 함수 - 키 입력시 체크
            inputByteCheck: function(control_name, max_size) {
                var control_value = control_name; 			// 이벤트가 일어난 컨트롤의 value
                var str_len = control_value.length; 		// 전체 길이
                var li_max = max_size; 						// 제한할 글자수 크기
                var li_byte = 0; 							// 한글일 경우는 2, 그 밖에는 1을 더함
                var li_len = 0; 							// substring 하기 위해서 사용
                var ls_one_char = ""; 						// 한 글자씩 검사한다.

                for (var i = 0; i < str_len; i++) {
                    ls_one_char = control_value.charAt(i); 	// 한글자 추출
                    
                    if (escape(ls_one_char).length > 4) {	// 한글이면 2를 더한다.
                        li_byte += 2;
                    } else { 
                        li_byte++;							// 그 밖의 경우는 1을 더한다.
                    }
                    // 전체 크기가 li_max를 넘지 않는 경우
                    if (li_byte <= li_max) {
                        li_len = i + 1;
                    }
                }
                // 전체길이를 초과하면
                if (li_byte > li_max) {
                    return errorLocale.validation.password.notInputByteSizes; 	// 10 글자를 초과 입력할 수 없습니다. 다시 입력 바랍니다.
                }
                return true;
            },
            idCheck: function(id) {
                var pattern = /^[a-zA-Z]+([a-zA-Z0-9]){5,14}$/g;
                if (!pattern.test(id)) {
                    return errorLocale.validation.idFail;
                }
                return true;
            },
            // 패스워드 검증 9자리 이상 20자리 이하, 영대소문자, 숫자, 특수문자 모두 사용하는 규칙조합
            validatePassword: function(username, newPassword1, newPassword2) {
                // 길이 \W : 특수문자 
            	// 2018.03.06 W의 경우 _ 제외되어 추가! => 특수문자로변경
            	//var reg = /^[a-zA-Z0-9\W]{9,20}$/
            	//var reg = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{9,20}$/;
            	var reg = /^[a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]{9,20}$/g;
                if (!reg.test(newPassword1)) {
                    return errorLocale.validation.password.invalidRule;
                }
				// 영문, 숫자, 특수문자 혼용
                var chk = 0;
                if (newPassword1.search(/[0-9]/g) != -1)
                    chk++;
                if (newPassword1.search(/[a-z]/g) != -1)
                    chk++;
                if (newPassword1.search(/[A-Z]/g) != -1)
                    chk++;
                if (newPassword1.search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g) != -1)
                    chk++;
                if (chk < 4) {
                    return errorLocale.validation.password.invalidMix;
                }
				// 아이디 포함 여부
                if (newPassword1.search(username) > -1) {
                    return errorLocale.validation.password.equalUsername;
                }
				// 동일한 문자/숫자 4이상, 연속된 문자
                if (/(\w)\1\1/.test(newPassword1) || this.isContinuedValue(newPassword1)) {
                    return errorLocale.validation.password.invalidContinue;
                }
                // 자판 배열 상의 연속된 문자, 숫자 입력 방지
                if (this.isContinuedValueInKeyboard(newPassword1)) {
                    return errorLocale.validation.password.invalidContinueInKeyboard;
                }
                // 재입력 일치 여부
                if (newPassword1 != newPassword2) {
                    return errorLocale.validation.password.notEqualRetype;
                }
                return true;
            },
            // 패스워드 일치여부 확인
            isPasswordCheck: function(userId, passwd) {
                var setParams = {
                    id: userId, //사용자 아이디
                    password: passwd, //사용자 비밀번호
                };
                Backbone.ajax({
                    method: 'POST',
                    url: 'api/preferences/isPasswordCheck',
                    contentType: 'application/json',
                    dataType: 'text',
                    data: JSON.stringify(setParams),
                    async: false,
                    success: function(data) {
                        return data;
                    }
                });
            },
            isContinuedValue: function(value) {
                var intCnt1 = 0;
                var intCnt2 = 0;
                var temp0 = "";
                var temp1 = "";
                var temp2 = "";

                for (var i = 0; i < value.length - 3; i++) {
                    temp0 = value.charAt(i);
                    temp1 = value.charAt(i + 1);
                    temp2 = value.charAt(i + 2);

                    if (temp0.charCodeAt(0) - temp1.charCodeAt(0) == 1 && temp1.charCodeAt(0) - temp2.charCodeAt(0) == 1) {
                        intCnt1 = intCnt1 + 1;
                    }

                    if (temp0.charCodeAt(0) - temp1.charCodeAt(0) == -1 && temp1.charCodeAt(0) - temp2.charCodeAt(0) == -1) {
                        intCnt2 = intCnt2 + 1;
                    }
                }

                return (intCnt1 > 0 || intCnt2 > 0);
            },
            isContinuedValueInKeyboard: function(value) {
                // 대문자로 바뀔 수 있는 문자 따로 구분
                const word = ["qwertyuiop", "asdfghjkl", "zxcvbnm"];

                // 숫자를 제외한 모든 문자 대문자로
                const wordAll = ["1234567890", ...word, ...word.map(v => v.toUpperCase())];

                // 해당 문자 배열을 역순으로 조건 생성
                const reverseWord = [...wordAll.map(v => [...v].reverse().join(""))];

                // 생성한 조건을 합치기
                const keyboard = [...wordAll, ...reverseWord];

                for (let i = 0; i < value.length-3; i++) {
                    const sliceValue = value.substring(i, i + 4);

                    if (keyboard.some(code => code.includes(sliceValue))) {
                        return true;
                    }
                }

                return false;
            },
            // 전화번호
            validateTelephone: function(data) {
                //if (!data.match(/\d{2,3}\-\d{4}\-\d{4}/)) {
                // if (!data.match(/^\d{2}-\d{3,4}-\d{4}$/)||!data.match(/^\d{3}-\d{3,4}-\d{4}$/)) {
                if (data.match(/^(070|02|031|032|033|041|042|043|044|051|052|053|054|055|061|062|063|064)-\d{3,4}-\d{4}$/u) ||
                    data.match(/^(070|02|031|032|033|041|042|043|044|051|052|053|054|055|061|062|063|064)\d{7,8}$/u)) {
                    return true;
                }
                return errorLocale.validation.invaildTelephone;
            },
            // 휴대폰번호
            validateMobile: function(data) {
                if (data.match(/^((01[1|6|7|8|9])-\d{3,4}-\d{4})$/)||data.match(/^((010)-\d{4}-\d{4})$/)||
                    data.match(/^((01[1|6|7|8|9])\d{7,8})$/)||data.match(/^((010)\d{8})$/)) {
                    return true;
                }
                return errorLocale.validation.invaildMobile;
            },
            // 숫자만 가능 
            validateIsNumber: function(data) {
                if (!data.match(/^[0-9]*$/)) {
                    return errorLocale.validation.invaildNumber;
                }
                return true;
            },
            validateIThresholdRangeNum: function(value) {
                if (!(value <= 999999 && value >= 0)) {
                    return errorLocale.validation.validateIThresholdRangeNum;
                }
                return true;
            },
            validateIThresholdRangeTime: function(value) {
                if (!(value <= 60 && value >= 0)) {
                    return errorLocale.validation.validateIThresholdRangeTime;
                }
                return true;
            },
            // 숫자와 '.'만 체크  
            validateNumberAndDot: function(data) {
                if (!data.match(/[0-9.]/)) {
                    return alert(errorLocale.validation.numberCommaInput);
                }
                return true;
            },
            // mask 8~32 까지 
            validateMask: function(data) {
                if (data >= 8 && data <= 32) {
                    return true;
                } else {
                    return errorLocale.validation.invaildMask;
                }
            },
            // mask 8~32 까지 
            validateIpv6Mask: function(data) {
                if (data >= 1 && data <= 128) {
                    return true;
                } else {
                    return errorLocale.validation.invaildIpv6Mask;
                }
            },
            // mask 값에 맞는 ip 형식이 맞는지 판단
            validateIpAndMask: function(ip, mask) {

                var ipArray = ip.split('.');
                if (mask == 32) {
                    if (ipArray[0] == 0 || ipArray[1] == 0 || ipArray[2] == 0 || ipArray[3] == 0) {
                        return errorLocale.validation.invaildMaskAndIp;
                    }
                } else if (24 <= mask && mask < 32) {
                    if (ipArray[3] != 0) {
                        return errorLocale.validation.invaildMaskAndIp;
                    }
                    return true;
                } else if (16 <= mask && mask < 24) {
                    if (ipArray[2] != 0 || ipArray[3] != 0) {
                        return errorLocale.validation.invaildMaskAndIp;
                    }
                    return true;
                } else if (8 <= mask && mask < 16) {
                    if (ipArray[1] != 0 || ipArray[2] != 0 || ipArray[3] != 0) {
                        return errorLocale.validation.invaildMaskAndIp;
                    }
                    return true;
                } else if (8 <= mask && mask <= 32 && ipArray[0] == 0) {
                    return errorLocale.validation.invaildMaskAndIp;
                }
                return true;
            },
            validateNull: function(data) {
                if (!data.match(/\0/)) {
                    return errorLocale.validation.invaildNull;
                }

            },
            // 0 부터 무한대 숫자까지 0999(x), 
            validateNumber: function(data) {
                if (!data.match(/^([0-9]|[1-9][0-9]+)$/)) {
                    return errorLocale.validation.invaildNumber;
                }
                return true;
            },
            validateIsNull: function(data) {
                if (data == "") {
                    return errorLocale.validation.invaildNull;
                } else {
                    return true;
                }

            },
            validateFilterNameNull: function (data) {
                if (data == "") {
                    return errorLocale.validation.invaildFilterNameNull;
                } else {
                    return true;
                }

            },
            validateIsNull2: function(data) {
                if (data == null) {
                    return errorLocale.validation.invaildNull;
                } else {
                    return true;
                }

            },
            // 영문 여부 확인 (영문일 경우 True)
            isEng: function(value) {
                if (!value.match(/^[a-zA-Z]+$/)) {
                    return errorLocale.validation.invaildNumber;
                }
                return true;
            },
            isEngHyphen: function(value) {
                //if (!value.match(/^[a-zA-Z]+$/)) {
            	if (!value.match(/^[a-zA-Z\-]+$/)) {
                    return errorLocale.validation.invaildHangeulAndNumber;
                }
                return true;
            },
            isEngNumHyphenUnderbar: function(value) {
                //if (!value.match(/^[a-zA-Z]+$/)) {
            	if (!value.match(/^[a-zA-Z0-9\-_]+$/)) {
                    return errorLocale.validation.invaildHangeulAndNumber;
                }
                return true;
            },
            isEngHyphenUnderbar: function(value) {
                //if (!value.match(/^[a-zA-Z]+$/)) {
            	if (!value.match(/^[a-zA-Z\-_]+$/)) {
                    return errorLocale.validation.invaildHangeulAndNumber;
                }
                return true;
            },
            // 영문과 숫자 _(언더바)만 가능. 첫문자는 영문만 가능. (YARA룰 이름) 
            isEngAndNum: function(value) {
                if (!value.match(/^[A-Za-z][A-Za-z0-9_]*$/)) {
                    return errorLocale.validation.invalidHangeul;
                }
                return true;
            },
            isDuplicationValue: function(value) {
                if (!value) {
                    return errorLocale.validation.isDuplicataValue;
                }
                return true;
            },
            trafficThreshold: function(value, maxLength) {
                if (!_.isString(value) || value.length > maxLength) {
                    return errorLocale.validation.length + " " + maxLength + errorLocale.validation.shouldBeSmaller;
                }
                var maxValue = "9";
                for (var i = 0; i < maxLength - 1; i++) {
                    maxValue = maxValue + "9";
                }
                if (!(value <= parseInt(maxValue) && value >= 0)) {
                    return errorLocale.validation.invalidRange;
                }
                return true;
            },
            /*
             *  사용자정의정책 등록시 코드 입력 범위는 1000000 이상
             */
            validatelCodeRange: function(value, valueType) {
                // 사용자 정의 시그니처일때
                if (valueType == "99") {
                    if (value >= 1000000) {
                        return true;
                    } else {
                        return errorLocale.validation.isDuplicatelCodeValue;
                    }
                } else {
                    // 밴더 시그니처일때
                    if (value < 1000000) {
                        return true;
                    } else {
                        return errorLocale.validation.isDuplicatelCodeValue;
                    }
                }
            },
            // 1부터 500까지의 숫자를 체크한다.  099=false, 99=true
            validateNumber500Range: function(value) {
                if (!(value <= 500 && value >= 0)) {
                    return "0 ~ 500 " + errorLocale.validation.inputLimitValidation;
                }
                return true;
            },
//            rangeFrom0To99999: function (value) {
//                if (!(value < 100000 && value >= 0)) {
//                    return "0 ~ 99999 까지 입력가능합니다.";
//                }
//                return true;
//            },
            rangeFromMinToMax: function(value, min, max) {
                if (!(value <= max && value >= min)) {
                    return min + " ~ " + max +" " + errorLocale.validation.inputLimitValidation;
                }
                return true;
            },
            rangeFrom1To99999: function(value) {
                if (!(value < 100000 && value >= 1)) {
                    return "1 ~ 99999 " + errorLocale.validation.inputLimitValidation;
                }
                return true;
            },
            rangeFrom1To999: function(value) {
                if (!(value < 1000 && value >= 1)) {
                    return "1 ~ 999 " + errorLocale.validation.inputLimitValidation;
                }
                return true;
            },
            rangeFrom1To100: function(value) {
                if (!(value <= 100 && value >= 1)) {
                    return "1 ~ 100" + errorLocale.validation.inputLimitValidation;
                }
                return true;
            },
            rangeFrom0To100: function(value) {
                if (!(value <= 100 && value >= 0)) {
                    return "0 ~ 100" + errorLocale.validation.inputLimitValidation;
                }
                return true;
            },
            rangeFrom0To200: function(value) {
                if (!(value <= 200 && value >= 0)) {
                    return "0 ~ 200" + errorLocale.validation.inputLimitValidation;
                }
                return true;
            },
            rangeFrom0To999: function(value) {
                if (!(value < 1000 && value >= 0)) {
                    return "0 ~ 999 " + errorLocale.validation.inputLimitValidation;
                }
                return true;
            },
            rangeFromMinTo99: function(value, min) {
                if (!(value < 100 && value >= min)) {
                    return min + " ~ 99" + errorLocale.validation.inputLimitValidation;
                }
                return true;
            },
            bpsLength15: function(value) {
                if (!(value >= 0 && value < 1000000000000000)) {
                    return "0 ~ 999999G " + errorLocale.validation.inputLimitValidation;
                }
                return true;
            },
            isValidZero: function(value) {
                if (value == 0) {
                    return true;
                } else {
                    return false;
                }
            },
            validateNativeNumber: function(value) {

                if (value <= 0) {
                    return errorLocale.validation.zeroMinusInputValidation;
                } else {
                    return true;
                }
            },
            specialCharacter: function(value) {
                if (value.match(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/)) {
                    return errorLocale.validation.specialCharecterInputValidation;
                }
                return true;
            },
            rangeFrom0To1000: function(value) {
                if (!(value <= 1000 && value >= 0)) {
                    return "0 ~ 1000" + errorLocale.validation.inputLimitValidation;
                }
                return true;
            },
            maxLength: function(value, maxLength) {
                if (!_.isString(value) || value.length > maxLength) {
                    return errorLocale.validation.length + " " + maxLength + errorLocale.validation.shouldBeSmaller;
                }
            },
            minLength: function(value, minLength) {
                if (!_.isString(value) || value.length < minLength) {
                    return errorLocale.validation.requirdInputValidation;
                }
                return true;
            },
            /**
             * model Validation  
             * Backbone Model Validation을 사용하는 utils는 true일 경우 return 에 값이 없어야합나다.
             */

            ip: function(value) {
                if (!value.match(/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/)) {
                    return errorLocale.validation.invaildIp;
                }
            },
            // 임계치 범위 model validation에 사용 return true 던지지 말것  
            validateIsNumberRange: function(value) {
                if (!(value <= 100000 && value >= 0)) {
                    return errorLocale.validation.invaildNumber;
                } else {
                    return true;
                }
            },
            validateIsTimeRange: function(value) {
            	if (!(value <= 60 && value >= 0)) {
                    return errorLocale.validation.invaildNumber;
                } else {
                    return true;
                }
            },
            bps15Length: function(value, type) {
                if (type != undefined) {
                    switch (type) {
                        case 'n' :
                            value = value * 1;
                            break;
                        case 'k' :
                            value = value * 1000;
                            break;
                        case 'm' :
                            value = value * 1000 * 1000;
                            break;
                        case 'g' :
                            value = value * 1000 * 1000 * 1000;
                            break;
                        default :
                        	value = value * 1;
                            
                    }
                }
                if (!(value >= 0 && value < 1000000000000000)) {
                    return "0 ~ 999999G " + errorLocale.validation.inputLimitValidation;
                }
                return true;
            },
            bpsValueComp: function(maxValue, minValue) {
                if (typeof maxValue != 'number') {
                    maxValue = Number(maxValue);
                }
                if (typeof minValue != 'number') {
                    minValue = Number(minValue);
                }
                if ((maxValue == 0 && minValue == 0) || maxValue > minValue) {
                    return true;
                } else {
                    return errorLocale.validation.upperLawLimitValidation;
                }
            },
            strByteCheck: function(strValue, maxByte) {
                var str = strValue;
//                var byte = this.getStrByte(str);
                var byte = this.getStrByteFor(str);

                if(byte > maxByte) {
                    return errorLocale.validation.stringLength+"(byte : " + maxByte +")" + errorLocale.validation.exceededValidation + " \n" + errorLocale.validation.korLanguageByteValidation;
                }
                return true;
            },
            getStrByte: function(str) {
                // 정규식 한글 3byte
                return str.replace(/[\0-\x7f]|([0-\u07ff]|(.))/g,"$&$1$2").length;
            },
            getStrByteFor: function(str) {
                // 한글 2byte
                var rtn = 0;
                for (var i = 0 ; i < str.length ; i++) {
                    var a = str.charAt(i);
                    if (escape(a).length > 4) {
                        rtn += 2;
                    } else {
                        rtn+=1;
                    }
                }
                return rtn;
            },
            getStrByteForLength: function(str, maxByte) {
                // 한글 2byte
                var rtn = 0;
                for (var i = 0 ; i < str.length ; i++) {
                    var a = str.charAt(i);
                    if (escape(a).length > 4) {
                        rtn += 2;
                    } else {
                        rtn+=1;
                    }
                    if (maxByte < rtn) {
                        return i;
                    }
                }
            },
            // 이메일 서버 Validation 영문(대/소문자)숫자로 시작하고 - . 을포함한 문자열
            validateMailServer: function(value) {
                if (!value.match(/^[A-Za-z0-9][A-Za-z0-9\-\.]*$/)) {
                    return errorLocale.validation.invalidEmailServer;
                }
                return true;
            },
            // 매니저 시간동기화 표준시간 서버 입력
            validateServer: function(value) {
                if (!value.match(/^[A-Za-z0-9][A-Za-z0-9\.]*$/)) {
                    return errorLocale.validation.invalidServer;
                }
                return true;
            },
            protectFromXss: function(str) {
                str = str.replace(/</gi, "&lt;");
                str = str.replace(/>/gi, "&gt;");
                str = str.replace(/cookie/gi, "cook1e");
                str = str.replace(/document/gi, "d0cument");
                str = str.replace(/script/gi, "scr1pt");
                str = str.replace(/" "/g, "&nbsp;");
                str = str.replace(/"/g, "&quot;");
                str = str.replace(/'/g, "&#39;");
                str = str.replace(/\r/g, "");
                str = str.replace(/\n\n/g, "</p><p>");
                str = str.replace(/\n/g, "<BR>");
                return str;
            },
            keyDownNumber: function(keyEvent) {
                var key = (keyEvent.keyCode ? keyEvent.keyCode : keyEvent.which);
		        if (key == 16|| keyEvent.shiftKey) {
		            return false;
		        }
                if (key == 8 || key == 9 || key == 13 || key == 37 || key == 39 || key == 46 || key == 144 || 
                        (key >= 33 && key <= 47 ) || (key >= 48 && key <= 57) || (key >= 96 && key <= 105)) {
                    return true;
                } else {
                    return false;
                }
            },
            keyUpNumber: function(keyEvent) {
                var pattern = /[^0-9]/;
                var value = $(keyEvent.currentTarget).val();
                if (pattern.test(value)) {
                    $(keyEvent.currentTarget).val(value.replace(/[^0-9]/gi, ''));
                }
            },
            keyUpFolderName: function(keyEvent) {
                // , \ / : * ? " < > | 문자 입력제한
                var pattern =/[\\\,/:\*\?<>\|]/;
                var value = $(keyEvent.currentTarget).val();
                if (pattern.test(value)) {
                    $(keyEvent.currentTarget).val(value.replace(/[\\\,/:\*\?<>\|]/gi, ''));
                }
            },
            keyUpCleanXSS: function(keyEvent) {
                // < >  문자 입력제한
                var pattern =/[<>]/;
                var value = $(keyEvent.currentTarget).val();
                if (pattern.test(value)) {
                    $(keyEvent.currentTarget).val(value.replace(/[<>]/gi, ''));
                }
            },
            validateOnlyNumber: function(values) {
                var str = /[^0-9]/gi;
                if (values.match(str)) {
                    return errorLocale.validation.korLanguageInputValidation;
                }
                return true;
            },
            validateTextArea: function(keyEvent, maxByte) {
                var str = $(keyEvent.currentTarget).val();
//                var byte = this.getStrByte(str);
                var byte = this.getStrByteFor(str);

                if(byte > maxByte) {
                    $(keyEvent.currentTarget).val(str.substring(0, this.getStrByteForLength(str, maxByte)));
                }
            },
            keyUpNonKr: function(keyEvent) {
                ///[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g,\'\'
                var pattern = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g;
                var value = $(keyEvent.currentTarget).val();
                if (pattern.test(value)) {
                    $(keyEvent.currentTarget).val(value.replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/gi,''));
                }
            },
            isAlphaNumWithUnderscore: function (param) {
                var pattern = /^[a-zA-Z0-9_]+$/;
                return pattern.test(param);
            },
        },
        Casting: {
            // encode
            encodeUri: function(data) {
                return encodeURI(data);
            },
            // decode
            decodeUri: function(data) {
                return decodeURI(data);
            },
            // ip를 long 형식으로 
            convertIpToLong: function(ip) {
//                var data = ip.split('.');
//                var num = ((((((+data[0]) * 256) + (+data[1])) * 256) + (+data[2])) * 256) + (+data[3]);
//                return num;
            	if(ip.indexOf(":") == -1){ //ipv4
            		var data = ip.split('.');
            		var num = ((((((+data[0]) * 256) + (+data[1])) * 256) + (+data[2])) * 256) + (+data[3]);
            		return num;
            	}
            	else { //ipv6
            		var tmp = ip.split(':');
            		var data = [];
            		if(tmp.length < 8){// 축약존재
            			for(var i=0; i < 8;i++){
            				if(tmp.length == i+1){
            					data[i] = 0;
            				}
            				else {
            					if(i == 7){
            						data[i] = tmp[tmp.length-1];
            					}
            					else {
            						if(Number(tmp[i]) != NaN && tmp[i] != null  && tmp[i] != ''){
            							data[i] = tmp[i];
            						}
            						else {
            							data[i] = 0;
            						}
            					}
            				}
            			}
            		}
            		else {
            			data = tmp;
            		}
            		
            		var num = 0;
            		for(var i=0; i < 8;i++){
            			if(Number(data[i]) != NaN){
            				var tm = 1;
            				if(i > 0){
            					for(var j =0; j< i; j++){
            						tm *= 256;
            					}
            				}
            				num += (+parseInt(data[i], 16) * tm);
            			}
            			
            		}
            		
            		return num;
            	}
            },
            // long형식의 ip를 ip형식으로 (1.1.1.1)
            convertLongToIp: function(num) {
                var longData = num % 256;
                for (var i = 3; i > 0; i--) {
                    num = Math.floor(num / 256);
                    longData = num % 256 + '.' + longData;
                }
                return longData;
            },
            //사용 예 : Backbone.Utils.Casting.stringEmptyToData(data, "-");
            stringEmptyToData: function(data, changeData) {
                var rtn = '';
                if (typeof changeData != undefined) {
                    rtn = changeData;
                }
                if (data != null && data.trim() != '') {
                    rtn = data;
                }
                return rtn;
            }
        },
        Calculation: {
            // 문자열 byte 계산
            stringByteLength: function(data) {
                var i = 0;
                var c;
                for (var b = i = 0; c = data.charCodeAt(i++); b += c >> 11 ? 3 : c >> 7 ? 2 : 1){
                	console.log(b);
                }
                return b;
            },
            //cidr 을 이용한 ip 범위 구하기 
            parseCidrRangeIp2: function(ip, mask) {
                var CIDR = ip + '/' + mask;

                //Beginning IP address
                var begin = CIDR.substr(CIDR, CIDR.indexOf('/'));
                var end = begin;
                var off = (1 << (32 - parseInt(CIDR.substr(CIDR.indexOf('/') + 1)))) - 1;
                var sub = begin.split('.').map(function (a) {
                    return parseInt(a)
                });

                //An IPv4 address is just an UInt32...
                var buf = new ArrayBuffer(4); //4 octets 
                var i32 = new Uint32Array(buf);

                //Get the UInt32, and add the bit difference
                i32[0] = (sub[0] << 24) + (sub[1] << 16) + (sub[2] << 8) + (sub[3]) + off;

                //Recombine into an IPv4 string:
                var end = Array.apply([], new Uint8Array(buf)).reverse().join('.');

                return [begin, end];
            },
            // ip 값에 상관 없이 mask 범위에 해당하는 from ip, to ip를 return
            parseCidrRangeIp: function(ip, mask) {
                var mask = mask;
                var ip = ip;

                var maskNum;
                var tempMask;
                var fromIp, toIp;
                ipSplit = ip.split(".");
                if (mask >= 24 && mask <= 32) {

                    tempMask = Math.pow(2, mask - 24);
                    fromIp = new this.makeArray(tempMask);
                    toIp = new this.makeArray(tempMask);

                    for (var i = 0; i < fromIp.length; i++) {
                        fromIp[i] = Math.pow(2, (32 - mask)) * i;
                        toIp[i] = (Math.pow(2, (32 - mask)) * (i + 1)) - 1;

                        if (ipSplit[3] >= fromIp[i] && ipSplit[3] <= toIp[i]) {
                            fromIpValue = fromIp[i];
                            toIpValue = toIp[i];
                        }
                    }

                    var returnFromIp = ipSplit[0] + "." + ipSplit[1] + "." + ipSplit[2] + "." + fromIpValue;
                    var returnToIp = ipSplit[0] + "." + ipSplit[1] + "." + ipSplit[2] + "." + toIpValue;
                    return [returnFromIp, returnToIp];

                } else if (mask >= 16 && mask < 24) {

                    tempMask = Math.pow(2, mask - 16);
                    fromIp = new this.makeArray(tempMask);
                    toIp = new this.makeArray(tempMask);

                    for (var i = 0; i < fromIp.length; i++) {
                        fromIp[i] = Math.pow(2, (24 - mask)) * i;
                        toIp[i] = (Math.pow(2, (24 - mask)) * (i + 1)) - 1;

                        if (ipSplit[2] >= fromIp[i] && ipSplit[2] <= toIp[i]) {
                            fromIpValue = fromIp[i];
                            toIpValue = toIp[i];
                        }
                    }

                    var returnFromIp = ipSplit[0] + "." + ipSplit[1] + "." + fromIpValue + ".0";
                    var returnToIp = ipSplit[0] + "." + ipSplit[1] + "." + toIpValue + ".255";
                    return [returnFromIp, returnToIp];
                } else if (8 <= mask && mask < 16) {
                    tempMask = Math.pow(2, mask - 8);
                    fromIp = new this.makeArray(tempMask);
                    toIp = new this.makeArray(tempMask);

                    for (var i = 0; i < fromIp.length; i++) {
                        fromIp[i] = Math.pow(2, (16 - mask)) * i;
                        toIp[i] = (Math.pow(2, (16 - mask)) * (i + 1)) - 1;

                        if (ipSplit[1] >= fromIp[i] && ipSplit[1] <= toIp[i]) {
                            fromIpValue = fromIp[i];
                            toIpValue = toIp[i];
                        }
                    }
                    var returnFromIp = ipSplit[0] + "." + fromIpValue + ".0.0";
                    var returnToIp = ipSplit[0] + "." + toIpValue + ".255.255";
                    return [returnFromIp, returnToIp];
                }
            },
            makeArray: function(n) {
                this.length = n;

                for (var i = 1; i <= n; i++) {
                    this[i] = 0;
                }

                return this;
            }
        },
        DataExpression: {
            severityData: function (data) {
                if (data == 5) {
                    return 'High';
                } else if (data == 3) {
                    return 'Medieum';
                } else if (data == 1) {
                    return 'Low';
                } else if (data == 0) {
                    return 'Info ';
                } else {
                    return 'absence';
                }
            },
            // 위험도 숫자값을 칼라로 표현할때  
            severityColorData: function(data) {
                if (data == 5) {
                    return 5;
                } else if (data == 3) {
                    return 4;
                } else if (data == 1) {
                    return 3;
                } else if (data == 0) {
                    return 2;
                } else {
                    return 1;
                }
            },
        },
        convertStrToDate: function(dateStr) {
            var a = dateStr.split(" ");
            var d = a[0].split("-");
            var t = a[1].split(":");

            try {
                return new Date(d[0], (d[1] - 1), d[2], t[0], t[1]);
            } catch (e) {
                return null;
            }
        },

        // 그래프가 있는 화면에서 분석 기간을 30분 이하로 조회 했을 경우 유효성 검사를 위해 비교 
        getValidateStartEndTime: function(startDateString, endDateString) {
            var startDate = new Date(this.convertStrToDate(startDateString));
            var endDate = new Date(this.convertStrToDate(endDateString));

            var tempStartDate = startDate.getTime();
            var tempEndDate = endDate.getTime();

            var getDiffTime = parseInt(tempEndDate) - parseInt(tempStartDate);
            if (getDiffTime < 30 * 60 * 1000) {
                return false;
            }
        },

        // 시작시간 종료시간 검사 (시간포맷: yyyy-MM-dd HH:mm)
        compareDate: function(fromDate, toDate) {
            var rtn = true;

            var validation = this.getValidateStartEndTime(fromDate, toDate);
            if (validation == false) {
                return false;
            }
            return rtn;
        },
        compareMaxDate: function(fromDate, toDate) {
            var rtn = true;
            var fromDt = this.getStringToDateTime(fromDate);
            var toDt = this.getStringToDateTime(toDate);

            if ((toDt - fromDt) <= 0)
                return false;

            return rtn;
        },
        compareDay: function(fromDate, toDate) {
            var rtn = true;
            var fromDt = this.getStringToDate(fromDate);
            var toDt = this.getStringToDate(toDate);

            if ((toDt - fromDt) < 0)
                return false;

            return rtn;
        },
        getStringToDate: function(date) {
            var temp = date.split(" ");
            var date = temp[0].split("-");

            var year = Number(date[0]);
            var month = Number(date[1]) - 1;
            var day = Number(date[2]);

            var date = new Date(year, month, day);

            return date;
        },
        getStringToDateTime: function(date) {
            var temp = date.split(" ");
            var date = temp[0].split("-");
            var time = temp[1].split(":");

            var year = Number(date[0]);
            var month = Number(date[1]) - 1;
            var day = Number(date[2]);
            var hour = Number(time[0]);
            var minute = Number(time[1]);

            var date = new Date(year, month, day, hour, minute);

            return date;
        },
        Tip: {
            // data-toggle 속성과 title 값이 테이블안에 있어야함
            defualtTooltip: function() {
                $('[data-toggle="tooltip"]').tooltip();
            },
            /**
             * Utils.validation Message를 보여줄수 있는 tooltips
             * selector: tooltips을 나타낼 selector $("#aaa")
             * msg: tootips에 나타날 메세지 i18n 값
             */
            validationTooltip: function (attr, msg) {
//                var selector = null;
//                console.log(attr);
//                if(attr.selector == null || attr.selector == undefined){
//                	selector = attr[0].selector;
//                }
//                else {
//                	selector = attr.selector;
//                }
//                
//                console.log(selector, attr[0].selector);
                
//                var $control = selector.closest('.form-control', this.el);
            	$control = $(attr);
            	console.log($control);
                if ($control.attr('data-toggle') == 'tooltip') {
                    $control.removeAttr('data-toggle');
                    $control.removeClass('validation-error');
//                    $control.tooltip('destroy');
                }

                if (msg != true) {
                    $control.attr('data-toggle', 'tooltip');
                    $control.addClass('validation-error');
                    // 170308 툴팁 메세지 오류. 주석처리
//                    $control.tooltip({
//                        title: msg,
//                        placement: 'bottom'
//                    }).tooltip('show');
                    return false;
                }
                return true;
            }
        },
        /**
         * table header 위치를 찾아서 하이라이트를 준다. 
         * th는 한번만 render하기 때문에 remove가 필요하다.
         * td는 template를 그릴때 highlight class를 준다. 
         **/
        DomSeletor: {
            showThHighLight: function(highLightEl, target) {
                var highLightCls = 'colgroup-highlight';
                // 하이라이트 제거 후 append 
                $('th', target).removeClass(highLightCls);
                $('.' + highLightEl, target).addClass(highLightCls);
            },
            hideThHightLight: function(highLightEl, target) {
                // 하이라이트 제거만(데이터 없을 경우 처리)
                var highLightCls = 'colgroup-highlight';
                $('th', target).removeClass(highLightCls);
            }
        }
    };

});
