<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #008080; color: white;" v-if="userRole === 'ADMIN'"
        @click="openRegisterDialog">
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
    </div>

    <!-- Modal Cadastro -->
    <q-dialog v-model="showModalCadastro">
      <q-card class="modal-card">
        <q-card-section>
          <div class="titulo-cadastro">Cadastro de Aluguel</div>
        </q-card-section>
        <q-card-section>
          <q-form @submit.prevent="saveNewRent">

            <q-select v-model="newRent.renterId" label="Selecione o Locatário" filled use-input input-debounce="0"
              :options="renterOptions" @filter="filterPublisher" option-label="name" option-value="id" emit-value
              map-options class="q-mb-md" :rules="[val => !!val || 'É obrigatório selecionar um locatário']" />

            <q-select v-model="newRent.bookId" label="Selecione o Livro" filled use-input input-debounce="0"
              :options="bookOptions" @filter="filterBook" option-label="name" option-value="id" emit-value map-options
              class="q-mb-md" :rules="[val => !!val || 'É obrigatório selecionar um livro']" />

            <q-input v-model="newRent.deadLine" label="Prazo final" type="date"
              :rules="[val => !!val || 'É obrigatório informar um prazo']" />

            <div class="button-container">
              <q-btn type="submit" label="CADASTRAR" class="center-width q-mt-md" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>


    <!-- Modal Devolução -->
    <q-dialog v-model="showModalDevolucao">
      <q-card class="modal-card-confirmacao">
        <q-card-section class="text-center">
          <div class="circulo">
            <i class="fa-solid fa-exclamation"></i>
          </div>
          <h3 class="titulo-confirmacao">Tem certeza que deseja devolver?</h3>
        </q-card-section>
        <q-card-actions class="button-confirmacao">
          <q-btn label="SIM" color="secondary" @click="confirmReturn" class="q-mr-sm" />
          <q-btn label="NÃO" color="negative" @click="cancelReturn" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- Tabela de livros -->
    <div class="table-container">
      <q-table class="custom-table" :rows="filteredRows" :columns="columns" row-key="id" :pagination="pagination"
        :filter="filter">

        <template v-slot:header-cell-renterName="props">
          <q-th v-bind="props">
            Locatário
            <q-icon name="keyboard_arrow_up" @click="sortRowsAscByRenterName" class="cursor-pointer" size="20px" />
            <q-icon name="keyboard_arrow_down" @click="sortRowsDescByRenterName" class="cursor-pointer" size="20px" />
          </q-th>
        </template>
        <template v-slot:body-cell-renterName="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.row.renterName }}</div>
          </q-td>
        </template>

        <template v-slot:header-cell-bookName="props">
          <q-th v-bind="props">
            Livro
            <q-icon name="keyboard_arrow_up" @click="sortRowsAscByBookName" class="cursor-pointer" size="20px" />
            <q-icon name="keyboard_arrow_down" @click="sortRowsDescByBookName" class="cursor-pointer" size="20px" />
          </q-th>
        </template>
        <template v-slot:body-cell-bookName="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.row.bookName }}</div>
          </q-td>
        </template>

        <template v-slot:header-cell-deadLineDate="props">
          <q-th v-bind="props">
            Alugado
            <q-icon name="keyboard_arrow_up" @click="sortRowsAscByDeadLineDate" class="cursor-pointer" size="20px" />
            <q-icon name="keyboard_arrow_down" @click="sortRowsDescByDeadLineDate" class="cursor-pointer" size="20px" />
          </q-th>
        </template>
        <template v-slot:body-cell-deadLineDate="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.row.deadLineDate }}</div>
          </q-td>
        </template>

        <template v-slot:header-cell-rentDate="props">
          <q-th v-bind="props">
            Devolução
            <q-icon name="keyboard_arrow_up" @click="sortRowsAscByRentDate" class="cursor-pointer" size="20px" />
            <q-icon name="keyboard_arrow_down" @click="sortRowsDescByRentDate" class="cursor-pointer" size="20px" />
          </q-th>
        </template>
        <template v-slot:body-cell-rentDate="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.row.rentDate }}</div>
          </q-td>
        </template>

        <template v-slot:body-cell-status="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ formatStatus(props.row.status) }}</div>
          </q-td>
        </template>

        <template v-slot:body-cell-actions="props">
          <q-td :props="props" style="vertical-align: middle;">
            <q-btn flat color="accent" v-if="userRole === 'ADMIN'" @click="showReturnModal(props.row)" icon="check"
              aria-label="Confirm" />
          </q-td>
        </template>
      </q-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useQuasar } from 'quasar'
