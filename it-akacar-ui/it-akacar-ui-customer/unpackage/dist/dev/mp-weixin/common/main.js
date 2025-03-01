(global["webpackJsonp"] = global["webpackJsonp"] || []).push([["common/main"],{

/***/ 0:
/*!********************************************************************!*\
  !*** F:/code/it-akacar/it-akacar-ui/it-akacar-ui-customer/main.js ***!
  \********************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(wx, createApp, uni) {

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ 4);
var _defineProperty2 = _interopRequireDefault(__webpack_require__(/*! @babel/runtime/helpers/defineProperty */ 11));
__webpack_require__(/*! uni-pages */ 26);
var _App = _interopRequireDefault(__webpack_require__(/*! ./App */ 27));
var _vue = _interopRequireDefault(__webpack_require__(/*! vue */ 25));
var _uviewUi = _interopRequireDefault(__webpack_require__(/*! uview-ui */ 33));
function ownKeys(object, enumerableOnly) { var keys = Object.keys(object); if (Object.getOwnPropertySymbols) { var symbols = Object.getOwnPropertySymbols(object); enumerableOnly && (symbols = symbols.filter(function (sym) { return Object.getOwnPropertyDescriptor(object, sym).enumerable; })), keys.push.apply(keys, symbols); } return keys; }
function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = null != arguments[i] ? arguments[i] : {}; i % 2 ? ownKeys(Object(source), !0).forEach(function (key) { (0, _defineProperty2.default)(target, key, source[key]); }) : Object.getOwnPropertyDescriptors ? Object.defineProperties(target, Object.getOwnPropertyDescriptors(source)) : ownKeys(Object(source)).forEach(function (key) { Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key)); }); } return target; }
// @ts-ignore
wx.__webpack_require_UNI_MP_PLUGIN__ = __webpack_require__;
_vue.default.config.productionTip = false;
_App.default.mpType = 'app';
var app = new _vue.default(_objectSpread({}, _App.default));

//注册uView组件

