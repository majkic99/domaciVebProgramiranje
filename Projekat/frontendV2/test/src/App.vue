<template>
  <div id="nav">
    <router-link to="/allnews">All NEWS</router-link> |
    <router-link to="/newsByVisits">NEWS SORT BY VISITS</router-link> |
    <router-link to="/newsByKarma">NEWS SORT BY KARMA</router-link> |
    <router-link to="/search">SEARCH</router-link> |
  </div>
  <router-view/>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
