<template>
	<div class="row">
		<div class="col">
			<h1>Repo containers</h1>
			<div class="row">
				<div class="col-3" v-for="item in data">
					
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
				fetch("/api/repository")
				.then(response => response.json())
				.then(data => this.data = data);
			}
		}
	};
</script>
