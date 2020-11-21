<template>
	<div class="container mt-3">
		<form @submit.prevent="loadData">
			<div class="row">
				<div class="col-12 col-md-4">
					<div class="form-group">
						<label>Developer</label>
						<input type="text" class="form-control" placeholder="Developer" v-model="developer">
					</div>
				</div>
				<div class="col-12">
					<button class="btn btn-primary btn-sm" type="submit">Search</button>
					<hr>	
				</div>
			</div>
		</form>
		<pull-requests-stats-list/>
	</div>
</template>

<script>

	import 'bootstrap-daterangepicker';
	export default {
		data: function () {
			return {
				developer: null,
			}
		},
		mounted() {
		},
		methods: {
			loadData: function(){
				var q = {
					username: this.developer,
				}

				Object.keys(q).forEach((key) => (q[key] == null) && delete q[key]);
				
				this.$router.replace({query: {
					...this.$router.query, ...q
				}})
			}
		}

	};
</script>