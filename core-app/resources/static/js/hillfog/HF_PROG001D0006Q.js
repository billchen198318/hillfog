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
	var that = this;
	parent.bootbox.confirm(
			"Delete?", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
					'./hfOkrBaseDeleteJson', 
					{ 
						'oid'	:	oid	
					}, 
					function(data) {
						if ( _qifu_success_flag != data.success ) {
							parent.notifyWarning( data.message );
						} else {
							parent.notifyInfo( data.message );
						}
						that.queryObjectives();
					}, 
					that.queryObjectives,
					_qifu_defaultSelfPleaseWaitShow
				);
			}
	);			
}

function enterMeasure4ObjectiveItem(oid) {
	appUnmount();
	window.location = parent.getProgUrlForOid('HF_PROG001D0006M', oid);
}

function editObjectiveItem(oid)	{
	appUnmount();
	window.location = parent.getProgUrlForOid('HF_PROG001D0006E', oid);
}

function setObjectiveList(data) {
	if ( _qifu_success_flag != data.success ) {
		parent.notifyWarning( data.message );
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

function appUnmount() {
	app.unmount();
	console.log('HF_PROG001D0006Q appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
