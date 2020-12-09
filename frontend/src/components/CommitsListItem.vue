<template>
	<div class="row align-items-center">
		<div class="col">
			<router-link class="text-dark text-decoration-none" :to="{name: 'commit', params: {owner: data.owner,name: data.repo, id: data.commitName}}">
				<p class="mb-0">{{ data.shortMessage }}</p>			
			</router-link> 
			<p class="text-muted mb-0">{{ data.developerName }} commited on {{ formattedDate }}</p>	
		</div>

		<div class="col-2 text-center" v-if="buggyProbability != null">
			<span class="badge badge-pill" :class="{'badge-danger': buggyProbability >= 0.7, 'badge-info': buggyProbability < 0.7}" title="Probability of being buggy" data-toggle="tooltip" data-placement="bottom">
				{{ formatBuggyProbability }}
			</span>
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
			}
		},
		mounted(){
			this.$nextTick(function () {
				$('[data-toggle="tooltip"]').tooltip()
			})
		},
		computed: {
			formattedDate(){
				return this.$moment(this.data.commitDate).format('MMM D')
			},
			buggyProbability: function(){
				if(this.data.cleanProbability){
					return (1 - this.data.cleanProbability);
				}
				return null
			},
			formatBuggyProbability: function(){
				if(this.buggyProbability >= 0){
					return (this.buggyProbability*100).toFixed(2)+"%";
				}
				return "";
			}
		}
	};
</script>

<style scoped>
h5:hover{
	color: #0366d6 !important;
}
</style>