<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;" @click="showModalCadastro = true">
        <div class="buttonCadastrar">
          CADASTRAR ALUGUEL
        </div>
      </q-btn>
    </div>

    <!-- Modal Cadastro -->
    <q-dialog v-model="showModalCadastro">
      <q-card class="modal-card">
        <q-card-section>
          <div class="titulo-cadastro">Cadastro de Aluguel</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="submitFormCadastro" ref="formCadastro">
            <q-input filled v-model="formCadastro.nome" label="Nome do Usuário" required lazy-rules
              :rules="[val => !!val || 'Nome é obrigatório']" />

            <q-input filled v-model="formCadastro.nomeLivro" label="Nome do Livro" required lazy-rules
              :rules="[val => !!val || 'Nome do Livro é obrigatório']" />

            <q-input filled v-model="formCadastro.data" label="Data de Devolução" required lazy-rules
              :rules="[val => !!val || 'Data de Devolução é obrigatória']" mask="##/##/####" />

            <div class="button-container">
              <q-btn type="submit" label="CADASTRAR" class="center-width q-mt-md" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>

    <!-- Modal Editar -->
    <q-dialog v-model="showModalEditar">
      <q-card class="modal-card">
        <q-card-section>
          <div class="titulo-cadastro">Editar Editora</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="submitFormEditar" ref="formEditar">
            <q-input filled v-model="formEditar.nome" label="Nome do Usuário" required lazy-rules
              :rules="[val => !!val || 'Nome é obrigatório']" />

            <q-input filled v-model="formEditar.nomeLivro" label="Nome do Livro" required lazy-rules
              :rules="[val => !!val || 'Nome do Livro é obrigatório']" />

            <q-input filled v-model="formEditar.data" label="Data de Devolução" required lazy-rules
              :rules="[val => !!val || 'Data de Devolução é obrigatória']" mask="##/##/####" />

            <div class="button-container">
              <q-btn type="submit" label="ATUALIZAR" class="center-width q-mt-md" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>


    <!-- Modal Exclusão -->
    <q-dialog v-model="showModalExcluir">
      <q-card class="modal-card-exclusao">
        <q-card-section class="text-center">
          <div class="circulo">
            <i class="fa-solid fa-exclamation"></i>
          </div>
          <h3 class="titulo-exclusao">Tem certeza que deseja excluir?</h3>
        </q-card-section>

        <q-card-actions class="button-exclusao">
          <q-btn label="SIM" color="negative" @click="confirmDelete" class="q-mr-sm" />
          <q-btn label="NÃO" color="secondary" @click="cancelDelete" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- Modal Devolução -->
    <q-dialog v-model="showModalDevolucao">
      <q-card class="modal-card-exclusao">
        <q-card-section class="text-center">
          <div class="circulo">
            <i class="fa-solid fa-exclamation"></i>
          </div>
          <h3 class="titulo-exclusao">Tem certeza que deseja devolver?</h3>
        </q-card-section>

        <q-card-actions class="button-exclusao">
          <q-btn label="SIM" color="negative" @click="confirmReturn" class="q-mr-sm" />
          <q-btn label="NÃO" color="secondary" @click="cancelReturn" />
        </q-card-actions>
      </q-card>
    </q-dialog>


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
            <q-btn flat color="primary" @click="editRow(props.row)" icon="edit" aria-label="Edit" />
            <q-btn flat color="negative" @click="showDeleteModal(props.row)" icon="delete" aria-label="Delete" />
            <q-btn flat color="accent" @click="showReturnModal(props.row)" icon="check" aria-label="Confirm" />
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
      showModalCadastro: false,
      showModalEditar: false,
      showModalExcluir: false,
      showModalDevolucao: false,
      rowToDelete: null,
      rowToReturn: null,
      formCadastro: {
        nome: '',
        nomeLivro: '',
        data: '',
      },
      formEditar: {
        nome: '',
        nomeLivro: '',
        data: '',
      },
      selectedRow: null,
      rows: [
        { codigo: '1', nome: 'Pablo Moreira', nomeLivro: '1984', dataDevolucao: '31/03/2007', status: 'Alugado' },
        { codigo: '2', nome: 'Ana Silva', nomeLivro: 'Brave New World', dataDevolucao: '15/04/2007', status: 'Alugado' },
        { codigo: '3', nome: 'João Santos', nomeLivro: 'Fahrenheit 451', dataDevolucao: '25/03/2007', status: 'Alugado' },
        { codigo: '4', nome: 'Maria Oliveira', nomeLivro: 'The Catcher in the Rye', dataDevolucao: '01/04/2007', status: 'Alugado' },
        { codigo: '5', nome: 'Carlos Pereira', nomeLivro: 'To Kill a Mockingbird', dataDevolucao: '20/03/2007', status: 'Alugado' },
        { codigo: '6', nome: 'Fernanda Costa', nomeLivro: 'The Great Gatsby', dataDevolucao: '30/03/2007', status: 'Alugado' },
        { codigo: '7', nome: 'Roberto Lima', nomeLivro: 'Moby Dick', dataDevolucao: '10/04/2007', status: 'Alugado' },
        { codigo: '8', nome: 'Juliana Almeida', nomeLivro: 'War and Peace', dataDevolucao: '05/04/2007', status: 'Alugado' },
        { codigo: '9', nome: 'Ricardo Sousa', nomeLivro: 'Pride and Prejudice', dataDevolucao: '02/04/2007', status: 'Alugado' },
        { codigo: '10', nome: 'Patrícia Fernandes', nomeLivro: 'The Hobbit', dataDevolucao: '12/04/2007', status: 'Alugado' }
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
    submitFormCadastro() {
      if (this.$refs.formCadastro.validate()) {
        this.rows.push({
          codigo: (this.rows.length + 1).toString(), 
          nome: this.formCadastro.nome,
          nomeLivro: this.formCadastro.nomeLivro,
          dataDevolucao: this.formCadastro.data
        });
        this.showModalCadastro = false;
        this.$q.notify({
          color: 'positive',
          textColor: 'white',
          icon: 'check_circle',
          message: 'Aluguel cadastrado com sucesso.',
          position: 'top'
        });
        this.formCadastro = {
          nome: '',
          nomeLivro: '',
          data: '',
        };
      }
    },

    showDetails(row) {
      this.selectedRow = row;
      this.showModalSobre = true;
    },
    editRow(row) {
      this.selectedRow = row;
      this.formEditar = {
        nome: row.nome,
        nomeLivro: row.nomeLivro,
        data: row.dataDevolucao
      };
      this.showModalEditar = true;
    },
    submitFormEditar() {
      if (this.$refs.formEditar.validate()) {
        const index = this.rows.findIndex(row => row.codigo === this.selectedRow.codigo);
        if (index !== -1) {

          this.rows.splice(index, 1, {
            codigo: this.selectedRow.codigo,
            nome: this.formEditar.nome,
            nomeLivro: this.formEditar.nomeLivro,
            dataDevolucao: this.formEditar.data,
            status: this.rows[index].status
          });
          this.showModalEditar = false;
          this.$q.notify({
            color: 'positive',
            textColor: 'white',
            icon: 'check_circle',
            message: 'Aluguel atualizado com sucesso.',
            position: 'top'
          });
        }
      }
    },
    showDeleteModal(row) {
      this.rowToDelete = row;
      this.showModalExcluir = true;
    },
    confirmDelete() {
      const index = this.rows.findIndex(r => r.codigo === this.rowToDelete.codigo);
      if (index !== -1) {
        this.rows.splice(index, 1);
        this.$q.notify({
          color: 'red',
          textColor: 'white',
          icon: 'delete',
          message: 'Aluguel excluído com sucesso!',
          position: 'top'
        });
      }
      this.showModalExcluir = false;
    },
    cancelDelete() {
      this.rowToDelete = null;
      this.showModalExcluir = false;
    },
    showReturnModal(row) {
      this.rowToReturn = row;
      this.showModalDevolucao = true;
    },
    confirmReturn() {
      const index = this.rows.findIndex(r => r.codigo === this.rowToReturn.codigo);
      if (index !== -1) {
        this.rows[index].status = 'Devolvido';  // Atualize o status
        this.rows[index].dataDevolucao = new Date().toLocaleDateString();  // Atualize a data de devolução
        this.$q.notify({
          color: 'positive',
          textColor: 'white',
          icon: 'check_circle',
          message: 'Livro devolvido com sucesso!',
          position: 'top'
        });
      }
      this.showModalDevolucao = false;
    },
    cancelReturn() {
      this.rowToReturn = null;
      this.showModalDevolucao = false;
    }
  }
}
</script>

<style scoped>
.content {
  padding: 16px;
}

.containerButton {
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
}

.modal-card {
  width: 400px;
  max-width: 90vw;
}

.modal-card-exclusao {
  width: 400px;
  max-width: 90vw;
}

.titulo-cadastro {
  font-size: 1.2rem;
  text-align: center;
  margin-bottom: 16px;
}

.checkbox {
  display: flex;
  justify-content: space-around;
}

.button-container {
  display: flex;
  justify-content: center;
}

.table-container {
  margin-top: 16px;
}

.buttonCadastrar {
  background-color: #006666;
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
}

.text-center {
  text-align: center;
}

.titulo-exclusao,
.titulo-sobre {
  font-size: 1.2rem;
  margin-bottom: 16px;
}

.button-exclusao,
.button-sobre {
  display: flex;
  justify-content: center;
}

.center-width {
  width: 100%;
}


.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.form-grid q-input {
  width: 100%;
}

.form-grid q-input:nth-child(1),
.form-grid q-input:nth-child(2) {
  grid-column: span 2;
}

.form-grid q-input:nth-child(5),
.form-grid q-input:nth-child(6) {
  grid-column: span 2;
}

.form-grid q-input:nth-child(7) {
  grid-column: span 2;
}
</style>
