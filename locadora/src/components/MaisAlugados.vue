<template>
  <q-page>
    <q-card class="mais-alugados">
      <q-card-section>
        <div class="header">
          <q-icon name="star" color="yellow-5" size="xl" />
          <h1 class="title">TOP 3 usuários com mais aluguéis</h1>
        </div>

        <div class="dados">
          <q-table
            :rows="rows"
            :columns="columns"
            row-key="codigo"
            :pagination="pagination"
            :rows-per-page-options="[0]"
            hide-bottom
            class="mais-alugados"
          />
        </div>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script>
export default {
  name: 'MaisAlugados',
  data() {
    return {
      columns: [
        { name: 'codigo', label: 'Código', align: 'center', field: row => row.codigo },
        { name: 'nome', label: 'Nome', align: 'center', field: row => row.nome },
        { name: 'totalEmprestimos', label: 'Total de empréstimos', align: 'center', field: row => row.totalEmprestimos },
        { name: 'alugueisAtivos', label: 'Aluguéis ativos', align: 'center', field: row => row.alugueisAtivos },
      ],
      rows: [
        { codigo: 1, nome: 'Pablo Moreira Santos', totalEmprestimos: 52, alugueisAtivos: 20 },
        { codigo: 2, nome: 'Pablo Moreira Santos', totalEmprestimos: 52, alugueisAtivos: 20 },
        { codigo: 3, nome: 'Pablo Moreira Santos', totalEmprestimos: 52, alugueisAtivos: 20 },
      ],
      pagination: {
        rowsPerPage: 0
      }
    }
  }
}
</script>

<style scoped>
.mais-alugados {
  width: 100%;
  max-width: 1111px;
  margin: auto;
  box-shadow: 3px 4px 10px 0px rgba(0, 0, 0, 0.25);
  border-radius: 20px;
  background-color: white;
  margin-top: 20px
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
