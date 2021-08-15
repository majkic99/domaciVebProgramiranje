<template>

  <div class="news" style="text-align: center">
      <input v-model="message" placeholder="ADVANCED SEARCH">
        <button @click="search('novak')">SEARCH</button>
    <h1 class="mt-4">News</h1>

    <div class="row" style="display:inline;">
      <div class="col-4 mx-auto" >
        <table class=" table text-center" >

          <thead>
          <tr>
<!--            <th scope="col">ID</th>-->
            <th scope="col">Title</th>
            <th scope="col">Created At</th>
            <th scope="col">Content</th>
          </tr>
          </thead>

          <tbody >

          <tr v-for="news in vest" :key="news.id" @click="
          find(news.id)">

            <b-card style="margin-top: 10px">
            <td scope="row"> {{ news.title }}</td>
             <td>{{ news.text }}</td>
             
            </b-card>


          </tr>

          </tbody>

        </table>
        
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      selectedNews: null,
      vest: [],
      message: ""
    }
  },
  methods: {
    find(id) {
        this.$router.push(`/news/${id}`);
    },
    search(textTest){
        console.log(textTest);
        const article = { text : textTest}
        console.log(article);
    this.axios.post("http://localhost:8080/Backend/api/news/advaced/10/1", article).then((response) => {
      this.vest = response.data;
    });
    }
  },
  mounted() {
    
  }
}
</script>