<template>
  <div class="news" style="text-align: center">
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
             <td>{{ news.date }}</td>
             <td>{{ news.author }}</td>
             <td>{{ news.visits }}</td>
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
      vest: []
    }
  },
  methods: {
    find(id) {
        this.$router.push(`/news/${id}`);
    }
  },
  mounted() {
    console.log('here');
    this.axios.get("http://localhost:8080/Backend/api/news/10/1").then((response) => {
      this.vest = response.data;
    });
  }
}
</script>