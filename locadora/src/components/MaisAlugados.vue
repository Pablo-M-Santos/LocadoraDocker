<template>
  <div class="mais-alugados">
    <div>
        <div class="dados">
          <q-table
            :rows="rows"
            :columns="columns"
            row-key="id"
            hide-bottom
          />
        </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { api, authenticate } from 'src/boot/axios';

export default {
  name: 'MaisAlugados',
  setup() {
    const columns = [
      { name: 'name', label: 'Nome', align: 'center', field: row => row.name },
      { name: 'totalRents', label: 'Total de Empréstimos', align: 'center', field: row => row.totalRents || 0 },
      { name: 'activeRents', label: 'Aluguéis Ativos', align: 'center', field: row => row.activeRents || 0 }
    ];

    const rows = ref([]);
    const pagination = ref({ rowsPerPage: 0 });

    const fetchLocatarios = async () => {
      try {
        await authenticate();
        const response = await api.get('/rent/renters');

        if (response.data && response.data.content && Array.isArray(response.data.content)) {
          const sortedLocatarios = response.data.content.sort((a, b) => b.totalRents - a.totalRents);

          rows.value = sortedLocatarios.slice(0, 10).map(locatario => ({
            id: locatario.id,
            name: locatario.name,
            totalRents: locatario.totalRents || 0,
            activeRents: locatario.activeRents || 0
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

    return {
      columns,
      rows,
      pagination
    };
  }
}
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
