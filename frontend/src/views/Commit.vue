<template>
	<div class="container mt-3 mb-3">
		<div v-if="isLoading" class="row">
			<div class="col-12 text-center">
				<div class="spinner-border text-primary" role="status"  style="width: 3rem; height: 3rem;">
					<span class="sr-only">Loading...</span>
				</div>
			</div>
			<div class="col-12 text-center">
				<h3>Calculating metrics...</h3>
			</div>
		</div>
		<div class="card" v-if="!isLoading && commit">
			<div class="card-header">
				<h3 class="d-inline mr-1">{{ commit.shortMessage }}</h3>
			</div>
			<div class="card-header" v-if="commit.hasMetrics">
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
			calculateChange: function(curr, old){
				return (old ? (old - curr) / old * 100.0 * -1 : 0).toFixed(2);
			}
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
			}
		},
		watch: {
			'$route' (to, from) {
				this.loadData();
			}
		}
	};
</script>
