const PageEventHandling = {
	data() {
		return {
			perspectives	:	[],
			tabs			:	0,
			currSelTabNum	:	0
		}
	},
	methods: {
		init			:	initPerspectives,
		setCurrSelTab	:	setCurrentSelectTabNum
	},
	mounted() {
		this.init();
	}
}

function initPerspectives() {
	var p = ['Financial', 'Customer', 'Internal business processes', 'Learning and growth'];
	for (var n in p) {
		this.perspectives.push({
			'name'					:	p[n],
			'strategyObjectives'	:	[],
			'weight'				:	'0',
			'numTab'				:	n+''
		});		
	}
	this.tabs += p.length;
}

function setCurrentSelectTabNum(index) {
	this.currSelTabNum = index;
}

function appUnmount() {
	app.unmount();
	console.log('HF_PROG001D0008A appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');