import { api } from 'src/boot/axios.js'

const $q = useQuasar()

const showModalCadastro = ref(false)
const showModalDevolucao = ref(false)
const rowToReturn = ref(null)
const search = ref('')

const today = new Date();
const maxDate = new Date();
maxDate.setDate(today.getDate() + 30);
const selectedDate = ref('');

const newRent = ref({
  renterId: '',
  bookId: '',
  deadLine: ''
})

const rows = ref([])
const columns = computed(() => {
  const baseColumns = [
    { name: 'renterName', align: 'center', label: 'Locatário', field: 'renterName' },
    { name: 'bookName', align: 'center', label: 'Livro', field: 'bookName' },
    { name: 'rentDate', align: 'center', label: 'Alugado', field: 'rentDate' },
    { name: 'deadLineDate', align: 'center', label: 'Devolução', field: 'deadLineDate' },
    { name: 'status', align: 'center', label: 'Status', field: 'status' }
  ];

  if (userRole.value === 'ADMIN') {
    baseColumns.push({ name: 'actions', align: 'center', label: 'Ações', field: 'actions' });
  }

  return baseColumns;
});


const pagination = reactive({ page: 1, rowsPerPage: 5 })
const filter = ref('')

const openRegisterDialog = () => {
  newRent.value = { renterId: '', bookId: '', deadLine: '' }
  showModalCadastro.value = true
}
const sortRowsAscByRenterName = () => {
  rows.value.sort((a, b) => a.renterName.localeCompare(b.renterName));
};

const sortRowsDescByRenterName = () => {
  rows.value.sort((a, b) => b.renterName.localeCompare(a.renterName));
};

const sortRowsAscByBookName = () => {
  rows.value.sort((a, b) => a.bookName.localeCompare(b.bookName));
};

const sortRowsDescByBookName = () => {
  rows.value.sort((a, b) => b.bookName.localeCompare(a.bookName));
};

const sortRowsAscByDeadLineDate = () => {
  rows.value.sort((a, b) => new Date(a.deadLineDate) - new Date(b.deadLineDate));
};

const sortRowsDescByDeadLineDate = () => {
  rows.value.sort((a, b) => new Date(b.deadLineDate) - new Date(a.deadLineDate));
};

const sortRowsAscByRentDate = () => {
  rows.value.sort((a, b) => new Date(a.rentDate) - new Date(b.rentDate));
};

const sortRowsDescByRentDate = () => {
  rows.value.sort((a, b) => new Date(b.rentDate) - new Date(a.rentDate));
};


const saveNewRent = () => {
  const today = new Date();
  const deadLine = new Date();
  deadLine.setDate(today.getDate() + 30);

  api.post('/rent', {
    renterId: newRent.value.renterId,
    bookId: newRent.value.bookId,
    deadLine: deadLine.toISOString().split('T')[0]
  })
    .then(response => {
      showModalCadastro.value = false;
      showNotification('positive', "Aluguel cadastrado com sucesso!");
      getRows();
    })
    .catch(error => {
      let errorMessage = 'Erro desconhecido ao cadastrar aluguel.';

      if (error.response) {
        if (error.response.status === 400) {

          errorMessage = Object.values(error.response.data).join(', ') || errorMessage;
        } else if (error.response.data.message) {

          errorMessage = error.response.data.message;
        }
      }

      console.error("Erro ao cadastrar aluguel:", error.response ? error.response.data : error.message);
      showNotification('negative', errorMessage);
    });
};

