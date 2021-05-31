const PageEventHandling = {
	data() {
		return {
			objectives	:	[],
			pdcaOids	:	[],
			kpis		:	[]
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
		paintCurrentPdcaChart	:	queryPdcaChart,
		
		queryKpiChart			:	queryKpiScoreData,
		setKpiChartScoreData	:	setKpiScoreData,
		clearKpiChartScoreData	:	clearKpiScoreData,
		paintCurrentGaugeChart	:	paintGaugeChart,
		toMeasureDataInput		:	toMeasureDataInputTab
		
	},
	mounted() {
		this.queryObjectives();
		this.queryPdcaProjects();
		this.queryKpiChart('3');
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



function queryKpiScoreData(freq) {
	var startDate = startDateM;
	var endDate = endDateM;
	if ('6' == freq) {
		startDate = startDateY;
		endDate = endDateY;
	}
	if ('5' == freq) {
		startDate = startDateH;
		endDate = endDateH;		
	}
	if ('4' == freq) {
		startDate = startDateQ;
		endDate = endDateQ;		
	}		
	currKpiFrequency = freq;
	xhrSendParameter(
		'./hfKpiReportContentDataJson', 
		{
			'noPdca'		:	_qifu_success_flag,
			'checkKpiOwner'	:	_qifu_success_flag,
			'kpiOid'		:	'',
			'date1'			:	startDate,
			'date2'			:	endDate,
			'frequency'		:	freq,
			'kpiEmpl'		:	currUserId
		}, 
		this.setKpiChartScoreData, 
		this.clearKpiChartScoreData,
		_qifu_defaultSelfPleaseWaitShow
	);	
	
}
function setKpiScoreData(data) {
	this.kpis = data.value;
	var ths = this;
	setTimeout(function(){  
		console.log('setTimeout, because document.getElementById will miss!');
		ths.paintCurrentGaugeChart();
	}, 50);
}
function clearKpiScoreData() {
	this.kpis = [];
}
function paintGaugeChart() {
	for (var n in this.kpis) {
		var name = this.kpis[n].kpi.name;
		var freq = this.kpis[n].frequency;
		if ('6' == freq) {
			name += ' (Current Year)';
		}
		if ('5' == freq) {
			name += ' (Current Half of year)';
		}
		if ('4' == freq) {
			name += ' (Current Quarter)';
		}
		if ('3' == freq) {
			name += ' (Current Month)';
		}						
		gaugeChart('gauge_' + this.kpis[n].kpi.oid, name, this.kpis[n].score, 'The completion rate');
	}
}
function toMeasureDataInputTab(oid) {
	parent.addTab('HF_PROG001D0005M', parent.getProgUrlForOid('HF_PROG001D0005M', oid) );
}


function appUnmount() {
	app.unmount();
	console.log('HF_PROG001D0007Q appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
