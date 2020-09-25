<template>
	<div class="container mt-3">
		<div class="row">
			<div class="col">
				<h1>Software Analytics - Group 4</h1>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<form @submit.prevent="postRepository" novalidate>
					<div class="input-group">
						<input type="url" placeholder="Analyze a repository" v-model="repositoryName" aria-label="Repository search" class="form-control" :class="{'is-invalid': isInvalid}"> 
						<div class="input-group-append">
							<button type="submit" class="btn btn-secondary rounded-right">
								<i class="material-icons text-white align-middle">search</i>
							</button>
						</div>
						<div class="invalid-feedback">
							Not a valid GitHub url
						</div>
					</div>
				</form>
			</div>
		</div>
		<hr>
		<div class="row mt-2">
			<div class="col">
				<repositories-container/>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data: () => {
			return {
				repositoryName: "",
				isInvalid: false,
			}
		},
		mounted(){

		},
		methods: {
			postRepository: function() {
				try{
					var url = new URL(this.repositoryName);
					if(url.host != "github.com"){
						this.isInvalid = true;
						return;
					}
				}catch{
					this.isInvalid = true;
					return;
				}
				var parts = url.pathname.split('/');
				fetch("/api/repo",
				{
					method: 'POST',
					headers: {
						'Content-Type': 'application/json',
					},
					body: JSON.stringify({
						owner: parts[1],
						name: parts[2]
					}),
				})
				.then(response => {
					this.repositoryName = "";
					this.isInvalid = false;
					return response.json()
				})
				.then(data => console.log(data));
			}
		}
	};
</script>
