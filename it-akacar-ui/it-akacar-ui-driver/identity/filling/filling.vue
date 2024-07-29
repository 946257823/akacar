<template>
	<view class="page">
		<view class="notice">
			<u-icon name="info-circle-fill" color="#AC9146" size="30" top="3" />
			<text>准确填写个人信息，可享受每份代驾订单人身意外险</text>
		</view>
		<view class="credentials-container">
			<view class="credentials">
				<image :src="cardBackground[0]" class="bg"></image>
				<view class="cover">
					<image src="../static/filling/card.png" mode="widthFix" class="card"></image>
					<text class="desc">身份证正面</text>
					<ocr-navigator @onSuccess="scanIdcardFront" certificateType="idCard" :opposite="false">
						<button class="camera"></button>
					</ocr-navigator>
				</view>
			</view>
			<view class="credentials">
				<image :src="cardBackground[1]" class="bg"></image>
				<view class="cover">
					<image src="../static/filling/card.png" mode="widthFix" class="card"></image>
					<text class="desc">身份证背面</text>
					<ocr-navigator @onSuccess="scanIdcardBack" certificateType="idCard" :opposite="true">
						<button class="camera"></button>
					</ocr-navigator>
				</view>
			</view>
			<view class="credentials">
				<image :src="cardBackground[2]" class="bg"></image>
				<view class="cover">
					<image src="../static/filling/card.png" mode="widthFix" class="card"></image>
					<text class="desc">手持身份证</text>
					<button class="camera" @tap="takePhoto('idcardHolding')"></button>
				</view>
			</view>
			<view class="credentials">
				<image :src="cardBackground[3]" class="bg"></image>
				<view class="cover">
					<image src="../static/filling/card.png" mode="widthFix" class="card"></image>
					<text class="desc">驾驶证正面</text>
					<ocr-navigator @onSuccess="scanDrcardFront" certificateType="driverslicense">
						<button class="camera"></button>
					</ocr-navigator>
				</view>
			</view>
			<view class="credentials">
				<image :src="cardBackground[4]" class="bg"></image>
				<view class="cover">
					<image src="../static/filling/card.png" mode="widthFix" class="card"></image>
					<text class="desc">驾驶证背面</text>
					<!-- <ocr-navigator @onSuccess="scanDrcardBack" certificateType="menu">
						<button class="camera"></button>
					</ocr-navigator> -->
					<button class="camera" @tap="takePhoto('drcardBack')"></button>
				</view>
			</view>
			<view class="credentials">
				<image :src="cardBackground[5]" class="bg"></image>
				<view class="cover">
					<image src="../static/filling/card.png" mode="widthFix" class="card"></image>
					<text class="desc">手持驾驶证</text>
					<button class="camera" @tap="takePhoto('drcardHolding')"></button>
				</view>
			</view>
		</view>

		<view class="title">个人信息</view>
		<view class="list">
			<u-cell-group border="false">
				<u-cell-item title="真实姓名" :value="idcard.name" :value-style="style" :arrow="false" />
				<u-cell-item title="性别" :value="idcard.sex" :value-style="style" :arrow="false" />
				<u-cell-item title="生日" :value="idcard.birthday" :value-style="style" :arrow="false" />
				<u-cell-item title="身份证号" :value="idcard.idNumber" :value-style="style" :arrow="false" />
				<u-cell-item title="身份证地址" :value="idcard.idcardAddress.substr(0,10)+'...'" :value-style="style"  />
				<u-cell-item title="身份证有效期" :value="idcard.idcardExpire" :value-style="style" :arrow="false" />
			</u-cell-group>
		</view>
		<view class="title">联系方式</view>
		<view class="list">
			<u-cell-group border="false">
				<u-cell-item title="手机号码" :value="contact.phone" :value-style="style"  @click="enterContent('手机号码', 'phone')" />
				<u-cell-item title="电子信箱" :value="contact.email" :value-style="style" @click="enterContent('电子信箱', 'email')" />
				<u-cell-item
					title="收信地址"
					:value="contact.mailAddress  "
					:value-style="style"
					@click="enterContent('收信地址', 'mailAddress')"
				/>
				<u-cell-item
					title="紧急联系人"
					:value="contact.contactName"
					:value-style="style"
					@click="enterContent('紧急联系人', 'contactName')"
				/>
				<u-cell-item
					title="紧急联系人电话"
					:value="contact.contactPhone"
					:value-style="style"
					@click="enterContent('紧急联系人电话', 'contactPhone')"
				/>
			</u-cell-group>
		</view>
		<view class="title">驾驶证</view>
		<view class="list">
			<u-cell-group border="false">
				<u-cell-item title="驾驶证类型" :value="drcard.carClass" :value-style="style" :arrow="false" />
				<u-cell-item title="驾驶证有效期" :value="drcard.drcardExpire" :value-style="style" :arrow="false" />
				<u-cell-item title="初次领证日期" :value="drcard.drcardIssueDate" :value-style="style" :arrow="false" />
			</u-cell-group>
		</view>

		<!-- <button class="btn" @tap="save" :disabled="realAuthStatus == 3">保存信息</button> -->
		<button class="btn" :disabled="realAuthStatus == 0 || realAuthStatus == 1" @tap="save" >
			<text v-if="realAuthStatus == 0">审核中</text>
			<text v-else-if="realAuthStatus == 1">已通过</text>
			<text v-else>提交实名</text>
		</button>
		<view class="remark">您每次提交实名信息之后，都需要工作人员严格审查，请等候1~3天，这期间您将无法接单，特此声明！</view>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
