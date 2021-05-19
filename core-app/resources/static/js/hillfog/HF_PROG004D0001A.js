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
	
	},
	mounted() {
	
	}
}

const app = Vue.createApp(PageEventHandling);
var vm = app.mount('#main-content');
