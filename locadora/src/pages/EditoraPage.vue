<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;" @click="showModalCadastro = true">
        <div class="buttonCadastrar">
          CADASTRAR EDITORA
        </div>
      </q-btn>
    </div>

    <!-- Modal Cadastro -->
    <q-dialog v-model="showModalCadastro">
      <q-card class="modal-card">
        <q-card-section>
          <div class="titulo-cadastro">Cadastro de Editora</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="submitFormCadastro" ref="formCadastro">
            <q-input filled v-model="formCadastro.nome" label="Nome da Editora" required lazy-rules :rules="[val => !!val || 'Nome da Editora é obrigatório',
            val => val.length >= 5 || 'Nome da Editora deve ter pelo menos 5 caracteres']" />

            <q-input filled v-model="formCadastro.telefone" label="Telefone" type="tel" required lazy-rules :rules="[val => !!val || 'Telefone é obrigatório',
            val => /^\d{10,15}$/.test(val) || 'Telefone deve ter entre 10 e 15 dígitos']" />

            <q-input filled v-model="formCadastro.email" label="Email" type="email" required lazy-rules
              :rules="[val => !!val || 'Email é obrigatório', val => /.+@.+\..+/.test(val) || 'Email inválido']" />

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
            <q-input filled v-model="formEditar.nome" label="Nome da Editora" required lazy-rules :rules="[val => !!val || 'Nome é obrigatório',
            val => val.length >= 5 || 'Nome deve ter pelo menos 5 caracteres']" />

            <q-input filled v-model="formEditar.telefone" label="Telefone" type="tel" required lazy-rules :rules="[val => !!val || 'Telefone é obrigatório',
            val => /^\d{10,15}$/.test(val) || 'Telefone deve ter entre 10 e 15 dígitos']" />

            <q-input filled v-model="formEditar.email" label="Email" type="email" required lazy-rules
              :rules="[val => !!val || 'Email é obrigatório', val => /.+@.+\..+/.test(val) || 'Email inválido']" />

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

    <!-- Table -->
    <div class="table-container">
      <q-table :rows="rows" :columns="columns" row-key="codigo" :pagination="pagination" :filter="filter">
        <template v-slot:body-cell-actions="props">
          <q-td :props="props" class="text-center">
            <q-btn flat color="primary" @click="editRow(props.row)" icon="edit" aria-label="Edit" />
            <q-btn flat color="negative" @click="showDeleteModal(props.row)" icon="delete" aria-label="Delete" />
          </q-td>
        </template>
      </q-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EditoraPage',
  data() {
    return {
      showModalCadastro: false,
      showModalEditar: false,
      showModalExcluir: false,
      rowToDelete: null,
      formCadastro: {
        nome: '',
        telefone: '',
        email: '',
      },
      formEditar: {
        nome: '',
        telefone: '',
        email: ''
      },
      selectedRow: null,
      rows: [
        { codigo: '001', nome: 'Editora A', telefone: '123-456-7890', email: 'contato@editoraa.com' },
        { codigo: '002', nome: 'Editora B', telefone: '234-567-8901', email: 'contato@editorab.com' },
        { codigo: '003', nome: 'Editora C', telefone: '345-678-9012', email: 'contato@editorac.com' },
        { codigo: '004', nome: 'Editora D', telefone: '456-789-0123', email: 'contato@editorad.com' },
        { codigo: '005', nome: 'Editora E', telefone: '567-890-1234', email: 'contato@editorae.com' },
        { codigo: '006', nome: 'Editora F', telefone: '678-901-2345', email: 'contato@editoraf.com' },
        { codigo: '007', nome: 'Editora G', telefone: '789-012-3456', email: 'contato@editorag.com' },
        { codigo: '008', nome: 'Editora H', telefone: '890-123-4567', email: 'contato@editorah.com' },
        { codigo: '009', nome: 'Editora I', telefone: '901-234-5678', email: 'contato@editorai.com' },
        { codigo: '010', nome: 'Editora J', telefone: '012-345-6789', email: 'contato@editoraj.com' }
      ],
      columns: [
        { name: 'codigo', required: true, label: 'Código', align: 'center', field: row => row.codigo },
        { name: 'nome', align: 'center', label: 'Nome da Editora', field: row => row.nome },
        { name: 'telefone', align: 'center', label: 'Telefone', field: row => row.telefone },
        { name: 'email', align: 'center', label: 'Email', field: row => row.email },
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
          codigo: (this.rows.length + 1).toString().padStart(3, '0'),
          nome: this.formCadastro.nome,
          telefone: this.formCadastro.telefone,
          email: this.formCadastro.email
        });
        this.$q.notify({
          color: 'green',
          textColor: 'white',
          icon: 'check',
          message: 'Cadastro realizado com sucesso!',
          position: 'top'
        });
        this.showModalCadastro = false;
        this.formCadastro = { nome: '', telefone: '', email: '' };
      }
    },
    submitFormEditar() {
      if (this.$refs.formEditar.validate()) {
        const index = this.rows.findIndex(row => row.codigo === this.selectedRow.codigo);
        if (index !== -1) {
          this.rows[index] = { ...this.formEditar, codigo: this.selectedRow.codigo };
        }
        this.$q.notify({
          color: 'green',
          textColor: 'white',
          icon: 'check',
          message: 'Dados atualizados com sucesso!',
          position: 'top'
        });
        this.showModalEditar = false;
        this.formEditar = { nome: '', telefone: '', email: '' };
      }
    },
    editRow(row) {
      this.selectedRow = row;
      this.formEditar = { ...row };
      this.showModalEditar = true;
    },
    showDeleteModal(row) {
      this.rowToDelete = row;
      this.showModalExcluir = true;
    },
    confirmDelete() {
      if (this.rowToDelete) {
        this.rows = this.rows.filter(row => row.codigo !== this.rowToDelete.codigo);
        this.$q.notify({
          color: 'red',
          textColor: 'white',
          icon: 'delete',
          message: 'Editora excluída com sucesso!',
          position: 'top'
        });
        this.rowToDelete = null;
        this.showModalExcluir = false;
      }
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
  padding: 20px;
}

.containerButton {
  text-align: center;
  margin-bottom: 20px;
}

.modal-card, .modal-card-exclusao {
  width: 400px;
}

.titulo-cadastro, .titulo-exclusao {
  text-align: center;
  font-size: 1.5em;
  margin-bottom: 20px;
}

.button-container, .button-exclusao {
  display: flex;
  justify-content: center;
}

.center-width {
  width: 100%;
}

.table-container {
  margin-top: 20px;
}

.text-center {
  text-align: center;
}

</style>
