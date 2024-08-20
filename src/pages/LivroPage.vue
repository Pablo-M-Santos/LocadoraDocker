<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;" @click="openRegisterDialog">
        <div class="buttonCadastrar">
          CADASTRAR LIVRO
        </div>
      </q-btn>
    </div>

    <!-- Barra de Pesquisa -->
    <div class="container">
      <div class="pesquisa">
        <q-input filled v-model="search" placeholder="Pesquisar Livro" class="pesquisa" @input="onSearch">
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
          <div class="titulo-cadastro">Cadastro de Livro</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="registerAction">
            <q-input v-model="bookToCreate.name" label="Título do livro" filled lazy-rules
              :rules="[val => val && val.length > 4 || 'É necessário ter mais de quatro caracteres']" />
            <q-input v-model="bookToCreate.author" label="Autor" filled lazy-rules
              :rules="[val => val && val.length > 3 || 'É necessário ter mais de três caracteres']" />
            <q-input v-model="bookToCreate.totalQuantity" label="Quantidade" type="number" filled lazy-rules
              :rules="[val => val > 0 || 'É necessário ter pelo menos 1']" />
            <q-input v-model="bookToCreate.launchDate" label="Data de lançamento" type="date" mask="####-##-##"
              fill-mask filled lazy-rules :rules="[val => val && val.length >= 6 || 'Adicione uma data válida']" />
            <q-input v-model="bookToCreate.publisherId" label="ID da editora" type="number" filled lazy-rules />

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
          <q-form>
            <q-input v-model="bookToEdit.id" label="Id" />
            <q-input v-model="bookToEdit.name" label="Nome" />
            <q-input v-model="bookToEdit.author" label="Autor" />
            <q-input v-model="bookToEdit.totalQuantity" label="Quantidade total" type="number" />
            <q-input v-model="bookToEdit.launchDate" label="Data de lançamento" type="date" />

            <q-input v-model="bookToEdit.publisherId" label="Id da editora" type="number" />

            <div class="button-container">
              <q-btn type="submit" label="ATUALIZAR" @click="saveEdit" class="center-width q-mt-md" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>

    <!-- Modal Sobre -->
    <q-dialog v-model="showModalSobre">
      <q-card class="modal-card">
        <q-card-section>
          <div class="titulo-sobre text-center">Detalhes da Editora</div>
        </q-card-section>
        <q-card-section>
          <div class="form-grid">
            <q-input filled v-model="InfosEdit.id" label="ID" readonly />
            <q-input filled v-model="InfosEdit.name" label="Nome" readonly />
            <q-input filled v-model="InfosEdit.author" label="Autor" readonly />
            <q-input filled v-model="InfosEdit.totalQuantity" label="Quantidade Total:" readonly />
            <q-input filled v-model="InfosEdit.launchDate" label="Data de lançamento:" readonly />
          </div>
        </q-card-section>
        <q-card-actions class="button-sobre">
          <q-btn label="Fechar" @click="showModalSobre = false" />
        </q-card-actions>
      </q-card>
    </q-dialog>


    <!-- Modal Exclusão -->
    <q-dialog v-model="showModalExcluir">
      <q-card class="modal-card-exclusao">
        <q-card-section class="text-center">
          <div class="circulo">
            <i class="fa-solid fa-exclamation"></i>
          </div>
          <h3 class="titulo-exclusao"> Você tem certeza que deseja excluir o livro "{{ rowToDelete.name }}"?</h3>
        </q-card-section>

        <q-card-actions class="button-exclusao">
          <q-btn label="SIM" color="negative" @click="confirmDelete" class="q-mr-sm" />
          <q-btn label="NÃO" color="secondary" @click="cancelDelete" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- Tabela de livros -->
    <div class="table-container">
      <q-table class="custom-table" :rows="filteredRows" :columns="columns" row-key="id" :pagination="pagination">
        <template v-slot:body-cell="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.value }}</div>
          </q-td>
        </template>
        <template v-slot:body-cell-actions="props">
          <q-td clas :props="props" style="vertical-align: middle;">
            <q-btn flat color="primary" @click="showDetails(props.row)" icon="visibility" aria-label="View" />
            <q-btn flat color="secondary" @click="editRow(props.row)" icon="edit" aria-label="Edit" />
            <q-btn flat color="negative" @click="showDeleteModal(props.row)" icon="delete" aria-label="Delete" />
          </q-td>
        </template>
      </q-table>
    </div>
  </div>
</template>

<script setup>
import { useQuasar } from 'quasar';
import { ref, computed, onMounted } from 'vue';
import { api, authenticate } from 'src/boot/axios.js';
import { Notify } from 'quasar';

const $q = useQuasar();

const InfosEdit = ref({});
const bookToCreate = ref({
  name: '',
  author: '',
  totalQuantity: '',
  launchDate: '',
  publisherId: ''
});

const showModalCadastro = ref(false);
const showModalEditar = ref(false);
const showModalExcluir = ref(false);
const showModalSobre = ref(false);
const rowToDelete = ref(null);
const search = ref('');
const rows = ref([]);
const columns = ref([
  { name: 'id', align: 'center', label: 'Id', field: 'id' },
  { name: 'title', required: true, label: 'Título', align: 'center', field: row => row.name, format: val => `${val}` },
  { name: 'author', align: 'center', label: 'Autor', field: 'author' },
  { name: 'availableQuantity', align: 'center', label: 'Disponíveis', field: 'availableQuantity' },
  { name: 'inUseQuantity', align: 'center', label: 'Alugados', field: 'inUseQuantity' },
  { name: 'actions', align: 'center', label: 'Ações', field: 'actions' },
]);
const pagination = ref({ page: 1, rowsPerPage: 5 });

