<template>
	<div class="row">
		<div class="col">
			<router-link class="text-dark text-decoration-none" :to="{name: 'issue', params: {owner: data.owner,name: data.repo, id: issue.number}}">
				<p class="mb-0 font-weight-bold">{{ issue.title }}</p>			
			</router-link> 
			<div>
				<span class="badge badge-pill mr-1" :style="{'background-color': '#'+label.color }" v-for="label in issue.labels">
					{{ label.name }}
				</span>
			</div>
			<p class="text-muted mb-0">#{{ issue.number }} opened {{ humanReadableDate }} by {{ issue.user.login }}</p>	
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
			issue: function(){
				return this.data.issue
			},
			humanReadableDate(){
				return this.$moment(this.issue.createdAt).fromNow()
			}
		}
	};
</script>

<style scoped>
h5:hover{
	color: #0366d6 !important;
}
</style>