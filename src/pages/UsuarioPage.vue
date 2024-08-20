<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;" @click="openRegisterDialog">
        <div class="buttonCadastrar">
          CADASTRAR USUÁRIO
        </div>
      </q-btn>
    </div>

    <!-- Barra de Pesquisa -->
    <div class="container">
      <div class="pesquisa">
        <q-input filled v-model="search" placeholder="Pesquisar Usuário" class="pesquisa" @input="onSearch">
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
          <div class="titulo-cadastro">Cadastro de Usuário</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit="submitFormCadastro">
            <q-input filled v-model="userCreate.name" label="Nome" required lazy-rules :rules="[val => !!val || 'Nome é obrigatório', val => val.length >= 5 || 'Nome deve ter pelo menos 5 caracteres',
            val => /^[a-zA-Z\s]+$/.test(val) || 'Nome deve conter apenas letras e espaços']" />

            <q-input filled v-model="userCreate.email" label="Email" type="email" required lazy-rules
              :rules="[val => !!val || 'Email é obrigatório', val => /.+@.+\..+/.test(val) || 'Email inválido']" />

            <q-input filled v-model="userCreate.password" label="Senha" type="password" required lazy-rules
              :rules="[val => !!val || 'Senha é obrigatória']" />

            <div class="q-mt-md checkbox">
              <q-radio v-model="userCreate.role" checked-icon="task_alt" unchecked-icon="panorama_fish_eye" val="ADMIN"
                label="Editor" />
              <q-radio v-model="userCreate.role" checked-icon="task_alt" unchecked-icon="panorama_fish_eye"
                val="VISITOR" label="Locatário" />
            </div>

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
          <div class="titulo-cadastro">Editar Usuário</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="submitFormEditar">
            <q-input filled v-model="formEditar.name" label="Nome" required lazy-rules :rules="[val => !!val || 'Nome é obrigatório', val => val.length >= 5 || 'Nome deve ter pelo menos 5 caracteres',
            val => /^[a-zA-Z\s]+$/.test(val) || 'Nome deve conter apenas letras e espaços']" />

            <q-input filled v-model="formEditar.email" label="Email" type="email" required lazy-rules
              :rules="[val => !!val || 'Email é obrigatório', val => /.+@.+\..+/.test(val) || 'Email inválido']" />

            <div class="q-mt-md checkbox">
              <q-radio v-model="formEditar.role" checked-icon="task_alt" unchecked-icon="panorama_fish_eye" val="ADMIN"
                label="Editor" />
              <q-radio v-model="formEditar.role" checked-icon="task_alt" unchecked-icon="panorama_fish_eye"
                val="VISITOR" label="Locatário" />
            </div>

            <div class="button-container">
              <q-btn type="submit" label="ATUALIZAR" class="center-width q-mt-md" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>


    <!-- Modal Sobre -->
    <q-dialog v-model="showModalSobre">
      <q-card class="modal-card">
        <q-card-section>
          <div class="titulo-sobre text-center">Detalhes do Usuário</div>
        </q-card-section>
        <q-card-section>
          <div class="form-grid">
            <q-input filled v-model="selectedRow.name" label="Nome" readonly />
            <q-input filled v-model="selectedRow.email" label="Email" readonly />
            <q-input filled v-model="selectedRow.password" label="Senha" readonly />
            <q-input filled v-model="selectedRow.role" label="Permissão" readonly />
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
          <h3 class="titulo-exclusao">Tem certeza que deseja excluir?</h3>
        </q-card-section>

        <q-card-actions class="button-exclusao">
          <q-btn label="SIM" color="negative" @click="confirmDelete" class="q-mr-sm" />
          <q-btn label="NÃO" color="secondary" @click="cancelDelete" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- Tabela de usuários -->
    <div class="table-container">
      <q-table class="custom-table" :rows="filteredRows" :columns="columns" row-key="email" :pagination="pagination">
        <template v-slot:body-cell="props">
          <q-td :props="props" style="vertical-align: middle;">
            <div>{{ props.value }}</div>
          </q-td>
        </template>
        <template v-slot:body-cell-actions="props">
          <q-td :props="props" style="vertical-align: middle;">
            <q-btn flat color="primary" @click="showDetails(props.row)" icon="visibility" aria-label="View" />
            <q-btn flat color="secondary" @click="editRow(props.row)" icon="edit" aria-label="Edit" />
          </q-td>
        </template>
      </q-table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { api, authenticate } from 'src/boot/axios';
