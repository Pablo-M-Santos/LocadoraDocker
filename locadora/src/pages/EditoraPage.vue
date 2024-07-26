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
import { ref, onMounted } from 'vue';
import { api, authenticate } from 'src/boot/axios.js';

export default {
  name: 'EditoraPage',
  setup() {
    const showModalCadastro = ref(false);
    const showModalEditar = ref(false);
    const showModalExcluir = ref(false);
    const rowToDelete = ref(null);
    const formCadastro = ref({ nome: '', telefone: '', email: '' });
    const formEditar = ref({ nome: '', telefone: '', email: '' });
    const selectedRow = ref(null);

    const columns = [
      { name: 'name', required: true, label: 'Nome da Editora', align: 'center', field: row => row.name, format: val => `${val}` },
      { name: 'actions', align: 'center', label: 'Ações', field: 'actions' },
    ];
    const rows = ref([]);

    const getRows = (srch = '') => {
      api.get('/publisher', {})
        .then(response => {
          if (Array.isArray(response.data.content)) {
            rows.value = response.data.content;
            console.log("Dados obtidos com sucesso");
          } else {
            console.error('A resposta da API não é um array:', response.data);
            rows.value = [];
          }
          console.log('Resposta da API:', response.data);
        })
        .catch(error => {
          console.error("Erro ao obter dados:", error);
        });
    }

    const pagination = ref({ page: 1, rowsPerPage: 5 });
    const filter = ref('');

    onMounted(() => {
      authenticate()
        .then(() => {
          console.log("Conectado com API");
          getRows();
        })
        .catch(error => {
          console.error('Erro na autenticação:', error);
        });
    });

    const submitFormCadastro = () => {
      if (this.$refs.formCadastro.validate()) {
        rows.value.push({
          codigo: (rows.value.length + 1).toString().padStart(3, '0'),
          nome: formCadastro.value.nome,
          telefone: formCadastro.value.telefone,
          email: formCadastro.value.email
        });
        this.$q.notify({
          color: 'green',
          textColor: 'white',
          icon: 'check',
          message: 'Cadastro realizado com sucesso!',
          position: 'top'
        });
        showModalCadastro.value = false;
        formCadastro.value = { nome: '', telefone: '', email: '' };
      }
    };

    const submitFormEditar = () => {
      if (this.$refs.formEditar.validate()) {
        const index = rows.value.findIndex(row => row.codigo === selectedRow.value.codigo);
        if (index !== -1) {
          rows.value[index] = { ...formEditar.value, codigo: selectedRow.value.codigo };
          this.$q.notify({
            color: 'blue',
            textColor: 'white',
            icon: 'edit',
            message: 'Dados atualizados com sucesso!',
            position: 'top'
          });
          showModalEditar.value = false;
          formEditar.value = { nome: '', telefone: '', email: '' };
        }
      }
    };

    const showDeleteModal = (row) => {
      rowToDelete.value = row;
      showModalExcluir.value = true;
    };

    const confirmDelete = () => {
      rows.value = rows.value.filter(row => row !== rowToDelete.value);
      this.$q.notify({
        color: 'red',
        textColor: 'white',
        icon: 'delete',
        message: 'Registro excluído com sucesso!',
        position: 'top'
      });
      showModalExcluir.value = false;
    };

    const cancelDelete = () => {
      showModalExcluir.value = false;
    };

    const editRow = (row) => {
      selectedRow.value = row;
      formEditar.value = { ...row };
      showModalEditar.value = true;
    };

    return {
      showModalCadastro,
      showModalEditar,
      showModalExcluir,
      formCadastro,
      formEditar,
      rows,
      columns,
      pagination,
      filter,
      submitFormCadastro,
      submitFormEditar,
      showDeleteModal,
      confirmDelete,
      cancelDelete,
      editRow
    };
  }
};
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

.buttonCadastrar {
  font-size: 16px;
  font-weight: bold;
}

.modal-card {
  width: 500px;
}

.modal-card-exclusao {
  width: 400px;
}

.titulo-cadastro {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}

.button-container {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}

.center-width {
  width: 100%;
}

.table-container {
  margin-top: 16px;
}

.titulo-exclusao {
  font-size: 18px;
  margin: 16px 0;
}

.button-exclusao {
  display: flex;
  justify-content: center;
}
</style>
