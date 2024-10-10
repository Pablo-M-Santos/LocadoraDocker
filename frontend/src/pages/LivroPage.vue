<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #008080; color: white;" v-if="userRole === 'ADMIN'"
        @click="openRegisterDialog">
        <div class="buttonCadastrar">
          CADASTRAR LIVRO
        </div>
      </q-btn>
    </div>

    <!-- Barra de Pesquisa -->
    <div class="container">
      <q-form @submit="getRows(search)" class="pesquisa">
        <q-input filled v-model="search" placeholder="Pesquisar Aluguel" class="pesquisa" @input="onSearch"
          @keyup.enter="performSearch">
          <template v-slot:prepend>
            <q-icon v-if="search !== ''" @click="search = '', getRows(search)" name="search" />
          </template>
        </q-input>
      </q-form>
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
              :rules="[val => !!val || 'Título do Livro é obrigatório']" />

            <q-input v-model="bookToCreate.author" label="Autor" filled lazy-rules
              :rules="[val => !!val || 'Autor é obrigatório']" />

            <q-input v-model="bookToCreate.totalQuantity" label="Quantidade" type="number" filled lazy-rules
              :rules="[val => val > 0 || 'É necessário ter pelo menos 1']" />

            <q-input v-model="bookToCreate.launchDate" label="Data de lançamento" type="date" :max="today"
              mask="####-##-##" fill-mask filled lazy-rules
              :rules="[val => val && val.length >= 6 || 'Adicione uma data válida']" />

            <q-select v-model="bookToCreate.publisherId" label="Selecione a editora" filled use-input input-debounce="0"
              :options="publisherOptions" @filter="filterPublisher" option-label="name" option-value="id" emit-value
              map-options />

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
            <q-input v-model="bookToEdit.name" label="Título do livro" filled lazy-rules
              :rules="[val => !!val || 'Título do Livro é obrigatório']" />

            <q-input v-model="bookToEdit.author" label="Autor" filled lazy-rules
              :rules="[val => val && val.length > 3 || 'É necessário ter mais de três caracteres']" />

            <q-input v-model="bookToEdit.totalQuantity" label="Quantidade" type="number" filled lazy-rules
              :rules="[val => !!val || 'Autor é obrigatório']" />

            <q-input v-model="bookToEdit.launchDate" label="Data de lançamento" type="date" :max="today"
              mask="####-##-##" fill-mask filled lazy-rules
              :rules="[val => val && val.length >= 6 || 'Adicione uma data válida']" />

            <q-select v-model="bookToEdit.publisherId" label="Selecione a editora" filled use-input input-debounce="0"
              :options="publisherOptions" @filter="filterPublisher" option-label="name" option-value="id" emit-value
              map-options />

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
          <div class="titulo-sobre text-center">Detalhes do Livro</div>
        </q-card-section>
        <q-card-section>
          <div class="form-grid">
            <q-input filled v-model="InfosEdit.name" label="Nome" readonly />
            <br>
            <q-input filled v-model="InfosEdit.author" label="Autor" readonly />
            <br>
            <q-input filled v-model="InfosEdit.totalQuantity" label="Quantidade Total:" readonly />
            <br>
            <q-input filled v-model="InfosEdit.launchDate" label="Data de lançamento:" readonly />
            <br>
            <q-input filled v-model="InfosEdit.publisher.name" label="Editora" readonly />
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
      <q-table class="custom-table" :rows="filteredRows" :columns="columns" row-key="id">

        <template v-slot:header-cell-title="props">
          <q-th v-bind="props">
            Título
            <q-icon name="keyboard_arrow_up" @click="sortRowsAscBy" class="cursor-pointer" size="20px" />
            <q-icon name="keyboard_arrow_down" @click="sortRowsDescBy" class="cursor-pointer" size="20px" />
          </q-th>
        </template>
        <template v-slot:body-cell-title="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.row.name }}</div>
          </q-td>
        </template>

        <template v-slot:header-cell-author="props">
          <q-th v-bind="props">
            Autor
            <q-icon name="keyboard_arrow_up" @click="sortRowsAscBy" class="cursor-pointer" size="20px" />
            <q-icon name="keyboard_arrow_down" @click="sortRowsDescBy" class="cursor-pointer" size="20px" />
          </q-th>
        </template>
        <template v-slot:body-cell-author="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.row.author }}</div>
          </q-td>
        </template>

        <template v-slot:header-cell-totalQuantity="props">
          <q-th v-bind="props">
            Disponiveis
            <q-icon name="keyboard_arrow_up" @click="sortRowsAscByTotalQuantity" class="cursor-pointer" size="20px" />
            <q-icon name="keyboard_arrow_down" @click="sortRowsDescByTotalQuantity" class="cursor-pointer"
              size="20px" />
          </q-th>
        </template>
        <template v-slot:body-cell-totalQuantity="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.row.totalQuantity }}</div>
          </q-td>
        </template>

        <template v-slot:header-cell-totalInUse="props">
          <q-th v-bind="props">
            Alugados
            <q-icon name="keyboard_arrow_up" @click="sortRowsAscBy" class="cursor-pointer" size="20px" />
            <q-icon name="keyboard_arrow_down" @click="sortRowsDescBy" class="cursor-pointer" size="20px" />
          </q-th>
        </template>
        <template v-slot:body-cell-totalInUse="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.row.totalInUse }}</div>
          </q-td>
        </template>

        <template v-slot:body-cell-actions="props">
          <q-td clas :props="props" style="vertical-align: middle;">
            <q-btn flat color="primary" @click="showDetails(props.row)" icon="visibility" aria-label="View" />
            <q-btn flat color="secondary" v-if="userRole === 'ADMIN'" @click="editRow(props.row)" icon="edit"
              aria-label="Edit" />
            <q-btn flat color="negative" v-if="userRole === 'ADMIN'" @click="showDeleteModal(props.row)" icon="delete"
              aria-label="Delete" />
          </q-td>
        </template>
      </q-table>
    </div>
    <div class="row justify-center q-my-md">
      <q-btn :disable="page.value <= 0" @click="prevPage" class="q-mx-sm">
        <q-icon name="chevron_left" />
      </q-btn>
      <q-btn :disable="page.value >= totalPages - 1" @click="nextPage" class="q-mx-sm">
        <q-icon name="chevron_right" />
      </q-btn>
    </div>
  </div>
