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
import { api, authenticate } from 'src/boot/axios';

const $q = useQuasar();

const showNotification = (type, msg) => {
  $q.notify({
    type: type,
    message: msg,
    position: 'top',
    timeout: 1000
  });
};

Chart.register(...registerables);

defineOptions({
  name: 'MostRentedBooksChart'
});

const topBook1 = ref(null);
const topBook2 = ref(null);
const topBook3 = ref(null);

const fetchTopBookData = async (bookId, bookRef) => {
  try {
    await authenticate();
    const response = await api.get(`/rent/most-rented/${bookId}`);
    bookRef.value = response.data;
  } catch (error) {
    showNotification('negative', "Erro ao obter dados!");
    console.error("Erro ao obter dados:", error);
  }
};

onMounted(async () => {
  await fetchTopBookData(1, topBook1);
  await fetchTopBookData(2, topBook2);
  await fetchTopBookData(3, topBook3);

  if (topBook1.value && topBook2.value && topBook3.value) {
    const ctx = document.getElementById('mostRentedBooksChart').getContext('2d');
    new Chart(ctx, {
      type: 'pie',
      data: {
        labels: [topBook1.value.bookName, topBook2.value.bookName, topBook3.value.bookName],
        datasets: [{
          label: 'Livros mais alugados',
          data: [topBook1.value.rentedNumber, topBook2.value.rentedNumber, topBook3.value.rentedNumber],
          backgroundColor: ['rgba(0, 64, 128, 1)', 'rgba(0, 128, 0, 1)', 'rgba(255, 165, 0, 1)'],
          borderWidth: 0
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });
  } else {
    showNotification('negative', "Dados insuficientes para o gráfico.");
  }
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
