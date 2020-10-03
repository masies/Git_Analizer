<template>
	<div class="container mt-3">
		<div class="row">
			<div class="col">
				<h1>Software Analytics - Group 4</h1>
			</div>
		</div>
		<div class="row">
			<div class="col-12">
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
			<div class="col-12 mt-2">
				<status-bar :owner="owner" :name="name" v-if="showStatusBar" @fetchIsComplete="reloadData"/>
			</div>
		</div>
		<hr>
		<div class="row mt-2">
			<div class="col">
				<repositories-container ref="repos"/>
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
				name: null,
				owner: null,
				showStatusBar: false
			}
		},
		mounted(){

		},
		methods: {
			postRepository: function() {
				this.owner = null;
				this.name = null;
				this.showStatusBar = false;
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
					this.owner = parts[1];
					this.name = parts[2];
					this.showStatusBar = true;
					return response.json()
				})
				.then(data => console.log(data));
			},
			reloadData: function() {
				this.$refs.repos.loadData();
			}
		}
	};
</script>
