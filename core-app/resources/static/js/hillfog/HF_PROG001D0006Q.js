var objectiveList = [];
const PageEventHandling = {
	data() {
		return {
			objectives	:	objectiveList
		}
	},
	methods: {
		deleteObjective		:	deleteObjectiveItem,
		editObjective		:	editObjectiveItem,
		queryObjectives		:	queryObjectiveList,
		setObjectives		:	setObjectiveList,
		clearObjectives		:	clearObjectiveList,
		enMeasure4Objective	:	enterMeasure4ObjectiveItem
	},
	mounted() {
		this.queryObjectives();
	}
}

function deleteObjectiveItem(oid) {
	alert('DEL: ' + oid);
}

function enterMeasure4ObjectiveItem(oid) {
	window.location = parent.getProgUrlForOid('HF_PROG001D0006M', oid);
}

function editObjectiveItem(oid)	{
	window.location = parent.getProgUrlForOid('HF_PROG001D0006E', oid);
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
		
}

function queryObjectiveList() {
	xhrSendParameter(
		'./hfOkrBaseQueryJson', 
		{ 
			'startDate'		:	$("#date1").val(),
			'endDate'		:	$("#date2").val(),
			'ownerAccount'	:	$("#objOwner").val(),
			'departmentId'	:	$("#objOrg").val()		
		}, 
		this.setObjectives, 
		this.clearObjectives,
		_qifu_defaultSelfPleaseWaitShow
	);		
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
