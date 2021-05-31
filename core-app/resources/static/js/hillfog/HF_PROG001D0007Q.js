const PageEventHandling = {
	data() {
		return {
			objectives	:	[]
		}
	},
	methods: {
		queryObjectives		:	queryObjectiveList,
		setObjectives		:	setObjectiveList,
		clearObjectives		:	clearObjectiveList,
		viewObjectiveDetail	:	viewObjectiveDetailItem,
		progressDiv			:	progressDivFormatter
	},
	mounted() {
		this.queryObjectives();
	}
}

function progressDivFormatter(val) {
	return `
		<div class="progress">
			<div class="progress-bar bg-info" role="progressbar" style="width: ${val}%" aria-valuenow="${val}" aria-valuemin="0" aria-valuemax="100"></div>
		</div>
	`;
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
}

function queryObjectiveList() {
	xhrSendParameter(
		'./hfOkrReportQueryJson', 
		{ 
			'ownerAccount'	:	currUserId
		}, 
		this.setObjectives, 
		this.clearObjectives,
		_qifu_defaultSelfPleaseWaitShow
	);		
}

function viewObjectiveDetailItem(oid) {
	parent.addTab('HF_PROG003D0001Q01D', parent.getProgUrlForOid('HF_PROG003D0001Q01D', oid));
}

function appUnmount() {
	app.unmount();
	console.log('HF_PROG001D0007Q appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
