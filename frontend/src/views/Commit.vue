<template>
	<div class="container mt-3 mb-3">
		<div v-if="isLoading" class="row">
			<div class="col-12 text-center">
				<div class="spinner-border text-primary" role="status"  style="width: 3rem; height: 3rem;">
					<span class="sr-only">Loading...</span>
				</div>
			</div>
			<div class="col-12 text-center">
				<h3>Computing metrics...</h3>
			</div>
		</div>
		<div class="card" v-if="!isLoading && commit">
			<div class="card-header">
				<h3 class="d-inline mr-1">{{ commit.shortMessage }}</h3>
			</div>
			<div class="card-header" v-if="commit.linkedFixedIssues.length > 0">
				<div class="row">
					<div class="col-12">
						Related issues: <span v-for="(issue, i) in commit.linkedFixedIssues">
							<router-link class="text-decoration-none" :to="{name: 'issue', params: {owner: commit.owner, name: commit.repo, id: issue}}">
								#{{ issue }}<span v-if="i < commit.linkedFixedIssues.length-1">,</span>
							</router-link>
						</span>
					</div>
					
				</div>
			</div>
			<div class="card-header" v-if="commit.bugInducingCommits">
				<div class="row">
					<div class="col-12">
						This commit fixes bugs induced by: 
						<ul>
							<li v-for="commitBug in commit.bugInducingCommits">
								<router-link class="text-decoration-none" :to="{name: 'commit', params: {owner: commit.owner, name: commit.repo, id: commitBug.commitName}}">
									{{ commitBug.commitName }}
								</router-link>
								developed by <a :href="'https://github.com/'+commitBug.developerName" target="_blank">{{ commitBug.developerName }}</a> on {{ formatDate(commitBug.commitDate) }}
							</li>
						</ul>
						
					</div>
					
				</div>
			</div>
			<div class="card-header" v-if="commit.bugInducing">
				<div class="row">
					<div class="col-12">
						This commit induces bugs fixed by: 
						<ul>
							<li v-for="commitBug in commit.bugFixingCommits">
								<router-link class="text-decoration-none" :to="{name: 'commit', params: {owner: commit.owner, name: commit.repo, id: commitBug.commitName}}">
									{{ commitBug.commitName }}
								</router-link>
								developed by <a :href="'https://github.com/'+commitBug.developerName" target="_blank">{{ commitBug.developerName }}</a> on {{ formatDate(commitBug.commitDate) }}
							</li>
						</ul>
						
					</div>
					
				</div>
			</div>
			<div class="card-header" v-if="hasDeepMetrics">
				<div class="row text-center">
					<div class="col">
						{{ metrics.loc.toFixed(2) }} 
						<span>
							<span v-if="changeLOC > 0">▴</span>
							<span v-else-if="changeLOC < 0">▾</span>
							<span v-else>-</span>{{changeLOC}}%
						</span>
						<br>
						<span title="Lines Of Code" data-toggle="tooltip" data-placement="bottom"><b>LOC</b></span>
					</div>
					<div class="col">
						{{ metrics.cbo.toFixed(2) }} 
						<span :class="{'text-danger': changeCBO > 0, 'text-success': changeCBO < 0}">
							<span v-if="changeCBO > 0">▴</span>
							<span v-else-if="changeCBO < 0">▾</span>
							<span v-else>-</span>{{ changeCBO }}%
						</span>
						<br>
						<span title="Coupling between Objects" data-toggle="tooltip" data-placement="bottom"><b>CBO</b></span>
					</div>
					<div class="col">
						{{ metrics.wmc.toFixed(2) }} 
						<span :class="{'text-danger': changeWMC > 0, 'text-success': changeWMC < 0}">
							<span v-if="changeWMC > 0">▴</span>
							<span v-else-if="changeWMC < 0">▾</span>
							<span v-else>-</span>{{ changeWMC }}%
						</span>
						<br>
						<span title="Weighted Methods for Class" data-toggle="tooltip" data-placement="bottom"><b>WMC</b></span>
					</div>
					<div class="col">
						{{ metrics.lcom.toFixed(2) }} 
						<span :class="{'text-danger': changeLCOM > 0, 'text-success': changeLCOM < 0}">
							<span v-if="changeLCOM > 0">▴</span>
							<span v-else-if="changeLCOM < 0">▾</span>
							<span v-else>-</span>{{ changeLCOM }}%
						</span>
						<br>
						<span title="Lack of Cohesion in Methods" data-toggle="tooltip" data-placement="bottom"><b>LCOM</b></span>
					</div>
				</div>
			</div>
			<div class="card-header" v-else>
				<div class="row text-center">
					<div class="col">
						<button class="btn btn-primary btn-sm" @click="loadDataDeep" v-if="!isLoadingDeepMetrics">Compute project metrics</button>
						<button class="btn btn-primary btn-sm loading" v-else="isLoadingDeepMetrics" disabled>Computing project metrics</button>
					</div>
				</div>
			</div>
			<div class="card-body">
				<p  class="mb-0" v-html="parsedMessage" v-if="commit.fullMessage != ''"></p>
			</div>
			<div class="card-footer text-muted">
				Commited {{ humanReadableDate }} 
				<span class="float-right" v-if="parentCommit">parent 
					<router-link class="text-decoration-none" :to="{name: 'commit', params: {owner: commit.owner, name: commit.repo, id: parentCommit}}">
						{{ parentCommit }}
					</router-link>
				</span>
			</div>
		</div>
		<div v-if="!isLoading && commit">
			<commit-diff-item v-for="diff in commit.modifications" :data="diff" class="mt-3" :key="diff.newPath" />
		</div>
	</div>
