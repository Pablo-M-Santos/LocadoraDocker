<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;">
        <div class="buttonCadastrar">
          CADASTRAR LIVRO
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
            <q-btn flat color="secondary" @click="editRow(props.row)" icon="edit" aria-label="Edit" />
            <q-btn flat color="negative" @click="deleteRow(props.row)" icon="delete" aria-label="Delete" />
          </q-td>
        </template>
      </q-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LivroPage',
  data() {
    return {
      rows: [
        { codigo: '001', nome: '1984', autor: 'George Orwell', editora: 'Companhia das Letras', data: '15/04/2025' },
        { codigo: '002', nome: 'Brave New World', autor: 'Aldous Huxley', editora: 'Harper & Brothers', data: '01/05/2025' },
        { codigo: '003', nome: 'Fahrenheit 451', autor: 'Ray Bradbury', editora: 'Ballantine Books', data: '17/04/2025' },
        { codigo: '004', nome: 'The Catcher in the Rye', autor: 'J.D. Salinger', editora: 'Little, Brown and Company', data: '16/07/2025' },
        { codigo: '005', nome: 'To Kill a Mockingbird', autor: 'Harper Lee', editora: 'J.B. Lippincott & Co.', data: '11/07/2025' },
        { codigo: '006', nome: 'The Great Gatsby', autor: 'F. Scott Fitzgerald', editora: 'Charles Scribner\'s Sons', data: '10/04/2025' },
        { codigo: '007', nome: 'Moby Dick', autor: 'Herman Melville', editora: 'Harper & Brothers', data: '18/10/2025' },
        { codigo: '008', nome: 'War and Peace', autor: 'Leo Tolstoy', editora: 'The Russian Messenger', data: '01/01/2025' },
        { codigo: '009', nome: 'Pride and Prejudice', autor: 'Jane Austen', editora: 'T. Egerton', data: '28/01/2025' },
        { codigo: '010', nome: 'The Hobbit', autor: 'J.R.R. Tolkien', editora: 'George Allen & Unwin', data: '21/09/2025' }
      ],
      columns: [
        { name: 'codigo', required: true, label: 'Código', align: 'center', field: row => row.codigo },
        { name: 'nome', align: 'center', label: 'Nome do Livro', field: row => row.nome },
        { name: 'autor', align: 'center', label: 'Autor', field: row => row.autor },
        { name: 'editora', align: 'center', label: 'Editora', field: row => row.editora },
        { name: 'data', align: 'center', label: 'Data de Lançamento', field: row => row.data },
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
