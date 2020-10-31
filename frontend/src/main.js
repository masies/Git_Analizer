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
			// Link issue with hash -> @1234
			var re = new RegExp(`https:\/\/github.com\/${owner}\/${repo}\/(issues|pull)\/[0-9]*`, 'gi')
			text = text.replace(re, function(u){
				var parts = u.split('/');
				var number = parts.pop() || parts.pop();
				return ` <a href="/repository/${owner}/${repo}/issue/${number}">#${number}</a>`
			});

			// Link username -> @myUsername
			text = text.replace(/[@]+[A-Za-z0-9-_]+/g, function(u){
				var username = u.replace("@","");
				return `<a href="https://github.com/${username}" target="_blank">@${username}</a>`
			});
			// Link issue with hash -> @1234
			text = text.replace(/ +[#]+[A-Za-z0-9-_]+/g, function(u){
				var number = u.replace("#","").trim();
				return ` <a href="/repository/${owner}/${repo}/issue/${number}">#${number}</a>`
			});

			// Link commit hash (40 chars) -> 0123456789012345678901234567890123456789
			text = text.replace(/^\b[0-9a-f]{40}\b/g, function(id){
				return ` <a href="/repository/${owner}/${repo}/commit/${id}">${id}</a>`
			});
			// General links
			return anchorme({input: text, options: {
				attributes: {
					target: "_blank",
				}
			}})
		},
	},
	computed: {
		queryString: function(){
			var tmp = this.$route.query;
			delete tmp.page;
			delete tmp.size;
			return'?'+Object.keys(this.$route.query).map((key) => {
				return key + '=' +  encodeURIComponent(this.$route.query[key]);
			}).join('&');
		}
	}

});

Vue.config.productionTip = false;

new Vue({
	router,
	store,
	render: (h) => h(App),
}).$mount('#app');
