<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;">
        <div class="buttonCadastrar">
          CADASTRAR LOCATÁRIO
        </div>
      </q-btn>
    </div>

    <!-- Table -->
    <div class="table-container">
      <q-table
        :rows="rows"
        :columns="columns"
        row-key="codigo"
        :pagination="pagination"
        :filter="filter"
      >
        <template v-slot:body-cell-actions="props">
          <q-td :props="props" class="text-center">
            <q-btn flat color="primary" @click="viewDetails(props.row)" icon="visibility" aria-label="View" />
            <q-btn flat color="primary" @click="editRow(props.row)" icon="edit" aria-label="Edit" />
            <q-btn flat color="negative" @click="deleteRow(props.row)" icon="delete" aria-label="Delete" />
          </q-td>
        </template>
      </q-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LocatarioPage',
  data() {
  return {
    rows: [
      { codigo: '1', nome: 'Pablo Moreira', email: 'pablo@gmail.com', celular: '(85) 9 8785-8860', cidade: 'Brasília', bairro: 'Santa Maria' },
      { codigo: '2', nome: 'Ana Silva', email: 'ana@gmail.com', celular: '(21) 9 9876-5432', cidade: 'Rio de Janeiro', bairro: 'Copacabana' },
      { codigo: '3', nome: 'João Santos', email: 'joao@gmail.com', celular: '(11) 9 8765-4321', cidade: 'São Paulo', bairro: 'Vila Madalena' },
      { codigo: '4', nome: 'Maria Oliveira', email: 'maria@gmail.com', celular: '(31) 9 7654-3210', cidade: 'Belo Horizonte', bairro: 'Savassi' },
      { codigo: '5', nome: 'Carlos Pereira', email: 'carlos@gmail.com', celular: '(61) 9 6543-2109', cidade: 'Brasília', bairro: 'Asa Norte' },
      { codigo: '6', nome: 'Fernanda Costa', email: 'fernanda@gmail.com', celular: '(41) 9 5432-1098', cidade: 'Curitiba', bairro: 'Centro' },
      { codigo: '7', nome: 'Roberto Lima', email: 'roberto@gmail.com', celular: '(85) 9 4321-0987', cidade: 'Fortaleza', bairro: 'Aldeota' },
      { codigo: '8', nome: 'Juliana Almeida', email: 'juliana@gmail.com', celular: '(71) 9 3210-9876', cidade: 'Salvador', bairro: 'Barra' },
      { codigo: '9', nome: 'Ricardo Sousa', email: 'ricardo@gmail.com', celular: '(51) 9 2109-8765', cidade: 'Porto Alegre', bairro: 'Centro' },
      { codigo: '10', nome: 'Patrícia Fernandes', email: 'patricia@gmail.com', celular: '(62) 9 1098-7654', cidade: 'Goiânia', bairro: 'Setor Bueno' }
    ],
    columns: [
      { name: 'codigo', required: true, label: 'Código', align: 'center', field: row => row.codigo },
      { name: 'nome', align: 'center', label: 'Nome', field: row => row.nome },
      { name: 'email', align: 'center', label: 'Email', field: row => row.email },
      { name: 'celular', align: 'center', label: 'Celular', field: row => row.celular },
      { name: 'cidade', align: 'center', label: 'Cidade', field: row => row.cidade },
      { name: 'bairro', align: 'center', label: 'Bairro', field: row => row.bairro },
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
