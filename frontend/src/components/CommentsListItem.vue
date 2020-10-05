<template>
	<div class="card">
		<div class="card-header">
			<img :src="comment.user.avatarUrl" class="rounded-circle avatar mr-2"> 
			<a :href="'https://github.com/'+comment.user.login" target="_blank">{{ comment.user.login }}</a> commented on {{ formattedDate }}
		</div>
		<div class="card-body">
			<p class="card-text" v-html="parsedBody"></p>
		</div>
	</div>
</template>

<script>
	export default {
		props: {
			data: {
				type: Object,
				default: null
			}
		},
		data: () => {
			return {
			}
		},
		mounted(){
			
		},
		methods: {
			
		},
		computed: {
			comment: function(){
				return this.data.comment
			},
			formattedDate(){
				return this.$moment(this.comment.createdAt).format("MMM DD, YYYY")
			},
			parsedBody: function(){
				var parseLines = this.comment.body.replace(/(?:\r\n|\r|\n)/g, '<br>');
				return this.textToLinksParser(parseLines, this.$route.params.owner, this.$route.params.name)
			}
		}
	};
</script>
<style scoped>
.avatar{
	vertical-align: bottom;
	width: 25px;
}
</style>