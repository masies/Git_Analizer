<template>
	<div class="col" v-if="data">
		<div class="row list-group">
			<div class="col-12 list-group-item list-group-item-header">
				<div class="row font-weight-bold">
					<div class="col-8">Name</div>
					<div class="col-4">Top Contributor</div>
				</div>
			</div>
			<div class="col-12 list-group-item py-2" v-if="!isRoot">
				<div class="row clickable" @click="goBackFolder"> 
					<div class="col-12 name">
						<span>..</span>
					</div>
				</div>
			</div>
			<div class="col-12 list-group-item py-2" v-for="item in sortedItem">
				<div class="row" @click="goToFolder(item)" :class="{clickable: !item.file}">
					<div class="col-8 name">
						<i class="material-icons mr-1">{{ getTypeIcon(item.file) }}</i>
						<span>{{item.filename}}</span>
					</div>
					<div class="col-4" v-if="item.file">
						{{ item.topContributor }}
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data: () => {
			return {
				data: null,
			}
		},
		mounted(){
			this.loadData();
		},
		methods: {
			loadData: function() {
				
				var path = this.$route.params.pathMatch;
				path = path == "" || path == undefined ? "/" : path;
				fetch(`/api/repo/${this.$route.params.owner}/${this.$route.params.name}/tree${path}`)
				.then(response => {
					return response.json()
				})
				.then(data => {
					this.data = data
				});
			},
			formatDate(date){
				return this.$moment(date).format("MMM DD, YYYY")
			},
			getTypeIcon: function(type){
				return type ? "description" : "folder" 
			},
			goBackFolder: function(){
				var path = this.$route.path
				path = path.substring(0, path.lastIndexOf("/") + 1).replace(/\/+$/, "");
				this.$router.push({ path: path })
			},
			goToFolder: function(item){
				if(!item.file){
					var t = this.$route.name != "tree" ? "/tree" : ""
					var path = this.$route.path + t + "/" + item.filename
					this.$router.push({ path: path })
				}
			}
		},
		computed: {
			isRoot: function(){
				return this.$route.name != "tree" || this.$route.params.pathMatch == "" || this.$route.params.pathMatch == "/" || this.$route.params.pathMatch == undefined ;
			},
			sortedItem: function(){
				return this.data.sort((a, b) => { 
					if(a.file == b.file){
						return a.filename.localeCompare(b.filename) 
					}
					return a.file ? 1 : -1
				});
			}
		},
		watch: {
			'$route' (to, from) {
				this.loadData();
			}
		}
	};
</script>
<style scoped>
.list-group-item-header{
	background-color: rgba(0, 0, 0, 0.03);

}

.list-group-item:hover{
	background-color: #f6f8fa;
}

.material-icons{
	font-size: 18px;
	vertical-align: middle!important;
}

.clickable{
	cursor: pointer;
}

.clickable:hover .name span{
	text-decoration: underline;
}
</style>
