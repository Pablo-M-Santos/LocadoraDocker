<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;" class="buttonCadastrar" @click="showModalCadastro = true">
        CADASTRAR LOCATÁRIO
      </q-btn>
    </div>

    <!-- Barra de Pesquisa -->
    <div class="container">
      <div class="pesquisa">
        <q-input filled v-model="search" placeholder="Pesquisar Locatário" class="pesquisa" @input="onSearch">
          <template v-slot:prepend>
            <q-icon name="search" />
          </template>
        </q-input>
      </div>
      <q-btn class="button-pesquisar" label="PESQUISAR" @click="onSearch" />
    </div>

    <!-- Modal Cadastro -->
    <q-dialog v-model="showModalCadastro">
      <q-card class="modal-card">
        <q-card-section>
          <div class="titulo-cadastro">Cadastro de Locatário</div>
        </q-card-section>

        <q-card-section>
          <q-form>
            <div class="form-grid">
              <q-input filled v-model="newRenter.name" label="Nome" required lazy-rules />
              <q-input filled v-model="newRenter.email" label="Email" type="email" required lazy-rules />
              <q-input filled v-model="newRenter.telephone" label="Celular" required lazy-rules />
              <q-input filled v-model="newRenter.address" label="Endereço" required lazy-rules />
              <q-input filled v-model="newRenter.cpf" label="CPF" required lazy-rules type="number" />
            </div>

            <div class="button-container">
              <q-btn type="submit" label="CADASTRAR" @click="saveNewRenter" class="center-width q-mt-md" />
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
            <q-input filled v-model="InfosEdit.name" label="Nome" readonly />
            <q-input filled v-model="InfosEdit.email" label="Email" readonly />
            <q-input filled v-model="InfosEdit.telephone" label="Celular" readonly />
            <q-input filled v-model="InfosEdit.address" label="Endereço" readonly />
            <q-input filled v-model="InfosEdit.cpf" label="CPF" readonly />

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
          <q-form>
            <div class="form-grid">
              <q-input filled v-model="formEdit.name" label="Nome" required lazy-rules />
              <q-input filled v-model="formEdit.email" label="Email" type="email" required lazy-rules />
              <q-input filled v-model="formEdit.telephone" label="Celular" required lazy-rules />
              <q-input filled v-model="formEdit.address" label="Endereço" required lazy-rules />
              <q-input filled v-model="formEdit.cpf" label="CPF" required lazy-rules />
            </div>

            <div class="button-container">
              <q-btn type="submit" label="ATUALIZAR" @click="saveEdit" class="custom-button q-mt-md" />
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
      <q-table class="custom-table" :rows="filteredRows" :columns="columns" row-key="codigo" :pagination="pagination">
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
<script setup>
import { useQuasar, Notify } from 'quasar';
import { ref, computed, onMounted } from 'vue';
import { api, authenticate } from 'src/boot/axios.js';

const $q = useQuasar();

const showModalCadastro = ref(false);
const showModalSobre = ref(false);
const showModalEditar = ref(false);
const showModalExcluir = ref(false);
const rowToDelete = ref(null);
const search = ref('');
const newRenter = ref({
  name: '',
  email: '',
  telephone: '',
  address: '',
  cpf: '',
});
const formEdit = ref({
  name: '',
  email: '',
  telephone: '',
  address: '',
  cpf: '',
});
const selectedRow = ref(null);
const InfosEdit = ref({});
const rows = ref([]);
const columns = [
  { name: 'name', required: true, label: 'Nome do locatário', align: 'center', field: row => row.name, format: val => `${val}` },
  { name: 'email', align: 'center', label: 'Email', field: 'email' },
  { name: 'telephone', align: 'center', label: 'Telefone', field: 'telephone' },
  { name: 'actions', align: 'center', label: 'Ações', field: 'actions' },
];
const pagination = ref({ page: 1, rowsPerPage: 5 });

onMounted(() => {
  authenticate()
    .then(() => {
      console.log("Conectado com API");
      getRows();
    })
    .catch(error => {
      console.error('Erro na autenticação:', error);
      showNotification('negative', 'Erro na autenticação!');
    });
});

const getRows = () => {
  api.get('/renter')
    .then(response => {
      if (Array.isArray(response.data.content)) {
        rows.value = response.data.content;
        showNotification('positive', 'Dados obtidos com sucesso!');
      } else {
        console.error('A resposta da API não é um array:', response.data);
        rows.value = [];
      }
    })
    .catch(error => {
      showNotification('negative', 'Erro ao obter dados!');
      console.error('Erro ao obter dados:', error);
    });
};