_vue.default.use(_uviewUi.default);
createApp(app).$mount();
_vue.default.prototype.Consts = {
  QQMAP_KEY: "GD3BZ-VTZWQ-S2I5Z-2X6V5-QNKNE-7CB5M",
  //腾讯地图的秘钥
  API: {
    CUSTOMER_REGISTER: "/customer/app/customer/register",
    //乘客注册
    APP_LOGIN: "/uaa/app/login/wechat" //app微信登录
  },

  GtPush: {
    appid: "Qb5XleyFh67nRidkiR9xz",
    msg_type_pay: 0
  },
  OrderStatus: {
    ORDER_STATUS_WAIT: 0,
    ORDER_STATUS_ACCEPTED: 1,
    ORDER_STATUS_ARRIVE: 2,
    ORDER_STATUS_START_DRIVING: 3,
    ORDER_STATUS_COMPLETE_DRIVED: 4,
    ORDER_STATUS_ENSURE: 5,
    ORDER_STATUS_NOT_PAY: 6,
    ORDER_STATUS_PAYED: 7,
    ORDER_STATUS_FINISH_ORDER: 8,
    ORDER_STATUS_CUSTOMER_CANCEL_ORDER: 9,
    ORDER_STATUS_DRIVER_CANCEL_ORDER: 10,
    ORDER_STATUS_ACCIDENT_CLOSE: 11,
    ORDER_STATUS_OTHER: 12
  }
};
var baseUrl = "http://127.0.0.1:10010/akacar";
function sendRequest(url, method, sendData, callBack) {
  //uni.showLoading({ title: "请求中..." })

  uni.request({
    "url": baseUrl + url,
    "method": method,
    "timeout:": 10000,
    "header": {
      satoken: uni.getStorageSync("tokenValue")
    },
    "data": sendData,
    success: function success(resp) {
      if (resp.statusCode == 401) {
        uni.redirectTo({
          url: "/pages/login/login.vue"
        });
      } else if (resp.statusCode == 200 && resp.data.code == 200) {
        var data = resp.data;
        if (data.hasOwnProperty("token")) {
          var token = data.token;
          uni.setStorageSync("token", token);
        }
        if (callBack) {
          callBack(resp.data);
        }
      } else {
        console.log("请求结果异常：", resp.data.message);
        uni.showToast({
          icon: "none",
          title: "请求失败[" + resp.data.message + "]"
        });
        if (callBack) {
          callBack(resp.data);
        }
      }
    },
    complete: function complete() {
      setTimeout(function () {
        uni.hideLoading();
      }, 1000);
    },
    fail: function fail(error) {
      uni.showToast({
        icon: "none",
        title: "请求错误[" + error.errMsg + "]"
      });
    }
  });
}
_vue.default.prototype.post = function (url, data, fun) {
  var sendData = typeof data == "function" ? {} : data;
  var callBack = typeof data == "function" ? data : fun;
  sendRequest(url, "post", sendData, callBack);
};
_vue.default.prototype.get = function (url, data, fun) {
  var sendData = typeof data == "function" ? {} : data;
  var callBack = typeof data == "function" ? data : fun;
  sendRequest(url, "get", sendData, callBack);
};
_vue.default.prototype.del = function (url, data, fun) {
  var sendData = typeof data == "function" ? {} : data;
  var callBack = typeof data == "function" ? data : fun;
  sendRequest(url, "del", sendData, callBack);
};
_vue.default.prototype.put = function (url, data, fun) {
  var sendData = typeof data == "function" ? {} : data;
  var callBack = typeof data == "function" ? data : fun;
  sendRequest(url, "put", sendData, callBack);
};
_vue.default.prototype.request = function (url, method, data, fun) {
  var sendData = typeof data == "function" ? {} : data;
  var callBack = typeof data == "function" ? data : fun;
  sendRequest(url, method, sendData, callBack);
};
_vue.default.prototype.refreshMessage = function (that) {
  uni.request({
    "url": that.url.refreshMessage,
    "method": "POST",
    "header": {
      token: uni.getStorageSync("token")
    },
    "data": {
      identity: 'driver'
    },
    success: function success(resp) {
      if (resp.statusCode == 401) {
        uni.redirectTo({
          url: "/pages/login/login.vue"
        });
      } else if (resp.statusCode == 200 && resp.data.code == 200) {
        uni.$emit("updateMessageService", true);
        var result = resp.data.result;
        var lastRows = result.lastRows;
        var unreadRows = result.unreadRows;
        if (lastRows > 0) {
          uni.$emit("showMessageTip", lastRows);
        }
      } else {
        //在工作台页面触发更新消息服务状态，显示服务可用或者不可用
        uni.$emit("updateMessageService", false);
      }
    },
    fail: function fail(error) {
      //在工作台页面触发更新消息服务状态，显示服务可用或者不可用
      uni.$emit("updateMessageService", false);
    }
  });
};
_vue.default.prototype.uploadCos = function (url, path, module, fun) {
  uni.uploadFile({
    url: url,
    filePath: path,
    name: "file",
    header: {
      token: uni.getStorageSync("token")
    },
    formData: {
      "module": module
    },
    success: function success(resp) {
      var data = JSON.parse(resp.data);
      if (resp.statusCode == 401) {
        uni.redirectTo({
          url: "/pages/login/login.vue"
        });
      } else if (resp.statusCode == 200 && data.code == 200) {
        fun(resp);
      } else {
        uni.showToast({
          icon: "none",
          title: data.error
        });
      }
    }
  });
};
_vue.default.prototype.upload = function (url, path, data, fun) {
  uni.uploadFile({
    url: url,
    filePath: path,
    name: "file",
    header: {
      token: uni.getStorageSync("token")
    },
    formData: data,
    success: function success(resp) {
      var data = JSON.parse(resp.data);
      if (resp.statusCode == 401) {
        uni.redirectTo({
          url: "/pages/login/login.vue"
        });
      } else if (resp.statusCode == 200 && data.code == 200) {
        fun(resp);
      } else {
        uni.showToast({
          icon: "none",
          title: data.error
        });
      }
    }
  });
};
_vue.default.prototype.toPage = function (url) {
  uni.navigateTo({
    url: url
  });
};
_vue.default.prototype.checkNull = function (data, name) {
  if (data == null) {
    this.$refs.uToast.show({
      title: name + "不能为空",
      type: 'error'
    });
    return true;
  }
  return false;
};
_vue.default.prototype.checkBlank = function (data, name) {
  if (data == null || data == "") {
    this.$refs.uToast.show({
      title: name + "不能为空",
      type: 'error'
    });
    return true;
  }
  return false;
};
_vue.default.prototype.checkValidName = function (data, name) {
  if (data == null || data == "") {
    this.$refs.uToast.show({
      title: name + "不能为空",
      type: 'error'
    });
    return false;
  } else if (!/^[\u4e00-\u9fa5]{2,15}$/.test(data)) {
    this.$refs.uToast.show({
      title: name + "不正确",
      type: 'error'
    });
    return false;
  }
  return true;
};
_vue.default.prototype.checkValidTel = function (data, name) {
  if (data == null || data == "") {
    this.$refs.uToast.show({
      title: name + "不能为空",
      type: 'error'
    });
    return false;
  } else if (!/^1[0-9]{10}$/.test(data)) {
    this.$refs.uToast.show({
      title: name + "不正确",
      type: 'error'
    });
    return false;
  }
  return true;
};
_vue.default.prototype.checkValidEmail = function (data, name) {
  if (data == null || data == "") {
    this.$refs.uToast.show({
      title: name + "不能为空",
      type: 'error'
    });
    return false;
  } else if (!/^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/.test(data)) {
    this.$refs.uToast.show({
      title: name + "不正确",
      type: 'error'
    });
    return false;
  }
  return true;
};
_vue.default.prototype.checkValidAddress = function (data, name) {
  if (data == null || data == "") {
    this.$refs.uToast.show({
      title: name + "不能为空",
      type: 'error'
    });
    return false;
  } else if (!/^[0-9a-zA-Z\u4e00-\u9fa5\-]{6,50}$/.test(data)) {
    this.$refs.uToast.show({
      title: name + "不正确",
      type: 'error'
    });
    return false;
  }
  return true;
};
_vue.default.prototype.checkValidFee = function (data, name) {
  if (data == null || data == "") {
    this.$refs.uToast.show({
      title: name + "不能为空",
      type: 'error'
    });
    return false;
  } else if (!/^[1-9]\d*\.\d{1,2}$|^0\.\d{1,2}$|^[1-9]\d*$/.test(data)) {
    this.$refs.uToast.show({
      title: name + "不正确",
      type: 'error'
    });
    return false;
  }
  return true;
};
_vue.default.prototype.checkValidCarType = function (data, name) {
  if (data == null || data == "") {
    this.$refs.uToast.show({
      title: name + "不能为空",
      type: 'error'
    });
    return false;
  } else if (!/^[\u4e00-\u9fa5A-Za-z0-9\-\_\s]{2,20}$/.test(data)) {
    this.$refs.uToast.show({
      title: name + "不正确",
      type: 'error'
    });
    return false;
  }
  return true;
};
_vue.default.prototype.checkValidCarPlate = function (data, name) {
  if (data == null || data == "") {
    this.$refs.uToast.show({
      title: name + "不能为空",
      type: 'error'
    });
    return false;
  } else if (!/^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})$/.test(data)) {
    this.$refs.uToast.show({
      title: name + "不正确",
      type: 'error'
    });
    return false;
  }
  return true;
};
_vue.default.prototype.changeNumber = function (value) {
  var newValue = ['', ''];
  var fr = 1000;
  var ad = 1;
  var num = 3;
  var fm = 1;
  while (value / fr >= 1) {
    fr *= 10;
    num += 1;
  }
  if (num <= 4) {
    // 千
    newValue[1] = '千';
    newValue[0] = parseInt(value / 1000) + '';
  } else if (num <= 8) {
    // 万
    var text1 = parseInt(num - 4) / 3 > 1 ? '千万' : '万';
    var _fm = '万' === text1 ? 10000 : 10000000;
    newValue[1] = text1;
    newValue[0] = value / _fm + '';
  } else if (num <= 16) {
    // 亿
    var _text = (num - 8) / 3 > 1 ? '千亿' : '亿';
    _text = (num - 8) / 4 > 1 ? '万亿' : _text;
    _text = (num - 8) / 7 > 1 ? '千万亿' : _text;
    // tslint:disable-next-line:no-shadowed-variable
    var _fm2 = 1;
    if ('亿' === _text) {
      _fm2 = 100000000;
    } else if ('千亿' === _text) {
      _fm2 = 100000000000;
    } else if ('万亿' === _text) {
      _fm2 = 1000000000000;
    } else if ('千万亿' === _text) {
      _fm2 = 1000000000000000;
    }
    newValue[1] = _text;
    newValue[0] = parseInt(value / _fm2) + '';
  }
  if (value < 1000) {
    newValue[1] = '';
    newValue[0] = value + '';
  }
  var temp = Math.floor(newValue[0] * 100) / 100;
  return temp + newValue[1];
};
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/wx.js */ 1)["default"], __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/index.js */ 2)["createApp"], __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/index.js */ 2)["default"]))

