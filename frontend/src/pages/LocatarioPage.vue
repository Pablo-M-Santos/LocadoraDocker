<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #008080; color: white;" v-if="userRole === 'ADMIN'"
        class="buttonCadastrar" @click="showModalCadastro = true">
        CADASTRAR LOCATÁRIO
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
          <div class="titulo-cadastro">Cadastro de Locatário</div>
        </q-card-section>

        <q-card-section>
          <q-form>
            <div class="form-grid">
              <q-input filled v-model="newRenter.name" label="Nome" required lazy-rules
                :rules="[val => !!val || 'Nome do Locatário é obrigatório']" />

              <q-input filled v-model="newRenter.email" label="Email" type="email" required lazy-rules
                :rules="[val => !!val || 'Email é obrigatório', val => /^.+@gmail\.com$/.test(val) || 'O e-mail deve ser um endereço Gmail válido']" />

              <q-input filled v-model="newRenter.telephone" label="Celular" type="tel" required lazy-rules
                mask="(##) #####-####"
                :rules="[val => !!val || 'Telefone é obrigatório', val => /^\(\d{2}\) \d{5}-\d{4}$/.test(val) || 'Telefone inválido']" />

              <q-input filled v-model="newRenter.address" label="Endereço" required lazy-rules
                :rules="[val => !!val || 'Endereço é obrigatório']" />

              <q-input filled v-model="newRenter.cpf" label="CPF" required lazy-rules mask="###.###.###-##"
                :rules="[val => !!val || 'CPF é obrigatório', validateCPF]" />
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
            <br>
            <q-input filled v-model="InfosEdit.email" label="Email" readonly />
            <br>
            <q-input filled v-model="InfosEdit.telephone" label="Celular" readonly />
            <br>
            <q-input filled v-model="InfosEdit.address" label="Endereço" readonly />
            <br>
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
              <q-input filled v-model="formEdit.name" label="Nome" required lazy-rules
                :rules="[val => !!val || 'Nome do Locatário é obrigatório']" />

              <q-input filled v-model="formEdit.email" label="Email" type="email" required lazy-rules
                :rules="[val => !!val || 'Email é obrigatório', val => /^.+@gmail\.com$/.test(val) || 'O e-mail deve ser um endereço Gmail válido']" />

              <q-input filled v-model="formEdit.telephone" label="Celular" type="tel" required lazy-rules
                mask="(##) #####-####"
                :rules="[val => !!val || 'Telefone é obrigatório', val => /^\(\d{2}\) \d{5}-\d{4}$/.test(val) || 'Telefone inválido']" />

              <q-input filled v-model="formEdit.address" label="Endereço" required lazy-rules
                :rules="[val => !!val || 'Endereço é obrigatório']" />

              <q-input filled v-model="formEdit.cpf" label="CPF" required lazy-rules mask="###.###.###-##"
                :rules="[val => !!val || 'CPF é obrigatório', validateCPF]" />
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
      <q-table class="custom-table" :rows="filteredRows" :columns="columns" row-key="email">
        <template v-slot:header-cell-name="props">
          <q-th v-bind="props">
            Nome da Editora
            <q-icon name="keyboard_arrow_up" @click="sortRowsAscByName" class="cursor-pointer" size="20px" />
            <q-icon name="keyboard_arrow_down" @click="sortRowsDescByName" class="cursor-pointer" size="20px" />
          </q-th>
        </template>
        <template v-slot:body-cell-name="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.row.name }}</div>
          </q-td>
        </template>

        <template v-slot:header-cell-email="props">
          <q-th v-bind="props">
            Email
            <q-icon name="keyboard_arrow_up" @click="sortRowsAscByEmail" class="cursor-pointer" size="20px" />
            <q-icon name="keyboard_arrow_down" @click="sortRowsDescByEmail" class="cursor-pointer" size="20px" />
          </q-th>
        </template>
        <template v-slot:body-cell-email="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.row.email }}</div>
          </q-td>
        </template>

        <template v-slot:header-cell-telephone="props">
          <q-th v-bind="props">
            Telefone
            <q-icon name="keyboard_arrow_up" @click="sortRowsAscByTelephone" class="cursor-pointer" size="20px" />
            <q-icon name="keyboard_arrow_down" @click="sortRowsDescByTelephone" class="cursor-pointer" size="20px" />
          </q-th>
        </template>
        <template v-slot:body-cell-telephone="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.row.telephone }}</div>
          </q-td>
        </template>

        <template v-slot:body-cell-actions="props">
          <q-td :props="props" class="text-center">
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
import { useQuasar, Notify } from 'quasar';
import { ref, computed, onMounted } from 'vue';
import { api } from 'src/boot/axios.js';
import { cpf as cpfValidator } from 'cpf-cnpj-validator';

