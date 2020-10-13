import Vue from 'vue';
import Vuex from 'vuex';
import languagesColor from './languagesColor.json'

Vue.use(Vuex);

export default new Vuex.Store({
	state: {
		languagesColor: languagesColor		
	},
	mutations: {
	},
	actions: {
	},
	modules: {
	},
	getters: {
		getLanguageColorByName: (state) => (name) => {
			return state.languagesColor[name];
		}
	}
});