</template>

<script>
	export default {
		data: () => {
			return {
				commit: null,
				isLoading: true,
				isLoadingDeepMetrics: false,
			}
		},
		mounted(){
			this.loadData();
		},
		methods: {
			loadData: function() {
				this.isLoading = true;
				fetch(`/api/repo/${this.$route.params.owner}/${this.$route.params.name}/commits/${this.$route.params.id}`)
				.then(response => {
					return response.json()
				})
				.then(data => {
					this.commit = data
					this.isLoading = false;
					this.$nextTick(function () {
						$('[data-toggle="tooltip"]').tooltip()
					})

				});
			},
			loadDataDeep: function() {
				this.isLoadingDeepMetrics = true;
				fetch(`/api/repo/${this.$route.params.owner}/${this.$route.params.name}/commits/${this.$route.params.id}?mode=deep`)
				.then(response => {
					return response.json()
				})
				.then(data => {
					this.commit = data
					this.$nextTick(function () {
						$('[data-toggle="tooltip"]').tooltip()
					})
					this.isLoadingDeepMetrics = false;

				});
			},
			calculateChange: function(curr, old){
				return (old ? (old - curr) / old * 100.0 * -1 : 0).toFixed(2);
			},
			formatDate(date){
				return this.$moment(date).format("MMM DD, YYYY")
			},
		},
		computed: {
			metrics: function(){
				return this.commit.projectMetrics
			},
			humanReadableDate(){
				return this.$moment(this.commit.commitDate).fromNow()
			},
			parsedMessage: function(){
				var parseLines = this.commit.fullMessage.replace(/(?:\r\n|\r|\n)/g, '<br>');
				return this.textToLinksParser(parseLines, this.$route.params.owner, this.$route.params.name)
			},
			parentCommit: function() {
				return this.commit.commitParentsIDs[0] ? this.commit.commitParentsIDs[0] : null
			},
			changeLOC: function(){
				return this.calculateChange(this.metrics.loc, this.metrics.parentLOC);
			},
			changeLCOM: function(){
				return this.calculateChange(this.metrics.lcom, this.metrics.parentLCOM);
			},
			changeWMC: function(){
				return this.calculateChange(this.metrics.wmc, this.metrics.parentWMC);
			},
			changeCBO: function(){
				return this.calculateChange(this.metrics.cbo, this.metrics.parentCBO);
			},
			hasDeepMetrics: function(){
				return !(this.metrics.loc == 0 && this.metrics.lcom == 0 && this.metrics.wmc == 0 && this.metrics.cbo == 0);
			}
		},
		watch: {
			'$route' (to, from) {
				this.loadData();
			}
		}
	};
</script>
<style scoped>
.loading:after {
	animation: dotty steps(1,end) 1s infinite;
	content: '';
}

@keyframes dotty {
	0% {
		content: '\00a0\00a0\00a0';
	}
	30% {
		content: '.\00a0\00a0';
	}
	60% {
		content: '..\00a0';
	}
	90% {
		content: '...';
	}
}
</style>