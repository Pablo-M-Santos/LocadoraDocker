<template>
  <div class="grafico1">
    <canvas ref="barchart"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { Chart, registerables } from 'chart.js';
import { api, authenticate } from 'src/boot/axios';

Chart.register(...registerables);

const barchart = ref(null);

const rentsQtd = ref(0);
const inTime = ref(0);
const delivered = ref(0);
const delayed = ref(0);

const getRents = async () => {
  try {
    await authenticate();
    const response = await api.get('/rent');
    rentsQtd.value = response.data.totalElements;
  } catch (error) {
    showNotification('negative', "Erro ao obter dados!");
    console.error("Erro ao obter dados:", error);
  }
};

const getRentsInTime = async () => {
  try {
    await authenticate();
    const response = await api.get('/rent', {
      params: {
        status: 'IN_TIME'
      }
    });
    inTime.value = response.data.totalElements;
  } catch (error) {
    showNotification('negative', "Erro ao obter dados!");
    console.error("Erro ao obter dados:", error);
  }
};

const getRentsDelivered = async () => {
  try {
    await authenticate();
    const response = await api.get('/rent', {
      params: {
        status: 'DELIVERED'
      }
    });
    delivered.value = response.data.totalElements;
  } catch (error) {
    showNotification('negative', "Erro ao obter dados!");
    console.error("Erro ao obter dados:", error);
  }
};

const getRentsDelayed = async () => {
  try {
    await authenticate();
    const response = await api.get('/rent', {
      params: {
        status: 'DELAYED'
      }
    });
    delayed.value = response.data.totalElements;
  } catch (error) {
    showNotification('negative', "Erro ao obter dados!");
    console.error("Erro ao obter dados:", error);
  }
};

onMounted(async () => {
  await getRents();
  await getRentsInTime();
  await getRentsDelivered();
  await getRentsDelayed();

  const ctx = barchart.value.getContext('2d');

  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['Devolvidos dentro do prazo', 'Devolvidos fora do prazo'],
      datasets: [{
        label: 'Relação de livros',
        data: [delivered.value, delayed.value],
        backgroundColor: ['rgba(0, 102, 102, 1)', '#rgba(0, 64, 128,1)'],
        borderWidth: 0,
        borderRadius: 5,
        
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
});
</script>

<style scoped>
.grafico1 {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 505px;
  height: 299px;
  box-shadow: 3px 4px 10px 0px rgba(0, 0, 0, 0.25);
  border-radius: 20px;
  margin: 10px;
  background-color: white;
  padding: 0;
}


@media (max-width: 370px) {
  .grafico1 {
    width: 320px;
  }
}
</style>
