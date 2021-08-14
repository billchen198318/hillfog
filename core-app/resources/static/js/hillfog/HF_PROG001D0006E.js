var keyResultItemList = [];
var initiativesItemList = [];
const PageEventHandling = {
	data() {
		return {
			keyResultList	:	keyResultItemList,
			initiativesList	:	initiativesItemList
		}
	},
	methods: {
		addKeyResult		:	addKeyRes,
		removeKeyResult		:	removeKeyRes,
		clearKeyResult		:	clearKeyRes,
		addInitiative		:	addInitiativeItem,
		removeInitiative	:	removeInitiativeItem,
		clearInitiative		:	clearInitiativeItem,
		queryObjective		:	queryObjectiveItem,
		setQueryData		:	setObjectiveAndInitiatives,
		clearPage			:	clearEditPage
	},
	mounted() {	
		this.queryObjective();
	}
}

function queryObjectiveItem() {
	xhrSendParameter(
			'./hfOkrBaseQueryAllDataJson', 
			{ 'oid' : _oid }, 
			this.setQueryData, 
			this.clearPage,
			_qifu_defaultSelfPleaseWaitShow
	);		
}

function setObjectiveAndInitiatives(data) {
	if ( _qifu_success_flag != data.success ) {
		parent.notifyWarning( data.message );
		return;
	}		
	this.keyResultList = data.value.keyResList;
	this.initiativesList = data.value.initiativeList;
	
}

function clearEditPage() {
	appUnmount();
	window.location = parent.getProgUrlForOid('HF_PROG001D0006E', _oid);
}

function addKeyRes() {
	this.keyResultList.push({
		'oid'			:	'0',
		'name'			:	'',
		'target'		:	'0',
		'gpType'		:	'1',
		'opTarget'		:	'1',
		'description'	:	''
	});
}

function addInitiativeItem() {
	this.initiativesList.push({
		'content'	:	''
	});	
}

function removeKeyRes(index) {
	removeArrayByPos(this.keyResultList, index);
}

function clearKeyRes() {
	this.keyResultList = [];
}

function removeInitiativeItem(index) {
	removeArrayByPos(this.initiativesList, index);
}

function clearInitiativeItem() {
	this.initiativesList = [];
}

function appUnmount() {
	app.unmount();
	console.log('HF_PROG001D0006E appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
