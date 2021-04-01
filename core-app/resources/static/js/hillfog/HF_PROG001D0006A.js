var keyResultItemList = [];
const PageEventHandling = {
	data() {
		return {
			keyResultList  :	keyResultItemList
		}
	},
	methods: {
		addKeyResult	:	addKeyRes,
		removeKeyResult	:	removeKeyRes,
		clearKeyResult	:	clearKeyRes
	},
	mounted() {
		this.addKeyResult();
	}
}

function addKeyRes() {
	this.keyResultList.push({
		'name'			:	'',
		'target'		:	0,
		'gpType'		:	'1',
		'opTarget'		:	'1',
		'description'	:	''
	});
	
	console.log(this.keyResultList);
}

function removeKeyRes(index) {
	removeArrayByPos(this.keyResultList, index);
}

function clearKeyRes() {
	this.keyResultList = [];
}

const app = Vue.createApp(PageEventHandling);
app.mount('#main-content');
