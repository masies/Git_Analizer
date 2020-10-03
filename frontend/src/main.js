require('bootstrap');

import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import Paginate from 'vuejs-paginate'
import 'bootstrap/dist/css/bootstrap.css';
import 'material-design-icons/iconfont/material-icons.css';
import anchorme from "anchorme";

Vue.use(require('vue-moment'));
Vue.component('paginate', Paginate)

const files = require.context('./', true, /\.vue$/i)
files.keys().map(key => Vue.component(key.split('/').pop().split('.')[0], files(key).default))


window._ = require('lodash');

window.Popper = require('popper.js').default;
window.$ = window.jQuery = require('jquery');

Vue.mixin( {
	methods: {
		textToLinksParser: function (text, owner, repo) {
			text = text.replace(/[@]+[A-Za-z0-9-_]+/g, function(u){
				var username = u.replace("@","");
				return `<a href="https://github.com/${username}" target="_blank">@${username}</a>`
			});
			text = text.replace(/ +[#]+[A-Za-z0-9-_]+/g, function(u){
				var number = u.replace("#","").trim();
				return ` <a href="/repository/${owner}/${repo}/issue/${number}">#${number}</a>`
			});
			return anchorme({input: text, options: {
				attributes: {
					target: "_blank",
				}
			}})
		},
	}
});

Vue.config.productionTip = false;

new Vue({
	router,
	store,
	render: (h) => h(App),
}).$mount('#app');
