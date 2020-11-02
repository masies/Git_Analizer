<template>
	<div v-if="data">
		<div class="row rounded list-group m-0">
			<div class="col-12 list-group-item" v-for="item in data.content">
				<issues-list-item :data="item"/>
			</div>
			<div class="col-12" v-if="!data.content.length">
				<div class="alert alert-secondary" role="alert">
					No issues found!
				</div>
			</div>
		</div>
		<div class="row mt-3" v-if="showPagination">
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
		<router-link :to="{name: 'issuesListContainer', params: {owner: owner, name: name}}" class="btn btn-sm btn-primary w-100 mt-2" v-if="!showPagination && data.content.length">
			Show more
		</router-link>

	</div>
</template>

<script>
	export default {
		props: {
			size: {
				type: Number,
				default: 20
			},
			showPagination: {
				type: Boolean,
				default: true
			}
		},
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
				fetch(`/api/repo/${this.$route.params.owner}/${this.$route.params.name}/issues${this.queryString}&page=${this.currentPage-1}&size=${this.size}`)
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
		computed: {
			owner: function(){
				return this.$route.params.owner;
			},
			name: function(){
				return this.$route.params.name;
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