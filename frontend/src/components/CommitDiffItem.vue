<template>
	<div class="card">
		<div class="card-header">
			{{ data.oldPath }}
		</div>
		<div class="card-body p-0" style="overflow-x: scroll;">
			<div class="container-fluid" v-for="chunk in chunks">
				<div class="row diff-chunk">
					<div class="col-2 diff-line">
					</div>
					<div class="col-10 diff-content text-muted">
						{{ chunk.content }}
					</div>
				</div>
				<div class="row" v-for="change in chunk.changes" :class="{ 'diff-del': change.type == 'del', 'diff-add': change.type == 'add'}">
					<div class="col-1 text-center text-muted diff-line">
						{{ getLine1(change) }}
					</div>
					<div class="col-1 text-center text-muted diff-line">
						{{ getLine2(change) }}
					</div>
					<div class="col-10 diff-content">
						{{ change.content }}
					</div>
				</div>
			</div>

		</div>
	</div>
</template>

<script>
	var parse = require('parse-diff');
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
		methods: {
			getLine1: function(change){
				if(change.type == "del"){
					return change.ln
				}
				if(change.type == "normal"){
					return change.ln1
				}
				return "";
			},
			getLine2: function(change){
				if(change.type == "add"){
					return change.ln
				}
				if(change.type == "normal"){
					return change.ln2
				}
				return "";
			}
		},
		computed: {
			chunks: function(){
				return parse(this.data.diffs)[0].chunks;
			}
		}
	};
</script>
<style scoped>
.diff-del{
	background-color: #ffeef0;
}

.diff-del .diff-line{
	background-color: #ffdce0;
}

.diff-add{
	background-color: #e6ffed;
}

.diff-add .diff-line{
	background-color: #cdffd8;
}

.diff-chunk{
	background-color: #f1f8ff;
}

.diff-chunk .diff-line{
	background-color: #dbedff;
}

.diff-content{
	white-space: pre;
}
</style>