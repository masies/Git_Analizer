<template>
	<div class="card">
		<div class="card-header"  @click="toggleCommit">
			{{ data.oldPath }}
			<span class="float-right">
				<i class="material-icons" v-if="toggle">keyboard_arrow_up</i>
				<i class="material-icons" v-else>keyboard_arrow_down</i>
			</span>
		</div>
		<div class="card-header" v-if="data.changeType == 'MODIFY' && metrics && showMetrics" @click="toggleCommit">
			<div class="row text-center">
				<div class="col">
					{{ metrics.loc.toFixed(2) }} 
					<span>
						<span v-if="changeLOC > 0">▴</span>
						<span v-else-if="changeLOC < 0">▾</span>
						<span v-else>- </span>{{changeLOC}}%
					</span>
					<br>
					<span title="Lines Of Code" data-toggle="tooltip" data-placement="bottom"><b>LOC</b></span>
				</div>
				<div class="col">
					{{ metrics.cbo.toFixed(2) }} 
					<span :class="{'text-danger': changeCBO > 0, 'text-success': changeCBO < 0}">
						<span v-if="changeCBO > 0">▴</span>
						<span v-else-if="changeCBO < 0">▾</span>
						<span v-else> - </span>{{ changeCBO }}%
					</span>
					<br>
					<span title="Coupling between Objects" data-toggle="tooltip" data-placement="bottom"><b>CBO</b></span>
				</div>
				<div class="col">
					{{ metrics.wmc.toFixed(2) }} 
					<span :class="{'text-danger': changeWMC > 0, 'text-success': changeWMC < 0}">
						<span v-if="changeWMC > 0">▴</span>
						<span v-else-if="changeWMC < 0">▾</span>
						<span v-else> - </span>{{ changeWMC }}%
					</span>
					<br>
					<span title="Weighted Methods for Class" data-toggle="tooltip" data-placement="bottom"><b>WMC</b></span>
				</div>
				<div class="col">
					{{ metrics.lcom.toFixed(2) }} 
					<span :class="{'text-danger': changeLCOM > 0, 'text-success': changeLCOM < 0}">
						<span v-if="changeLCOM > 0">▴</span>
						<span v-else-if="changeLCOM < 0">▾</span>
						<span v-else> - </span>{{ changeLCOM }}%
					</span>
					<br>
					<span title="Lack of Cohesion in Methods" data-toggle="tooltip" data-placement="bottom"><b>LCOM</b></span>
				</div>
			</div>
		</div>
		<div class="card-body p-0 collapse" style="overflow-x: scroll;" ref="collapse">
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
				default: null,
			}
		},
		data: () => {
			return {
				toggle: false
			}
		},
		mounted(){

		},
		methods: {
			toggleCommit: function(){
				this.toggle = !this.toggle;
				$(this.$refs.collapse).collapse('toggle')
			},
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
			},
			calculateChange: function(curr, old){
				var val = (old ? (old - curr) / old * 100.0 * -1 : 0).toFixed(2);
				return val;
			}
		},
		computed: {
			chunks: function(){
				return parse(this.data.diffs)[0].chunks;
			},
			showMetrics: function(){
				return this.metrics.loc || this.metrics.lcom || this.metrics.wmc || this.metrics.cbo
			},
			metrics: function(){
				return this.data.metrics
			},
			changeLOC: function(){
				if(!this.metrics){
					return 0;
				}
				return this.calculateChange(this.metrics.loc, this.metrics.parentLOC);
			},
			changeLCOM: function(){
				if(!this.metrics){
					return 0;
				}
				return this.calculateChange(this.metrics.lcom, this.metrics.parentLCOM);
			},
			changeWMC: function(){
				if(!this.metrics){
					return 0;
				}
				return this.calculateChange(this.metrics.wmc, this.metrics.parentWMC);
			},
			changeCBO: function(){
				if(!this.metrics){
					return 0;
				}
				return this.calculateChange(this.metrics.cbo, this.metrics.parentCBO);
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