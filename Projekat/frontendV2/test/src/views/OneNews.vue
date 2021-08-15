<template>
<div>
  <div>
    {{ vest.id }}
    
  </div>
  <div>
    {{ vest.title }}
    
  </div>
  <div>
    {{ vest.text }}
    
  </div>
  <div>
    {{ vest.creationDate }}
    
  </div>
  <div>
    {{ vest.visitNumber }}
    
  </div>
  <div>
    {{ vest.comments }}
    
  </div>
  <div>
    <tr v-for="tag in vest.keywords" :key="tag.id" @click="
          findTag(tag.id)">

            <b-card style="margin-top: 10px">
            <td scope="row"> {{ tag.name }}</td>
             
            </b-card>


          </tr>
  </div>
  <div @click="find(vest.category.name)">
    {{ vest.category }}
  </div>
  <div>
  <button @click="like(vest.id)">LIKE</button>
  <button @click="dislike(vest.id)">DISLIKE</button>
  {{karma}}
  </div>
  ----------------------------------------------------------------
  </div>
</template>

<script>
export default {
  name: "SingleNews",
  data() {
    return {
      vest: {},
    }
  },
  methods: {
    find(id) {
        this.$router.push(`/category/${id}`);
    },
    findTag(id){
        this.$router.push(`/newsByTag/${id}`);
    },
    like(id){
      this.axios.get(`http://localhost:8080/Backend/api/news/like/${id}`)
    },
    dislike(id){
    this.axios.get(`http://localhost:8080/Backend/api/news/dislike/${id}`)
    },
  },
  mounted() {
    this.axios.get(`http://localhost:8080/Backend/api/news/${this.$route.params.id}`).then((response) => {
      this.vest = response.data;
    });
    this.axios.get(`http://localhost:8080/Backend/api/news/karma/${this.$route.params.id}`).then((response) => {
      this.karma = response.data;
    });
    
  }
}
</script>

<style scoped>
</style>