const getRows = () => {
  api.get('/book')
    .then(response => {
      if (Array.isArray(response.data.content)) {
        rows.value = response.data.content;
        showNotification('positive', "Dados obtidos com sucesso!");
      } else {
        console.error('A resposta da API não é um array:', response.data);
        rows.value = [];
      }
    })
    .catch(error => {
      showNotification('negative', "Erro ao obter dados!");
      console.error("Erro ao obter dados:", error);
    });
};

const getApi = (id) => {
  return api.get(`/book/${id}`)
    .then(response => {
      InfosEdit.value = response.data;
      return response.data;
    })
    .catch(error => {
      console.error("Erro", error);
      throw error;
    });
};


const showDetails = (row) => {
  getApi(row.id);
  showModalSobre.value = true;
};

const bookToEdit = ref({
  id: '',
  name: '',
  author: '',
  totalQuantity: '',
  launchDate: '',
  publisherId: ''
});


const editRow = (row) => {
  getApi(row.id).then(() => {
    bookToEdit.value = {
      id: InfosEdit.value.id,
      name: InfosEdit.value.name,
      author: InfosEdit.value.author,
      totalQuantity: InfosEdit.value.totalQuantity,
      launchDate: formatDate(InfosEdit.value.launchDate),
      publisherId: InfosEdit.value.publisherId
    };
    showModalEditar.value = true;
  }).catch(error => {
    console.error("Erro ao obter dados para edição:", error);
  });
};



const formatDate = (dateString) => {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};


const openRegisterDialog = () => {
  bookToCreate.value = { name: '', author: '', totalQuantity: '', launchDate: '', publisherId: '' };
  showModalCadastro.value = true;
}

const saveEdit = () => {
  const index = rows.value.findIndex(r => r.id === bookToEdit.value.id);
  if (index !== -1) {
    const formattedBook = {
      ...bookToEdit.value,
      totalQuantity: Number(bookToEdit.value.totalQuantity),
      publisherId: Number(bookToEdit.value.publisherId)
    };

    api.put(`/book`, formattedBook)
      .then(response => {
        showNotification('positive', "Livro atualizado com sucesso!");
        getRows();
        showModalEditar.value = false;
      })
      .catch(error => {
        console.error("Erro ao atualizar livro:", error.response ? error.response.data : error.message);
        showNotification('negative', `Erro ao atualizar livro: ${error.response ? error.response.data.message : error.message}`);
      });
  }
};


const registerAction = () => {
  createRow(bookToCreate.value);
};

const createRow = (bookToCreate) => {
  bookToCreate.totalQuantity = Number(bookToCreate.totalQuantity);
  bookToCreate.launchDate = new Date(bookToCreate.launchDate).toISOString().split('T')[0];

  api.post('/book', bookToCreate)
    .then(response => {
      showNotification('positive', "Livro criado com sucesso!");
      getRows();
    })
    .catch(error => {
      let errorMessage = 'Erro desconhecido ao criar livro.';

      if (error.response) {
        const responseData = error.response.data;
        if (responseData && responseData.detail) {
          errorMessage = `Erro ao criar livro: ${responseData.detail}`;
        } else if (responseData && responseData.errors) {
          errorMessage = `Erro ao criar livro: ${responseData.errors.join(', ')}`;
        } else if (responseData && responseData.message) {
          errorMessage = `Erro ao criar livro: ${responseData.message}`;
        }
      } else if (error.message) {
        errorMessage = `Erro ao criar livro: ${error.message}`;
      }

      console.error("Erro ao criar livro:", error);
      showNotification('negative', errorMessage);
    });
};




const showDeleteModal = (row) => {
  rowToDelete.value = row;
  showModalExcluir.value = true;
};

const confirmDelete = () => {
  if (rowToDelete.value) {
    api.delete(`/book/${rowToDelete.value.id}`)
      .then(() => {
        rows.value = rows.value.filter(row => row.id !== rowToDelete.value.id);
        showModalExcluir.value = false;
        Notify.create({
          color: 'green',
          textColor: 'white',
          icon: 'check_circle',
          message: 'Livro excluído com sucesso!',
          position: 'top'
        });
      })
      .catch(error => {
        console.error("Erro ao excluir livro:", error);
        Notify.create({
          color: 'red',
          textColor: 'white',
          icon: 'error',
          message: 'Erro ao excluir livro!',
          position: 'top'
        });
      });
  }
};

const cancelDelete = () => {
  showModalExcluir.value = false;
  rowToDelete.value = null;
};

const showNotification = (color, message) => {
  $q.notify({
    color: color,
    textColor: 'white',
    icon: 'check_circle',
    message: message,
    position: 'top'
  });
};

const onSearch = () => {
  console.log("Pesquisa atual:", search.value);
};

const filteredRows = computed(() => {
  const searchTerm = search.value.toLowerCase();
  return rows.value.filter(row =>
    row.name.toLowerCase().includes(searchTerm) ||
    row.author.toLowerCase().includes(searchTerm) ||
    String(row.availableQuantity).toLowerCase().includes(searchTerm) ||
    String(row.inUseQuantity).toLowerCase().includes(searchTerm)
  );
});

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

.container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  padding-bottom: 20px;
  max-width: 1300px;
  width: 100%;
  margin: 0 auto;
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
