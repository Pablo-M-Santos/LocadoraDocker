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
            val => val.length >= 20 || 'Nome deve ter pelo menos 20 caracteres',
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
      showModalCadastro: false,
      showModalEditar: false,
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
    handleCheckboxEditar(type) {
      this.formEditar.tipo = [type];
    },
    submitFormCadastro() {
      if (this.$refs.formCadastro.validate()) {
        if (this.formCadastro.tipo.length === 0) {
          this.$q.notify({
            color: 'negative',
            textColor: 'white',
            icon: 'error',
            message: 'Pelo menos uma opção deve ser selecionada.',
            position: 'top'
          });
          return;
        }
        this.$q.notify({
          color: 'green',
          textColor: 'white',
          icon: 'check',
          message: 'Cadastro realizado com sucesso!',
          position: 'top'
        });
        console.log('Formulário de cadastro enviado', this.formCadastro);
        this.showModalCadastro = false;
        this.formCadastro = { nome: '', email: '', senha: '', tipo: [] }; // Reset form
      }
    },
    submitFormEditar() {
      if (this.$refs.formEditar.validate()) {
        if (this.formEditar.tipo.length === 0) {
          this.$q.notify({
            color: 'negative',
            textColor: 'white',
            icon: 'error',
            message: 'Pelo menos uma opção deve ser selecionada.',
            position: 'top'
          });
          return;
        }
        const index = this.rows.findIndex(row => row.codigo === this.selectedRow.codigo);
        if (index !== -1) {
          this.rows[index] = { ...this.formEditar, codigo: this.selectedRow.codigo, nivelPermissao: this.formEditar.tipo[0] }; // Preserve codigo and update nivelPermissao
        }
        this.$q.notify({
          color: 'green',
          textColor: 'white',
          icon: 'check',
          message: 'Dados atualizados com sucesso!',
          position: 'top'
        });
        console.log('Formulário de edição enviado', this.formEditar);
        this.showModalEditar = false;
        this.formEditar = { nome: '', email: '', senha: '', tipo: [] }; // Reset form
      }
    },
    editRow(row) {
      this.selectedRow = row;
      this.formEditar = { ...row, tipo: [row.nivelPermissao.toLowerCase()] }; // Preencher o formulário com os dados da linha selecionada e selecionar o tipo
      this.showModalEditar = true;
    },
    deleteRow(row) {
      this.rows = this.rows.filter(r => r.codigo !== row.codigo);
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

.modal-card {
  width: 691px;
  border-radius: 20px;
}

.titulo-cadastro {
  display: flex;
  justify-content: center;
  font-size: 32px;
}

.checkbox {
  display: flex;
  justify-content: center;
  gap: 50px;
}

.button-container {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
