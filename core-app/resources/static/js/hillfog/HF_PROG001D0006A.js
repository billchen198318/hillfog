var keyResultItemList = [];
const PageEventHandling = {
	data() {
		return {
			keyResultList  : keyResultItemList
		}
	},
	methods: {
		addKeyResult	: addKeyRes,
		removeKeyResult	: removeKeyRes
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

const app = Vue.createApp(PageEventHandling);
app.mount('#main-content');