/***/ }),

/***/ 27:
/*!********************************************************************!*\
  !*** F:/code/it-akacar/it-akacar-ui/it-akacar-ui-customer/App.vue ***!
  \********************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./App.vue?vue&type=script&lang=js& */ 28);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
/* harmony import */ var _App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./App.vue?vue&type=style&index=0&lang=scss& */ 30);
/* harmony import */ var _software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_runtime_componentNormalizer_js__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/runtime/componentNormalizer.js */ 32);
var render, staticRenderFns, recyclableRender, components
var renderjs





/* normalize component */

var component = Object(_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_runtime_componentNormalizer_js__WEBPACK_IMPORTED_MODULE_2__["default"])(
  _App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__["default"],
  render,
  staticRenderFns,
  false,
  null,
  null,
  null,
  false,
  components,
  renderjs
)

component.options.__file = "App.vue"
/* harmony default export */ __webpack_exports__["default"] = (component.exports);

/***/ }),

/***/ 28:
/*!*********************************************************************************************!*\
  !*** F:/code/it-akacar/it-akacar-ui/it-akacar-ui-customer/App.vue?vue&type=script&lang=js& ***!
  \*********************************************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _software_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! -!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/babel-loader/lib!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--13-1!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/script.js!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!./App.vue?vue&type=script&lang=js& */ 29);
