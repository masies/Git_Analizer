<template>
	<div class="container mt-3" v-if="data">
		<div class="row rounded list-group">
			<div class="col-12 list-group-item" v-for="item in data.content">
				<issues-list-item :data="item"/>
			</div>
		</div>
		<div class="row mt-3">
			<div class="col mx-auto">		
				<paginate
				v-model="currentPage"
				:page-count="data.totalPages"
				:click-handler="changePage"
				:prev-text="'Prev'"
				:next-text="'Next'"
				:container-class="'pagination pagination-dark justify-content-center'"
				:page-class="'page-item'"
				:page-link-class="'page-link'" 
				:prev-class="'page-item'"
				:prev-link-class="'page-link'"
				:next-class="'page-item'"
				:next-link-class="'page-link'" 
				/>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data: function () {
			return {
				data: null,
				currentPage: this.$route.query.page ? parseInt(this.$route.query.page) : 1,
			}
		},
		mounted(){
			this.loadData();
		},
		methods: {
			loadData: function() {
				fetch(`/api/repo/${this.$route.params.owner}/${this.$route.params.name}/issues?page=${this.currentPage-1}`)
				.then(response => {
					return response.json()
				})
				.then(data => this.data = data);
			},
			changePage: function(){
				this.$router.push({ query: {...this.$route.query, page: this.currentPage }}) 
				window.scroll({
					top: 0,
					left: 0,
					behavior: 'smooth'
				})
			}
		},
		watch: {
			"$route.query": function (id) {
				this.loadData();
			}
		}
	};
</script>
<style scoped>
.list-group-item:hover{
	background-color: #f6f8fa;
}
</style>