let dayjs = require('dayjs');
export default {
	data() {
		return {
			realAuthStatus:null,
			mode: 'fill',
			style: {
				color: '#FF9900'
			},
			//证件背景：用作小程序预览
			cardBackground: [
				'../static/filling/credentials-bg.jpg',
				'../static/filling/credentials-bg.jpg',
				'../static/filling/credentials-bg.jpg',
				'../static/filling/credentials-bg.jpg',
				'../static/filling/credentials-bg.jpg',
				'../static/filling/credentials-bg.jpg'
			],
			idcard: {
				//身份证ID
				idNumber: '',
				name: '',
				sex: '',
				idcardAddress: '',
				//地址
				shortAddress: '',
				//生日
				birthday: '',
				//过期时间
				idcardExpire: '',
				//身份证正面:云地址
				idcardFront: '',
				//身份证背面:云地址
				idcardBack: '',
				//手持身份证:云地址
				idcardHolding: ''
			},
			//联系人
			contact: {
				//手机号
				phone: '18244229577',
				//电子邮件
				email: '18244229577@qq.com',
				//短邮箱
				shortEmail: '18244229577@qq.com',
				//收信地址
				mailAddress: '广州市xx区xx乡镇',
				//短地址
				shortMailAddress: '广州市xx区xx乡镇',
				//联系人
				contactName: '阿崔',
				//联系电话
				contactPhone: '18244229577'
			},
			//驾驶证
			drcard: {
				//颁发时间
				drcardIssueDate: '',
				//驾驶证类型
				carClass: '',
				//驾驶证过期时间
				drcardExpire: '',
				//驾驶证正面:云地址
				drcardFront: '',
				//驾驶证背面:云地址
				drcardBack: '',
				//手持驾驶证:云地址
				drcardHolding: ''
			},
			//记录所有云文件地址，用作删除
			allCosImg: [],
			//保存有效的文件地址
			currentCosImg: [],
			realAuth: uni.getStorageSync('realAuth')
		};
	},
	methods: {
		//提交认证材料
		save(){
			let _this = this;
			
		},
		//输入联系人信息
		enterContent(title,field){
			let _this = this;
			uni.showModal({
				title:"请输入 "+title,
				editable:true,
				content:_this.contact[field],
				success(res) {
					_this.contact[field] = res.content;
				}
			})
		},
		//手持身份证拍照
		takePhoto(type){
			//跳转拍照页面
			uni.navigateTo({
				url:"../identity_camera/identity_camera?type="+type
			})
		},
		//拍照页面图片回传
		uploadPhoto(type,photoPath){
			let _this = this;
		
		},
		//驾驶证正面
		scanDrcardFront(res){
			let _this = this;
			let result = res.detail;
		},
		//身份证背面识别
		scanIdcardBack(res){
			let _this = this;
			let result = res.detail;
			
		},
		//身份证识别回调
		scanIdcardFront(res){
			let _this = this;
			let result = res.detail;
		},
		//文件上传
		uploadFile(imagePath,num,callback){
			let _this = this;
		},
		//加载司机的实名信息
		loadDriverAuthMaterial(){
			let _this = this;
		}
	},
	//获取上个页面传入的参数
	onLoad: function(options) {
		console.log(options.driverId);
	},
	onShow() {
		let _this = this;
		_this.loadDriverAuthMaterial();
	}
};
</script>

<style lang="less">
@import url('filling.less');
</style>
