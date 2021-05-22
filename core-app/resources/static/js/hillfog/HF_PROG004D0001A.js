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
		removePlanOwnerItem	:	removePlanOwner,
		
		removeDoResult		:	removeDo,
		addDoItem			:	addDo,
		doOwnerUidChange	:	doOwnerUidSelectChange,
		removeDoOwnerItem	:	removeDoOwner
	},
	mounted() {
		this.addPlanItem();
		this.addDoItem();
	}
}



/* ----------------------------------------------------------------------- */
/* Plan */
/* ----------------------------------------------------------------------- */
function removePlan(index) {
	removeArrayByPos(this.planList, index);
}

function addPlan() {
	this.planList.push({
		'oid'			:	parent.guid(),
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
/* ----------------------------------------------------------------------- */



/* ----------------------------------------------------------------------- */
/* Do */
/* ----------------------------------------------------------------------- */
function removeDo(index) {
	removeArrayByPos(this.doList, index);
}

function addDo() {
	this.doList.push({
		'oid'			:	parent.guid(),
		'name'			:	'',
		'startDate'		:	'',
		'endDate'		:	'',
		'description'	:	'',
		'currentSelect'	:	_qifu_please_select_id,
		'ownerList'		:	[]
	});
}

function doOwnerUidSelectChange(doListIndex, event) {
	var owner = event.target.value;
	if ( _qifu_please_select_id == owner ) {
		return;
	}
	var found = false;
	for (var n in this.doList[doListIndex].ownerList) {
		if ( owner == this.doList[doListIndex].ownerList[n] ) {
			found = true;
		}
	}
	if (!found) {
		this.doList[doListIndex].ownerList.push( owner );
		//console.log('push owner:' + owner);
	}
	this.doList[doListIndex].currentSelect = _qifu_please_select_id;
}

function removeDoOwner(planIndex, ownerIndex) {
	removeArrayByPos(this.doList[planIndex].ownerList, ownerIndex);
}
/* ----------------------------------------------------------------------- */



const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
