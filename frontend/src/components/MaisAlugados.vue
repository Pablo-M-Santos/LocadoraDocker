<template>
  <div class="mais-alugados">
    <div>
      <div class="header">
        <h3 class="title">Top 3 Livros Mais Alugados</h3>
      </div>
      <div class="dados">
        <q-table :rows="rows" :columns="columns" row-key="id" hide-bottom />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { api } from 'src/boot/axios';

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

const getRows = () => {
  api.get('/dashboard/rentsPerRenter')
    .then(response => {
      if (Array.isArray(response.data)) {
        // Ordena os dados pelo campo 'rentsQuantity' em ordem decrescente
        rows.value = response.data
          .sort((a, b) => b.rentsQuantity - a.rentsQuantity)
          .slice(0, 3); // Pega apenas os 3 primeiros
      } else {
        console.error('A resposta da API não é um array:', response.data);
        rows.value = [];
      }
    })
    .catch(error => {
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
}

.title {
  font-size: 18px;
  font-weight: bold;
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
