<template>
	<view>
		<image src="../../static/login/top3.png" mode="widthFix" class="top"></image>
		<image src="../../static/login/logo.jpg" mode="widthFix" class="logo"></image>
		<view class="desc">
			<text class="name">专业代驾 服务第一</text>
		</view>
		<button class="btn" @tap="wxLogin()">司机登陆</button>
		<view class="register-container">
			没有账号?
			<text class="link" @tap="toRegisterPage()">立即注册</text>
		</view>
		<text class="remark">小程序仅限于飞驰专车代驾使用,登录前请先进行微信注册,注册前请消息阅读注册需求和使用流程</text>
		<u-toast ref="uToast" />
	</view>
</template>

<script>


export default {
	data() {
		return {};
	},
	methods: {
		
	},
	onLoad: function() {
	}
};
</script>

<style lang="less">
@import url('login.less');
</style>
