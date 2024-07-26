<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;" @click="showModalCadastro = true">
        <div class="buttonCadastrar">
          CADASTRAR USUÁRIO
        </div>
      </q-btn>
    </div>

    <!-- Modal Cadastro -->
    <q-dialog v-model="showModalCadastro">
      <q-card class="modal-card">
        <q-card-section>
          <div class="titulo-cadastro">Cadastro de Usuário</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="submitFormCadastro" ref="formCadastro">

            <q-input filled v-model="formCadastro.nome" label="Nome" required lazy-rules :rules="[val => !!val || 'Nome é obrigatório',
            val => val.length >= 5 || 'Nome deve ter pelo menos 5 caracteres',
            val => /^[a-zA-Z\s]+$/.test(val) || 'Nome deve conter apenas letras e espaços']" />

            <q-input filled v-model="formCadastro.email" label="Email" type="email" required lazy-rules
              :rules="[val => !!val || 'Email é obrigatório', val => /.+@.+\..+/.test(val) || 'Email inválido']" />

            <q-input filled v-model="formCadastro.senha" label="Senha" type="password" required lazy-rules
              :rules="[val => !!val || 'Senha é obrigatória']" />

            <div class="q-mt-md checkbox">
              <q-checkbox v-model="formCadastro.tipo" val="editor" label="Editor"
                :disable="formCadastro.tipo.includes('leitor')" @input="handleCheckboxCadastro('editor')" />
              <q-checkbox v-model="formCadastro.tipo" val="leitor" label="Leitor"
                :disable="formCadastro.tipo.includes('editor')" @input="handleCheckboxCadastro('leitor')" />
            </div>

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
          <div class="titulo-cadastro">Editar Usuário</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="submitFormEditar" ref="formEditar">
            <q-input filled v-model="formEditar.nome" label="Nome" required lazy-rules :rules="[val => !!val || 'Nome é obrigatório',
            val => val.length >= 5 || 'Nome deve ter pelo menos 5 caracteres',
            val => /^[a-zA-Z\s]+$/.test(val) || 'Nome deve conter apenas letras e espaços']" />

            <q-input filled v-model="formEditar.email" label="Email" type="email" required lazy-rules
              :rules="[val => !!val || 'Email é obrigatório', val => /.+@.+\..+/.test(val) || 'Email inválido']" />

            <q-input filled v-model="formEditar.senha" label="Senha" type="password" required lazy-rules
              :rules="[val => !!val || 'Senha é obrigatória']" />

            <div class="q-mt-md checkbox">
              <q-checkbox v-model="formEditar.tipo" val="editor" label="Editor"
                :disable="formEditar.tipo.includes('leitor')" @input="handleCheckboxEditar('editor')" />
              <q-checkbox v-model="formEditar.tipo" val="leitor" label="Leitor"
                :disable="formEditar.tipo.includes('editor')" @input="handleCheckboxEditar('leitor')" />
            </div>

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

    <!-- Tabela de usuários -->
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
  name: 'UsuarioPage',
  data() {
    return {
      showModalCadastro: false,
      showModalEditar: false,
      showModalExcluir: false,
      rowToDelete: null,
      formCadastro: {
        nome: '',
        email: '',
        senha: '',
        tipo: []
      },
      formEditar: {
        nome: '',
        email: '',
        senha: '',
        tipo: []
      },
      selectedRow: null,
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
    handleCheckboxCadastro(type) {
      this.formCadastro.tipo = [type];
    },
    submitFormCadastro() {
      if (this.$refs.formCadastro.validate()) {
        if (this.formCadastro.tipo.length === 0) {
          this.$q.notify({
            color: 'negative',
            textColor: 'white',
            icon: 'warning',
            message: 'Selecione um tipo de permissão.',
            position: 'top'
          });
          return;
        }
        this.rows.push({
          codigo: this.rows.length + 1,
          nome: this.formCadastro.nome,
          email: this.formCadastro.email,
          senha: this.formCadastro.senha,
          nivelPermissao: this.formCadastro.tipo[0]
        });
        this.showModalCadastro = false;
        this.$q.notify({
          color: 'positive',
          textColor: 'white',
          icon: 'check_circle',
          message: 'Usuário cadastrado com sucesso.',
          position: 'top'
        });
        this.formCadastro = { nome: '', email: '', senha: '', tipo: [] };
      }
    },
    handleCheckboxEditar(type) {
      this.formEditar.tipo = [type];
    },
    editRow(row) {
      this.selectedRow = row;
      this.formEditar = { ...row, tipo: [row.nivelPermissao] };
      this.showModalEditar = true;
    },
    submitFormEditar() {
      if (this.$refs.formEditar.validate()) {
        if (this.formEditar.tipo.length === 0) {
          this.$q.notify({
            color: 'negative',
            textColor: 'white',
            icon: 'warning',
            message: 'Selecione um tipo de permissão.',
            position: 'top'
          });
          return;
        }
        const index = this.rows.findIndex(row => row.codigo === this.selectedRow.codigo);
        if (index !== -1) {
          this.rows.splice(index, 1, {
            codigo: this.selectedRow.codigo,
            nome: this.formEditar.nome,
            email: this.formEditar.email,
            senha: this.formEditar.senha,
            nivelPermissao: this.formEditar.tipo[0]
          });
          this.showModalEditar = false;
          this.$q.notify({
            color: 'positive',
            textColor: 'white',
            icon: 'check_circle',
            message: 'Usuário atualizado com sucesso.',
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
          message: 'Usuário excluído com sucesso!',
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
