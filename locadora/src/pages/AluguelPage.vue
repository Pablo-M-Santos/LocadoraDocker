<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;">
        <div class="buttonCadastrar">
          CADASTRAR ALUGUEL
        </div>
      </q-btn>
    </div>
    <!-- Tabela de livros -->
    <div class="table-container">
      <q-table :rows="rows" :columns="columns" row-key="codigo" :pagination="pagination" :filter="filter">
        <template v-slot:body-cell="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.value }}</div>
          </q-td>
        </template>
        <template v-slot:body-cell-actions="props">
          <q-td :props="props" style="vertical-align: middle;">
            <q-btn flat color="primary" @click="viewDetails(props.row)" icon="visibility" aria-label="View" />
            <q-btn flat color="secondary" @click="editRow(props.row)" icon="edit" aria-label="Edit" />
            <q-btn flat color="negative" @click="deleteRow(props.row)" icon="delete" aria-label="Delete" />
            <q-btn flat color="accent" @click="confirmReturn(props.row)" icon="check" aria-label="Confirm" />
          </q-td>
        </template>
      </q-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AluguelPage',
  data() {
    return {
      rows: [
        { codigo: '1', nome: 'Pablo Moreira', nomeLivro: '1984', dataDevolucao: '31/03/2007', status: 'Alugado' },
        { codigo: '2', nome: 'Ana Silva', nomeLivro: 'Brave New World', dataDevolucao: '15/04/2007', status: 'Alugado' },
        { codigo: '3', nome: 'João Santos', nomeLivro: 'Fahrenheit 451', dataDevolucao: '25/03/2007', status: 'Devolvido' },
        { codigo: '4', nome: 'Maria Oliveira', nomeLivro: 'The Catcher in the Rye', dataDevolucao: '01/04/2007', status: 'Alugado' },
        { codigo: '5', nome: 'Carlos Pereira', nomeLivro: 'To Kill a Mockingbird', dataDevolucao: '20/03/2007', status: 'Devolvido' },
        { codigo: '6', nome: 'Fernanda Costa', nomeLivro: 'The Great Gatsby', dataDevolucao: '30/03/2007', status: 'Alugado' },
        { codigo: '7', nome: 'Roberto Lima', nomeLivro: 'Moby Dick', dataDevolucao: '10/04/2007', status: 'Devolvido' },
        { codigo: '8', nome: 'Juliana Almeida', nomeLivro: 'War and Peace', dataDevolucao: '05/04/2007', status: 'Alugado' },
        { codigo: '9', nome: 'Ricardo Sousa', nomeLivro: 'Pride and Prejudice', dataDevolucao: '02/04/2007', status: 'Alugado' },
        { codigo: '10', nome: 'Patrícia Fernandes', nomeLivro: 'The Hobbit', dataDevolucao: '12/04/2007', status: 'Devolvido' }
      ],
      columns: [
        { name: 'codigo', required: true, label: 'Código', align: 'center', field: row => row.codigo },
        { name: 'nome', align: 'center', label: 'Nome', field: row => row.nome },
        { name: 'nomeLivro', align: 'center', label: 'Nome do Livro', field: row => row.nomeLivro },
        { name: 'dataDevolucao', align: 'center', label: 'Data de Devolução', field: row => row.dataDevolucao },
        { name: 'status', align: 'center', label: 'Status', field: row => row.status },
        { name: 'actions', align: 'center', label: 'Ações', field: row => row }
      ],
      pagination: { page: 1, rowsPerPage: 5 },
      filter: ''
    }
  },
  methods: {
    editRow(row) {
      console.log('Edit row', row)
    },
    deleteRow(row) {
      console.log('Delete row', row)
    }
  }
}
</script>

<style scoped>
.content {
  padding: 20px;
}

.containerButton {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.buttonCadastrar {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.table-container {
  max-width: 1600px;
  width: 100%;
  margin: 0 auto;
  padding: 0 20px;
}

.q-table {
  width: 100%;
}

</style>
