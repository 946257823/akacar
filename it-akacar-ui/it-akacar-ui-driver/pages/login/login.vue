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
		return {
			driver_login_type: 1
		};
	},
	methods: {
		// 微信登录
		wxLogin() {
			const _this = this;
			wx.login({
				success(res) {
					if(res.code) {
						_this.post("/uaa/app/login/wxLogin/" + res.code + "/" + _this.driver_login_type,(resp) => {
							if(resp.success) {
								uni.showToast({
									icon:"success",
									title:"登录成功!",
									duration:2000
								})
								let {tokenName, tokenValue, permission, loginfo} = resp.data;
								// 保存Token到LocalStorage
								uni.setStorageSync("tokenName",tokenName);
								
								uni.setStorageSync("tokenValue",tokenValue);
								// 保存权限信息到LocalStorage
									uni.setStorageSync("permission",permission);
								// 保存用户信息到LocalStorage
								uni.setStorageSync("loginfo",loginfo);
								// 设置一个2000毫秒后执行的定时器
								setTimeout(function() {
								    uni.switchTab({
								    	url:"/pages/workbench/workbench"
								    })
								}, 2000);
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
		
		toRegisterPage() {
			uni.navigateTo({
				url:'/pages/register/register'
			})
		}
	},
	onLoad: function() {
	}
};
</script>

<style lang="less">
@import url('login.less');
</style>
