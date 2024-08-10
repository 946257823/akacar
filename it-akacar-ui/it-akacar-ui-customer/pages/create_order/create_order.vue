<template>
	<view>
		<view class="address-container">
			<view class="from">
				<text>{{ from.address }}</text>
			</view>
			<view class="dashed-line"></view>
			<view class="to">
				<text>{{ to.address }}</text>
			</view>
		</view>
		<view v-if="inPorcessOrderId != null" class="order-process" @click="toInProcessOrder">您有进行中的订单,点击查看</view>
		<map id="map" :longitude="from.longitude" :latitude="from.latitude" :style="contentStyle" scale="14"
			:enable-traffic="false" :show-location="true" class="map" :polyline="polyline" :markers="markers"></map>

		<view class="panel">
			<view class="row">
				<view class="info">
					<view class="label">全程</view>
					<view class="value">
						<text>{{ distance }}</text>
						公里
					</view>
					<view class="label">，预计</view>
					<view class="value">
						<text>{{ duration }}</text>
						分钟
					</view>
				</view>
				<view class="opt" @tap="chooseCarHandle" v-if="!showCar">选择车辆</view>
				<view class="opt" @tap="chooseCarHandle" v-if="showCar">{{ car.carType }}{{ car.carPlate }}</view>
			</view>
			<button class="btn" @tap="createOrderHandle">呼叫代驾</button>
		</view>
		<u-popup v-model="showPopup" mode="center" width="600" height="280" :mask-close-able="false">
			<view class="popup-title">您的订单正在等待司机接单</view>
			<view class="count-down">
				<u-count-down ref="uCountDown" :timestamp="timestamp" :autoplay="false" separator="zh"
					:show-hours="false" :show-border="true" bg-color="#DDF0FF" separator-color="#0083F3"
					border-color="#0D90FF" color="#0D90FF" font-size="32" @end="countDownCancelHandle"
					@change="countChangeHandle"></u-count-down>

			</view>
			<button class="btn" @tap="cancelHandle">取消订单</button>
		</u-popup>

		<u-top-tips ref="uTips"></u-top-tips>
		<u-modal v-model="uModalShow" show-confirm-button="继续等待" show-cancel-button="确定取消" @confirm="confirm"
			ref="uModal" :async-close="true"></u-modal>

	</view>
</template>

<script>
	let QQMapWX = require('../../lib/qqmap-wx-jssdk.min.js');
	let qqmapsdk;
	export default {
		data() {
			return {
				//订单路线：开始位置和结束位子
				from: {
					address: '',
					longitude: 0,
					latitude: 0
				},
				to: {
					address: '',
					longitude: 0,
					latitude: 0
				},
				//窗口样式
				contentStyle: '',
				windowHeight: 0,
				//距离和时长
				distance: 0,
				duration: 0,
				//路线规划，路线坐标和图标
				polyline: [],
				markers: [],

				//
				infoStatus: true,
				car: {
					//乘客选择的车辆
					carId: null,
					carPlate: null,
					carType: null,
				},
				showCar: false,
				showPopup: false,
				timestamp: 60,
				timestampStaring: false,
				orderNo: null,
				//地图组件
				map: null,
				//接单的定时任务
				acceptOrderTimer: null,
				uModalShow: false,
				//是否有进行中的订单
				inPorcessOrderId: null
			};
		},
		methods: {
			
			// 呼叫代驾
			createOrderHandle() {
				const _this = this;
				let param = {
					"start_place": _this.from.address,
					"start_place_latitude": _this.from.latitude,
					"start_place_longitute": _this.from.longitute,
					"end_place": _this.to.address,
					"end_place_latitude": _this.to.latitude,
					"end_place_longitute": _this.to.longitute,
					"expect_minutes": _this.duration,
					"expects_mileage": _this.distance,
					"car_plate": _this.car.carPlate,
					"car_type": _this.car.carType
				}
				
				_this.post("order/app/order/createOrder")
			},
			
			
			
			chooseCarHandle() {
				uni.navigateTo({
					url:"/pages/car_list/car_list"
				})
			},
			
			
			
			showCarHandler() {
				
			},
			

			// 画线方法
			drawLine() {
				const _this = this;
				//调用距离计算接口
				qqmapsdk.direction({
					mode: 'driving', //可选值：'driving'（驾车）、'walking'（步行）、'bicycling'（骑行），不填默认：'driving',可不填
					//from参数不填默认当前地址
					from: _this.from,
					to: _this.to,
					success: function(res) {
						console.log(res);
						var ret = res;
						var coors = ret.result.routes[0].polyline,

							_this.distance = (ret.result.routes[0].distance / 1000).toFixed(2);
						_this.duration = ret.result.routes[0].duration;

						pl = [];
						//坐标解压（返回的点串坐标，通过前向差分进行压缩）
						var kr = 1000000;
						for (var i = 2; i < coors.length; i++) {
							coors[i] = Number(coors[i - 2]) + Number(coors[i]) / kr;
						}
						//将解压后的坐标放入点串数组pl中
						for (var i = 0; i < coors.length; i += 2) {
							pl.push({
								latitude: coors[i],
								longitude: coors[i + 1]
							})
						}
						console.log(pl)
						//设置polyline属性，将路线显示出来,将解压坐标第一个数据作为起点
						_this.polyline = [{
							points: p1,
							color: "#0000AA",
							// dottedLine: true,
							// arrowLine: true,
							// arrowIconPath: true,
							width: 4
						}];

						_this.markers = [{
							id: 1,
							latitude: _this.from.latitude,
							longitude: _this.from.longitude,
							iconPath: '/static/order/start.png'
						}, {
							id: 2,
							latitude: _this.to.latitude,
							longitude: _this.to.latitude,
							iconPath: '/static/order/end.png'
						}]
					},
					fail: function(error) {
						console.error(error);
					},
					complete: function(res) {
						console.log(res);
					}
				});
			}


			//初始化窗口样式
			initStyle() {
				let _this = this;
				//处理窗口样式
				let windowHeight = uni.getSystemInfoSync().windowHeight;
				_this.windowHeight = windowHeight;
				_this.contentStyle = `height:${_this.windowHeight}px;`;
			},
			//初始化地图
			initMap() {
				let _this = this;
				//腾讯地图
				qqmapsdk = new QQMapWX({
					key: _this.Consts.QQMAP_KEY
				});
				//uniapp地图
				_this.map = uni.createMapContext("map", this);
			},

		},
		onLoad: function(options) {
			let _this = this;
			_this.initStyle();
			_this.initMap();
		},
		onShow: function() {
			let _this = this;
			_this.from = uni.getStorageSync("from");
			_this.to = uni.getStorageSync("to");
			_this.drawLine();
			_this.showCarHandler();
		},
		onUnload() {}
	};
</script>

<style lang="less">
	@import url('create_order.less');
</style>