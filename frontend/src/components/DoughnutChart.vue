<template>
  <div>
    <div class="chart-container">
      <div class="title">Top 3 Livros mais alugados</div>
      <canvas id="mostRentedBooksChart"></canvas>
    </div>
  </div>
</template>

<script setup>
import { useQuasar } from 'quasar';
import { ref, onMounted } from 'vue';
import { Chart, registerables } from 'chart.js';
import { api } from 'src/boot/axios';

const $q = useQuasar();

const showNotification = (type, msg) => {
  $q.notify({
    type: type,
    message: msg,
    position: 'bottom-right',
    timeout: 3000
  });
};

Chart.register(...registerables);

defineOptions({
  name: 'chartPieComponent'
});

const mostRented = ref([]);

const getRents = async () => {
  try {
    const response = await api.get('/dashboard/bookMoreRented');
    mostRented.value = response.data;
  } catch (error) {
    showNotification('negative', "Erro ao obter dados!");
    console.error("Erro ao obter dados:", error);
  }
};

onMounted(async () => {
  await getRents();

  const ctx2 = document.getElementById('mostRentedBooksChart').getContext('2d');
  new Chart(ctx2, {
    type: 'pie',
    data: {
      labels: mostRented.value.map(book => book.name),
      datasets: [{
        label: 'Livros mais alugados',
        data: mostRented.value.map(book => book.totalRents),
        backgroundColor: ['#509358', '#B22222', '#46769A'],
        borderWidth: 0
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false
    }
  });
});
</script>

<style scoped>
#mostRentedBooksChart {
  margin-bottom: 1rem;
}

.chart-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 505px;
  height: 270px;
  box-shadow: 3px 4px 10px 0px rgba(0, 0, 0, 0.25);
  border-radius: 20px;
  margin: 10px;
  background-color: white;
  padding: 10px;
}

.title {
  margin-bottom: 1px;
  font-weight: bold;
}

@media (max-width: 450px) {
  .chart-container {
    width: 210px;
  }
}

@media (max-width: 550px) {
  .chart-container {
    width: 420px;
  }
}
</style>
