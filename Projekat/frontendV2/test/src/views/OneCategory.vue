<template><div class="news" style="text-align: center">
    <h1 class="mt-4">News</h1>

    <div>
      <div  >
        <table class=" table text-center" style="width: 650px;">

          <thead>
          <tr>
            <th scope="col">Title</th>
            <th scope="col">Created At</th>
            <th scope="col">Content</th>
          </tr>
          </thead>

          <tbody >

          <tr v-for="news in vesti" :key="news.id">

            <b-card style="margin-top: 10px">
            <h1>{{ news.title }}</h1>
            <div>
            {{ news.text }}
            </div>
            <div>
             {{ news.author }}
            </div>
            <div>
            {{ news.visits }}
            </div>
            <div>
            {{ news.date }}
            </div>
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
  name: "SingleNews",
  data() {
    return {
      vest: {},
      vesti: []
    }
  },
  methods: {
    find(id) {
        this.$router.push(`/category/${id}`);
    }
  },
  mounted() {
    this.axios.get(`http://localhost:8080/Backend/api/news/category/${this.$route.params.id}/10/1`).then((response) => {
      this.vesti = response.data;
    });
  }
}
</script>

<style scoped>
</style>