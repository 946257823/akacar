<template>
	<view>
		<!-- 一.未实名认证，显示实名提示 -->
		<view class="review-container" v-show="!realAuthSuccess">
			<image src="../../static/workbench/review3.png" mode="widthFix"></image>
			<view class="title">您的实名资料还未认证审核通过</view>
			<view class="desc">这期间您将无法接单，请等待资料审核通过</view>
			<view class="toRealAuth">
				<button class="btn"  @click="toRealAuth" >我要认证</button>
			</view>
		</view>
		<!-- 二.实名成功，显示司机的统计数据 -->
		<view v-show="realAuthSuccess">
			<view class="top-container">
				<view class="notice">今日数据：凌晨00:00 ~ 本日23:59</view>
				<view class="summary-container">
					<view class="item">
						<view class="title">代驾时长</view>
						<view class="value">
							<text class="number">{{ summary.driveDuration }}</text>
							<text class="unit">时</text>
						</view>
					</view>
					<view class="item">
						<view class="title">今日收入</view>
						<view class="value">
							<text class="number">{{ summary.todayIncome }}</text>
							<text class="unit">元</text>
						</view>
					</view>
					<view class="item">
						<view class="title">今日成单</view>
						<view class="value">
							<text class="number">{{ summary.todayTradeOrders }}</text>
							<text class="unit">单</text>
						</view>
					</view>
				</view>
				<view class="service-container">
					<view class="item" :style="service.locationStyle">
						<image :src="service.locationIcon" mode="widthFix" class="icon icon-1"></image>
						{{ service.locationText }}
					</view>
					<view class="item" :style="service.messageStyle">
						<image :src="service.messageIcon" mode="widthFix" class="icon icon-2"></image>
						{{ service.messageText }}
					</view>
					<view class="item" :style="service.listenStyle" @tap="listenHandle()">
						<image :src="service.listenIcon" mode="widthFix" class="icon icon-3"></image>
						{{ service.listenText }}
					</view>
					<view class="item" :style="service.settingStyle" @tap="settingHandle()">
						<image :src="service.settingIcon" mode="widthFix" class="icon icon-4"></image>
						{{ service.settingText }}
					</view>
				</view>
			</view>
			<!-- 三.停止接单 -->
			<view v-show="workStatus == '停止接单'">
				<map id="map" :longitude="location.longitude" :latitude="location.latitude" 
				scale="15" :style="contentStyle" :enable-traffic="true" 
				:show-location="true" :enable-poi="true" style="width: 100%;">
					<cover-image class="location" src="../../static/workbench/location.png" @tap="returnLocationHandle()"></cover-image>
				</map>
			</view>
			<!-- 四.显示开始接单 -->
			<scroll-view scroll-y="true" :style="contentStyle" v-show="workStatus == '开始接单'">
				<view class="new-order">
					<view v-if="newOrder == null">
						<image src="../../static/workbench/no-order.png" mode="widthFix" class="no-order-img"></image>
						<view class="no-order-title">目前暂无订单</view>
					</view>
					
					<!-- 五.拉取到订单，执行抢单 -->
					<view v-if="newOrder != null">
						<view class="line-1">
							<view>
								<view class="sub-time">距离您</view>
								<view>
									<text class="number">{{ newOrder.distance }}</text>
									<text class="unit">公里</text>
								</view>
							</view>
							<view>
								<view class="sub-time">全程</view>
								<view>
									<text class="number">{{ newOrder.mileage }}</text>
									<text class="unit">公里</text>
								</view>
							</view>
							<view>
								<view class="tag">含加价{{ newOrder.favourFee }}元</view>
								<view>
									<text class="number">{{ newOrder.expectsFee }}</text>
									<text class="unit">元</text>
								</view>
							</view>
						</view>
						<view class="line-2">
							<view class="from">
								<text>{{ newOrder.from }}</text>
							</view>
							<view class="dashed-line"></view>
							<view class="to">
								<text>{{ newOrder.to }}</text>
							</view>
						</view>
						<button class="accept-btn" 		@tap="killOrderHandle">立即抢单</button>
						<button class="disabled-btn" 	@tap="ignoreHandle">拒绝接单</button>
					
					</view>
				</view>
				<view class="notice-container">
					<view class="title">【 抢单说明 】</view>
					<view class="desc">
						<text class="num">1.</text>
						当前地区可能存在多个代驾订单，但是司机端只能显示其中一条订单。这是为了保障代驾乘客订单的接单率，避免代驾司机只挑选优质订单。
					</view>
					<view class="desc">
						<text class="num">2.</text>
						某条订单如果10秒钟之内您没有抢单，系统则认定您主动放弃该订单，系统将为你推送新的订单。
					</view>
				</view>
			</scroll-view>
			<!-- 六.抢到订单，显示订单详情 -->
			<scroll-view scroll-y="true" :style="contentStyle" class="execute-order-container"
			 v-show="['接客户', '到达代驾点', '开始代驾'].includes(workStatus)">
				<view class="call" @tap="callCustomerHandle"></view>
				<view class="customer-container">
					<u-avatar :src="executeOrder.customerPhoto" mode="square"></u-avatar>
					<view class="info">
						<view class="customer-name">代驾客户（{{ executeOrder.customerName }}）</view>
						<view class="customer-phone">phone：{{ executeOrder.customerPhone }}</view>
					</view>
				</view>
				<view class="address-container">
					<view class="from">
						<text>{{ executeOrder.startPlace }}</text>
					</view>
					<view class="dashed-line"></view>
					<view class="to">
						<text>{{ executeOrder.endPlace }}</text>
					</view>
				</view>
				<view class="order-container">
					<view>【 订单号码 】 {{ executeOrder.orderNo }}</view>
					<view>【 下单时间 】 {{ executeOrder.createTime }}</view>
					<view>【 客户红包 】 {{ executeOrder.favourFee }}元</view>
					<view>【 代驾车型 】 {{ executeOrder.carType }}</view>
					<view>【 代驾车牌 】 {{ executeOrder.carPlate }}</view>
				</view>
				<view class="operate-container">
					<view class="item">
						<image src="../../static/workbench/alarm-icon2.png" mode="widthFix" class="alarm-icon"></image>
						<text class="alarm-text">立即报警</text>
					</view>
				
					<view class="vline"></view>
					<view class="item" v-show="workStatus == '接客户'" @tap="arriveStartPlaceHandle">
						<image src="../../static/workbench/arrive-start-icon.png" mode="widthFix" class="arrive-start-icon"></image>
						<text class="arrive-start-text">到达代驾点</text>
					</view>
					<view class="item" v-show="workStatus == '到达代驾点'" @tap="startDrivingHandle">
						<image src="../../static/workbench/drive-start-icon.png" mode="widthFix" class="drive-start-icon"></image>
						<text class="drive-start-text">开始代驾</text>
					</view>
					<!-- <button class="item btn" v-show="workStatus == '开始代驾'"  open-type="getPhoneNumber" @getphonenumber="endDrivingHandle">-->
					
					<view class="item" v-show="workStatus == '开始代驾'" @tap="endDrivingHandle">
						<image src="../../static/workbench/drive-stop-icon.png" mode="widthFix" class="drive-stop-icon"></image>
						<text class="drive-stop-text">结束代驾</text>
					</view>
					<view class="vline"></view>
					<view class="item" @tap="cancelOrderHandle">
						<image src="../../static/workbench/cancel-icon.png" mode="widthFix" class="cancel-icon"></image>
						<text class="cancel-text">取消订单</text>
					</view>
				</view>
				<view class="other-container">
					<view class="item" @tap="showNavigationHandle">
						<image src="../../static/workbench/other-icon-a.png" mode="widthFix" class="location-icon"></image>
						<text class="location-text">定位导航</text>
					</view>
					<view class="item" @tap="showMoveHandle">
						<image src="../../static/workbench/other-icon-b.png" mode="widthFix" class="display-icon"></image>
						<text class="display-text">司乘同显</text>
					</view>
					<view class="item" @tap="showOrderHandle">
						<image src="../../static/workbench/other-icon-c.png" mode="widthFix" class="order-icon"></image>
						<text class="order-text">订单详情</text>
					</view>
					<view class="item" @tap="callServiceHandle">
						<image src="../../static/workbench/other-icon-d.png" mode="widthFix" class="service-icon"></image>
						<text class="service-text">客服热线</text>
					</view>
				</view>
			</scroll-view>
			
			<!-- 七.开始接单和停止接单 -->
			<view class="bottom-container" v-show="['开始接单', '停止接单'].includes(workStatus)">
				<view class="btn" @tap="gohomeHandle()">
					<image src="../../static/workbench/btn-icon-1.png" mode="widthFix" class="btn-icon-1"></image>
					<text>回家</text>
				</view>
				<view class="btn primary">
					<text v-show="workStatus == '停止接单'" @tap="startWorkHandle()">开始接单</text>
					<text v-show="workStatus == '开始接单'" @tap="stopWorkHandle()">停止接单</text>
				</view>
				<view class="btn" @tap="rechargeHandle()">
					<image src="../../static/workbench/btn-icon-2.png" mode="widthFix" class="btn-icon-2"></image>
					<text>充值</text>
				</view>
			</view>
		</view>
		<u-top-tips ref="uTips"></u-top-tips>
		<u-toast ref="uToast" />
		<u-modal
			v-model="model.cancelConfirm.show"
			:title="model.cancelConfirm.title"
			:content="model.cancelConfirm.content"
			:show-confirm-button="true"
			:show-cancel-button="true"
			@confirm="confirmCancelOrder"
		></u-modal>
		<u-modal
			v-model="model.cancelSuccess.show"
			:title="model.cancelSuccess.title"
			:content="model.cancelSuccess.content"
			:show-confirm-button="true"
			:show-cancel-button="false"
		></u-modal>
	</view>
