<template>
	<div id="app">
		<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
			<div class="container">
				<router-link class="navbar-brand" to="/">Group 4</router-link>
				<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#mainNavbar" aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation">
					<span class="navbar-toggler-icon"></span>
				</button>

				<div class="collapse navbar-collapse" id="mainNavbar">
					<ul class="navbar-nav mr-auto">
						<li class="nav-item">
							<router-link class="nav-link" to="/" exact>Index</router-link> 
						</li>
						<li class="nav-item">
							<router-link class="nav-link" :to="{name: 'repositoryList'}">Repositories</router-link> 
						</li>
					</ul>
				</div>
			</div>
		</nav>
		<main class="flex-fill">
			<router-view />
		</main>
	</div>
</template>

<style lang="scss">
#app {
	font-family: Avenir, Helvetica, Arial, sans-serif;
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
	color: #2c3e50;
}
</style>
<style scoped>
.router-link-active{
	color: #fff !important;
}
</style>