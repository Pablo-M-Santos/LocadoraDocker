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

const columns = [
  { name: 'name', label: 'Nome', align: 'center', field: row => row.name, format: val => `${val}` },
  { name: 'rentsQuantity', label: 'Total de Empréstimos', align: 'center', field: 'rentsQuantity' },
  { name: 'rentsActive', label: 'Aluguéis Ativos', align: 'center', field: 'rentsActive' }
];

const rows = ref([]);
const pagination = ref({ rowsPerPage: 0 });

const fetchLocatarios = async () => {
  try {
    await authenticate();
    const response = await api.get('/dashboard/rentsPerRenter');

    if (response.data && Array.isArray(response.data)) {
      rows.value = response.data.map(locatario => ({
        id: locatario.id,
        name: locatario.name,
        rentsQuantity: locatario.rentsQuantity || 0,
        rentsActive: locatario.rentsActive || 0
      }));
    } else {
      console.error('Resposta da API tem estrutura inesperada:', response.data);
    }
  } catch (error) {
    console.error('Erro ao obter dados dos locatários:', error);
  }
};

onMounted(() => {
  fetchLocatarios();
});

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