</template>

<script setup>
import { useQuasar } from 'quasar';
import { ref, computed, onMounted } from 'vue';
import { api } from 'src/boot/axios.js';
import { Notify } from 'quasar';

const $q = useQuasar();

const InfosEdit = ref({});

const bookToCreate = ref({
  name: '',
  author: '',
  totalQuantity: '',
  launchDate: '',
  publisherId: '',
  totalInUse: ''
});

const showModalCadastro = ref(false);
const showModalEditar = ref(false);
const showModalExcluir = ref(false);
const showModalSobre = ref(false);
const rowToDelete = ref(null);
const search = ref('');
const rows = ref([]);
const page = ref(0);
const rowsPerPage = 5;

const columns = [
  { name: 'title', required: true, label: 'Título', align: 'center', field: row => row.name, format: val => `${val}` },
  { name: 'author', align: 'center', label: 'Autor', field: 'author' },
  { name: 'totalQuantity', align: 'center', label: 'Disponíveis', field: 'totalQuantity' },
  { name: 'totalInUse', align: 'center', label: 'Alugados', field: 'totalInUse' },
  { name: 'actions', align: 'center', label: 'Ações', field: 'actions' },
];

const sortRowsAscBy = () => {
  rows.value.sort((a, b) => a.name.localeCompare(b.name));
};

const sortRowsDescBy = () => {
  rows.value.sort((a, b) => b.name.localeCompare(a.name));
};