const saveNewRenter = async () => {
  const formattedRenter = {
    name: newRenter.value.name.trim(),
    email: newRenter.value.email.trim(),
    address: newRenter.value.address.trim(),
    telephone: newRenter.value.telephone.trim(),
    cpf: newRenter.value.cpf.trim(),
  };

  api.post('/renter', formattedRenter, {
    headers: {
      'Content-Type': 'application/json',
    },
  })
    .then(response => {
      rows.value.push(response.data);
      showModalCadastro.value = false;
      newRenter.value = { name: '', email: '', telephone: '', address: '', cpf: '' };
      showNotification('positive', 'Locatário criado com sucesso!');
    })
    .catch(error => {
      const errorMessage = error.response?.data?.userMessage || 'Erro ao cadastrar locatário!';
      console.error('Erro ao criar novo locatário:', error.response ? error.response.data : error.message);
      showNotification('negative', "Erro ao criar novo locatário");
    });
};

const saveEdit = () => {
  if (!formEdit.value.id) {
    showNotification('negative', 'Locatário não selecionado!');
    return;
  }

  api.put(`/renter`, formEdit.value)
    .then(() => {
      const index = rows.value.findIndex(r => r.id === formEdit.value.id);
      if (index !== -1) {
        rows.value[index] = { ...formEdit.value };
      }
      showNotification('positive', 'Locatário atualizado com sucesso!');
      showModalEditar.value = false;
    })
    .catch(error => {
      console.error('Erro ao editar locatário:', error);
      showNotification('negative', 'Erro ao atualizar locatário!');
    });
};

const showDetails = (row) => {
  getApi(row.id);
  selectedRow.value = row;
  showModalSobre.value = true;
};

const editRow = (row) => {
  showModalEditar.value = true;
  getApi(row.id).then(() => {
    formEdit.value = {
      id: InfosEdit.value.id,
      name: InfosEdit.value.name,
      email: InfosEdit.value.email,
      telephone: InfosEdit.value.telephone,
      address: InfosEdit.value.address,
      cpf: InfosEdit.value.cpf,
    };
  }).catch(error => {
    console.error('Erro ao obter dados para edição:', error);
    showNotification('negative', 'Erro ao obter dados para edição!');
  });
};

const showDeleteModal = (row) => {
  rowToDelete.value = row;
  showModalExcluir.value = true;
};

const confirmDelete = () => {
  const index = rows.value.findIndex(r => r.id === rowToDelete.value.id);
  if (index !== -1) {
    api.delete(`/renter/${rowToDelete.value.id}`)
      .then(() => {
        rows.value.splice(index, 1);
        showNotification('positive', 'Locatário excluído com sucesso!');
        showModalExcluir.value = false;
      })
      .catch(error => {
        console.error('Erro ao excluir:', error);
        showNotification('negative', 'Erro ao excluir locatário!');
      });
  }
};

const cancelDelete = () => {
  rowToDelete.value = null;
  showModalExcluir.value = false;
};

const onSearch = () => {
  console.log('Pesquisa atual:', search.value);
};

const filteredRows = computed(() => {
  const searchTerm = search.value.toLowerCase();
  return rows.value.filter(row =>
    row.name.toLowerCase().includes(searchTerm) ||
    row.email.toLowerCase().includes(searchTerm) ||
    row.telephone.toLowerCase().includes(searchTerm)
  );
});

const getApi = (id) => {
  return api.get(`/renter/${id}`)
    .then(response => {
      InfosEdit.value = response.data;
      return response.data;
    })
    .catch(error => {
      console.error('Erro', error);
      showNotification('negative', 'Erro ao obter dados do locatário!');
      throw error;
    });
};

const showNotification = (type, message) => {
  Notify.create({
    color: type === 'positive' ? 'green' : 'red',
    textColor: 'white',
    icon: type === 'positive' ? 'check_circle' : 'error',
    message: message,
    position: 'top',
  });
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

.container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  padding-bottom: 20px;
}

.q-input.pesquisa {
  font-size: 16px;
  font-weight: 800;
  color: rgba(0, 0, 0, 0.60);
}

.custom-table {
  max-width: 1300px;
  width: 100%;
  margin: 0 auto;
}

.pesquisa {
  display: flex;
  max-width: 1160px;
  height: 53px;
  border-radius: 4px;
  width: 100%;
  margin: 0 auto;
}

.button-pesquisar {
  font-size: 15px;
  font-weight: 800;
}

@media (max-width: 700px) {
  .button-pesquisar {
    display: none;
  }
}

</style>