</template>

<script>
import {BitStateUtil} from "utils/bitstate.js"
import GtPush from '../../lib/gtpush-min.js'
//录音
let wechatSIPlugin = requirePlugin('WechatSI');
let dayjs = require('dayjs');

//腾讯地图插件
let QQMapWX = require('../../lib/qqmap-wx-jssdk.min.js');

let qqmapsdk;
export default {
	data() {
		return {
			getui:{
				//个推初始化
				appid: 'PpBUrcNGiL9fnExoJeCsQ5',
				cid: '',
				online: false,
				messages: '',
			},
			//是否实名认证成功
			realAuthSuccess: false,
			//司机的今日结算数据
			summary:{
				//驾龄
				driveDuration: 0,
				//今日收入
				todayIncome: 0,
				//订单数量
				todayTradeOrders: 0,
			},
			//司机当前定位，用户展示地图中心位子
			location:{
				latitude:"",
				longitude:"",
			},
			//地图控件
			map: null,
			//屏幕窗口高度
			windowHeight: 0,
			contentStyle: '',
			//服务图标和文本
			service: {
				locationIcon: '../../static/workbench/service-icon-1.png',
				locationText: '定位正常',
				locationStyle: '',
				messageIcon: '../../static/workbench/service-icon-2.png',
				messageText: '推送正常',
				messageStyle: '',
				listenIcon: '../../static/workbench/service-icon-3.png',
				listenText: '收听订单',
				listenStyle: '',
				settingIcon: '../../static/workbench/service-icon-4.png',
				settingText: '接单设置',
				settingStyle: ''
			},
			//司机的设置
			settings: {
				listenService: true,
				autoAccept: false
			},
			//司机当前的工作状态
			workStatus: uni.getStorageSync('workStatus') || "停止接单",
			//新订单，当前展示
			newOrder: null,
			//订单列表，所有订单
			newOrderList: [],
			//当前执行中的订单
			accpeting:false,
			executeOrder: {
				orderNo: '',
				customerPhoto: '',
				customerName: '',
				customerPhone: '',
				customerId: '',
				
				startPlace: '',
				startLongitude: '',
				startLatitude: '',
				
				endPlace: '',
				endLongitude: '',
				endLatitude: '',
				
				favourFee: '',
				carPlate: '',
				carType: '',
				createTime: ''
			},
			//是否播放语音
			playFlag: false,
			//语音播放器
			voicePlayer: null,
			//显示弹窗
			showModel: false,
			modelTitle: '',
			modelContent: '',
			model: {
				cancelConfirm: {
					show: false,
					title: '提示消息',
					content: '如果不满足订单免费取消规则，只能有偿取消订单，并且可能被禁止接单一段时间，您确认要取消当前订单？'
				},
				cancelSuccess: {
					show: false,
					title: '提示消息',
					content: ''
				}
			},
			//录音相关
			recordNum: 0,
			recordManager: null,
			stopRecord: false,
			//轮询获取订单的定时器
			pullOrderTimer:null
		};
	},
		
	methods: {
		// 语音播报
		playVoice(src) {
			const innerAudioContext = uni.createInnerAudioContext();
			innerAudioContext.autoplay = true;
			innerAudioContext.src = src;
			innerAudioContext.play();
		} ,
		
	
		// 开始接单
		startWorkHandle() {
			const _this = this;
			_this.post("/driver/app/driver/online" , (res) => {
				let {succes, message} = res;
				if(!success) {
					uni.showToast({
						icon:"error",
						title:"上线失败"
					})
				}
			})
			uni.setStorageSync("workStatus", _this.Consts.WORK_STATUS.START_ACCPET_ORDER);
			_this.workStatus._this.Consts.WORK_STATUS.START_ACCPET_ORDER;
			_this.playVoice("/static/voice/voice_1.mp3")
		},
		
		// 停止接单
		stopWorkHandle() {
			const _this = this;
			_this.post("/driver/app/driver/offline" , (res) => {
				let {succes, message} = res;
				if(!success) {
					uni.showToast({
						icon:"error",
						title:"下线失败"
					})
				}
			})
			uni.setStorageSync("workStatus", _this.Consts.WORK_STATUS.STOP_ACCPET_ORDER);
			_this.workStatus._this.Consts.WORK_STATUS.STOP_ACCPET_ORDER;
			_this.playVoice("/static/voice/voice_2.mp3")
		},
		
		
		// 回到当前位置
		returnLocationHandle() {
			const _this = this;
			_this._mapContext.moveToLocaltion();
		},
		

		//初始化窗口样式
		initStyle(){
			let _this = this;
			//处理窗口样式
			let windowHeight = uni.getSystemInfoSync().windowHeight;
			_this.windowHeight = windowHeight-200;
			_this.contentStyle = `height:${_this.windowHeight}px;`;
		},
		
		
		toRealAuth() {
			uni.navigateTo({
				 //跳转实名页面，把司机ID传递过去
				url:"/identity/filling/filling"
			})
		},
		
		getDriverRealAuthInfo() {
			const _this = this;
			_this.get("/driver/app/driver/realAuthInfo", (res) => {
				let {success, message, data} = res;
			
				if(success) {
					if(data != null && data.realAuthStatus == 1) {
						_this.realAuthSuccess = true;
					}else {
						uni.setStorageSync("realAuthSuccess", data);
						_this.realAuthSuccess = false;
					}
				}else {
					uni.showToast({
						icon:"error",
						title:message
					})
				}
			})
		},
		
		getDriverDaySummery() {
			const _this = this;
			_this.get("/driver/app/driver/daySummary", (res) => {
				let {success, message, data} = res;
				if(success) {
					_this.summary = data
				}else {
					uni.showToast({
						icon:"error",
						title:message
					})
				}
			})
		},
		
		// 初始化地图
		initMap() {
			const _this = this;
			// 1.创建map对象
			this._mapContext = uni.createMapContext("Map", this)
			// 2.设置初始经纬度
			uni.getLocation({
				type: 'wgs84',
				success: function (res) {
					console.log('当前位置的经度：' + res.longitude);
					console.log('当前位置的纬度：' + res.latitude);
					_this.location.longitude = res.longitude;
					_this.location.latitude = res.latitude;
				},
			})
		},
		
		onLoad: function() {
			//加载进行中的订单
			let _this = this;
			_this.initStyle();
			
		},
		onShow: function() {
			// 查询
			let _this = this;
			_this.getDriverRealAuthInfo();
			
			_this.getDriverDaySummery();
			
			_this.initMap();
		},
		onHide: function() {
			
		}
	}
};
</script>

<style lang="less">
@import url('workbench.less');
</style>
