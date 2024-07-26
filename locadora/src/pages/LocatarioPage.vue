<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;" @click="showModalCadastro = true">
        <div class="buttonCadastrar">
          CADASTRAR LOCATÁRIO
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
            <div class="form-grid">
              <q-input filled v-model="formCadastro.nome" label="Nome" required lazy-rules :rules="[val => !!val || 'Nome é obrigatório',
              val => val.length >= 5 || 'Nome deve ter pelo menos 5 caracteres',
              val => /^[a-zA-Z\s]+$/.test(val) || 'Nome deve conter apenas letras e espaços']" />

              <q-input filled v-model="formCadastro.email" label="Email" type="email" required lazy-rules
                :rules="[val => !!val || 'Email é obrigatório', val => /.+@.+\..+/.test(val) || 'Email inválido']" />

              <q-input filled v-model="formCadastro.celular" label="Celular" required lazy-rules
                :rules="[val => !!val || 'Celular é obrigatório']" />

              <q-input filled v-model="formCadastro.cidade" label="Cidade" required lazy-rules
                :rules="[val => !!val || 'Cidade é obrigatória']" />

              <q-input filled v-model="formCadastro.bairro" label="Bairro" required lazy-rules
                :rules="[val => !!val || 'Bairro é obrigatório']" />

              <q-input filled v-model="formCadastro.numero" label="Número" type="number" required lazy-rules
                :rules="[val => !!val || 'Número é obrigatório']" />

              <q-input filled v-model="formCadastro.uf" label="UF" required lazy-rules
                :rules="[val => !!val || 'UF é obrigatória']" />

              <q-input filled v-model="formCadastro.logadouro" label="Logadouro" required lazy-rules
                :rules="[val => !!val || 'Logadouro é obrigatório']" />

              <q-input filled v-model="formCadastro.cpf" label="CPF" required lazy-rules
                :rules="[val => !!val || 'CPF é obrigatório']" />
            </div>

            <div class="button-container">
              <q-btn type="submit" label="Salvar" class="center-width q-mt-md" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>

    <!-- Table -->
    <div class="table-container">
      <q-table :rows="rows" :columns="columns" row-key="codigo" :pagination="pagination" :filter="filter">
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
      showModalCadastro: false,
      showModalEditar: false,
      showModalExcluir: false,
      rowToDelete: null,
      formCadastro: {
        nome: '',
        email: '',
        celular: '',
        cidade: '',
        bairro: '',
        numero: '',
        uf: '',
        logadouro: '',
        cpf: ''
      },
      formEditar: {
        nome: '',
        email: '',
        celular: '',
        cidade: '',
        bairro: '',
        numero: '',
        uf: '',
        logadouro: '',
        cpf: ''
      },
      selectedRow: null,
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
    submitFormCadastro() {
      if (this.$refs.formCadastro.validate()) {
        this.rows.push({
          codigo: (this.rows.length + 1).toString(),
          nome: this.formCadastro.nome,
          email: this.formCadastro.email,
          celular: this.formCadastro.celular,
          cidade: this.formCadastro.cidade,
          bairro: this.formCadastro.bairro,
          numero: this.formCadastro.numero,
          uf: this.formCadastro.uf,
          logadouro: this.formCadastro.logadouro,
          cpf: this.formCadastro.cpf
        });
        this.showModalCadastro = true;
        this.$q.notify({
          color: 'positive',
          textColor: 'white',
          icon: 'check_circle',
          message: 'Locatário cadastrado com sucesso.',
          position: 'top'
        });
        this.formCadastro = {
          nome: '',
          email: '',
          celular: '',
          cidade: '',
          bairro: '',
          numero: '',
          uf: '',
          logadouro: '',
          cpf: ''
        };
      }
    },
    editRow(row) {
      this.selectedRow = row;
      this.formEditar = { ...row };
      this.showModalEditar = true;
    },
    submitFormEditar() {
      if (this.$refs.formEditar.validate()) {
        const index = this.rows.findIndex(r => r.codigo === this.selectedRow.codigo);
        if (index !== -1) {
          this.rows.splice(index, 1, this.formEditar);
          this.showModalEditar = false;
          this.$q.notify({
            color: 'positive',
            textColor: 'white',
            icon: 'check_circle',
            message: 'Locatário atualizado com sucesso.',
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
          message: 'Locatário excluído com sucesso!',
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

.modal-card {
  width: 90vw;
  max-width: 1050px;
  color: white;
}

.titulo-cadastro {
  font-size: 26px;
  text-align: center;
  margin-bottom: 16px;
  color: black
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
  grid-column: span 5;
}
</style>
