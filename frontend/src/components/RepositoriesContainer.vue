<template>
	<div class="row">
		<div class="col">
			<h2>Repositories</h2>
			<div class="row" v-if="data">
				<div class="col-12 col-md-6 col-lg-4 d-flex align-items-stretch" v-for="item in data.content">
					<repository-list-item :data="item"/>
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
				fetch("/api/repo")
				.then(response => response.json())
				.then(data => this.data = data);
			}
		}
	};
</script>
