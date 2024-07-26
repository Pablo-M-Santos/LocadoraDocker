<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn class="buttonCadastrar" @click="showModalCadastro = true">
        CADASTRAR LOCATÁRIO
      </q-btn>
    </div>

    <!-- Modal Cadastro -->
    <q-dialog v-model="showModalCadastro">
      <q-card class="modal-card">
        <q-card-section>
          <div class="titulo-cadastro">Cadastro de Locatário</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="submitFormCadastro" ref="formCadastro">
            <div class="form-grid">
              <q-input filled v-model="formCadastro.nome" label="Nome" required lazy-rules :rules="nomeRules" />

              <q-input filled v-model="formCadastro.email" label="Email" type="email" required lazy-rules
                :rules="emailRules" />

              <q-input filled v-model="formCadastro.celular" label="Celular" required v-mask="'(##) # ####-####'"
                lazy-rules :rules="celularRules" />

              <q-input filled v-model="formCadastro.cidade" label="Cidade" required lazy-rules :rules="cidadeRules" />

              <q-input filled v-model="formCadastro.bairro" label="Bairro" required lazy-rules :rules="bairroRules" />

              <q-input filled v-model="formCadastro.numero" label="Número" type="number" required lazy-rules
                :rules="numeroRules" />

              <q-input filled v-model="formCadastro.uf" label="UF" required lazy-rules
                :rules="[val => val.length === 2 || 'A UF deve ter 2 letras']" />

              <q-input filled v-model="formCadastro.logadouro" label="Logadouro" required lazy-rules
                :rules="logadouroRules" />

              <q-input filled v-model="formCadastro.cpf" label="CPF" requiredlazy-rules :rules="cpfRules" />
            </div>

            <div class="button-container">
              <q-btn type="submit" label="CADASTRAR" class="center-width q-mt-md" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>

    <!-- Modal Sobre -->
    <q-dialog v-model="showModalSobre">
      <q-card class="modal-card">
        <q-card-section>
          <div class="titulo-sobre text-center">Detalhes do Locatário</div>
        </q-card-section>

        <q-card-section>
          <div class="form-grid">
            <q-input filled v-model="selectedRow.codigo" label="Código" readonly />
            <q-input filled v-model="selectedRow.nome" label="Nome" readonly />
            <q-input filled v-model="selectedRow.email" label="Email" readonly />
            <q-input filled v-model="selectedRow.celular" label="Celular" readonly />
            <q-input filled v-model="selectedRow.cidade" label="Cidade" readonly />
            <q-input filled v-model="selectedRow.bairro" label="Bairro" readonly />
            <q-input filled v-model="selectedRow.numero" label="Número" readonly />
            <q-input filled v-model="selectedRow.uf" label="UF" readonly />
            <q-input filled v-model="selectedRow.logadouro" label="Logadouro" readonly />
            <q-input filled v-model="selectedRow.cpf" label="CPF" readonly />
          </div>
        </q-card-section>

        <q-card-actions class="button-sobre">
          <q-btn label="Fechar" @click="showModalSobre = false" />
        </q-card-actions>
      </q-card>
    </q-dialog>


    <!-- Modal Editar -->
    <q-dialog v-model="showModalEditar">
      <q-card class="modal-card">
        <q-card-section>
          <div class="titulo-cadastro">Editar Locatário</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="submitFormEditar" ref="formEditar">
            <div class="form-grid">
              <q-input filled v-model="formEditar.nome" label="Nome" required lazy-rules :rules="nomeRules" />

              <q-input filled v-model="formEditar.email" label="Email" type="email" required lazy-rules
                :rules="emailRules" />

              <q-input filled v-model="formEditar.celular" label="Celular" required v-mask="'(##) # ####-####'"
                lazy-rules :rules="celularRules" />

              <q-input filled v-model="formEditar.cidade" label="Cidade" required lazy-rules :rules="cidadeRules" />

              <q-input filled v-model="formEditar.bairro" label="Bairro" required lazy-rules :rules="bairroRules" />

              <q-input filled v-model="formEditar.numero" label="Número" type="number" required lazy-rules
                :rules="numeroRules" />

              <q-input filled v-model="formEditar.uf" label="UF" required lazy-rules
                :rules="[val => val.length === 2 || 'A UF deve ter 2 letras']" />

              <q-input filled v-model="formEditar.logadouro" label="Logadouro" required lazy-rules
                :rules="logadouroRules" />

              <q-input filled v-model="formEditar.cpf" label="CPF" requiredlazy-rules :rules="cpfRules" />
            </div>
            <div class="button-container">
              <q-btn type="submit" label="ATUALIZAR" class="custom-button q-mt-md" />
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
            <q-btn flat color="primary" @click="showDetails(props.row)" icon="visibility" aria-label="View" />
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
  name: 'LocatarioPage',
  data() {
    return {
      showModalCadastro: false,
      showModalSobre: false,
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
        { codigo: '1', nome: 'Pablo Moreira', email: 'pablo@gmail.com', celular: '(85) 9 8785-8860', cidade: 'Brasília', bairro: 'Santa Maria', numero: '036', uf: 'DF', logadouro: 'Coronel Aurelio', cpf: '684.617.550-49' },
        { codigo: '2', nome: 'Ana Silva', email: 'ana@gmail.com', celular: '(21) 9 9876-5432', cidade: 'Rio de Janeiro', bairro: 'Copacabana', numero: '123', uf: 'RJ', logadouro: 'Avenida Atlântica', cpf: '123.456.789-01' },
        { codigo: '3', nome: 'João Santos', email: 'joao@gmail.com', celular: '(11) 9 8765-4321', cidade: 'São Paulo', bairro: 'Vila Madalena', numero: '456', uf: 'SP', logadouro: 'Rua dos Três Irmãos', cpf: '987.654.321-00' },
        { codigo: '4', nome: 'Maria Oliveira', email: 'maria@gmail.com', celular: '(31) 9 7654-3210', cidade: 'Belo Horizonte', bairro: 'Savassi', numero: '789', uf: 'MG', logadouro: 'Avenida do Contorno', cpf: '345.678.901-23' },
        { codigo: '5', nome: 'Carlos Pereira', email: 'carlos@gmail.com', celular: '(61) 9 6543-2109', cidade: 'Brasília', bairro: 'Asa Norte', numero: '012', uf: 'DF', logadouro: 'Quadra 101', cpf: '234.567.890-12' },
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
    showDetails(row) {
      this.selectedRow = row;
      this.showModalSobre = true;
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
            email: this.formEditar.email,
            celular: this.formEditar.celular,
            cidade: this.formEditar.cidade,
            bairro: this.formEditar.bairro,
            numero: this.formEditar.numero,
            uf: this.formEditar.uf,
            logadouro: this.formEditar.logadouro,
            cpf: this.formEditar.cpf
          });
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
  width: 1050px;
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
