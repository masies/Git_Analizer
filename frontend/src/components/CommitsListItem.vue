<template>
	<div class="row">
		<div class="col">
			<router-link class="text-dark text-decoration-none" :to="{name: 'commit', params: {owner: data.owner,name: data.repo, id: data.commitName}}">
				<p class="mb-0">{{ data.shortMessage }}</p>			
			</router-link> 
			<p class="text-muted mb-0">{{ data.developerName }} commited on {{ formattedDate }}</p>	
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
		},
		computed: {
			formattedDate(){
				return this.$moment(this.data.commitDate).format('MMM D')
			}
		}
	};
</script>

<style scoped>
h5:hover{
	color: #0366d6 !important;
}
</style>