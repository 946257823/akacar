<template>
	<view>
		<view v-if="inPorcessOrderId != null" class="order-process" @click="toOrder">您有进行中的订单,点击查看</view>
		
		<map id="map" :longitude="from.longitude" :latitude="from.latitude" :style="contentStyle" scale="15" :enable-traffic="false" :show-location="true" :enable-poi="true" class="map">
			<cover-image class="location" src="../../static/workbench/location.png" @tap="toLocationHandle()"></cover-image>
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
let qqmapsdk ;

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
			map: null,
			//标记:开始位置点选或者结束位置点选
			chooseFromOrToflag: null,
			inPorcessOrderId:null,
			chooseType:"",
		};
	},
	methods: {
		
		
	},
	onShow: function() {
		
	},
	onHide: function() {
	},
	onLoad: function() {
		let _this = this;
	
		
	},
	onUnload: function() {
		
	}
};
</script>

<style lang="less">
@import url('workbench.less');
</style>
