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
		clearObjectives		:	clearObjectiveList
	},
	mounted() {
		this.queryObjectives();
	}
}

function deleteObjectiveItem(oid) {
	alert('DEL: ' + oid);
}

function editObjectiveItem(oid)	{
	alert('EDIT: ' + oid);
}

function setObjectiveList(data) {
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
	}	
	this.objectives = data.value;
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
			{ }, 
			this.setObjectives, 
			this.clearObjectives,
			_qifu_defaultSelfPleaseWaitShow
	);		
}

const app = Vue.createApp(PageEventHandling);
app.mount('#main-content');
