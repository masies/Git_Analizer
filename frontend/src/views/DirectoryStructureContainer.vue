<template>
	<div class="container mt-3">
		<div class="row">
			<div class="col">
				<h3>
					<router-link class="text-decoration-none" :to="{name: 'repository', params: {owner: owner, name: name}}">
						{{ owner }}/{{ name }}
					</router-link>
				</h3>
			</div>
		</div>
		<div class="row">
			<directory-structure />
		</div>
	</div>
</template>

<script>
	export default {
		computed: {
			owner: function(){
				return this.$route.params.owner
			},
			name: function(){
				return this.$route.params.name
			}
		}
	};
</script>
