const PageEventHandling = {
	data() {
		return {
			scorecolorsDefault	:	[],
			scorecolorsCustom	:	[],
			rangeMax			:	9999999,
			rangeMin			:	-9999999
		}
	},
	methods: {	
		queryScorecardColors	:	queryScorecardColorsItem,
		setQueryData			:	setScorecardColorData,
		clearPage				:	clearEditPage,
		removeColor				:	removeColorItem
	},
	mounted() {
		this.queryScorecardColors();
	}
}

function queryScorecardColorsItem() {
	xhrSendParameter(
			'./hfScorecardQuerColorDataJson', 
			{ 'oid' : _oid }, 
			this.setQueryData, 
			this.clearPage,
			_qifu_defaultSelfPleaseWaitShow
	);		
}

function setScorecardColorData(data) {
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
		return;
	}		
	this.scorecolorsDefault = data.value.defaultData;
	this.scorecolorsCustom = data.value.customData;
	
	console.log(this.scorecolorsDefault);
	console.log(this.scorecolorsCustom);
	
}

function clearEditPage() {
	appUnmount();
	window.location = parent.getProgUrlForOid('HF_PROG001D0008S', _oid);
}

function removeColorItem(index) {
	var that = this;
	parent.bootbox.confirm(
		"Remove this color item?", 
		function(result) { 
			if (!result) {
				return;
			}
			removeArrayByPos(that.scorecolorsCustom, index);	
		}
	);		
}


function appUnmount() {
	app.unmount();
	console.log('HF_PROG001D0008S appUnmount');
}



const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');