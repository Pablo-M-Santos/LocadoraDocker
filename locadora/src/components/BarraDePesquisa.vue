<!-- src/components/SearchBar.vue -->
<template>
  <div class="container">
    <div class="pesquisa">
      <q-input
        filled
        v-model="search"
        placeholder="Pesquisa da Editora"
        class="pesquisa"
        @input="onSearch"
      >
        <template v-slot:prepend>
          <q-icon name="search" />
        </template>
      </q-input>
    </div>
    <q-btn class="button-pesquisar" label="PESQUISAR" @click="onSearch" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      search: ''
    };
  },
  methods: {
    onSearch() {

      console.log('Searching for:', this.search);
    }
  }
};
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  padding-bottom: 20px;
}

.pesquisa {
  display: flex;
  width: 1160px;
  height: 53px;
  border-radius: 4px;
}

.q-input.pesquisa {
  font-size: 16px;
  font-weight: 800;
  color: rgba(0, 0, 0, 0.60);
}

.button-pesquisar {
  font-size: 16px;
  font-weight: 800;
}
</style>
