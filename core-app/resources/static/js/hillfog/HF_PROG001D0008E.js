const PageEventHandling = {
	data() {
		return {
			perspectives				:	[],
			tabs						:	0,
			currSelTabNum				:	0,
			autoAllocationWeightFlag	:	true
		}
	},
	methods: {
		setCurrSelTab					:	setCurrentSelectTabNum,
		addPerspective					:	addPerspectiveItem,
		removeStrategyObjective			:	removeStrategyObjectiveItem,
		addStrategyObjective			:	addStrategyObjectiveItem,
		removePerspective				:	removePerspectiveItem,
		kpisSelChange					:	kpisSelChangeEvent,
		okrsSelChange					:	okrsSelChangeEvent,		
		removeStrategyObjectiveOwnerKpi	:	removeStrategyObjectiveOwnerKpiItem,
		removeStrategyObjectiveOwnerOkr	:	removeStrategyObjectiveOwnerOkrItem,
		autoAllocationWeight			:	autoAllocationWeightItem,
		
		queryPerspectives				:	queryPerspectivesItem,
		setQueryData					:	setPerspectiveData,
		clearPage						:	clearEditPage		
	},
	mounted() {
		this.queryPerspectives();
	}
}

function queryPerspectivesItem() {
	xhrSendParameter(
			'./hfScorecardQueryEditDataJson', 
			{ 'oid' : _oid }, 
			this.setQueryData, 
			this.clearPage,
			_qifu_defaultSelfPleaseWaitShow
	);		
}

function setPerspectiveData(data) {
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
		return;
	}		
	this.perspectives = data.value;
	this.tabs = this.perspectives.length;	
}

function clearEditPage() {
	appUnmount();
	window.location = parent.getProgUrlForOid('HF_PROG001D0008E', _oid);
}

function fillInitStrategyObjectiveData(so) {
	so.push({
		'oid'			:	'',
		'name'			:	'',
		'weight'		:	0.00,
		'kpis'			:	[],
		'okrs'			:	[],
		'currentSelect1':	_qifu_please_select_id,
		'currentSelect2':	_qifu_please_select_id
	});	
}

function setCurrentSelectTabNum(index) {
	this.currSelTabNum = index;
}

function addPerspectiveItem() {
	this.tabs = this.tabs + 1;
	var so = [];
	fillInitStrategyObjectiveData(so);
	this.perspectives.push({
		'oid'				:	'',
		'name'				:	'New perspective-' + this.tabs,
		'strategyObjectives':	so,
		'weight'			:	0.00,
		'tabNum'			:	this.tabs + ''
	});
	if (this.autoAllocationWeightFlag) {
		this.autoAllocationWeight();
	}
}

function removeStrategyObjectiveItem(perspectiveIndex, strategyObjectiveIndex) {
	var per = this.perspectives[perspectiveIndex];
	removeArrayByPos(per.strategyObjectives, strategyObjectiveIndex);
	if (this.autoAllocationWeightFlag) {
		this.autoAllocationWeight();
	}	
}

function addStrategyObjectiveItem(perspectiveIndex) {
	var per = this.perspectives[perspectiveIndex];
	var so = per.strategyObjectives;
	fillInitStrategyObjectiveData(so);
	if (this.autoAllocationWeightFlag) {
		this.autoAllocationWeight();
	}	
}

