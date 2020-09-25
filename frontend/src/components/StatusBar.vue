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
						<samp>
							> Fetching repository information <span v-if="tmp.hasRepository">- complete!</span>
						</samp>
					</p>
					<p class="mb-0">
						<samp v-if="tmp.hasRepository">
							> Fetching issues <span v-if="tmp.hasIssues">- complete!</span>
						</samp>
					</p>
					<p class="mb-0">
						<samp v-if="tmp.hasIssues">
							> Fetching commits <span v-if="tmp.hasCommits">- complete!</span>
						</samp>
					</p>
					<p class="mb-0">
						<samp v-if="tmp.hasCommits">
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
				data: null,
				tmp: {
					hasRepository: true,
					hasIssues: false,
					hasCommits: false
				}
			}
		},
		mounted(){
			this.loadData();
			setInterval(this.loadData, 5000)
		},
		methods: {
			loadData: function() {
				fetch(`/api/repo/${this.owner}/${this.name}/status`)
				.then(response => {
					return response.json()
				})
				.then(data => this.data = data);
			}
		},
		computed: {
			percentage: function(){
				var size = _.size(this.tmp)
				var done = _.values(this.tmp).filter((x) => x).length;
				return done/size*100
			},
			currentStatus: function(){
				if(!this.tmp.hasRepository){
					return "Fetching repository information"
				}else if(!this.tmp.hasIssues){
					return "Fetching issues"
				}else if(!this.tmp.hasCommits){
					return "Fetching commits"
				}else{
					return "Fetching complete"
				}
			},
			isComplete: function(){
				return _.values(this.tmp).filter((x) => !x).length == 0;
			}
		}
	};
</script>
<style scoped>
.progress{
	height: 20px;
}
.loading {
	margin: 20px;
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