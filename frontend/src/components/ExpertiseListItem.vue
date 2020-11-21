<template>
	<div class="row">
		<div class="col-9">
			{{ data.email }}
		</div>
		<div class="col-3 text-center">
			{{ data.expertise }}
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