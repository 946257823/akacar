<template>
	<view>
		<view v-if="inPorcessOrderId != null" class="order-process" @click="toOrder">您有进行中的订单,点击查看</view>

		<map id="map" :longitude="from.longitude" :latitude="from.latitude" :style="contentStyle" scale="15"
			:enable-traffic="false" :show-location="true" :enable-poi="true" class="map">
			<cover-image class="location" src="../../static/workbench/location.png"
				@tap="toLocationHandle()"></cover-image>
		</map>
		<view class="panel">
			<view class="from" @tap="chooseLocationHandle('from')">
				<text>{{ from.address }}</text>
			</view>
			<view class="dashed-line"></view>
			<view class="to" @tap="chooseLocationHandle('to')">
				<text>{{ to.address }}</text>
			</view>
			<button class="btn" @tap="toCreateOrderHandle">查看路线</button>
		</view>
	</view>
</template>

<script>
	import GtPush from '../../lib/gtpush-min.js'
	//地图点选插件
	const chooseLocation = requirePlugin('chooseLocation');
	let QQMapWX = require('../../lib/qqmap-wx-jssdk.min.js');
	let qqmapsdk;

	export default {
		data() {
			return {
				//开始位置
				from: {
					address: '输入你的开始位置',
					longitude: 0,
					latitude: 0
				},
				//结束位置
				to: {
					address: '输入你的目的地',
					longitude: 0,
					latitude: 0
				},

				//窗口样式
				contentStyle: '',
				windowHeight: 0,
				//地图对象
				_mapContext: null,
				//标记:开始位置点选或者结束位置点选
				chooseFromOrToflag: null,
				inPorcessOrderId: null,
				chooseType: "",
			};
		},
		methods: {


			initQQMap() {
				qqmapsdk = new QQMapWX({
					key: 'AFYBZ-7L3CZ-D5MXS-Z66W3-BXG4V-YEBFY'
				})
			},

			//初始化窗口样式
			initStyle() {
				let _this = this;
				//处理窗口样式
				let windowHeight = uni.getSystemInfoSync().windowHeight;
				_this.windowHeight = windowHeight - 200;
				_this.contentStyle = `height:${_this.windowHeight}px;`;
			},

			// 初始化地图
			initMap() {
				const _this = this;
				// 1.创建map对象
				this._mapContext = uni.createMapContext("Map", this)
				// 2.设置初始经纬度
				uni.getLocation({
					type: 'wgs84',
					success: function(res) {
						console.log('当前位置的经度：' + res.longitude);
						console.log('当前位置的纬度：' + res.latitude);
						_this.from.longitude = res.longitude;
						_this.from.latitude = res.latitude;
						// 根据经纬度得到当前位置信息(地址逆向解析)
						_this.reverseGeocoder(_this.from);
					},
				})
			},

			// 地址逆向解析
			reverseGeocoder(location) {
				const _this = this;
				qqmapsdk.reverseGeocoder({
					location: location,
					success: function(res) {
						if (res.status == 0) {
							_this.from.address = res.result.formatted_addresses.standard_address;
						} else {
							uni.showToast({
								icon: "error",
								title: "获取地址失败！"
							})
						}
					}
				})
			},


			// 回到当前位置
			toLocationHandle() {
				const _this = this;
				_this._mapContext.moveToLocaltion();
			}


			// 查看路线
			toCreateOrderHandle() {
				const _this = this;
				if (_this.to.address != "输入你的目的地") {
					uni.setStorageSync("from", _this.from)
					uni.setStorageSync("to", _this.to)
					uni.navigateTo({
						url: "/pages/create_order/create_order"
					})
				} else {
					uni.showToast({
						icon: "error",
						title: "请选择位置后再查看路线！"
					})
				}

			},


			// 选择位置
			chooseLocationHandle(type) {
				this.chooseType = type;
				const key = 'AFYBZ-7L3CZ-D5MXS-Z66W3-BXG4V-YEBFY'; //使用在腾讯位置服务申请的key
				const referer = 'akacar代驾'; //调用插件的app的名称
				const category = '生活服务,娱乐休闲';
				wx.navigateTo({
					url: 'plugin://chooseLocation/index?key=' + key + '&referer=' + referer + '&category=' +
						category
				});
			}


		},
		onShow: function() {
			const _this = this;
			// 获取地图选点结果
			// 从地图选点插件返回后，在页面的onShow生命周期函数中能够调用插件接口，取得选点结果对象
			const location = chooseLocation.getLocation(); // 如果点击确认选点按钮，则返回选点结果对象，否则返回null
			if (location) {
				console.log(location);
				if (_this.chooseType == "from") {
					_this.from.address = location.address;
					_this.from.latitude = location.latitude;
					_this.from.longitude = location.longitude;
				} else if (_this.chooseType == "to") {
					_this.to.address = location.address;
					_this.to.latitude = location.latitude;
					_this.to.longitude = location.longitude;
				}
			}
		},
		onHide: function() {},
		onLoad: function() {
			let _this = this;
			_this.initStyle();
			_this.initQQMap();
			_this.initMap();
		},
		onUnload: function() {
			// 页面卸载时设置插件选点数据为null，防止再次进入页面，geLocation返回的是上次选点结果
			chooseLocation.setLocation(null);
		}
	};
</script>

<style lang="less">
	@import url('workbench.less');
</style>