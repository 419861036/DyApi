function setCookie(key, val) {
    var exp = new Date();
    exp.setTime(exp.getTime() + 24 * 60 * 60 * 1000);//保存一天
    document.cookie = key + '=' + escape(val) + ';expires=' + exp.toGMTString() + ';path=/';
    return true;
}
var userStr="%7B%22userId%22%3A%22ZSB0005@ITVP%22%2C%22userID_encode%22%3A%22NXRK%3E7.2.DQ%5C%5C%22%2C%22userToken%22%3A%228liaq8liaq4NUqH9qEhdJv51jOLxzk72%22%2C%22userToken_encode%22%3A%2209i%7CvNMl/7tLbjCs7JoWL6ocgn6s_kj%3A%22%2C%22tokenExpiretime%22%3A%2220200209192524%22%2C%22groupId%22%3A%221%22%2C%22groupName%22%3A%22null%22%2C%22userIP%22%3A%2210.124.24.243%22%2C%22epgIP%22%3A%22null%22%2C%22epgPort%22%3A%22%22%2C%22areaCode%22%3A%2210403%22%2C%22tradeId%22%3A%222%22%2C%22stbId%22%3A%2200109116060101000000B8224F1CF77B%22%2C%22mac%22%3A%22null%22%2C%22VAStoEPG%22%3A%22null%22%2C%22optFlag%22%3A%22null%22%2C%22back_epg_url%22%3A%22null%22%2C%22back_epg_url_par%22%3A%22null%22%2C%22epgPlatform%22%3A%222%22%2C%22SourceOisIp%22%3A%22192.168.31.21%22%2C%22ModelType%22%3A%222%22%2C%22CrmArea%22%3A%22null%22%2C%22vasType%22%3A%22null%22%2C%22key%22%3A%221%3A2%22%2C%22epgtemplate%22%3A%22null%22%2C%22stbType%22%3A%22%22%2C%22stbVersion%22%3A%22%22%7D";
//setCookie("USERINFO",userStr);


