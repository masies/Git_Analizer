<template>
	<div class="container mt-3 mb-3" v-if="data">
		<div class="row">
			<div class="col card w-100 mt-2">
				<div class="card-body">
					<div class="card-title">
						<h5 class="d-inline-block">
							<router-link :to="{name: 'repository', params: { owner: repository.owner.login, name: repository.name }}">	
								{{ repository.owner.login }}/{{ repository.name }}
							</router-link>
						</h5>
						<button class="btn btn-primary btn-sm float-right" @click="updateRepository" :disabled="showStatusBar">
							<i class="material-icons align-middle md-18">cached</i>
						</button>
					</div>
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
						<div class="col text-center">
							<i class="material-icons align-middle">history</i>
							<span class="align-middle"> {{ data.totalCommits }}</span>
						</div>
					</div>
					<hr>
					<p class="card-text">
						{{ repository.description }}
					</p>

					<div class="row">
						<div class="col">
							<router-link class="btn btn-primary btn-sm w-100" role="button" :to="{name: 'expertiseListContainer', params: { owner: repository.owner.login, name: repository.name }}">	
								Developers expertise
							</router-link>
						</div>	
						<div class="col">
							<router-link class="btn btn-primary btn-sm w-100" role="button" :to="{name: 'pullRequestStatsListContainer', params: { owner: repository.owner.login, name: repository.name }}">	
								Developers PR rates 
							</router-link>
						</div>
						<div class="col">
							<router-link class="btn btn-primary btn-sm w-100" role="button" :to="{name: 'tree', params: { owner: repository.owner.login, name: repository.name, '0': '' }}">	
								Contributors Tree
							</router-link>
						</div>	
					</div>
					<hr>
					<div class="row">
						<div class="col mb-3">
							Metrics of the Neural Model trained on this repository
						</div>
						
					</div>
					<div class="row" v-if="data.predictorStats">
						<div class="col text-center">
							{{ formatNumber(data.predictorStats.accuracy) }} <br>
							<b>Accuracy</b>
						</div>
						<div class="col text-center">
							{{ formatNumber(data.predictorStats.precision) }} <br>
							<b>Precision</b>
						</div>
						<div class="col text-center">
							{{ formatNumber(data.predictorStats.recall) }} <br>
							<b>Recall</b> 
						</div>
					</div>
					<div class="row" v-else>
						<div class="col text-center">
							Not enough commits to display data
						</div>
						
					</div>
					
					<status-bar :owner="repository.owner.login" :name="repository.name" v-if="showStatusBar" class="mt-2" @fetchIsComplete="reloadData"/>
				</div>
			</div>
		</div>
		<div class="row mt-3">
			<div class="col-12 col-lg-6">
				<h5 class="mb-0">Issues</h5>
				<hr>
				<issues-list :size="10" :showPagination="false" ref="issuesList"/>
			</div>
			<div class="col-12 col-lg-6 mt-3 mt-lg-0">
				<h5 class="mb-0">Commits</h5>
				<hr>
				<commits-list :size="10" :showPagination="false" ref="commitsList"/>
			</div>
		</div> 
	</div> 
</template>

<script>
	export default {
		data: () => {
			return {
				data: null,
				showStatusBar: false
			}
		},
		mounted(){
			this.loadData();
		},
		methods: {
			loadData: function() {
				fetch(`/api/repo/${this.$route.params.owner}/${this.$route.params.name}`)
				.then(response => {
					return response.json()
				})
				.then(data => this.data = data);
			},
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
				.then(response => {
					return response.json()
				})
				.then(data => console.log(data));
			},
			reloadData: function() {
				this.$refs.issuesList.loadData();
				this.$refs.commitsList.loadData();
			},
			formatNumber: function(val){
				if(isNaN(val)){
					return "";
				}
				return val.toFixed(2);
			}
		},
		computed: {
			repository: function(){
				return this.data.repository
			},
			languageColor: function(){
				return this.$store.getters.getLanguageColorByName(this.repository.language)
			}
		}
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
.material-icons.md-18 { 
	font-size: 18px; 
}
</style>
