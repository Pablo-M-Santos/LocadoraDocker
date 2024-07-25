<template>
  <q-card class="my-card">
    <q-card-section class="titulo">
      <q-icon name="star" color="yellow" size="40px" />
      <div>
        <h1>TOP 3 Livros Alugados</h1>
      </div>
    </q-card-section>

    <q-card-section>
      <q-table
        :rows="rows"
        :columns="columns"
        row-key="classificacao"
        :pagination="pagination"
        :rows-per-page-options="[0]"
        hide-bottom
        class="my-table"
      />
    </q-card-section>
  </q-card>
</template>

<script>
export default {
  name: 'TopLivros',
  data() {
    return {
      columns: [
        { name: 'classificacao', label: 'Classificação', align: 'center', field: row => row.classificacao },
        { name: 'nome', label: 'Nome do Livro', align: 'center', field: row => row.nome },
        { name: 'autor', label: 'Autor', align: 'center', field: row => row.autor },
        { name: 'editora', label: 'Editora', align: 'center', field: row => row.editora }
      ],
      rows: [
        { classificacao: 1, nome: '1984', autor: 'George Orwell', editora: 'Companhia das Letras' },
        { classificacao: 2, nome: '1984', autor: 'George Orwell', editora: 'Companhia das Letras' },
        { classificacao: 3, nome: '1984', autor: 'George Orwell', editora: 'Companhia das Letras' }
      ],
      pagination: {
        rowsPerPage: 0
      }
    }
  }
}
</script>

<style scoped>
.my-card {
  width: 100%;
  max-width: 1111px;
  margin: auto;
  box-shadow: 3px 4px 10px 0px rgba(0, 0, 0, 0.25);
  border-radius: 20px;
  background-color: white;
}

.titulo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.titulo h1 {
  font-size: 25px;
}

@media (max-width: 800px) {
  .my-card {
    width: 100%;
  }
}
</style>