const sortRowsAscByTotalQuantity = () => {
  rows.value.sort((a, b) => a.totalQuantity - b.totalQuantity);
};

const sortRowsDescByTotalQuantity = () => {
  rows.value.sort((a, b) => b.totalQuantity - a.totalQuantity);
};


const getRows = (srch = '') => {
  api.get('/book', { params: { search: srch } })
    .then(response => {
      if (Array.isArray(response.data)) {
        rows.value = response.data;
      } else {
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
  getApi(row.id).then(() => {
    showModalSobre.value = true;
  });
};


const bookToEdit = ref({
  id: '',
  name: '',
  author: '',
  totalQuantity: '',
  launchDate: '',
  publisherId: '',
  totalInUse: ''
});

const editRow = (row) => {
  getApi(row.id).then(() => {
    bookToEdit.value = {
      id: InfosEdit.value.id,
      name: InfosEdit.value.name,
      author: InfosEdit.value.author,
      totalQuantity: InfosEdit.value.totalQuantity,
      launchDate: formatDate(InfosEdit.value.launchDate),
      publisherId: InfosEdit.value.publisher.id,
      totalInUse: InfosEdit.value.totalInUse
    };
    showModalEditar.value = true;
  });
};

const performSearch = () => {
  console.log("Executando pesquisa para:", search.value);
  onSearch();
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
  const formattedBook = {
    ...bookToEdit.value,
    totalQuantity: Number(bookToEdit.value.totalQuantity),
    totalInUse: Number(bookToEdit.value.totalInUse),
    publisherId: Number(bookToEdit.value.publisherId)
  };

  api.put(`/book/${bookToEdit.value.id}`, formattedBook)
    .then(response => {
      showNotification('positive', "Livro atualizado com sucesso!");
      getRows();
      showModalEditar.value = false;
    })
    .catch(error => {
      showNotification('negative', `Erro ao atualizar livro: ${error.message}`);
    });
};

const today = new Date().toISOString().split('T')[0];

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
      showModalCadastro.value = false;
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
};
const totalPages = computed(() => Math.ceil(rows.value.length / rowsPerPage));

const filteredRows = computed(() => {
  const start = page.value * rowsPerPage;
  return rows.value.slice(start, start + rowsPerPage);
});

const prevPage = () => {
  if (page.value > 0) page.value--;
};

const nextPage = () => {
  if (page.value < totalPages.value - 1) page.value++;
};
const userRole = ref('');

onMounted(() => {
  const token = localStorage.getItem('authToken');
  if (!token) {
    router.push('/login');
  } else {
    userRole.value = localStorage.getItem('role')
    getRows();
    loadPublishers();
  }
});


const publisherOptions = ref([]);
const allPublishers = ref([]);

const loadPublishers = () => {
  api.get('/publisher')
    .then(response => {
      allPublishers.value = response.data;
      publisherOptions.value = response.data;
    })
    .catch(error => {
      console.error('Erro ao carregar editoras:', error);
    });
};


const filterPublisher = (val, update) => {
  if (val === '') {
    update(() => {
      publisherOptions.value = allPublishers.value;
    });
    return;
  }

  const needle = val.toLowerCase();
  update(() => {
    publisherOptions.value = allPublishers.value.filter(publisher =>
      publisher.name.toLowerCase().includes(needle)
    );
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
  width: 600px;
  padding: 10px;
  border-radius: 20px;
  box-shadow: 15px 13px 61px -17px rgba(0, 0, 0, 0.49);
}

.modal-card-exclusao {
  width: 400px;
  border-radius: 10px;
  box-shadow: 15px 13px 61px -17px rgba(0, 0, 0, 0.49);
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

.titulo-sobre {
  font-size: 1.2rem;
  text-align: center;
  margin-bottom: 16px;
}

.pesquisa {
  display: flex;
  max-width: 1300px;
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
