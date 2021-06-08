const PageEventHandling = {
	data() {
		return {
			perspectives	:	[],
			tabs			:	0,
			currSelTabNum	:	0
		}
	},
	methods: {
		init					:	initPerspectives,
		setCurrSelTab			:	setCurrentSelectTabNum,
		addPerspective			:	addPerspectiveItem,
		removeStrategyObjective	:	removeStrategyObjectiveItem,
		addStrategyObjective	:	addStrategyObjectiveItem,
		removePerspective		:	removePerspectiveItem,
		kpisSelChange			:	kpisSelChangeEvent,
		okrsSelChange			:	okrsSelChangeEvent,
		
		removeStrategyObjectiveOwnerKpi	:	removeStrategyObjectiveOwnerKpiItem,
		removeStrategyObjectiveOwnerOkr	:	removeStrategyObjectiveOwnerOkrItem
	},
	mounted() {
		this.init();
	}
}

function initPerspectives() {
	var p = ['Financial', 'Customer', 'Internal business processes', 'Learning and growth'];
	for (var n in p) {
		var so = [];
		so.push({
			'oid'			:	'',
			'name'			:	'',
			'weight'		:	0,
			'kpis'			:	[],
			'okrs'			:	[],
			'currentSelect1':	_qifu_please_select_id,
			'currentSelect2':	_qifu_please_select_id
		});
		this.perspectives.push({
			'oid'					:	'',
			'name'					:	p[n],
			'strategyObjectives'	:	so,
			'weight'				:	25,
			'numTab'				:	n+''
		});		
	}
	this.tabs += p.length;
}

function setCurrentSelectTabNum(index) {
	this.currSelTabNum = index;
}

function addPerspectiveItem() {
	this.tabs = this.tabs + 1;
	var so = [];
	so.push({
		'oid'			:	'',
		'name'			:	'',
		'weight'		:	0,
		'kpis'			:	[],
		'okrs'			:	[],
		'currentSelect1':	_qifu_please_select_id,
		'currentSelect2':	_qifu_please_select_id
	});
	this.perspectives.push({
		'oid'				:	'',
		'name'				:	'New perspective-' + this.tabs,
		'strategyObjectives':	so,
		'weight'			:	0,
		'numTab'			:	this.tabs + ''
	});
}

function removeStrategyObjectiveItem(perspectiveIndex, strategyObjectiveIndex) {
	var per = this.perspectives[perspectiveIndex];
	removeArrayByPos(per.strategyObjectives, strategyObjectiveIndex);
}

function addStrategyObjectiveItem(perspectiveIndex) {
	var per = this.perspectives[perspectiveIndex];
	var so = per.strategyObjectives;
	so.push({
		'oid'			:	'',
		'name'			:	'',
		'weight'		:	0,
		'kpis'			:	[],
		'okrs'			:	[],
		'currentSelect1':	_qifu_please_select_id,
		'currentSelect2':	_qifu_please_select_id
	});	
}

function removePerspectiveItem(perspectiveIndex) {
	var that = this;
	parent.bootbox.confirm(
		"Delete?", 
		function(result) { 
			if (!result) {
				return;
			}
			removeArrayByPos(that.perspectives, perspectiveIndex);
			that.currSelTabNum = 0;
		}
	);	
}

function kpisSelChangeEvent(perspectiveIndex, strategyObjectiveIndex, event) {
	var selVal = event.target.value;
	if ( _qifu_please_select_id == selVal ) {
		return;
	}	
	var per = this.perspectives[perspectiveIndex];
	var so = per.strategyObjectives[strategyObjectiveIndex];
	var found = false;
	for (var n in so.kpis) {
		if ( so.kpis[n].oid == selVal) {
			found = true;
		}
	}
	if (!found) {
		for (var d in kpis) {
			if (kpis[d].oid == selVal) {
				so.kpis.push({'oid' : selVal, 'name' : kpis[d].name});
			}
		}
	}
	so.currentSelect1 = _qifu_please_select_id;
	//console.log(JSON.stringify(so));
}

function okrsSelChangeEvent(perspectiveIndex, strategyObjectiveIndex, event) {
	var selVal = event.target.value;
	if ( _qifu_please_select_id == selVal ) {
		return;
	}	
	var per = this.perspectives[perspectiveIndex];
	var so = per.strategyObjectives[strategyObjectiveIndex];
	var found = false;
	for (var n in so.okrs) {
		if ( so.okrs[n].oid == selVal) {
			found = true;
		}
	}
	if (!found) {
		for (var d in okrs) {
			if (okrs[d].oid == selVal) {
				so.okrs.push({'oid' : selVal, 'name' : okrs[d].name});
			}
		}
	}
	so.currentSelect2 = _qifu_please_select_id;
	//console.log(JSON.stringify(so));
}

function removeStrategyObjectiveOwnerKpiItem(perspectiveIndex, strategyObjectiveIndex, ownerIndex) {
	var per = this.perspectives[perspectiveIndex];
	var so = per.strategyObjectives[strategyObjectiveIndex];
	removeArrayByPos(so.kpis, ownerIndex);
}

function removeStrategyObjectiveOwnerOkrItem(perspectiveIndex, strategyObjectiveIndex, ownerIndex) {
	var per = this.perspectives[perspectiveIndex];
	var so = per.strategyObjectives[strategyObjectiveIndex];
	removeArrayByPos(so.okrs, ownerIndex);
}



function appUnmount() {
	app.unmount();
	console.log('HF_PROG001D0008A appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');