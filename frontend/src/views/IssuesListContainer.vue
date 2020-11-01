<template>
	<div class="container mt-3">
		<form @submit.prevent="loadData">
			<div class="row">
				<div class="col-12 col-md-4">
					<div class="form-group">
						<label>Text</label>
						<input type="text" class="form-control" placeholder="Text" v-model="text">
					</div>
				</div>
				<div class="col-12 col-md-4">
					<div class="form-group">
						<label>Developer</label>
						<input type="text" class="form-control" placeholder="Developer" v-model="developer">
					</div>
				</div>
				<div class="col-12 col-md-4">
					<div class="form-group">
						<label>Date range</label>
						<input type="text" class="form-control" id="datepicker" v-model="dates" aria-label="Date picker" placeholder="Date range">
					</div>
				</div>
				<div class="col-12">
					<button class="btn btn-primary btn-sm" type="submit">Search</button>
					<hr>	
				</div>
			</div>
		</form>
		<issues-list/>
	</div>
</template>

<script>

	import 'bootstrap-daterangepicker';
	export default {
		data: function () {
			return {
				developer: null,
				text: null,
				dates: null,
				startDate: null,
				endDate: null,
			}
		},
		mounted() {
			$('#datepicker').daterangepicker({autoUpdateInput: false}, (start, end, label) => {
				this.startDate = start.toISOString();
				this.endDate = end.endOf('day').toISOString();
				this.dates = start.format("DD/MM/YYYY") + " - " + end.format("DD/MM/YYYY")
			});
		},
		methods: {
			loadData: function(){
				var q = {
					userLogin: this.developer,
					text: this.text,
					startDate: this.startDate,
					endDate: this.endDate,
				}

				Object.keys(q).forEach((key) => (q[key] == null) && delete q[key]);
				
				this.$router.replace({query: {
					...this.$router.query, ...q
				}})
			}
		}

	};
</script>
<style>
@import '~bootstrap-daterangepicker/daterangepicker.css';
</style>