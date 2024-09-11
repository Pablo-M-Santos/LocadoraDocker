<template>
  <div class="grafico1">
    <div class="title">Estatísticas de Empréstimos</div>
    <canvas id="chartBarComponent"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { Chart, registerables } from 'chart.js';
import { api, authenticate } from 'src/boot/axios';

Chart.register(...registerables);

defineOptions({
  name: 'BarChar'
});

const rentsQtd = ref(0);
const late = ref(0);
const delivered = ref(0);
const delayed = ref(0);

const getRents = async () => {
  try {
    const response = await api.get('/dashboard/rentsQuantity');
    rentsQtd.value = response.data;
  } catch (error) {
    showNotification('negative', "Erro ao obter dados!");
    console.error("Erro ao obter dados:", error);
  }
};

const getRentsLate = async () => {
  try {
    const response = await api.get('/dashboard/rentsLateQuantity');
    late.value = response.data;
  } catch (error) {
    showNotification('negative', "Erro ao obter dados!");
    console.error("Erro ao obter dados:", error);
  }
};

const getRentsDelivered = async () => {
  try {
    const response = await api.get('/dashboard/deliveredInTimeQuantity');
    delivered.value = response.data;
  } catch (error) {
    showNotification('negative', "Erro ao obter dados!");
    console.error("Erro ao obter dados:", error);
  }
};

const getRentsDelayed = async () => {
  try {
    const response = await api.get('/dashboard/deliveredWithDelayQuantity');
    delayed.value = response.data;
  } catch (error) {
    showNotification('negative', "Erro ao obter dados!");
    console.error("Erro ao obter dados:", error);
  }
};

onMounted(async () => {
  await getRents();
  await getRentsLate();
  await getRentsDelivered();
  await getRentsDelayed();

  const ctx = document.getElementById('chartBarComponent').getContext('2d');
  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['Alugados', 'Atrasados', 'Devolvidos no prazo', 'Devolvidos fora do prazo'],
      datasets: [{
        data: [rentsQtd.value, late.value, delivered.value, delayed.value],
        backgroundColor: ['#509358', '#B22222', '#46769A', '#C65F15'],
        borderWidth: 0,
        borderRadius: 5
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        y: {
          beginAtZero: true
        }
      },
      plugins: {
        legend: {
          display: false,
          position: 'top',
        }
      }
    }
  });
});
</script>

<style scoped>
.grafico1 {
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
  margin-top: 3px;
  font-weight: bold;
}

@media (max-width: 370px) {
  .grafico1 {
    width: 320px;
  }
}
</style>