import { Notify } from 'quasar';

const showModalCadastro = ref(false);
const showModalEditar = ref(false);
const showModalExcluir = ref(false);
const showModalSobre = ref(false);
const rowToDelete = ref(null);
const search = ref('');

const userCreate = ref({
  name: '',
  email: '',
  password: '',
  role: ''
});

const formEditar = ref({
  id: null,
  name: '',
  email: '',
  role: ''
});

const selectedRow = ref({
  name: '',
  email: '',
  password: '',
  role: ''
});


const openRegisterDialog = () => {
  showModalCadastro.value = true;
};

const columns = [
  { name: 'name', required: true, label: 'Nome do usuário', align: 'center', field: row => row.name, format: val => `${val}` },
  { name: 'role', align: 'center', label: 'Permissão', field: 'role' },
  { name: 'actions', align: 'center', label: 'Ações', field: 'actions' }
];

const rows = ref([]);

const getRows = (srch = '') => {
  api.get('/users', { params: { search: srch } })
    .then(response => {
      if (response.data && Array.isArray(response.data.content)) {
        rows.value = response.data.content;
        showNotification('positive', "Dados obtidos com sucesso");
      } else {
        console.error('A resposta da API não contém um array em `content`:', response.data);
        rows.value = [];
      }
    })
    .catch(error => {
      showNotification('negative', "Erro ao obter dados!");
      console.error("Erro ao obter dados:", error);
    });
};

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

const pagination = ref({ page: 1, rowsPerPage: 5 });
const filteredRows = computed(() => {
  if (!search.value) {
    return rows.value;
  }
  return rows.value.filter(row => row.name.toLowerCase().includes(search.value.toLowerCase()));
});

const submitFormCadastro = () => {
  api.post('/users', userCreate.value)
    .then(response => {
      showNotification('positive', "Usuário cadastrado com sucesso!");
      getRows();
      showModalCadastro.value = false;
      resetFormCadastro();
    })
    .catch(error => {
      showNotification('negative', "Erro ao cadastrar usuário!");
      console.error("Erro ao cadastrar usuário:", error);
    });
};


const resetFormCadastro = () => {
  userCreate.value = {
    name: '',
    email: '',
    password: '',
    role: ''
  };
};

const editRow = (row) => {
  formEditar.value = { ...row };
  showModalEditar.value = true;
};

const submitFormEditar = () => {
  api.put(`/users/${formEditar.value.id}`, formEditar.value)
    .then(response => {
      showNotification('positive', "Usuário atualizado com sucesso!");
      getRows();
      showModalEditar.value = false;
    })
    .catch(error => {
      showNotification('negative', "Erro ao atualizar usuário!");
      console.error("Erro ao atualizar usuário:", error);
    });
};


const onSearch = () => {
  getRows(search.value);
};

const showNotification = (type, message) => {
  Notify.create({
    type: type,
    message: message,
    position: 'top'
  });
};


const showDetails = (row) => {
  showModalSobre.value = true;
  loadUserDetails(row.id);
};

const loadUserDetails = (id) => {
  api.get(`/users/${id}`)
    .then(response => {
      if (response.data) {
        selectedRow.value = response.data;
        showModalSobre.value = true;
      } else {
        console.error('Usuário não encontrado:', response.data);
      }
    })
    .catch(error => {
      console.error("Erro ao obter detalhes do usuário:", error);
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

.custom-table {
  max-width: 1300px;
  width: 100%;
  margin: 0 auto;
}

.container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  padding-bottom: 20px;
}

.pesquisa {
  display: flex;
  max-width: 1160px;
  height: 53px;
  border-radius: 4px;
  width: 100%;
  margin: 0 auto;
}

.q-input.pesquisa {
  font-size: 16px;
  font-weight: 800;
  color: rgba(0, 0, 0, 0.60);
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
