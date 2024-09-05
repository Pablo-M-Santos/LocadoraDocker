<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;" @click="openRegisterDialog">
        <div class="buttonCadastrar">
          CADASTRAR ALUGUEL
        </div>
      </q-btn>
    </div>

    <!-- Barra de Pesquisa -->
    <div class="container">
      <div class="pesquisa">
        <q-input filled v-model="search" placeholder="Pesquisar Aluguel" class="pesquisa" @input="onSearch">
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
          <div class="titulo-cadastro">Cadastro de Aluguel</div>
        </q-card-section>
        <q-card-section>
          <q-form @submit.prevent="saveNewRent">
            <q-input v-model="newRent.renterId" label="Id do locatário" />
            <q-input v-model="newRent.bookId" label="Id do livro" />
            <q-input v-model="newRent.deadLine" label="Prazo final" type="date" />
            <div class="button-container">
              <q-btn type="submit" label="CADASTRAR" class="center-width q-mt-md" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>

    <!-- Modal Devolução -->
    <q-dialog v-model="showModalDevolucao">
      <q-card class="modal-card-exclusao">
        <q-card-section class="text-center">
          <div class="circulo">
            <i class="fa-solid fa-exclamation"></i>
          </div>
          <h3 class="titulo-exclusao">Tem certeza que deseja devolver?</h3>
        </q-card-section>
        <q-card-actions class="button-exclusao">
          <q-btn label="SIM" color="secondary" @click="confirmReturn" class="q-mr-sm" />
          <q-btn label="NÃO" color="negative" @click="cancelReturn" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- Tabela de livros -->
    <div class="table-container">
      <q-table class="custom-table" :rows="filteredRows" :columns="columns" row-key="codigo" :pagination="pagination"
        :filter="filter">
        <template v-slot:body-cell="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.value }}</div>
          </q-td>
        </template>
        <template v-slot:body-cell-actions="props">
          <q-td :props="props" style="vertical-align: middle;">
            <q-btn flat color="accent" @click="showReturnModal(props.row)" icon="check" aria-label="Confirm" />
          </q-td>
        </template>
      </q-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useQuasar } from 'quasar';
import { api, authenticate } from 'src/boot/axios.js';

const $q = useQuasar();

const showModalCadastro = ref(false);
const showModalEditar = ref(false);
const showModalDevolucao = ref(false);
const rowToReturn = ref(null);
const selectedRow = ref(null);
const search = ref('');

const newRent = ref({
  renterId: 0,
  bookId: 0,
  deadLine: ''
});


const formEditar = reactive({
  renterId: '',
  renterName: '',
  bookId: '',
  deadLine: ''
});

const rows = ref([]);
const columns = [
  { name: 'renterName', align: 'center', label: 'Locatário', field: 'renterName' },
  { name: 'bookName', align: 'center', label: 'Livro', field: 'bookName' },
  { name: 'rentDate', align: 'center', label: 'Alugado', field: 'rentDate' },
  { name: 'deadLineDate', align: 'center', label: 'Devolução', field: 'deadLineDate' },
  { name: 'status', align: 'center', label: 'Status', field: 'status' },
  { name: 'actions', align: 'center', label: 'Ações', field: 'actions' },
];

const pagination = reactive({ page: 1, rowsPerPage: 5 });
const filter = ref('');


const openRegisterDialog = () => {
  newRent.value = { renterId: '', bookId: '', deadline: '' };
  showModalCadastro.value = true;
};

const saveNewRent = () => {
  api.post('/rent', {
    renterId: newRent.value.renterId,
    bookId: newRent.value.bookId,
    deadLine: newRent.value.deadLine
  }).then(response => {
    console.log('Resposta da API:', response.data);
    showModalCadastro.value = false;
    showNotification('positive', "Aluguel cadastrado com sucesso!");
    getRows();
  })
    .catch(error => {
      console.error('Erro na resposta do servidor:', error.response.data);
      showNotification('negative', 'Erro ao cadastrar aluguel!');
    });
};



const handleError = (error, defaultMessage) => {
  if (error.response) {
    console.error('Erro na resposta do servidor:', error.response.data);
    showNotification('negative', `Erro: ${error.response.data.message || defaultMessage}`);
  } else if (error.request) {
    console.error('Erro na requisição:', error.request);
    showNotification('negative', 'Erro na requisição. Verifique sua conexão.');
  } else {
    console.error('Erro:', error.message);
    showNotification('negative', defaultMessage);
  }
};

const confirmReturn = () => {
  if (!rowToReturn.value) {
    showNotification('negative', "Nenhuma linha selecionada para devolução.");
    return;
  }

  const row = rowToReturn.value;
  const updatedStatus = "ENTREGUE";

  if (!row.id) {
    showNotification('negative', "ID do livro não disponível.");
    return;
  }

  if (row.status === updatedStatus) {
    showNotification('negative', "Este aluguel já foi devolvido.");
    return;
  }

  api.put(`/rent/${row.id}`, { status: updatedStatus })
    .then(response => {
      const index = rows.value.findIndex(r => r.id === row.id);
      if (index !== -1) {
        rows.value[index].status = updatedStatus;
      }
      showNotification('positive', "Status atualizado com sucesso!");
      showModalDevolucao.value = false;
      getRows();
    })
    .catch(error => {
      handleError(error, "Erro ao atualizar status!");
    });
};

const showReturnModal = (row) => {
  if (row.status === "ENTREGUE" || row.status === "ENTREGUE_COM_ATRASO" || row.status === "NO_PRAZO") {
    showNotification('negative', "Este aluguel já foi devolvido.");
    return;
  }
  rowToReturn.value = row;
  showModalDevolucao.value = true;
};

const cancelReturn = () => {
  showModalDevolucao.value = false;
};


const showNotification = (type, message) => {
  $q.notify({
    type: type,
    message: message,
    timeout: 3000,
    position: 'top'
  });
};

const onSearch = () => {
  console.log("Searching for:", search.value);
};


const getRows = () => {
  api.get('/rent')
    .then(response => {
      if (Array.isArray(response.data)) {
        rows.value = response.data.map(item => ({
          id: item.id,
          renterName: item.renter ? item.renter.name : 'Não disponível',
          bookName: item.book ? item.book.name : 'Não disponível',
          rentDate: item.rentDate || 'Não disponível',
          deadLineDate: item.deadLine || 'Não disponível',
          devolutionDate: item.devolutionDate || 'Não disponível',
          status: item.status || 'Não disponível',
          actions: 'Actions'
        }));
      } else {
        rows.value = [];
      }
    })
    .catch(error => {
      showNotification('negative', "Erro ao obter dados!");
      console.error("Erro ao obter dados:", error);
    });
};


const filteredRows = computed(() => {
  if (!search.value) return rows.value;
  return rows.value.filter(row => {
    const searchLower = search.value.toLowerCase();
    return Object.values(row).some(value =>
      String(value).toLowerCase().includes(searchLower)
    );
  });
});

onMounted(() => {
  getRows();
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