/* harmony import */ var _software_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_software_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _software_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _software_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
 /* harmony default export */ __webpack_exports__["default"] = (_software_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 29:
/*!****************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** ./node_modules/babel-loader/lib!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--13-1!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/script.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!F:/code/it-akacar/it-akacar-ui/it-akacar-ui-customer/App.vue?vue&type=script&lang=js& ***!
  \****************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(wx, uni) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _default = {
  onLaunch: function onLaunch() {
    //订阅消息
    wx.requestSubscribeMessage({
      tmplIds: ['V_KaT0Y_CzAf264nC_hqkztS5RlVhr3EyKAaQ9l5lY4'],
      success: function success(res) {
        console.log("订阅消息，", res);
      }
    });

    //开启位置更新监听
    wx.startLocationUpdate({
      type: "wgs84",
      success: function success(res) {
        console.log("开始位置实时监听...");
      },
      fail: function fail(res) {
        uni.showToast({
          title: "定位失败",
          icon: "error"
        });
      }
    });
    wx.onLocationChange(function (res) {
      //获取经纬度
      var latitude = res.latitude,
        longitude = res.longitude;

      //把坐标位置传给locationChange 函数
      var location = {
        latitude: latitude,
        longitude: longitude
      };
      uni.$emit("locationChange", location);
    });
  },
  onShow: function onShow() {},
  onHide: function onHide() {
    console.log('App Hide');
  }
};
exports.default = _default;
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/wx.js */ 1)["default"], __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/index.js */ 2)["default"]))

/***/ }),

/***/ 30:
/*!******************************************************************************************************!*\
  !*** F:/code/it-akacar/it-akacar-ui/it-akacar-ui-customer/App.vue?vue&type=style&index=0&lang=scss& ***!
  \******************************************************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _software_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_software_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_2_software_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_3_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_sass_loader_dist_cjs_js_ref_8_oneOf_1_4_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_5_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! -!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/mini-css-extract-plugin/dist/loader.js??ref--8-oneOf-1-0!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/css-loader/dist/cjs.js??ref--8-oneOf-1-1!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/stylePostLoader.js!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--8-oneOf-1-2!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/postcss-loader/src??ref--8-oneOf-1-3!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/sass-loader/dist/cjs.js??ref--8-oneOf-1-4!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--8-oneOf-1-5!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!../../../../software/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!./App.vue?vue&type=style&index=0&lang=scss& */ 31);
/* harmony import */ var _software_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_software_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_2_software_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_3_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_sass_loader_dist_cjs_js_ref_8_oneOf_1_4_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_5_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_software_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_software_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_2_software_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_3_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_sass_loader_dist_cjs_js_ref_8_oneOf_1_4_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_5_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0__);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _software_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_software_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_2_software_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_3_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_sass_loader_dist_cjs_js_ref_8_oneOf_1_4_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_5_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _software_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_software_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_2_software_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_3_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_sass_loader_dist_cjs_js_ref_8_oneOf_1_4_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_5_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
 /* harmony default export */ __webpack_exports__["default"] = (_software_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_software_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_2_software_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_3_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_sass_loader_dist_cjs_js_ref_8_oneOf_1_4_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_5_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 31:
/*!**********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** ./node_modules/mini-css-extract-plugin/dist/loader.js??ref--8-oneOf-1-0!./node_modules/css-loader/dist/cjs.js??ref--8-oneOf-1-1!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/stylePostLoader.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--8-oneOf-1-2!./node_modules/postcss-loader/src??ref--8-oneOf-1-3!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/sass-loader/dist/cjs.js??ref--8-oneOf-1-4!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--8-oneOf-1-5!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!F:/code/it-akacar/it-akacar-ui/it-akacar-ui-customer/App.vue?vue&type=style&index=0&lang=scss& ***!
  \**********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin
    if(false) { var cssReload; }
  

/***/ })

},[[0,"common/runtime","common/vendor"]]]);
//# sourceMappingURL=../../.sourcemap/mp-weixin/common/main.js.map