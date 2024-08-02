<template>
	<view class="page">
		<image src="../../static/register/top2.jpg" mode="widthFix" class="top" />
		<view class="location-container">
			<view class="left">
				<image src="../../static/register/location.png" mode="widthFix" class="location"></image>
				<text>代驾服务地点</text>
			</view>
			<view class="right">{{cityName}}</view>
		</view>
		<view class="info-container">
			<view class="title-container">
				<image src="../../static/register/title-bg.png" mode="widthFix" class="title-bg"></image>
				<text class="title">注册条件</text>
			</view>
			<view class="list">
				<view class="item">
					<text>1.</text>
					车辆合规合法；
				</view>
				<view class="item">
					<text>2.</text>
					年龄18岁以上；
				</view>
				<view class="item">
					<text>3.</text>
					无违法犯罪记录、无精神病史、无吸毒史，以及平台认为不适合使用代驾的其他历史证明；
				</view>
				
				
			</view>
		</view>
		<view class="info-container">
			<view class="title-container">
				<image src="../../static/register/title-bg.png" mode="widthFix" class="title-bg"></image>
				<text class="title">使用流程</text>
			</view>
			<view class="list">
				<view class="complex-item">
					<view class="left">01</view>
					<view class="right">
						<text class="item-title">在线注册账号</text>
						<text class="item-desc">在小程序完成注册</text>
					</view>
				</view>
				<view class="complex-item">
					<view class="left">02</view>
					<view class="right">
						<text class="item-title">个人信息填写</text>
						<text class="item-desc">按照流程提交本人身份证照片</text>
					</view>
				</view>
				<view class="complex-item">
					<view class="left">03</view>
					<view class="right">
						<text class="item-title">代驾下单</text>
						<text class="item-desc">通过APP下单预约代驾</text>
					</view>
				</view>
				<view class="complex-item">
					<view class="left">04</view>
					<view class="right">
						<text class="item-title">费用支付</text>
						<text class="item-desc">订单结束自动扣除费用</text>
					</view>
				</view>
			</view>
		</view>
		<button class="btn" @tap="wxRegister()" >用户注册</button>
	<!-- 	<button class="btn" open-type="getPhoneNumber" @getphonenumber="phoneRegister" >微信注册</button> -->
		<u-toast ref="uToast" />
	</view>
</template>

<script>
var QQMapWX = require('../../lib/qqmap-wx-jssdk.min.js');
var qqmapsdk;	
export default {
	data() {
		return {
			code: null,
			cityName:"未知"
		};
	},
	onLoad(){
		// 一个页面只调一次

		// 在函数中有时this的指向会变，变为不是Vue实例对象，所有先保存一份this实例
		const _this = this;
		
		_this.initQQMap();
				
		let location = _this.getLocation();
		
		_this.reverseGeocoder();
	
	},
	methods: {
		// 1.获取经纬度信息
		getLocation() {
			uni.getLocation({
				type: 'wgs84',
				success: function (res) {
					console.log('当前位置的经度：' + res.longitude);
					console.log('当前位置的纬度：' + res.latitude);
					return res;
				}
			})
		},
		initQQMap() {
			qqmapsdk = new QQMapWX({
			    key: 'AFYBZ-7L3CZ-D5MXS-Z66W3-BXG4V-YEBFY'
			})
		},
		
		// 地址逆向解析
		reverseGeocoder(location) {
			const _this = this;
			qqmapsdk.reverseGeocoder({
				location:location,
				success: function(res) {
					if(res.status == 0) {
						_this.cityName = res.result.address_component.city;
					}else {
						uni.showToast({
							icon:"error",
							title:"获取地址失败！"
						})
					}
				}
			})
		},
		
		// 获取登录openId
		wxRegister() {
			const _this = this;
			wx.login({
				success(res) {
					if(res.code) {
						_this.get("/customer/app/customer/register/" + res.code,(resp) => {
							if(resp.success) {
								uni.showToast({
									icon:"success",
									title:"注册成功!",
									duration:2000
								})
								// 设置一个3000毫秒后执行的定时器
								setTimeout(function() {
								    uni.navigateTo({
								    	url:"/pages/login/login"
								    })
								}, 3000);
							}else {
								uni.showToast({
									icon:"error",
									title:resp.message,
								})
							}
						})
					}else {
						console.log('登录失败！' + res.errMsg)
					}
				}
			})
		},
		// 获取手机号
		phoneRegister(e) {
			const _this = this;
			let phoneCode = e.detail.code
			wx.login({
				success(res) {
					if(res.code && phoneCode) {
						_this.get("/customer/app/customer/register/" + res.code + "/" + phoneCode,(resp) => {
							if(resp.success) {
								uni.showToast({
									icon:"success",
									title:"注册成功!",
									duration:2000
								})
								// 设置一个3000毫秒后执行的定时器
								setTimeout(function() {
								    uni.navigateTo({
								    	url:"/pages/login/login"
								    })
								}, 3000);
							}else {
								uni.showToast({
									icon:"error",
									title:resp.message,
								})
							}
						})
					}else {
						console.log('登录失败！' + res.errMsg)
					}
				}
			})
		}
	}
};
</script>

<style lang="less">
@import url('register.less');
</style>
