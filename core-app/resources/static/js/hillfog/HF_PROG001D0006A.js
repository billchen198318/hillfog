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
		clearInitiative		:	clearInitiativeItem
	},
	mounted() {
		this.addKeyResult();
		this.addInitiative();
	}
}

function addKeyRes() {
	this.keyResultList.push({
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
	console.log('HF_PROG001D0006A appUnmount');
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
