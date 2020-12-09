<template>
	<div class="row">
		<div class="col-12">		
			<div class="progress">
				<div class="progress-bar" role="progressbar" :class="{'bg-success': isComplete}" :style="{width: percentage+'%'}" :aria-valuenow="percentage" aria-valuemin="0" aria-valuemax="100">
					<span :class="{loading: !isComplete}" v-if="!isComplete">Fetching repository</span>
					<span v-if="isComplete">Fetching complete!</span>
				</div>
			</div>
		</div>
		<div class="col-12">
			<a data-toggle="collapse" href="#details" role="button" aria-expanded="false" aria-controls="details">
				Show details
			</a>
			<div class="collapse" id="details">
				<div class="card card-body p-2">
					<p class="mb-0" v-for="state in status">
						<samp>
							{{ state }}
						</samp>
					</p>
					<p class="mb-0" v-if="isComplete">
						<samp>
							Fetching complete, repository information are ready at 
							<router-link :to="{name: 'repository', params: {owner: owner, name: name}}">
								{{ owner }}/{{ name }}
							</router-link>
						</samp>
					</p>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		props: {
			owner: {
				type: String,
				default: null
			},
			name: {
				type: String,
				default: null
			}
		},
		data: () => {
			return {
				data: {
					fetchedInfo: false,
					fetchedIssues: false,
					fetchedCommits: false,
					predictionsDone: false,
					szzdone: false
				},
				status: ["Starting the analysis…"
				]
			}
		},
		mounted(){
			this.interval = setInterval(this.loadData, 5000)
		},
		methods: {
			loadData: function() {
				if(this.isComplete){
					clearInterval(this.interval);
				}
				fetch(`/api/repo/${this.owner}/${this.name}/status`)
				.then(response => {
					return response.json()
				})
				.then(data => {
					this.$set(this.data, "fetchedInfo", data.fetchedInfo)
					this.$set(this.data, "fetchedIssues", data.fetchedIssues)
					this.$set(this.data, "fetchedCommits", data.fetchedCommits)
					this.$set(this.data, "szzdone", data.szzdone)
					this.$set(this.data, "predictionsDone", data.predictionsDone)
				});
			}
		},
		computed: {
			percentage: function(){
				var size = _.size(this.data)
				var done = _.values(this.data).filter((x) => x).length;
				return done/size*100
			},
			isComplete: function(){
				return _.values(this.data).filter((x) => !x).length == 0;
			}
		},
		watch: {
			isComplete: function(){
				if(this.isComplete){
					this.$emit("fetchIsComplete")
				}
			},
			'data.fetchedInfo': function(newVal, oldVal){
				this.status.push("Fetching reposiotry information complete!")
			},
			'data.fetchedIssues': function(newVal, oldVal){
				this.status.push("Fetching reposiotry issues complete!")
			},
			'data.fetchedCommits': function(newVal, oldVal){
				this.status.push("Fetching reposiotry commits complete!")
			},
			'data.predictionsDone': function(newVal, oldVal){
				this.status.push("Predicting buggy commits complete!")
			},
			'data.szzdone': function(newVal, oldVal){
				this.status.push("Computing SZZ complete!")
			}
		}
	}
</script>
<style scoped>
.progress{
	height: 20px;
}

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