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
		<!-- <button class="btn" @tap="wxLogin" >司机注册</button> -->
		<button class="btn" open-type="getPhoneNumber" @getphonenumber="phoneRegister" >微信注册</button>
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
			cityName:"成都"
		};
	},
	onLoad(){
		let _this = this;
	
	},
	methods: {
		
		
	}
};
</script>

<style lang="less">
@import url('register.less');
</style>
