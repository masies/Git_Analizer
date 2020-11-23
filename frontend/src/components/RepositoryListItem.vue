<template>
	<div class="card w-100 mt-2 shadow-sm">
		<div class="card-body">
			<h5 class="card-title">
				<router-link :to="{name: 'repository', params: { owner: repository.owner.login, name: repository.name }}">	
					{{ repository.owner.login }}/{{ repository.name }}
				</router-link>
			</h5>
			<h6 class="card-subtitle mb-2 text-muted" v-if="repository.language">
				<span class="repo-language-color" :style="{'background-color': languageColor.color}"></span>
				{{ repository.language }}
			</h6>
			<div class="row">
				<div class="col text-center">
					<a :href="repository.htmlUrl" target="_blank">
						<img src="/images/GitHub-Mark-32px.png" style="width: 16px;">
					</a>
				</div>
				<div class="col text-center">
					<svg height="16" class="octicon octicon-star" viewBox="0 0 16 16" version="1.1" width="16" aria-hidden="true"><path fill-rule="evenodd" d="M8 .25a.75.75 0 01.673.418l1.882 3.815 4.21.612a.75.75 0 01.416 1.279l-3.046 2.97.719 4.192a.75.75 0 01-1.088.791L8 12.347l-3.766 1.98a.75.75 0 01-1.088-.79l.72-4.194L.818 6.374a.75.75 0 01.416-1.28l4.21-.611L7.327.668A.75.75 0 018 .25zm0 2.445L6.615 5.5a.75.75 0 01-.564.41l-3.097.45 2.24 2.184a.75.75 0 01.216.664l-.528 3.084 2.769-1.456a.75.75 0 01.698 0l2.77 1.456-.53-3.084a.75.75 0 01.216-.664l2.24-2.183-3.096-.45a.75.75 0 01-.564-.41L8 2.694v.001z"></path></svg>
					{{ repository.watchers }}
				</div>
				<div class="col text-center">
					<svg class="octicon octicon-repo-forked" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M5 3.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm0 2.122a2.25 2.25 0 10-1.5 0v.878A2.25 2.25 0 005.75 8.5h1.5v2.128a2.251 2.251 0 101.5 0V8.5h1.5a2.25 2.25 0 002.25-2.25v-.878a2.25 2.25 0 10-1.5 0v.878a.75.75 0 01-.75.75h-4.5A.75.75 0 015 6.25v-.878zm3.75 7.378a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm3-8.75a.75.75 0 100-1.5.75.75 0 000 1.5z"></path></svg>
					{{ repository.forks }}
				</div>
			</div>
			<hr>

			<p class="card-text">
				{{ repository.description }}
			</p>
			<div class="row">
				<div class="col-12 text-center">
					<button class="btn btn-primary btn-sm" @click="updateRepository" :disabled="showStatusBar">
						<i class="material-icons align-middle md-18">cached</i>
					</button>
				</div>
			</div>
			
			<status-bar :owner="repository.owner.login" :name="repository.name" v-if="showStatusBar" class="mt-2"/>
		</div>
	</div>
</template>

<script>
	export default {
		props: {
			data: {
				type: Object,
				default: null
			}
		},
		data: () => {
			return {
				showStatusBar: false
			}
		},
		computed: {
			repository: function(){
				return this.data.repository
			},
			languageColor: function(){
				return this.$store.getters.getLanguageColorByName(this.repository.language)
			}
		},
		methods: {
			updateRepository: function(){
				this.showStatusBar = true;
				fetch("/api/repo",
				{
					method: 'POST',
					headers: {
						'Content-Type': 'application/json',
					},
					body: JSON.stringify({
						owner: this.repository.owner.login,
						name: this.repository.name
					}),
				})
			}
		},
		mounted(){
		},
	};
</script>
<style scoped>
.repo-language-color{
	position: relative;
	top: 1px;
	display: inline-block;
	width: 12px;
	height: 12px;
	border-radius: 50%;
}
</style>