const PageEventHandling = {
	data() {
		return {
			planList	:	[],
			doList		:	[],
			checkList	:	[],
			actList		:	[]
		}
	},
	methods: {
		removePlanResult	:	removePlan,
		addPlanItem			:	addPlan,
		planOwnerUidChange	:	planOwnerUidSelectChange
	},
	mounted() {
		this.addPlanItem();
	}
}

function removePlan(index) {
	removeArrayByPos(this.planList, index);
}

function addPlan() {
	this.planList.push({
		'name'			:	'',
		'startDate'		:	'',
		'endDate'		:	'',
		'description'	:	'',
		'ownerList'		:	[]
	});
}

function planOwnerUidSelectChange(planListIndex, event) {
	alert('planListIndex='+planListIndex + ' , ' + event.target.value);
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
