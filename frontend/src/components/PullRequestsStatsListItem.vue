<template>
	<div class="row">
		<div class="col-4">
			{{ data.username }}
		</div>
		<div class="col-2 text-center">
			{{ formatPercentage(data.accepted_opened_percentage) }}%
		</div>
		<div class="col-2 text-center">
			{{ formatPercentage(data.accepted_reviewed_percentage) }}%
		</div>
		<div class="col-2 text-center">
			{{ data.opened }}
		</div>
		<div class="col-2 text-center">
			{{ data.reviewed }}
		</div>
	</div>
</template>

<script>
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
			formatPercentage: function(value){
				return (value * 100).toFixed(2)
			}
		}
	};
</script>

<style scoped>
h5:hover{
	color: #0366d6 !important;
}

.closed.octicon, .reverted.octicon {
	color: #cb2431;
}

.open.octicon {
	color: #28a745;
}

.octicon {
	fill: currentColor;
}
</style>