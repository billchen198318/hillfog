var objectiveList = [];
const PageEventHandling = {
	data() {
		return {
			objectives	:	objectiveList
		}
	},
	methods: {
		queryObjectives		:	queryObjectiveList,
		setObjectives		:	setObjectiveList,
		clearObjectives		:	clearObjectiveList,
		viewDetail			:	viewDetailItem
	},
	mounted() {
		this.queryObjectives();
	}
}

function setObjectiveList(data) {
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
	}
	if (data.value != null) {
		this.objectives = data.value;
	} else {
		this.objectives = [];
	}
}

function clearObjectiveList() {
	this.objectives = [];
	$("#date1").val('');
	$("#date2").val('');	
	$("#objOrg").val('');
	$("#objOwner").val('');
	$("#name").val('');
}

function queryObjectiveList() {
	xhrSendParameter(
		'./hfOkrBaseQueryJson', 
		{ 
			'startDate'		:	$("#date1").val(),
			'endDate'		:	$("#date2").val(),
			'ownerAccount'	:	$("#objOwner").val(),
			'departmentId'	:	$("#objOrg").val(),
			'name'			:	$("#name").val()
		}, 
		this.setObjectives, 
		this.clearObjectives,
		_qifu_defaultSelfPleaseWaitShow
	);		
}

function viewDetailItem(oid) {
	alert(oid);
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
