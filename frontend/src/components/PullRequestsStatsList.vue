<template>
	<div v-if="data">
		<div class="row list-group">
			<div class="col-12 list-group-item list-group-item-header">
				<div class="row font-weight-bold">
					<div class="col-4">
						<span @click="sortBy('username')" role="button">Developer
							<i class="material-icons align-middle" v-if="sort == 'username' && order == 'asc'">arrow_drop_up</i>
							<i class="material-icons align-middle" v-if="sort == 'username' && order == 'desc'">arrow_drop_down</i>
						</span>
					</div>
					<div class="col-2 text-center">
						<span @click="sortBy('accepted_opened_percentage')" role="button" title="Percentage of merged pull requests among the reviewed ones" data-toggle="tooltip" data-placement="bottom">Marged rate
							<i class="material-icons align-middle" v-if="sort == 'accepted_opened_percentage' && order == 'asc'">arrow_drop_up</i>
							<i class="material-icons align-middle" v-if="sort == 'accepted_opened_percentage' && order == 'desc'">arrow_drop_down</i>
						</span>
					</div>
					<div class="col-2 text-center">
						<span @click="sortBy('accepted_reviewed_percentage')" role="button" title="Percentage of accepted Pull Request among the opened ones" data-toggle="tooltip" data-placement="bottom">Approved rate
							<i class="material-icons align-middle" v-if="sort == 'accepted_reviewed_percentage' && order == 'asc'">arrow_drop_up</i>
							<i class="material-icons align-middle" v-if="sort == 'accepted_reviewed_percentage' && order == 'desc'">arrow_drop_down</i>
						</span>
					</div>
					<div class="col-2 text-center">
						<span @click="sortBy('opened')" role="button">Opened
							<i class="material-icons align-middle" v-if="sort == 'opened' && order == 'asc'">arrow_drop_up</i>
							<i class="material-icons align-middle" v-if="sort == 'opened' && order == 'desc'">arrow_drop_down</i>
						</span>
					</div>
					<div class="col-2 text-center">
						<span @click="sortBy('reviewed')" role="button">Reviewed
							<i class="material-icons align-middle" v-if="sort == 'reviewed' && order == 'asc'">arrow_drop_up</i>
							<i class="material-icons align-middle" v-if="sort == 'reviewed' && order == 'desc'">arrow_drop_down</i>
						</span>
					</div>
				</div>
			</div>
			<div class="col-12 list-group-item" v-for="item in data.content">
				<pull-requests-stats-list-item :data="item"/>
			</div>
			<div class="col-12 list-group-item" v-if="!data.content.length">
				<div class="alert alert-secondary" role="alert">
					No users found!
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
				order: 'desc',
				sort: 'username',
			}
		},
		mounted(){
			this.loadData();

		},
		methods: {
			loadData: function() {
				fetch(`/api/repo/${this.$route.params.owner}/${this.$route.params.name}/developerPR/search${this.queryString}&page=${this.currentPage-1}&size=${this.size}&sort=${this.sort}&order=${this.order}`)
				.then(response => {
					return response.json()
				})
				.then(data => {
					this.data = data
					this.$nextTick(function () {
						$('[data-toggle="tooltip"]').tooltip()
					})
				});
			},
			changePage: function(){
				this.$router.push({ query: {...this.$route.query, page: this.currentPage }}) 
				window.scroll({
					top: 0,
					left: 0,
					behavior: 'smooth'
				})
			},
			sortBy: function(name){
				if(this.sort == name){
					this.order = this.order == 'asc' ? 'desc' : 'asc';
				}else{
					this.sort = name;
					this.order = 'asc';
				}
				this.loadData()
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

.list-group-item-header{
	background-color: rgba(0, 0, 0, 0.03);

}

.list-group .list-group-item:not(:first-child):hover{
	background-color: #f6f8fa;
}
</style>