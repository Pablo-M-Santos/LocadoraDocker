<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;" @click="showModalCadastro = true">
        <div class="buttonCadastrar">
          CADASTRAR LIVRO
        </div>
      </q-btn>
    </div>

    <!-- Modal Cadastro -->
    <q-dialog v-model="showModalCadastro">
      <q-card class="modal-card">
        <q-card-section>
          <div class="titulo-cadastro">Cadastro de Livro</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="submitFormCadastro" ref="formCadastro">

            <q-input filled v-model="formCadastro.nome" label="Nome do Livro" required lazy-rules
              :rules="[val => !!val || 'Nome do Livro é obrigatório']" />

            <q-input filled v-model="formCadastro.autor" label="Autor" required lazy-rules
              :rules="[val => !!val || 'Autor é obrigatório']" />

            <q-input filled v-model="formCadastro.editora" label="Editora" required lazy-rules
              :rules="[val => !!val || 'Editora é obrigatória']" />

            <q-input filled v-model="formCadastro.data" label="Data de Lançamento" required lazy-rules
              :rules="[val => !!val || 'Data de Lançamento é obrigatória']"
              mask="##/##/####" />

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
          <div class="titulo-cadastro">Editar Livro</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="submitFormEditar" ref="formEditar">

            <q-input filled v-model="formEditar.nome" label="Nome do Livro" required lazy-rules
              :rules="[val => !!val || 'Nome do Livro é obrigatório']" />

            <q-input filled v-model="formEditar.autor" label="Autor" required lazy-rules
              :rules="[val => !!val || 'Autor é obrigatório']" />

            <q-input filled v-model="formEditar.editora" label="Editora" required lazy-rules
              :rules="[val => !!val || 'Editora é obrigatória']" />

            <q-input filled v-model="formEditar.data" label="Data de Lançamento" required lazy-rules
              :rules="[val => !!val || 'Data de Lançamento é obrigatória']"
              mask="##/##/####" />

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
            <q-btn flat color="negative" @click="showDeleteModal(props.row)" icon="delete" aria-label="Delete" />
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
      showModalCadastro: false,
      showModalEditar: false,
      showModalExcluir: false,
      rowToDelete: null,
      formCadastro: {
        codigo: '',
        nome: '',
        autor: '',
        editora: '',
        data: ''
      },
      formEditar: {
        codigo: '',
        nome: '',
        autor: '',
        editora: '',
        data: ''
      },
      selectedRow: null,
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
    submitFormCadastro() {
      if (this.$refs.formCadastro.validate()) {
        this.rows.push({
          codigo: this.formCadastro.codigo,
          nome: this.formCadastro.nome,
          autor: this.formCadastro.autor,
          editora: this.formCadastro.editora,
          data: this.formCadastro.data
        });
        this.showModalCadastro = false;
        this.$q.notify({
          color: 'positive',
          textColor: 'white',
          icon: 'check_circle',
          message: 'Livro cadastrado com sucesso.',
          position: 'top'
        });
        this.formCadastro = { nome: '', autor: '', editora: '', data: '' };
      }
    },
    editRow(row) {
      this.selectedRow = row;
      this.formEditar = { ...row };
      this.showModalEditar = true;
    },
    submitFormEditar() {
      if (this.$refs.formEditar.validate()) {
        const index = this.rows.findIndex(row => row.codigo === this.selectedRow.codigo);
        if (index !== -1) {
          this.rows.splice(index, 1, {
            codigo: this.formEditar.codigo,
            nome: this.formEditar.nome,
            autor: this.formEditar.autor,
            editora: this.formEditar.editora,
            data: this.formEditar.data
          });
          this.showModalEditar = false;
          this.$q.notify({
            color: 'positive',
            textColor: 'white',
            icon: 'check_circle',
            message: 'Livro atualizado com sucesso.',
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
      const index = this.rows.findIndex(row => row.codigo === this.rowToDelete.codigo);
      if (index !== -1) {
        this.rows.splice(index, 1);
        this.$q.notify({
          color: 'red',
          textColor: 'white',
          icon: 'delete',
          message: 'Livro excluído com sucesso!',
          position: 'top'
        });
      }
      this.showModalExcluir = false;
    },
    cancelDelete() {
      this.rowToDelete = null;
      this.showModalExcluir = false;
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

.modal-card,
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
  display: flex;
  justify-content: center;
  align-items: center;
}

.text-center {
  text-align: center;
}

.titulo-exclusao {
  font-size: 1.2rem;
  margin-bottom: 16px;
}

.button-exclusao {
  display: flex;
  justify-content: center;
}

.center-width {
  width: 100%;
}
</style>
