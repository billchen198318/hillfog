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
		planOwnerUidChange	:	planOwnerUidSelectChange,
		removePlanOwnerItem	:	removePlanOwner
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
		'currentSelect'	:	_qifu_please_select_id,
		'ownerList'		:	[]
	});
}

function planOwnerUidSelectChange(planListIndex, event) {
	var owner = event.target.value;
	if ( _qifu_please_select_id == owner ) {
		return;
	}
	var found = false;
	for (var n in this.planList[planListIndex].ownerList) {
		if ( owner == this.planList[planListIndex].ownerList[n] ) {
			found = true;
		}
	}
	if (!found) {
		this.planList[planListIndex].ownerList.push( owner );
		//console.log('push owner:' + owner);
	}
	this.planList[planListIndex].currentSelect = _qifu_please_select_id;
}

function removePlanOwner(planIndex, ownerIndex) {
	removeArrayByPos(this.planList[planIndex].ownerList, ownerIndex);
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
