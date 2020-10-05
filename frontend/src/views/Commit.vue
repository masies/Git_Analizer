<template>
	<div class="container mt-3" v-if="commit">
		<div class="row">
			<div class="col">
				<h3 class="d-inline mr-1">{{ commit.fullMessage }}</h3>
				<p class="text-muted">{{ commit.commitName }}</p>
				<p v-html="parsedMessage" v-if="commit.shortMessage != ''"></p>
				
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
		},
		computed: {
			humanReadableDate(){
				return this.$moment(this.commit.commitDate).fromNow()
			},
			parsedMessage: function(){
				var parseLines = this.commit.shortMessage.replace(/(?:\r\n|\r|\n)/g, '<br>');
				return this.textToLinksParser(parseLines, this.$route.params.owner, this.$route.params.name)
			}
			
		}
	};
</script>
