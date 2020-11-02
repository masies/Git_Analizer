<template>
	<div class="container mt-3 mb-3" v-if="data">
		<div class="row">
			<div class="col-12 col-md-6 col-lg-4">
				<div class="input-group">
					<input type="text" class="form-control" placeholder="Search" v-model="query">
					<div class="input-group-append">
						<button class="btn btn-outline-secondary" type="button" @click="loadData">
							<i class="material-icons">search</i>
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-12 col-md-6 col-lg-4 d-flex align-items-stretch" v-for="item in data.content">
				<repository-list-item :data="item"/>
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
				query: ""
			}
		},
		mounted(){
			this.loadData();
		},
		methods: {
			loadData: function() {
				fetch(`/api/repo/search?page=${this.currentPage-1}&q=${this.query}`)
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
.material-icons{
	font-size: 18px;
	vertical-align: middle!important;
}
</style>