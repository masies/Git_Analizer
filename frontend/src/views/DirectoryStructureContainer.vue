<template>
	<div class="container mt-3 mb-3">
		<div class="row">
			<div class="col-12 col-md-6 col-lg-8">
				<h3>
					<router-link class="text-decoration-none" :to="{name: 'repository', params: {owner: owner, name: name}}">
						{{ owner }}/{{ name }}
					</router-link>
				</h3>
			</div>
			<div class="col-12 col-md-6 col-lg-4">
				<div class="input-group">
					<input type="text" class="form-control" placeholder="Search file" v-model="query">
					<div class="input-group-append">
						<button class="btn btn-outline-secondary" type="button" @click="loadData">
							<i class="material-icons">search</i>
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<directory-structure v-if="!searchData"/>
			<directory-structure-search @clear="searchData=null" :data="searchData" v-else-if="searchData"/>
		</div>
	</div>
</template>

<script>
	export default {
		data: () => {
			return {
				query: null,
				searchData: null,
			}
		},
		methods: {
			loadData: function(){
				if(this.query){
					fetch(`/api/repo/${this.$route.params.owner}/${this.$route.params.name}/tree/search?q=${this.query}`)
					.then(response => {
						return response.json()
					})
					.then(data => {
						this.searchData = data
					});
				}
			}
		},
		computed: {
			owner: function(){
				return this.$route.params.owner
			},
			name: function(){
				return this.$route.params.name
			}
		}
	};
</script>
<style scoped>
.material-icons{
	font-size: 18px;
	vertical-align: middle!important;
}
</style>