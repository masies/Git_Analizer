<template>
	<div class="col" v-if="data">
		<div class="row list-group">
			<div class="col-12 list-group-item list-group-item-header">
				<div class="row font-weight-bold">
					<div class="col-8">Name</div>
					<div class="col-4">Top Contributor</div>
				</div>
			</div>
			<div class="col-12 list-group-item py-2">
				<div class="row clickable" @click="$emit('clear')"> 
					<div class="col-12 name">
						<span>..</span>
					</div>
				</div>
			</div>
			<div class="col-12 list-group-item py-2" v-for="item in sortedItem">
				<div class="row">
					<div class="col-8 name">
						<i class="material-icons mr-1">{{ getTypeIcon(item.file) }}</i>
						<span class="text-muted">{{ cleanPath(item.dir) }}</span>
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
		props: {
			data: {
				type: Array,
				default: [],
			}
		},
		mounted(){
		},
		methods: {
			formatDate(date){
				return this.$moment(date).format("MMM DD, YYYY")
			},
			getTypeIcon: function(type){
				return type ? "description" : "folder" 
			},
			cleanPath: function(path){
				return "."+(path == "/" ? path : (path + "/"))
			}
		},
		computed: {
			sortedItem: function(){
				return this.data.sort((a, b) => { 
					if(a.file == b.file){
						return a.filename.localeCompare(b.filename) 
					}
					return a.file ? 1 : -1
				});
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
