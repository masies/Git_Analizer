<template>
	<div class="row">
		<div class="col-12">		
			<div class="progress">
				<div class="progress-bar" role="progressbar" :style="{width: percentage+'%'}" :aria-valuenow="percentage" aria-valuemin="0" aria-valuemax="100">
					<span :class="{loading: !isComplete}">{{ currentStatus }}</span>
				</div>
			</div>
		</div>
		<div class="col-12">
			<a data-toggle="collapse" href="#details" role="button" aria-expanded="false" aria-controls="details">
				Show details
			</a>
			<div class="collapse" id="details">
				<div class="card card-body p-2">
					<p class="mb-0">
						<samp :class="{loading: !data.fetchedInfo}">
							> Fetching repository information<span v-if="data.fetchedInfo"> - complete!</span>
						</samp>
					</p>
					<p class="mb-0">
						<samp v-if="data.fetchedInfo" :class="{loading: !data.fetchedIssues}">
							> Fetching issues<span v-if="data.fetchedIssues"> - complete!</span>
						</samp>
					</p>
					<p class="mb-0">
						<samp v-if="data.fetchedIssues" :class="{loading: !data.fetchedCommits}">
							> Fetching commits<span v-if="data.fetchedCommits"> - complete!</span>
						</samp>
					</p>
					<p class="mb-0">
						<samp v-if="isComplete">
							> Fetching complete, repository information are ready at 
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
					fetchedCommits: false
				}
			}
		},
		mounted(){
			this.loadData();
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
				.then(data => this.data = data);
			}
		},
		computed: {
			percentage: function(){
				var size = _.size(this.data)
				var done = _.values(this.data).filter((x) => x).length;
				return done/size*100
			},
			currentStatus: function(){
				if(!this.data.fetchedInfo){
					return "Fetching repository information"
				}else if(!this.data.fetchedIssues){
					return "Fetching issues"
				}else if(!this.data.fetchedCommits){
					return "Fetching commits"
				}else{
					return "Fetching complete"
				}
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
			}
		}
	};
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