const confirmReturn = () => {
  if (!rowToReturn.value) {
    showNotification('negative', "Nenhuma linha selecionada para devolução.")
    return
  }

  const row = rowToReturn.value
  const updatedStatus = "ENTREGUE"

  if (!row.id) {
    showNotification('negative', "ID do livro não disponível.")
    return
  }

  if (row.status === updatedStatus) {
    showNotification('negative', "Este aluguel já foi devolvido.")
    return
  }

  api.put(`/rent/${row.id}`, { status: updatedStatus })
    .then(response => {
      const index = rows.value.findIndex(r => r.id === row.id)
      if (index !== -1) {
        rows.value[index].status = updatedStatus
      }
      showNotification('positive', "Status atualizado com sucesso!")
      showModalDevolucao.value = false
      getRows()
    }).catch(error => {
      handleError(error, "Erro ao atualizar status!")
    })
}

const showReturnModal = (row) => {
  if (row.status === "ENTREGUE" || row.status === "ENTREGUE_COM_ATRASO" || row.status === "NO_PRAZO") {
    showNotification('negative', "Este aluguel já foi devolvido.")
    return
  }
  rowToReturn.value = row
  showModalDevolucao.value = true
}

const cancelReturn = () => {
  showModalDevolucao.value = false
}

const showNotification = (type, message) => {
  $q.notify({
    type: type,
    message: message,
    timeout: 3000,
    position: 'top'
  })
}

const formatStatus = (status) => {
  return status
    .replace(/_/g, ' ')
    .replace(/\b\w/g, (l) => l.toUpperCase())
}

const onSearch = () => {
}

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
          status: item.status || 'Não disponível',
          actions: 'Actions'
        }))
      } else {
        rows.value = []
      }
    }).catch(error => {
      showNotification('negative', "Erro ao obter dados!")
      console.error("Erro ao obter dados:", error)
    })
}

const filteredRows = computed(() => {
  if (!search.value) return rows.value
  return rows.value.filter(row => {
    const searchLower = search.value.toLowerCase()
    return Object.values(row).some(value =>
      String(value).toLowerCase().includes(searchLower)
    )
  })
})

const renters = ref([])
const books = ref([])

const getBooks = () => {
  api.get('/book')
    .then(response => {
      books.value = response.data || []
    })
    .catch(error => {
      console.error('Erro ao obter livros:', error)
      showNotification('negative', 'Erro ao obter livros!')
    })
}

const getRenters = () => {
  api.get('/renter')
    .then(response => {
      renters.value = response.data || []
    })
    .catch(error => {
      console.error('Erro ao obter locatários:', error)
      showNotification('negative', 'Erro ao obter locatários!')
    })
}


const renterOptions = ref([]);
const allRenter = ref([]);
const bookOptions = ref([]);
const allbook = ref([]);

const loadRenter = () => {
  api.get('/renter')
    .then(response => {
      allRenter.value = response.data;
      renterOptions.value = response.data;
    })
    .catch(error => {
      console.error('Erro ao carregar editoras:', error);
    });
};

const loadBook = () => {
  api.get('/book')
    .then(response => {
      allbook.value = response.data;
      bookOptions.value = response.data;
    })
    .catch(error => {
      console.error('Erro ao carregar Livro:', error);
    });
};

const filterPublisher = (val, update) => {
  if (val === '') {
    update(() => {
      renterOptions.value = allRenter.value;
    });
    return;
  }

  const needle = val.toLowerCase();
  update(() => {
    renterOptions.value = allRenter.value.filter(publisher =>
      publisher.name.toLowerCase().includes(needle)
    );
  });
};

const filterBook = (val, update) => {
  if (val === '') {
    update(() => {
      bookOptions.value = allbook.value;
    });
    return;
  }

  const needle = val.toLowerCase();
  update(() => {
    bookOptions.value = allbook.value.filter(renter =>
      renter.name.toLowerCase().includes(needle)
    );
  });
};

const userRole = ref('');

onMounted(() => {
  userRole.value = localStorage.getItem('role')
  getRows()
  getBooks()
  getRenters()
  loadRenter()
  loadBook()
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

.modal-card {
  width: 600px;
  padding: 10px;
  border-radius: 20px;
  box-shadow: 15px 13px 61px -17px rgba(0, 0, 0, 0.49);
}

.modal-card-confirmacao {
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

.titulo-confirmacao {
  font-size: 1.2rem;
  margin-bottom: 16px;
}

.button-confirmacao {
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
