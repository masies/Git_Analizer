<template>
	<div v-if="data">
		<div class="row position-relative timeline-bar" v-for="(items, day) in groupedByDay">
			<div class="timeline-badge">
				<svg height="16" viewBox="0 0 16 16" version="1.1" width="16" aria-hidden="true"><path fill-rule="evenodd" d="M10.5 7.75a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0zm1.43.75a4.002 4.002 0 01-7.86 0H.75a.75.75 0 110-1.5h3.32a4.001 4.001 0 017.86 0h3.32a.75.75 0 110 1.5h-3.32z"></path></svg>
			</div>
			<div class="col">
				<p class="mb-0">Commits on {{ getFormattedDate(day) }}</p>
				<div class="row rounded list-group m-0">
					<div class="col-12 list-group-item" v-for="item in items"  :class="commitStatus(item)">
						<commits-list-item :data="item"/>
					</div>
				</div>
			</div>
		</div>
		<div class="col-12" v-if="!Object.keys(groupedByDay).length">
			<div class="alert alert-secondary" role="alert">
				No commits found!
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
		<router-link :to="{name: 'commitsListContainer', params: {owner: owner, name: name}}" class="btn btn-sm btn-primary w-100 mt-2" v-if="!showPagination && Object.keys(groupedByDay).length">
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
				fetch(`/api/repo/${this.$route.params.owner}/${this.$route.params.name}/commits${this.queryString}&page=${this.currentPage-1}&size=${this.size}`)
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
			},
			getFormattedDate(date){
				return this.$moment(date).format('MMM D, YYYY')
			},
			commitStatus: function(commit){
				return {
					'bg-light-yellow': commit.bugInducing && commit.bugInducingCommits,
					'bg-light-green': !commit.bugInducing && commit.bugInducingCommits,
					'bg-light-red': commit.bugInducing && !commit.bugInducingCommits,
				}
			}
		},
		computed: {
			groupedByDay: function(){
				return _.groupBy(this.data.content, (x) => {
					return this.$moment(x.commitDate).startOf('day').format();
				});
			},
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
.timeline-bar::before{
	position: absolute;
	top: 0;
	display: none;
	bottom: 0;
	left: 0;
	display: block;
	width: 2px;
	content: "";
	background-color: #e1e4e8;
	margin-left: 15px;
}

.timeline-badge {
	position: relative;
	z-index: 1;
	display: flex;
	width: 32px;
	height: 32px;
	margin-right: -15px;
	margin-top: -5px;
	color: #444d56;
	align-items: center;
	border-radius: 50%;
	justify-content: center;
	flex-shrink: 0;
	background-color: #fff;
}

.bg-light-green{
	background-color: #e0ffeb;
}

.bg-light-green:hover{
	background-color: #cdffd8;
}

.bg-light-red{
	background-color: #ffedf0;
}

.bg-light-red:hover{
	background-color: #ffdadf;
}

.bg-light-yellow{
	background-color: #fffbdd;
}

.bg-light-yellow:hover{
	background-color: #fff5b1;
}

</style>