function removePerspectiveItem(perspectiveIndex) {
	var that = this;
	parent.bootbox.confirm(
		"Remove this perspective tab?", 
		function(result) { 
			if (!result) {
				return;
			}
			removeArrayByPos(that.perspectives, perspectiveIndex);
			that.currSelTabNum = 0;
			if (that.autoAllocationWeightFlag) {
				that.autoAllocationWeight();
			}			
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
				so.kpis.push({'oid' : selVal, 'name' : kpis[d].name, 'weight' : 0});
			}
		}
		if (this.autoAllocationWeightFlag) {
			this.autoAllocationWeight();
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
	if (this.autoAllocationWeightFlag) {
		this.autoAllocationWeight();
	}	
}

function removeStrategyObjectiveOwnerOkrItem(perspectiveIndex, strategyObjectiveIndex, ownerIndex) {
	var per = this.perspectives[perspectiveIndex];
	var so = per.strategyObjectives[strategyObjectiveIndex];
	removeArrayByPos(so.okrs, ownerIndex);	
}

function autoAllocationWeightItem() {
	if (this.perspectives.length < 1) {
		return;
	}
	var MAX_WEIGHT_VALUE = 100.0;
	var perWeightAvg = MAX_WEIGHT_VALUE / this.perspectives.length;
	var perWeightSum = 0.0;
	var perPointDecimals = 0;
	var perPointDecimalsTmp = (perWeightAvg+"").split(".");
	if (perPointDecimalsTmp.length == 2) {
		perPointDecimals = perPointDecimalsTmp[1].length;
	}
	for (var n = 0; n < this.perspectives.length; n++) {
		var per = this.perspectives[n];
		perWeightSum = perWeightSum + nv2Fixed(perWeightAvg);
		per.weight = nv2Fixed(perWeightAvg);
		if ( (n+1) == this.perspectives.length && perPointDecimals > 2 ) {
			per.weight = nv2Fixed( nv2Fixed(perWeightAvg) + ( MAX_WEIGHT_VALUE - nv2Fixed(perWeightSum) ) );
		}
		
		if (per.strategyObjectives.length < 1) {
			continue;
		}
		var soWeightAvg = MAX_WEIGHT_VALUE / per.strategyObjectives.length;
		var soWeightSum = 0.0;
		var soPointDecimals = 0;
		var soPointDecimalsTmp = (soWeightAvg+"").split(".");
		if (soPointDecimalsTmp.length == 2) {
			soPointDecimals = soPointDecimalsTmp[1].length;
		}
		for (var o = 0; o < per.strategyObjectives.length; o++) {
			var so = per.strategyObjectives[o];
			soWeightSum = soWeightSum + nv2Fixed(soWeightAvg);
			so.weight = nv2Fixed(soWeightAvg);
			if ( (o+1) == per.strategyObjectives.length && soPointDecimals > 2 ) {
				so.weight = nv2Fixed( nv2Fixed(soWeightAvg) + ( MAX_WEIGHT_VALUE - nv2Fixed(soWeightSum) ) );
			}
			
			if (so.kpis.length < 1) {
				continue;
			}
			var kpiWeightAvg = MAX_WEIGHT_VALUE / so.kpis.length;
			var kpiWeightSum = 0.0;
			var kpiPointDecimals = 0;
			var kpiPointDecimalsTmp = (kpiWeightAvg+"").split(".");
			if (kpiPointDecimalsTmp.length == 2) {
				kpiPointDecimals = kpiPointDecimalsTmp[1].length;
			}
			for (var k = 0; k < so.kpis.length; k++) {
				var kpi = so.kpis[k];
				kpiWeightSum = kpiWeightSum + nv2Fixed(kpiWeightAvg);
				kpi.weight = nv2Fixed(kpiWeightAvg);
				if ( (k+1) == so.kpis.length && kpiPointDecimals > 2 ) {
					kpi.weight = nv2Fixed( nv2Fixed(kpiWeightAvg) + ( MAX_WEIGHT_VALUE - nv2Fixed(kpiWeightSum) ) );
				}
			}
			
		}
		
	}
}

function nv2Fixed(val) {
	//return parseFloat(val).toFixed(2);
	var tmp = (val+"").split(".");
	if (tmp.length != 2) {
		return val;
	}
	var decimals = tmp[1];
	if (decimals.length > 2) {
		decimals = decimals.substring(0, 2);
	}
	return parseFloat(tmp[0] + "." + decimals);
}



function appUnmount() {
	app.unmount();
	console.log('HF_PROG001D0008E appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');