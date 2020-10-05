<template>
	<div class="container mt-3" v-if="commit">
		<div class="row">
			<div class="col">
				<h3 class="d-inline mr-1">{{ commit.fullMessage }}</h3><h3 class="text-muted d-inline">#{{ commit.commitName }}</h3>
				<p class="text-muted mb-0">
					<svg class="octicon octicon-issue-opened open mr-2" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true" v-if="commit.state=='open'" >
						<path fill-rule="evenodd" d="M8 1.5a6.5 6.5 0 100 13 6.5 6.5 0 000-13zM0 8a8 8 0 1116 0A8 8 0 010 8zm9 3a1 1 0 11-2 0 1 1 0 012 0zm-.25-6.25a.75.75 0 00-1.5 0v3.5a.75.75 0 001.5 0v-3.5z"></path>
					</svg>

					<svg v-else  class="octicon octicon-issue-closed closed mr-2" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M1.5 8a6.5 6.5 0 0110.65-5.003.75.75 0 00.959-1.153 8 8 0 102.592 8.33.75.75 0 10-1.444-.407A6.5 6.5 0 011.5 8zM8 12a1 1 0 100-2 1 1 0 000 2zm0-8a.75.75 0 01.75.75v3.5a.75.75 0 11-1.5 0v-3.5A.75.75 0 018 4zm4.78 4.28l3-3a.75.75 0 00-1.06-1.06l-2.47 2.47-.97-.97a.749.749 0 10-1.06 1.06l1.5 1.5a.75.75 0 001.06 0z"></path></svg>
					<a :href="'https://github.com/'+commit.user.login" target="_blank">{{ commit.user.login }}</a> opened this issue {{ humanReadableDate }}
				</p>
				<hr>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data: () => {
			return {
				commit: null,
			}
		},
		mounted(){
			this.loadData();
		},
		methods: {
			loadData: function() {
				fetch(`/api/repo/${this.$route.params.owner}/${this.$route.params.name}/commits/${this.$route.params.id}`)
				.then(response => {
					return response.json()
				})
				.then(data => this.commit = data);
			}
		}
	};
</script>
