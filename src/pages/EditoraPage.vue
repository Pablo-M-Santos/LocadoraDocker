<template>
  <div class="content">
    <!-- Button cadastrar -->
    <div class="containerButton">
      <q-btn style="width: 200px; background-color: #006666; color: white;" @click="openRegisterDialog">
        <div class="buttonCadastrar">
          CADASTRAR EDITORA
        </div>
      </q-btn>
    </div>

    <!-- Barra de Pesquisa -->
    <div class="container">
      <div class="pesquisa">
        <q-input filled v-model="search" placeholder="Pesquisar Editora" class="pesquisa" @input="onSearch">
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
          <div class="titulo-cadastro">Cadastro de Editora</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="saveNewPublisher">
            <q-input filled v-model="newPublisher.name" label="Nome da Editora" required lazy-rules
              :rules="[val => !!val || 'Nome da Editora é obrigatório', val => val.length >= 5 || 'Nome da Editora deve ter pelo menos 5 caracteres']" />

            <q-input filled v-model="newPublisher.telephone" label="Telefone" type="tel" required lazy-rules
              :rules="[val => !!val || 'Telefone é obrigatório', val => /^\d{10,15}$/.test(val) || 'Telefone deve ter entre 10 e 15 dígitos']" />

            <q-input filled v-model="newPublisher.email" label="Email" type="email" required lazy-rules
              :rules="[val => !!val || 'Email é obrigatório', val => /.+@.+\..+/.test(val) || 'Email inválido']" />

            <q-input filled v-model="newPublisher.site" label="Site" required lazy-rules />

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
          <q-form>
            <q-input filled v-model="editPublisher.name" label="Nome da Editora" required lazy-rules
              :rules="[val => !!val || 'Nome é obrigatório', val => val.length >= 5 || 'Nome deve ter pelo menos 5 caracteres']" />
            <q-input filled v-model="editPublisher.telephone" label="Telefone" type="tel" required lazy-rules
              :rules="[val => !!val || 'Telefone é obrigatório']" />
            <q-input filled v-model="editPublisher.email" label="Email" type="email" required lazy-rules
              :rules="[val => !!val || 'Email é obrigatório', val => /.+@.+\..+/.test(val) || 'Email inválido']" />
            <q-input filled v-model="editPublisher.site" label="Site" lazy-rules />
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
            <q-input filled v-model="selectedRow.name" label="Nome" readonly />
            <q-input filled v-model="selectedRow.telephone" label="Telefone" readonly />
            <q-input filled v-model="selectedRow.email" label="Email" readonly />
            <q-input filled v-model="selectedRow.site" label="Site" readonly />
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

    <!-- Table -->
    <div class="table-container">
      <q-table class="custom-table" :rows="filteredRows" :columns="columns" row-key="id" :pagination="pagination">
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
import { ref, computed, onMounted } from 'vue';
import { api, authenticate } from 'src/boot/axios.js';
import { Notify } from 'quasar';

const showModalCadastro = ref(false);
const showModalSobre = ref(false);
const showModalEditar = ref(false);
const showModalExcluir = ref(false);
const rowToDelete = ref(null);
const selectedRow = ref(null);
const search = ref('');

const newPublisher = ref({ name: '', email: '', telephone: '', site: '' });
const editPublisher = ref([]);

const columns = [
  { name: 'name', required: true, label: 'Nome da Editora', align: 'center', field: row => row.name, format: val => `${val}` },
  { name: 'actions', align: 'center', label: 'Ações', field: 'actions' },
];
const rows = ref([]);
const pagination = ref({ page: 1, rowsPerPage: 5 });


const saveNewPublisher = () => {
  api.post('/publisher', newPublisher.value)
    .then(response => {
      rows.value.push(response.data);
      Notify.create({
        color: 'green',
        textColor: 'white',
        icon: 'check_circle',
        message: 'Editora criada com sucesso!',
        position: 'top'
      });
      getRows();
      showModalCadastro.value = false;
    })
    .catch(error => {
      Notify.create({
        color: 'red',
        textColor: 'white',
        icon: 'error',
        message: 'Erro ao criar editora!',
        position: 'top'
      });
    });
};

const getRows = () => {
  api.get('/publisher')
    .then(response => {
      if (Array.isArray(response.data.content)) {
        rows.value = response.data.content;
        Notify.create({
          color: 'green',
          textColor: 'white',
          icon: 'check_circle',
          message: 'Dados obtidos com sucesso!',
          position: 'top'
        });
      } else {
        console.error('A resposta da API não é um array:', response.data);
        rows.value = [];
      }
    })
    .catch(error => {
      console.error("Erro ao obter dados:", error);
    });
};

const confirmDelete = () => {
  if (rowToDelete.value && rowToDelete.value.id) {
    const index = rows.value.findIndex(r => r.id === rowToDelete.value.id);
    if (index !== -1) {
      api.delete(`/publisher/${rowToDelete.value.id}`)
        .then(() => {
          rows.value.splice(index, 1);
          showModalExcluir.value = false;
          Notify.create({
            color: 'red',
            textColor: 'white',
            icon: 'delete',
            message: 'Registro excluído com sucesso!',
            position: 'top'
          });
        })
        .catch(error => {
          console.error("Erro ao excluir:", error);
        });
    }
  } else {
    Notify.create({
      color: 'red',
      textColor: 'white',
      icon: 'error',
      message: 'Nenhuma editora selecionada para exclusão!',
      position: 'top'
    });
  }
};

const onSearch = () => {
  console.log("Pesquisa atual:", search.value);
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

const loadPublisherDetails = (id) => {
  api.get(`/publisher/${id}`)
    .then(response => {
      if (response.data) {
        selectedRow.value = response.data;
        showModalSobre.value = true;
      } else {
        console.error('Editora não encontrada:', response.data);
      }
    })
    .catch(error => {
      console.error("Erro ao obter detalhes da editora:", error);
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

const openRegisterDialog = () => {
  showModalCadastro.value = true;
};

const showDetails = (row) => {
  loadPublisherDetails(row.id);
};

const showDeleteModal = (row) => {
  rowToDelete.value = row;
  showModalExcluir.value = true;
};

const cancelDelete = () => {
  showModalExcluir.value = false;
};

const editRow = (row) => {
  api.get(`/publisher/${row.id}`)
    .then(response => {
      if (response.data) {
        editPublisher.value = { ...response.data };
        showModalEditar.value = true;
      } else {
        console.error('Editora não encontrada:', response.data);
        Notify.create({
          color: 'red',
          textColor: 'white',
          icon: 'error',
          message: 'Editora não encontrada!',
          position: 'top'
        });
      }
    })
    .catch(error => {
      console.error("Erro ao obter detalhes da editora:", error);
      Notify.create({
        color: 'red',
        textColor: 'white',
        icon: 'error',
        message: 'Erro ao obter detalhes da editora!',
        position: 'top'
      });
    });
};
const saveEdit = () => {
  api.put(`/publisher`, editPublisher.value)
    .then(response => {
      const index = rows.value.findIndex(publisher => publisher.id === editPublisher.value.id);
      if (index !== -1) {
        rows.value[index] = response.data;
      }
      Notify.create({
        color: 'green',
        textColor: 'white',
        icon: 'check_circle',
        message: 'Editora atualizada com sucesso!',
        position: 'top'
      });
      showModalEditar.value = false;
    })
    .catch(error => {
      Notify.create({
        color: 'red',
        textColor: 'white',
        icon: 'error',
        message: 'Erro ao atualizar editora!',
        position: 'top'
      });
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
  max-width: 200px;
  margin: 0 auto;
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
