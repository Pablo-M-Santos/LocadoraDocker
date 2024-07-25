<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;" @click="showModal = true">
        <div class="buttonCadastrar">
          CADASTRAR EDITORA
        </div>
      </q-btn>
    </div>

    <!-- Modal -->
    <q-dialog v-model="showModal">
      <q-card>
        <q-card-section>
          <div class="text-h6">Cadastro de Usuário</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="submitForm" ref="form">
            <!-- Campo Nome -->
            <q-input filled v-model="form.nome" label="Nome" required lazy-rules
              :rules="[val => !!val || 'Nome é obrigatório',
                       val => val.length >= 20 || 'Nome deve ter pelo menos 20 caracteres',
                       val => /^[a-zA-Z\s]+$/.test(val) || 'Nome deve conter apenas letras e espaços']" />

            <!-- Campo Email -->
            <q-input filled v-model="form.email" label="Email" type="email" required lazy-rules
              :rules="[val => !!val || 'Email é obrigatório', val => /.+@.+\..+/.test(val) || 'Email inválido']" />

            <!-- Campo Senha -->
            <q-input filled v-model="form.senha" label="Senha" type="password" required lazy-rules
              :rules="[val => !!val || 'Senha é obrigatória']" />

            <!-- Checkbox Tipo -->
            <div class="q-mt-md">
              <q-checkbox v-model="form.tipo" val="editor" label="Editor"
                :disable="form.tipo.includes('leitor')"
                @input="handleCheckbox('editor')" />
              <q-checkbox v-model="form.tipo" val="leitor" label="Leitor"
                :disable="form.tipo.includes('editor')"
                @input="handleCheckbox('leitor')" />
            </div>

            <!-- Botão de Cadastro -->
            <q-btn type="submit" label="CADASTRAR" color="primary" class="full-width q-mt-md" />
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>

    <!-- Table -->
    <div class="table-container">
      <q-table :rows="rows" :columns="columns" row-key="codigo" :pagination="pagination" :filter="filter">
        <template v-slot:body-cell-actions="props">
          <q-td :props="props" class="text-center">
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
  name: 'EditoraPage',
  data() {
    return {
      showModal: false,
      form: {
        nome: '',
        email: '',
        senha: '',
        tipo: []
      },
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
    handleCheckbox(type) {
      // Garantir que apenas uma opção possa ser selecionada
      if (this.form.tipo.length > 1) {
        this.form.tipo = [type];
      }
    },
    submitForm() {
      if (this.$refs.form.validate()) {
        if (this.form.tipo.length === 0) {
          this.$q.notify({
            color: 'negative',
            textColor: 'white',
            icon: 'error',
            message: 'Pelo menos uma opção deve ser selecionada.'
          });
          return;
        }
        this.$q.notify({
          color: 'green',
          textColor: 'white',
          icon: 'check',
          message: 'Cadastro realizado com sucesso!'
        });
        console.log('Formulário enviado', this.form);
        this.showModal = false;
      }
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