/*
* createBy xiangmw
* date 20181016
* */
var EPGUtil = null;
(function (window, document) {
    //TODO 提供JS对象的扩展方法

    /******
     *@String的全替换方法
     *  @param x 表示替换目标
     *  @param y 表示替换样式
     *  @desc  用Y替换X
     */
    String.prototype.EPGReplaceAll = function (x, y) {
        var r = new RegExp(x, 'g');
        return this.replace(r, y);
    };

    /******
     *@数组拷贝
     *  @param arr 表示将要拷贝的数据
     *  @desc  拷贝arr
     */
    Array.prototype.EPGExtend = function (arr) {
        for (var i = 0, j = arr.length; i < j; i++) {
            this.push(arr[i]);
        }
    };

    Date.prototype.format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    //TODO 提供EPG3.0的工具方法
    function EPGUtils() {
    }

    EPGUtils.prototype = {
        /* 封装ajax函数
         * @param {object}opt.header 需要新增的头信息
         * @param {boolean}opt.debug 是否在屏幕上输出请求的url、method、data以及responseText
         * @param {string}opt.method http连接的方式，包括POST和GET两种方式
         * @param {string}opt.url 发送请求的url
         * @param {boolean}opt.async 是否为异步请求，true为异步的，false为同步的
         * @param {object}opt.data 发送的参数，格式为对象类型
         * @param {object}opt.user 发送的参数，格式为对象类型,用户信息
         * @param {string}opt.dataType json/text/log
         * @param {boolean}opt.handel true位此方法自动处理错误,默认false
         * @param {function}opt.success ajax发送并接收成功调用的回调函数
         * @param {function}opt.error ajax发送失败调用的回调函数
         */
        ajax: function (opt) {
            var _this = this;
            opt = opt || {};
            opt.header = opt.header || {};
            opt.debug = !!opt.debug;
            opt.method = (opt.method || 'GET').toUpperCase();
            opt.url = opt.url || '';
            opt.async = opt.async || true;
            opt.data = opt.data || null;
            opt.user = opt.user || this.getUserInfo();
            opt.dataType = opt.dataType || 'text';
            opt.handel = opt.handel || false;
            opt.success = opt.success || function () {
            };
            opt.error = opt.error || function () {
            };
            var xmlHttp = null;
            if (XMLHttpRequest) xmlHttp = new XMLHttpRequest();
            else xmlHttp = new ActiveXObject('Microsoft.XMLHTTP');
            var postData = '';
            if (opt.method === 'POST') {
                xmlHttp.open(opt.method, opt.url, opt.async);
                switch (opt.dataType) {
                    case 'json':
                    case 'log':
                        if (!opt.data instanceof Object) throw "opt.data, ajax: opt.data must be a Object";
                        postData = JSON.stringify(opt.data);
                        xmlHttp.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
                        break;
                    default:
                        if (!opt.data instanceof Object) throw "opt.data, ajax: opt.data must be a Object";
                        postData = this.formatParams(opt.data);
                        xmlHttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
                        break;
                }
            } else if (opt.method === 'GET') {
                postData = this.formatParams(opt.data);
                xmlHttp.open(opt.method, !postData ? opt.url : opt.url + '?' + postData, opt.async);
            }
            if (JSON.stringify(opt.header) !== '{}') {
                for (var item in opt.header) {
                    if (!opt.header.hasOwnProperty(item)) continue;
                    xmlHttp.setRequestHeader(item, opt.header[item]);
                }
            }
            xmlHttp.setRequestHeader('userId', opt.user.userId);
            xmlHttp.setRequestHeader('userToken', opt.user.userToken);
            xmlHttp.setRequestHeader('mac', opt.user.mac);
            xmlHttp.setRequestHeader('stbId', opt.user.stbId);
            xmlHttp.setRequestHeader('SourceOisIp', opt.user.SourceOisIp);
            xmlHttp.setRequestHeader('ModelType', opt.user.ModelType);
            xmlHttp.send(opt.method === 'GET' ? null : postData);
            if (opt.debug) this.debug('url-->' + opt.url + '<br/>method-->' + opt.method + '<br/>data-->' + postData);
            xmlHttp.onreadystatechange = function () {
                if (xmlHttp.readyState === 4) {
                    if (xmlHttp.status === 200) {
                        var result = '';
                        // if (xmlHttp.getResponseHeader('Content-Type').indexOf('application/json') > -1)s
                        result = JSON.parse(xmlHttp.responseText);
                        // else
                        //     result = xmlHttp.responseText;
                        if (opt.debug) _this.debug('responseText-->' + JSON.stringify(result));
                        if (opt.dataType === 'log') {
                            opt.success(result);
                        } else {
                            switch (result.code) {
                                case 0:
                                    opt.success(result);
                                    break;
                                case 302://userToken失效
                                    _this.updateUserToken(result.userToken);//更新userToken
                                    _this.ajax(opt);//重新请求
                                    break;
                                case 222://强制下线，暂时用222占位
                                    //TODO
                                    _this.showError({code: result.code, message: '您的账号已被强制下线！'});
                                    break;
                                default:
                                    if (opt.handel) {
                                        _this.showError({code: result.code, message: result.data});
                                    } else {
                                        opt.success(result);
                                    }
                                    break;
                            }
                        }
                    } else {
                        opt.error({
                            code: -1,
                            message: '日志上报失败',
                            data: opt.data
                        });
                        if (opt.dataType === 'log') {
                            //TODO 日志服务器宕机不跳转错误页面
                        } else {
                            // opt.error({status: xmlHttp.status, statusText: xmlHttp.statusText});
                            //TODO 接口请求失败，跳转错误页面
                            if (opt.handel)
                                _this.showError({code: xmlHttp.status, message: '服务器连接失败，请检查网络或重启机顶盒'});
                        }
                    }
                }
            };
        },
        /* 封装ajax函数同时携带其他参数
         * @param {object}opt.header 需要新增的头信息
         * @param {boolean}opt.debug 是否在屏幕上输出请求的url、method、data以及responseText
         * @param {string}opt.method http连接的方式，包括POST和GET两种方式
         * @param {string}opt.url 发送请求的url
         * @param {boolean}opt.async 是否为异步请求，true为异步的，false为同步的
         * @param {object}opt.data 发送的参数，格式为对象类型
         * @param {object}opt.user 发送的参数，格式为对象类型,用户信息
         * @param {string}opt.dataType json/text/log
         * @param {boolean}opt.handel true位此方法自动处理错误,默认false
         * @param {function}opt.success ajax发送并接收成功调用的回调函数
         * @param {function}opt.error ajax发送失败调用的回调函数
         */
        ajaxOObj: function (opt, obj) {
            var _this = this;
            opt = opt || {};
            opt.header = opt.header || {};
            opt.debug = !!opt.debug;
            opt.method = (opt.method || 'GET').toUpperCase();
            opt.url = opt.url || '';
            opt.async = opt.async || true;
            opt.data = opt.data || null;
            opt.user = opt.user || this.getUserInfo();
            opt.dataType = opt.dataType || 'text';
            opt.handel = opt.handel || false;
            opt.success = opt.success || function () {
            };
            opt.error = opt.error || function () {
            };
            var xmlHttp = null;
            if (XMLHttpRequest) xmlHttp = new XMLHttpRequest();
            else xmlHttp = new ActiveXObject('Microsoft.XMLHTTP');
            var postData = '';
            if (opt.method === 'POST') {
                xmlHttp.open(opt.method, opt.url, opt.async);
                switch (opt.dataType) {
                    case 'json':
                    case 'log':
                        if (!opt.data instanceof Object) throw "opt.data, ajax: opt.data must be a Object";
                        postData = JSON.stringify(opt.data);
                        xmlHttp.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
                        break;
                    default:
                        if (!opt.data instanceof Object) throw "opt.data, ajax: opt.data must be a Object";
                        postData = this.formatParams(opt.data);
                        xmlHttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
                        break;
                }
            } else if (opt.method === 'GET') {
                postData = this.formatParams(opt.data);
                xmlHttp.open(opt.method, !postData ? opt.url : opt.url + '?' + postData, opt.async);
            }
            if (JSON.stringify(opt.header) !== '{}') {
                for (var item in opt.header) {
                    if (!opt.header.hasOwnProperty(item)) continue;
                    xmlHttp.setRequestHeader(item, opt.header[item]);
                }
            }
            xmlHttp.setRequestHeader('userId', opt.user.userId);
            xmlHttp.setRequestHeader('userToken', opt.user.userToken);
            xmlHttp.setRequestHeader('mac', opt.user.mac);
            xmlHttp.setRequestHeader('stbId', opt.user.stbId);
            xmlHttp.setRequestHeader('SourceOisIp', opt.user.SourceOisIp);
            xmlHttp.setRequestHeader('ModelType', opt.user.ModelType);
            xmlHttp.send(opt.method === 'GET' ? null : postData);
            if (opt.debug) this.debug('url-->' + opt.url + '<br/>method-->' + opt.method + '<br/>data-->' + postData);
            xmlHttp.onreadystatechange = function () {
                if (xmlHttp.readyState === 4) {
                    if (xmlHttp.status === 200) {
                        var result = '';
                        // if (xmlHttp.getResponseHeader('Content-Type').indexOf('application/json') > -1)
                        result = JSON.parse(xmlHttp.responseText);
                        // else
                        //     result = xmlHttp.responseText;
                        if (opt.debug) _this.debug('responseText-->' + JSON.stringify(result));
                        if (opt.dataType === 'log') {
                            opt.success(result, obj);
                        } else {
                            switch (result.code) {
                                case 0:
                                    opt.success(result, obj);
                                    break;
                                case 302://userToken失效
                                    _this.updateUserToken(result.userToken);//更新userToken
                                    _this.ajax(opt);//重新请求
                                    break;
                                case 222://强制下线，暂时用222占位
                                    //TODO
                                    _this.showError({code: result.code, message: '您的账号已被强制下线！'});
                                    break;
                                default:
                                    if (opt.handel) {
                                        _this.showError({code: result.code, message: result.data});
                                    } else {
                                        opt.success(result, obj);
                                    }
                                    break;
                            }
                        }
                    } else {
                        opt.error({
                            code: -1,
                            message: '日志上报失败',
                            data: opt.data
                        });
                        if (opt.dataType === 'log') {
                            //TODO 日志服务器宕机不跳转错误页面
                        } else {
                            // opt.error({status: xmlHttp.status, statusText: xmlHttp.statusText});
                            //TODO 接口请求失败，跳转错误页面
                            if (opt.handel)
                                _this.showError({code: xmlHttp.status, message: '服务器连接失败，请检查网络或重启机顶盒'});
                        }
                    }
                }
            };
        },
        /* jsonp请求
         * @param {boolean}opt.debug 是否在屏幕上输出opt.data
         * @param {string}opt.url 发送请求的url
         * @param {string}opt.callback 为服务端获取回调方法的参数名，一般为callback
         * @param {object}opt.data 发送的参数，格式为对象类型
         * @param {number}opt.time 超时时间
         * @param {function}opt.success jsonp发送并接收成功调用的回调函数
         * @param {function}opt.fail jsonp发送超时调用的函数
         * @example
         *          EPGUtil.jsonp({
                        url: 'https://sug.so.360.cn/suggest',
                        callback: 'callback',//服务端回调方法参数名
                        time: 300,//超时时间
                        data: {
                            encodein: 'utf-8',
                            encodeout: 'utf-8',
                            format: 'json',
                            fields: 'word',
                            word: 'xiao'
                        },
                        success: function (callBack) {
                            console.log(callBack);
                        },
                        fail: function (fail) {
                            console.log(fail.message);
                        }
                    });
         */
        jsonp: function (opt) {
            opt = opt || {};
            opt.debug = !!opt.debug;
            if (opt.debug) this.debug(opt.data);
            if (!opt.url || !opt.callback) throw new Error("jsonp参数不合法");
            var callbackName = ('jsonp_' + Math.random()).replace(".", "");
            var oHead = document.getElementsByTagName('head')[0];
            opt.data[opt.callback] = callbackName;
            var params = this.formatParams(opt.data);
            var oS = document.createElement('script');
            oS.timer = null;
            oHead.appendChild(oS);
            window[callbackName] = function (json) {
                oHead.removeChild(oS);
                clearTimeout(oS.timer);
                window[callbackName] = null;
                opt.success && opt.success(json);
            };
            oS.src = opt.url + '?' + params;
            if (opt.time) {
                oS.timer = setTimeout(function () {
                    window[callbackName] = null;
                    oHead.removeChild(oS);
                    opt.fail && opt.fail({message: "超时"});
                }, opt.time);
            }
        },
        /* 点击上报
         * @param {string}type 日志类型 play||click
         * @param {string}parms 日志信息
         * @param {string}path 日志路径
         * @example EPGUtil.sendLog(
                        'play',
                        'sy|20181127142520|20181127143520|9383933002|CCTV-1高清|10000|1|b700v5|ab:dd:a0:b2|192.168.1.13',
                        '/test/aaa'
                    );
         */
        sendLog: function (type, parms, path) {
            this.ajax({
                method: 'POST',
                dataType: 'log',
                url: '/epgs/user_log' + path,
                data: {
                    type: type,
                    parms: parms
                }
            });
        },
        /* 将用户信息存储到cookie
         * @param {string}opt.userId 用户ID
         * @param {string}opt.userToken userToken
         * @param {string}opt.mac 客户端mac地址
         * @param {string}opt.stbId 机顶盒型号
         * @return {boolean}
         */
        setUserInfo: function (opt) {
            if (!opt.userId || !opt.userToken) return false;
            this.setCookie('USERINFO', JSON.stringify(opt));
            return !!this.getCookie('USERINFO');
        },
        /* 更新userToken
         * @param {string}userToken userToken
         * @return {boolean}
         */
        updateUserToken: function (userToken) {
            var userInfo = this.getUserInfo();
            userInfo.userToken = userToken;
            this.setUserInfo(userInfo);
            return true;
        },
        /* 获取用户信息
         * @return {object}
         */
        getUserInfo: function () {
            var userStr = this.getCookie('USERINFO');
            if (!userStr) return {};
            return JSON.parse(userStr);
        },
        /* 返回地址压栈
         * @param {string}currentUrl URL
         * @return {Boolean}
         */
        setBackUrl: function (currentUrl) {
            var controlUrlName = 'locationUrls', urlKey = 'location_url_';
            var index = !!this.getCookie(controlUrlName) ? this.getCookie(controlUrlName).split(',').length : 0;

            var controlUrlValue;
            for (var i = 0; i < index + 1; i++)
                controlUrlValue = i === 0 ? urlKey + i : controlUrlValue + ',' + urlKey + i;
            this.setCookie(controlUrlName, controlUrlValue);

            var currentUrlKey = urlKey + index;
            this.setCookie(currentUrlKey, currentUrl);
            return true;
        },
        /* 返回地址获取
         * @return {string} URL,没有则返回首页地址
         */
        getBackUrl: function () {
            var url = '';
            try {
                url = Authentication.CTCGetConfig("EPGDomain");
            } catch (e) {
                url = '';
            }
            var controlUrlName = 'locationUrls', urlKey = 'location_url_';
            var index = !!this.getCookie(controlUrlName) ? this.getCookie(controlUrlName).split(',').length : 0;
            if (index === 0) return url;

            var currentUrlKey = urlKey + (index - 1);
            url = this.getCookie(currentUrlKey);
            this.delCookie(currentUrlKey);

            this.delCookie(controlUrlName);
            if (index === 1) return url;

            var controlUrlValue;
            for (var i = 0; i < index - 1; i++)
                controlUrlValue = i === 0 ? urlKey + i : controlUrlValue + ',' + urlKey + i;
            this.setCookie(controlUrlName, controlUrlValue);

            return url;
        },
        /* 清空所有压栈地址,建议首页使用,防止乱跳
         * @return {Boolean}
         */
        delAllBackUrl: function () {
            var controlUrlName = 'locationUrls', urlKey = 'location_url_';
            var index = !!this.getCookie(controlUrlName) ? this.getCookie(controlUrlName).split(',').length : 0;
            if (index === 0) return true;
            for (var i = 0; i < index; i++) {
                var currentUrlKey = urlKey + i;
                this.delCookie(currentUrlKey);
            }
            this.delCookie(controlUrlName);
            return true;
        },
        /* @function formatParams
         * @param {object}opt 发送的参数，格式为对象类型
         * @return {string} 'a=aa&b=bb&...'
         */
        formatParams: function (opt) {
            if (!opt instanceof Object) throw "opt, formatParams: opt must be a Object";
            var arr = [];
            for (var name in opt) {
                if (!opt.hasOwnProperty(name)) continue;
                arr.push(encodeURIComponent(name) + '=' + encodeURIComponent(opt[name]));
            }
            return arr.join('&');
        },
        setCookie: function (key, val) {
            var exp = new Date();
            exp.setTime(exp.getTime() + 24 * 60 * 60 * 1000);//保存一天
            document.cookie = key + '=' + escape(val) + ';expires=' + exp.toGMTString() + ';path=/';
            return true;
        },
        getCookie: function (key) {
            var arr = null;
            if (document.cookie != null && document.cookie.length > 0)
                arr = document.cookie.match(new RegExp('(^| )' + key + '=([^;]*)(;|$)'));
            if (arr != null)
                return unescape(arr[2]);
            return null;
        },
        delCookie: function (key) {
            /*为了删除指定名称的cookie，可以将其过期时间设定为一个过去的时间*/
            var date = new Date();
            date.setTime(date.getTime() - 10000);
            document.cookie = key + '=;expires=' + date.toGMTString() + ';path=/';
            return true;
        },
        getQueryString: function (strname, url) {
            var hrefstr, pos, parastr, para, tempstr;
            hrefstr = window.location.href;
            if (typeof (url) !== "undefined")
                hrefstr = url;
            pos = hrefstr.indexOf("?");
            /*没有参数，则直接跳出*/
            if (pos === -1 && hrefstr.indexOf("=") === -1)
                return null;
            parastr = decodeURI(hrefstr.substring(pos + 1));
            para = parastr.split("&");
            tempstr = "";
            for (var i = 0; i < para.length; i++) {
                tempstr = para[i];
                pos = tempstr.indexOf("=");
                if (tempstr.substring(0, pos) === strname) {
                    return tempstr.substring(pos + 1);
                }
            }
            return '';
        },
        /**
         * @function numSupplyZero
         * @param {string} initNumStr 初始化字符串
         * @param {string} numStr 需要格式化数字
         * @return {string} 格式化后字符串
         * @description 数字前面补0
         * @example var str = EPGUtil.numSupplyZero("112","0000"); 结果为:0112
         */
        numSupplyZero: function (initNumStr, numStr) {
            var len = initNumStr.length;
            initNumStr = numStr + initNumStr;
            return initNumStr.substring(initNumStr.length - numStr.length);
        },
        /**
         * @function getSliceList
         * @param {array} objs 列表数组
         * @param {number} pageIndex 开始页
         * @param {number} pageSize 每页条数
         * @return {array} 本页数组
         * @description 根据数组开始页与每页条数，计算出当前页数组列表
         * @example EPGUtil.getSliceList([1,3,5,6,8,9,7],2,3);
         */
        getSliceList: function (objs, pageIndex, pageSize) {
            var tempObj = objs;
            if (objs != null && typeof (objs) === "object" && objs.length > 0) {
                pageIndex = parseInt(pageIndex, 10);
                pageSize = parseInt(pageSize, 10);
                var len = objs.length;
                if (pageIndex > 0 && pageSize > 0 && len > 0) {
                    var begin = pageSize * (pageIndex - 1);
                    if (begin < 0) begin = 0;
                    var end = pageSize * pageIndex;
                    if (end > len) end = len;
                    tempObj = objs.slice(begin, end);
                }
            }
            return tempObj;
        },
        /**
         * @function getPageTotal
         * @param {number} totalNum 总条数
         * @param {number} pageSize 每页条数
         * @return {number} 总页数
         * @description 根据总条数与每页条数，计算出总页数
         * @example EPGUtil.getPageTotal(112,10);
         */
        getPageTotal: function (totalNum, pageSize) {
            return Math.ceil(Number(totalNum) / Number(pageSize));
        },
        /**
         * @function getStrRealLen
         * @param {string} str 字符串
         * @return {number} 字符长度
         * @description 获取字符串真实长度,中文字符算2长度
         * @example var str = EPGUtil.getStrRealLen("test测试");
         */
        getStrRealLen: function (str) {/*获取字符串真实长度,中文字符算2长度*/
            if (typeof (str) !== "string" || str.length === 0)
                return 0;
            var len = 0;
            var strLen = str.length;
            for (var i = 0; i < strLen; i++) {
                a = str.charAt(i);
                len++;
                if (escape(a).length > 4) {/*中文字符的长度经编码之后大于4*/
                    len++;
                }
            }
            return len;
        },
        debug: function (error) {
            var debug = this.$('debug');
            if (debug === null) {
                debug = document.createElement("div");
                debug.id = 'debug';
                debug.style.cssText = 'z-index:20;font-size:16px;position:absolute;top:10px;' +
                    'left:10px;width:1240px;height:auto;overflow:hidden;padding:10px;' +
                    'background:#D0D0D0;color:red;word-break:break-all;display:block;' +
                    'filter:alpha(opacity=0.7);-moz-opacity:0.7;-khtml-opacity:0.7;opacity:0.7;';
                document.body.appendChild(debug);
            }
            var content = '';
            switch (typeof error) {
                case 'undefined':
                    content = 'undefined';
                    break;
                case 'boolean':
                    content = "(boolean):" + error;
                    break;
                case 'number':
                    content = "(number):" + error;
                    break;
                case 'string':
                    content = "(string):" + error;
                    break;
                case 'object':
                    if (!error) {
                        content = '(object):null';
                    } else {
                        var text = [];
                        for (var iError in error) {
                            if (error.hasOwnProperty(iError)) {
                                var value = error[iError];
                                text.push(String(iError + ":" + "(" + (typeof value) + ")" + value));
                            }
                        }
                        content = "(object) => {" + text.join(", ") + "}";
                    }
                    break;
            }
            var tempContent = debug.innerHTML;
            var date = new Date();
            debug.innerHTML = "<div style='font-size: 12px;color:#FFF;position:relative'>" +
                "<strong>debug   @" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() +
                "</strong></div><div id ='debug_box' style='position:relative'></div>";
            this.$('debug_box').innerHTML = content + "<br/>" + tempContent;
        },
        clearDebug: function () {
            this.$('debug').parentNode.removeChild(this.$('debug'));
        },
        /**
         * @function getTime
         * @param {number||string} utc UTC时间戳
         * @return {Object} 时间集合
         * @example EPGUtil.getTime() or EPGUtil.getTime('1443895871000')
         */
        getTime: function (utc) {
            if (utc) {
                utc = parseInt(utc);
                if (isNaN(utc)) throw 'getTime, format: utc is not a number';
            } else {
                var d = new Date();
                var localTime = d.getTime();
                // var localOffset = d.getTimezoneOffset() * 60 * 1000;//获得当地时间偏移的毫秒数,这里可能是负数
                var localOffset = 0;
                var GMT_UTC = localTime + localOffset;//utc即GMT时间
                var offset = 8;//时区，北京市+8  美国华盛顿为 -5
                utc = GMT_UTC + (60 * 60 * 1000 * offset);//本地对应的毫秒数
            }
            var date = new Date(utc);
            var timeObj = {};
            var weekArray = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
            timeObj.year = date.getUTCFullYear().toString();
            timeObj.month = this.numSupplyZero((date.getUTCMonth() + 1).toString(), '00');
            timeObj.date = this.numSupplyZero((date.getUTCDate()).toString(), '00');
            timeObj.hours = this.numSupplyZero((date.getUTCHours().toString()), '00');
            timeObj.minutes = this.numSupplyZero((date.getUTCMinutes().toString()), '00');
            timeObj.seconds = this.numSupplyZero((date.getUTCSeconds().toString()), '00');
            timeObj.week = weekArray[date.getUTCDay()];
            return timeObj;
        },
        /**
         * @function numToTime
         * @param {number} num 秒
         * @return {string} hhmmss
         * @example EPGUtil.numToTime(22)
         */
        numToTime: function (num) {
            if (num > -1) {
                var hour = Math.floor(num / 3600);
                var min = Math.floor(num / 60) % 60;
                var sec = num % 60;
                return this.numSupplyZero(hour, '00') + ":" + this.numSupplyZero(min, '00') + ":" + this.numSupplyZero(sec, '00');
            }
        },
        /**
         * @function getSubStr
         * @param {string} str 截取前字符串
         * @param {number} len 截取长度,中文字符算2长度
         * @param {boolean} isSuffix 是否加省略号，默认不加
         * @return {string} 截取后字符串
         * @description 截取字符串,中文字符算2长度
         * @example var str = EPGUtil.getSubStr("test测试",6);普通截取
         * @example var str = EPGUtil.getSubStr("test测试",6,true); 截取后面加省略号
         */
        getSubStr: function (str, len, isSuffix) {
            if (typeof (str) !== "string" || str.length === 0) return "";
            var realLen = this.getStrRealLen(str);
            if (realLen <= len) {
                return str;
            } else {
                var str_length = 0;
                var str_cut = String();
                var str_len = str.length;
                if (isSuffix)
                    len -= 3;
                for (var i = 0; i < str_len; i++) {
                    var a = str.charAt(i);
                    str_length++;
                    if (escape(a).length > 4) {
                        /*中文字符的长度经编码之后大于4*/
                        str_length++;
                    }
                    str_cut = str_cut.concat(a);
                    if (str_length >= len) {
                        if (isSuffix) {
                            str_cut = str_cut.concat("...");
                        }
                        return str_cut;
                    }
                }
                /*如果给定字符串小于指定长度，则返回源字符串；*/
                if (str_length < len)
                    return str;
            }
        },
        /**
         * @desc 排序算法，支持类型 [{},{},{},{}]
         * @param arr 需要排序的数组 key为排序的关键字
         * **/
        arrSort: function (arr, key) {
            var list = [];
            list.EPGExtend(arr);
            arr.length = 0;
            try { //尝试排序
                for (var i = 0, j = list.length; i < j; i++) {
                    var item = list[i]; //当前排序对象

                    //第一个直接插入
                    if (i === 0) {
                        arr.push(item);
                        continue;
                    }
                    //循环遍历排序
                    for (var k = 0, n = list.length; k < n; k++) {
                        var t0 = parseInt(item[key]);
                        var t1 = parseInt(arr[k][key]);
                        if (t0 < t1) {
                            arr.splice(k, 0, item);
                            break;
                        } else if (k === (n - 1)) {
                            arr.push(item);
                            break;
                        }
                    }
                }
            } catch (e) { //失败
                arr.EPGExtend(list);
            }
        },
        /**
         * @desc 提供一个弹窗工具，展示信息，2秒后自动消失
         * @param msg 需要展示的信息
         * **/
        remindTip: function (msg, remind) {
            var _this = this;
            var remind_id = 'Remind_Tip';
            this.initTips = function () {
                var html = document.createElement("div");
                var style = "position: absolute;left: 50%;width: 300px;height: 75px;color: yellow;" +
                    "font-size: 26px;text-align: center;line-height: 75px;border: 1px solid #999;" +
                    "margin-left: -150px;display: none;background-color:rgba(0,0,0,0.7);border-radius: 5px;";
                html.id = remind_id;
                html.style.cssText = style;
                document.body.appendChild(html);
            };
            if (!!remind) remind_id = remind.id;
            else this.initTips();
            if (this.remindTipsTimer) clearTimeout(this.remindTipsTimer);
            this.$(remind_id).innerHTML = msg;
            this.$(remind_id).style.display = 'block';
            this.remindTipsTimer = setTimeout(function () {
                _this.$(remind_id).innerHTML = '';
                _this.$(remind_id).style.display = 'none';
            }, 2000);
        },
        showError: function (opt) {
            window.location.href = '../../errPage/error.html?code=' + opt.code + '&message=' + encodeURIComponent(opt.message);
        },
        $: function (id) {
            return window.document.getElementById(id);
        },
        replaceParamVal:function(url,paramName,replaceVal) {
            var oUrl = url.toString();
            var re=eval('/('+ paramName+'=)([^&]*)/gi');
            var nUrl = oUrl.replace(re,paramName+'='+replaceVal);
            return nUrl;
        },

//验证手机号
        validatePhoneNum: function (elementId, phoneNo) {
            var _this = this;
            var elementId = _this.$(elementId);
            if (phoneNo == "" || phoneNo == "请输入手机号码") {
                elementId.innerHTML = "手机号不能为空";
                return false;
            }
            var partten = /^[0-9]+$/;
            var lenth = phoneNo.length;
            if (!partten.test(phoneNo)) {
                elementId.innerHTML = "只能是数字";
                return false;
            }
            if (lenth != 11) {
                elementId.innerHTML = "请输入11位号码";
                return false;
            }
            elementId.innerHTML = "";
            return true;
        }
};
    EPGUtil = new EPGUtils();
})(window, document);
			
