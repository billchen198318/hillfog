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
		removePlanResult		:	removePlan,
		addPlanItem				:	addPlan,
		planOwnerUidChange		:	planOwnerUidSelectChange,
		removePlanOwnerItem		:	removePlanOwner,
		
		removeDoResult			:	removeDo,
		addDoItem				:	addDo,
		doOwnerUidChange		:	doOwnerUidSelectChange,
		removeDoOwnerItem		:	removeDoOwner,
		
		removeCheckResult		:	removeCheck,
		addCheckItem			:	addCheck,
		checkOwnerUidChange		:	checkOwnerUidSelectChange,
		removeCheckOwnerItem	:	removeCheckOwner,
		
		removeActResult			:	removeAct,
		addActItem				:	addAct,
		actOwnerUidChange		:	actOwnerUidSelectChange,
		removeActOwnerItem		:	removeActOwner
		
	},
	mounted() {
		this.addPlanItem();
		this.addDoItem();
		this.addCheckItem();
		this.addActItem();
	}
}



/* ----------------------------------------------------------------------- */
/* Plan */
/* ----------------------------------------------------------------------- */
function removePlan(index) {
	removeArrayByPos(this.planList, index);
	if (this.planList.length < 1) {
		for (var d in this.doList) {
			this.doList[d].parentOid = _qifu_please_select_id;
		}
	}	
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
	if (this.doList.length < 1) {
		for (var d in this.checkList) {
			this.checkList[d].parentOid = _qifu_please_select_id;
		}
	}	
}

function addDo() {
	this.doList.push({
		'oid'			:	parent.guid(),
		'name'			:	'',
		'startDate'		:	'',
		'endDate'		:	'',
		'description'	:	'',
		'currentSelect'	:	_qifu_please_select_id,
		'ownerList'		:	[],
		'parentOid'		:	_qifu_please_select_id
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



/* ----------------------------------------------------------------------- */
/* Do */
/* ----------------------------------------------------------------------- */
function removeCheck(index) {
	removeArrayByPos(this.checkList, index);
	if (this.checkList.length < 1) {
		for (var d in this.actList) {
			this.actList[d].parentOid = _qifu_please_select_id;
		}
	}	
}

function addCheck() {
	this.checkList.push({
		'oid'			:	parent.guid(),
		'name'			:	'',
		'startDate'		:	'',
		'endDate'		:	'',
		'description'	:	'',
		'currentSelect'	:	_qifu_please_select_id,
		'ownerList'		:	[],
		'parentOid'		:	_qifu_please_select_id
	});
}

function checkOwnerUidSelectChange(checkListIndex, event) {
	var owner = event.target.value;
	if ( _qifu_please_select_id == owner ) {
		return;
	}
	var found = false;
	for (var n in this.checkList[checkListIndex].ownerList) {
		if ( owner == this.checkList[checkListIndex].ownerList[n] ) {
			found = true;
		}
	}
	if (!found) {
		this.checkList[checkListIndex].ownerList.push( owner );
		//console.log('push owner:' + owner);
	}
	this.checkList[checkListIndex].currentSelect = _qifu_please_select_id;
}

function removeCheckOwner(checkIndex, ownerIndex) {
	removeArrayByPos(this.checkList[checkIndex].ownerList, ownerIndex);
}
/* ----------------------------------------------------------------------- */



/* ----------------------------------------------------------------------- */
/* Act */
/* ----------------------------------------------------------------------- */
function removeAct(index) {
	removeArrayByPos(this.actList, index);
}

function addAct() {
	this.actList.push({
		'oid'			:	parent.guid(),
		'name'			:	'',
		'startDate'		:	'',
		'endDate'		:	'',
		'description'	:	'',
		'currentSelect'	:	_qifu_please_select_id,
		'ownerList'		:	[],
		'parentOid'		:	_qifu_please_select_id
	});
}

function actOwnerUidSelectChange(actListIndex, event) {
	var owner = event.target.value;
	if ( _qifu_please_select_id == owner ) {
		return;
	}
	var found = false;
	for (var n in this.actList[actListIndex].ownerList) {
		if ( owner == this.actList[actListIndex].ownerList[n] ) {
			found = true;
		}
	}
	if (!found) {
		this.actList[actListIndex].ownerList.push( owner );
		//console.log('push owner:' + owner);
	}
	this.actList[actListIndex].currentSelect = _qifu_please_select_id;
}

function removeActOwner(actIndex, ownerIndex) {
	removeArrayByPos(this.actList[actIndex].ownerList, ownerIndex);
}
/* ----------------------------------------------------------------------- */



const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
