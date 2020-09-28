<template>
	<div class="container mt-3">
		<div class="row">
			{{ data }}
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
				fetch(`/api/repo/${this.$route.params.owner}/${this.$route.params.name}/issues/${this.$route.params.id}`)
				.then(response => {
					return response.json()
				})
				.then(data => this.data = data);
			}
		}
	};
</script>
