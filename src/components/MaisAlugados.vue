<template>
  <div class="mais-alugados">
    <div>
      <div class="dados">
        <q-table :rows="rows" :columns="columns" row-key="id" hide-bottom />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { api, authenticate } from 'src/boot/axios';


//TOP 3 usuários com mais aluguéis
const columns = [
  { name: 'name', label: 'Nome', align: 'center', field: row => row.name, format: val => `${val}` },
  { name: 'rentsQuantity', label: 'Total de Empréstimos', align: 'center', field: 'rentsQuantity' },
  { name: 'rentsActive', label: 'Aluguéis Ativos', align: 'center', field: 'rentsActive' }
];

const rows = ref([]);
const pagination = ref({ rowsPerPage: 0 });


onMounted(() => {
  getRows();
});

const getRows = (srch = '') => {
  api.get('/dashboard/rentsPerRenter')
    .then(response => {
      if (Array.isArray(response.data)) {
        rows.value = response.data;
      } else {
        console.error('A resposta da API não é um array:', response.data);
        rows.value = [];
      }
    })
    .catch(error => {
      showNotification('negative', "Socorro!");
      console.error("Erro ao obter dados:", error);
    });
};
</script>

<style scoped>
.mais-alugados {
  width: 100%;
  max-width: 1300px;
  margin: 0 auto;
  background-color: white;
  padding: 20px;
  max-height: 400px;
}

.dados {
  box-shadow: 3px 4px 10px 0px rgba(0, 0, 0, 0.25);
}

.header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.title {
  font-size: 25px;
}

.q-table {
  width: 100%;
}

@media (max-width: 800px) {
  .mais-alugados {
    width: 100%;
  }
}
</style>