const $q = useQuasar();

const showModalCadastro = ref(false);
const showModalSobre = ref(false);
const showModalEditar = ref(false);
const showModalExcluir = ref(false);
const rowToDelete = ref(null);
const search = ref('');
const page = ref(0);
const rowsPerPage = 5;
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

const userRole = ref('');

onMounted(() => {
  const token = localStorage.getItem('authToken');
  if (!token) {
    router.push('/login');
  } else {
    userRole.value = localStorage.getItem('role')
    getRows();
  }
});

const sortRowsAscByName = () => {
  rows.value.sort((a, b) => a.name.localeCompare(b.name));
};

const sortRowsDescByName = () => {
  rows.value.sort((a, b) => b.name.localeCompare(a.name));
};

const sortRowsAscByEmail = () => {
  rows.value.sort((a, b) => a.email.localeCompare(b.email));
};

const sortRowsDescByEmail = () => {
  rows.value.sort((a, b) => b.email.localeCompare(a.email));
};

const sortRowsAscByTelephone = () => {
  rows.value.sort((a, b) => a.telephone.localeCompare(b.telephone));
};

const sortRowsDescByTelephone = () => {
  rows.value.sort((a, b) => b.telephone.localeCompare(a.telephone));
};

const getRows = (search = '') => {
  api.get('/renter', { params: { search: search, page: page.value } })
    .then(response => {
      if (Array.isArray(response.data)) {
        rows.value = response.data.sort((a, b) => a.name.localeCompare(b.name));
      } else {
        rows.value = [];
      }
    })
    .catch(error => {
      showNotification('negative', 'Erro ao obter dados!');
      console.error('Erro ao obter dados:', error);
    });
};

function validateCPF(value) {
  if (!value) return true;
  return cpfValidator.isValid(value) ? true : 'CPF inválido';
}

const saveNewRenter = async () => {
  const formattedRenter = {
    name: newRenter.value.name.trim(),
    email: newRenter.value.email.trim(),
    address: newRenter.value.address.trim(),
    telephone: newRenter.value.telephone.trim(),
    cpf: newRenter.value.cpf.trim(),
  };

  try {
    const response = await api.post('/renter', formattedRenter, {
      headers: {
        'Content-Type': 'application/json',
      },
    });

    rows.value.push(response.data);
    newRenter.value = { name: '', email: '', telephone: '', address: '', cpf: '' };
    showNotification('positive', 'Locatário criado com sucesso!');
    showModalCadastro.value = false;
    getRows();
  } catch (error) {

    let errorMessage = 'Erro ao cadastrar locatário!';

    if (error.response) {
      if (error.response.status === 400) {

        errorMessage = Object.values(error.response.data).join(', ') || errorMessage;
      } else if (error.response.data.message) {

        errorMessage = error.response.data.message;
      }
    }

    console.error('Erro ao criar novo locatário:', error.response ? error.response.data : error.message);
    showNotification('negative', errorMessage);
  }
};



const saveEdit = () => {
  if (!formEdit.value.id) {
    showNotification('negative', 'Locatário não selecionado!');
    return;
  }

  api.put(`/renter/${formEdit.value.id}`, formEdit.value)
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

const performSearch = () => {
  console.log("Executando pesquisa para:", search.value);
  onSearch();
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

.titulo-sobre {
  font-size: 1.2rem;
  text-align: center;
  margin-bottom: 16px;
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
