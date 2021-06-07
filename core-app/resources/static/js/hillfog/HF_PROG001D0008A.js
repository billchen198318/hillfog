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
		removePerspective		:	removePerspectiveItem
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
			'oid'		:	'',
			'name'		:	'',
			'weight'	:	0,
			'kpis'		:	[],
			'okrs'		:	[]
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
		'oid'	:	'',
		'name'	:	'',
		'weight':	0,
		'kpis'	:	[],
		'okrs'	:	[]
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
		'oid'	:	'',
		'name'	:	'',
		'weight':	0,
		'kpis'	:	[],
		'okrs'	:	[]
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
		}
	);	
}



function appUnmount() {
	app.unmount();
	console.log('HF_PROG001D0008A appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');