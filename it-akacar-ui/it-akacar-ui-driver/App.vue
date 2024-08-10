<script>
	


export default {
	data(){
		return {

		};
	},
	onLaunch: function() {
		let _this = this;
		
		//保持屏幕亮着11
		wx.setKeepScreenOn({ keepScreenOn:true }) 
		
	},
	methods:{
		// 初始化司机状态
		let _this = this;
		if(!uni.getStorageSync("workStatus")) {
			uni.setStorageSync("workStatus",_this.Consts.WORK_STATUS.STOP_ACCPET_ORDER)
		}
		
		// 监听司机位置变化，并缓存到redis-GEO
		changeDriverLocation2GEO() {
			const _this = this;
			// 1.监听司机位置变化
			uni.startLocationUpdate();
			uni.onLocationChange(function(res) {
				console.log("App页面监听纬度：" + res.latitude);
				console.log("App页面监听经度：" + res.longitude);
				if (_this.Consts.WORK_STATUS.START_ACCPET_ORDER == uni.getStorageSync("workStatus")) {
					let param = {
						"longitude": res.longitude,
						"latitude": res.latitude
					};
					_this.post("driver/app/driver/locationToGeo", param, (res) => {
						let { success, message } = res;
						if (success) {
							console.log("司机最新位置更新到GEO成功");
						} else {
							uni.showToast({
								icon: "error",
								title: "位置更新失败"
							});
						}
					});
				}
			});
		}
		
	},
	onShow: function() {
		// 每到一个页面就会触发一次
		const _this = this;
		changeDriverLocation2GEO();
	},
	onHide: function() {
	}
};
</script>

<style lang="scss">
@import 'uview-ui/index.scss';
</style>