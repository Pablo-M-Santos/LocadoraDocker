<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;">
        <div class="buttonCadastrar">
          CADASTRAR USUÁRIO
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
  name: 'UsuarioPage',
  data() {
    return {
      rows: [
        { codigo: '1', nome: 'Pablo Moreira Santos', email: 'pablo@gmail.com', senha: '123456789', nivelPermissao: 'Leitor' },
        { codigo: '2', nome: 'Ana Silva', email: 'ana@gmail.com', senha: 'password123', nivelPermissao: 'Administrador' },
        { codigo: '3', nome: 'João Santos', email: 'joao@gmail.com', senha: 'mypassword', nivelPermissao: 'Leitor' },
        { codigo: '4', nome: 'Maria Oliveira', email: 'maria@gmail.com', senha: '1234abcd', nivelPermissao: 'Editor' },
        { codigo: '5', nome: 'Carlos Pereira', email: 'carlos@gmail.com', senha: 'pass1234', nivelPermissao: 'Leitor' },
        { codigo: '6', nome: 'Fernanda Costa', email: 'fernanda@gmail.com', senha: 'abcd1234', nivelPermissao: 'Administrador' },
        { codigo: '7', nome: 'Roberto Lima', email: 'roberto@gmail.com', senha: 'qwerty', nivelPermissao: 'Editor' },
        { codigo: '8', nome: 'Juliana Almeida', email: 'juliana@gmail.com', senha: '123qwe', nivelPermissao: 'Leitor' },
        { codigo: '9', nome: 'Ricardo Sousa', email: 'ricardo@gmail.com', senha: 'admin123', nivelPermissao: 'Administrador' },
        { codigo: '10', nome: 'Patrícia Fernandes', email: 'patricia@gmail.com', senha: 'password1', nivelPermissao: 'Leitor' }
      ],
      columns: [
        { name: 'codigo', required: true, label: 'Código', align: 'center', field: row => row.codigo },
        { name: 'nome', align: 'center', label: 'Nome', field: row => row.nome },
        { name: 'email', align: 'center', label: 'Email', field: row => row.email },
        { name: 'senha', align: 'center', label: 'Senha', field: row => row.senha },
        { name: 'nivelPermissao', align: 'center', label: 'Nível de Permissão', field: row => row.nivelPermissao },
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
