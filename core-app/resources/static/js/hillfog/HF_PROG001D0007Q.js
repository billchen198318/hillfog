const PageEventHandling = {
	data() {
		return {
			objectives	:	[],
			pdcaOids	:	[]
		}
	},
	methods: {
		queryObjectives			:	queryObjectiveList,
		setObjectives			:	setObjectiveList,
		clearObjectives			:	clearObjectiveList,
		viewObjectiveDetail		:	viewObjectiveDetailItem,
		progressDiv				:	progressDivFormatter,
		
		queryPdcaProjects		:	queryPdcaProjectList,
		setPdcaProjects			:	setPdcaProjectList,
		clearPdcaProjects		:	clearPdcaProjectList,
		viewPdcaDetail			:	viewPdcaDetailItem,
		paintCurrentPdcaChart	:	qeruyPdcaChart
	},
	mounted() {
		this.queryObjectives();
		this.queryPdcaProjects();
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



function setPdcaProjectList(data) {
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
	}
	if (data.value != null) {
		this.pdcaOids = data.value;
	} else {
		this.pdcaOids = [];
	}
}

function clearPdcaProjectList() {
	this.pdcaOids = [];
}

function queryPdcaProjectList() {
	xhrSendParameter(
		'./hfPdcaOidListForOwnerDataJson', 
		{ 
			'ownerAccount'	:	currUserId
		}, 
		this.setPdcaProjects, 
		this.clearPdcaProjects,
		_qifu_defaultSelfPleaseWaitShow
	);		
}

function viewPdcaDetailItem(pdcaOid) {
	parent.addTab('HF_PROG004D0001V', parent.getProgUrlForOid('HF_PROG004D0001V', pdcaOid));
}



function appUnmount() {
	app.unmount();
	console.log('HF_PROG001D0007Q appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
