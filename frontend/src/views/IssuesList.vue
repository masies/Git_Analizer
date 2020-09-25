<template>
	<div class="container mt-3">
		<div class="row">
			<div class="col-3" v-for="item in data">
				<issues-list-item :data="item"/>
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
				fetch(`/api/repo/${this.$route.params.owner}/${this.$route.params.name}/issues`)
				.then(response => {
					return response.json()
				})
				.then(data => this.data = data);
			}
		}
	};
</script>
