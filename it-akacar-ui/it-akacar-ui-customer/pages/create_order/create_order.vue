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
		<map
			id="map"
			:longitude="from.longitude"
			:latitude="from.latitude"
			:style="contentStyle"
			scale="14"
			:enable-traffic="false"
			:show-location="true"
			class="map"
			:polyline="polyline"
			:markers="markers"
		></map>

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
				<u-count-down
					ref="uCountDown"
					:timestamp="timestamp"
					:autoplay="false"
					separator="zh"
					:show-hours="false"
					:show-border="true"
					bg-color="#DDF0FF"
					separator-color="#0083F3"
					border-color="#0D90FF"
					color="#0D90FF"
					font-size="32"
					@end="countDownCancelHandle"
					@change="countChangeHandle"
				></u-count-down>
				
			</view>
			<button class="btn" @tap="cancelHandle">取消订单</button>
		</u-popup>
		
		<u-top-tips ref="uTips"></u-top-tips>
		<u-modal v-model="uModalShow" 
		show-confirm-button="继续等待"
		show-cancel-button="确定取消"
		@confirm="confirm" ref="uModal" :async-close="true"></u-modal>

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
			car:{
				//乘客选择的车辆
				carId: null,
				carPlate: null,
				carType: null,
			},
			showCar: false,
			showPopup: false,
			timestamp: 60,
			timestampStaring:false,
			orderNo: null,
			//地图组件
			map:null,
			//接单的定时任务
			acceptOrderTimer:null,
			uModalShow:false,
			//是否有进行中的订单
			inPorcessOrderId:null
		};
	},
	methods: {
		//倒计时改变
		countChangeHandle(timestamp){
			let _this = this;
			//5秒一次，检查订单是否已经被接单
			if(timestamp % 5 == 0 || timestamp == 2){
				let _this = this;
				
				_this.get("/order/app/order/inprocess/customer",(res)=>{
					let {success , data} = res.data;
					if(success && data){
						//订单号赋值
						_this.orderNo = data.orderNo;
						
						//如果有进行中的订单，跳转 创建订单页
						if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_ACCEPTED ||
						   data.status == _this.Consts.OrderStatus.ORDER_STATUS_ARRIVE ||
						   data.status == _this.Consts.OrderStatus.ORDER_STATUS_START_DRIVING ){
						    //订单未接单，跳转创建页
						    _this.showPopup = false;
							_this.timestamp = 0;
						    //_this.$refs.uCountDown.stop();
						    //接单 ，已到达，代驾中
							//跳转司乘同显页面
							uni.navigateTo({
								url:"/pages/move/move?orderNo="+_this.orderNo
							})
						}
						
					}
					
				});
			}
			
		},
		//倒计时取消订单
		countDownCancelHandle(){
			let _this = this;
			_this.post("/order/app/order/cancel/auto/"+_this.orderNo,(res)=>{
				let {success, message} = res.data;
				if(success){
					uni.showToast({
						title:"订单自动取消",icon:"success"
					})
					_this.showPopup = false;
					_this.$refs.uCountDown.stop();
				}else{
					uni.showToast({
						title:"订单自动取消失败:"+message,icon:"error"
					})
				}
			});
		},
		//加载乘客进行中的订单
		loadInProcessOrder(){
			let _this = this;
			
			_this.get("/order/app/order/inprocess/customer",(res)=>{
				let {success , data} = res.data;
				if(success && data){
					//订单号赋值
					_this.orderNo = data.orderNo;
					
					//如果有进行中的订单，跳转 创建订单页
					if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_WAIT){ 
						//订单未接单，跳转创建页
						_this.showPopup = true;
						_this.$refs.uCountDown.start();
					}else if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_ACCEPTED ||
							data.status == _this.Consts.OrderStatus.ORDER_STATUS_ARRIVE ||
							data.status == _this.Consts.OrderStatus.ORDER_STATUS_START_DRIVING ){
						//接单 ，已到达，代驾中
						//跳转司乘同显页面
						uni.navigateTo({
							url:"/pages/move/move?orderNo="+_this.orderNo
						})
					}else{
						uni.navigateTo({
							url:"/pages/order/order?orderNo="+_this.orderNo
						})
					}
					
				}
				
			});
		},
		//乘客取消订单
		cancelHandle(){
			let _this = this;
			_this.post("/order/app/order/cancel/customer/"+_this.orderNo,(res)=>{
				let {success, message} = res.data;
				if(success){
					uni.showToast({
						title:"订单已取消",icon:"success"
					})
					_this.showPopup = false;
					_this.$refs.uCountDown.stop();
				}else{
					uni.showToast({
						title:"取消失败:"+message,icon:"error"
					})
				}
			});
		},
		//乘客下单
		createOrderHandle(){
			let _this = this;
			let parma = {
				//开始位置信息
				startPlace: _this.from.address,
				startPlaceLongitude:_this.from.longitude,
				startPlaceLatitude:_this.from.latitude,
				//结束位置信息
				endPlace:_this.to.address,
				endPlaceLongitude:_this.to.longitude,
				endPlaceLatitude:_this.to.latitude,
				//车辆信息
				carPlate:_this.car.carPlate,
				carType:_this.car.carType,
				//距离，时长
				expectsMileage:_this.distance,
				expectMinutes:_this.duration
			}
			//下单请求
			_this.post("/order/app/order/create",parma,(res)=>{
				let {success,message,data} = res.data;
				console.log(success,data);
				if(success){
					//订单号保存
					_this.orderNo = data;
					_this.showPopup = true;
					_this.timestamp = 60;
					_this.$refs.uCountDown.start();
				}
			});
		},
		//选车车辆
		chooseCarHandle(){
			let _this = this;
			uni.navigateTo({
				url:"/pages/car_list/car_list"
			})
		},
		//初始化窗口样式
		initStyle(){
			let _this = this;
			//处理窗口样式
			let windowHeight = uni.getSystemInfoSync().windowHeight;
			_this.windowHeight = windowHeight;
			_this.contentStyle = `height:${_this.windowHeight}px;`;
		},
		//初始化地图
		initMap(){
			let _this = this;
			//腾讯地图
			qqmapsdk = new QQMapWX({
			    key: _this.Consts.QQMAP_KEY
			}); 
			//uniapp地图
			_this.map = uni.createMapContext("map", this);
		},
		//画线
		drawLine(){
			let _this = this;
			//调用距离计算接口
			qqmapsdk.direction({
			  //可选值：'driving'（驾车）、'walking'（步行）、'bicycling'（骑行），不填默认：'driving',可不填
			  mode: 'driving',
			  //from参数不填默认当前地址
			  from: {
				  longitude: _this.from.longitude,
				  latitude: _this.from.latitude
			  },
			  to: {
				 longitude: _this.to.longitude,
				 latitude: _this.to.latitude 
			  },
			  success: function (res) {
				
				var ret = res;
				var coors = ret.result.routes[0].polyline, pl = [];
				//距离 ： 千米
				let distance = ret.result.routes[0].distance / 1000.0;
				//时长 
				let duration = ret.result.routes[0].duration;
				_this.distance = distance;
				_this.duration = duration;
				
				
				//坐标解压（返回的点串坐标，通过前向差分进行压缩）
				var kr = 1000000;
				for (var i = 2; i < coors.length; i++) {
				  coors[i] = Number(coors[i - 2]) + Number(coors[i]) / kr;
				}
				//将解压后的坐标放入点串数组pl中
				for (var i = 0; i < coors.length; i += 2) {
				  pl.push({ latitude: coors[i], longitude: coors[i + 1] })
				}
				
				//设置polyline属性，将路线显示出来,将解压坐标第一个数据作为起点
				_this.polyline = [{
					points:pl,	//路线坐标点-数组
					width:4, //线条宽度
					color:"#ff0000" , //线条颜色
					arrowLine:true	//带箭头的线
				}];
				//在地图上画 - 开始，结束图标
				_this.markers = [
					{
						id:1,
						latitude:_this.from.latitude,
						longitude:_this.from.longitude,
						title:"起点",
						iconPath:"https://mapapi.qq.com/web/lbs/javascriptGL/demo/img/start.png",
						width:26,
						height:35
					},
					{
						id:2,
						latitude:_this.to.latitude,
						longitude:_this.to.longitude,
						title:"终点",
						iconPath:"https://mapapi.qq.com/web/lbs/javascriptGL/demo/img/end.png",
						width:26,
						height:35
					}
				]
			  },
			  fail: function (error) {
				console.error(error);
			  },
			  complete: function (res) {
				console.log(res);
			  }
			});
		}
	},
	onLoad: function(options) {
		let _this = this;
		_this.initStyle();
		_this.initMap();
		//拿到位置信息
		_this.from = uni.getStorageSync("from");
		_this.to = uni.getStorageSync("to");
		
		//路线规划
		_this.drawLine();
	},
	onShow: function() {
		let _this = this;
		//回填车辆
		let car = uni.getStorageSync("car");
		if(car){
			_this.showCar = true;
			_this.car = car;
		}
		_this.loadInProcessOrder();
	},
	onUnload() {
	}
};
</script>

<style lang="less">
@import url('create_order.less');

</style>
