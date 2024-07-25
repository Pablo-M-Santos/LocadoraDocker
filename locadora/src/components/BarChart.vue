<template>
  <div class="grafico1">
    <canvas ref="barchart"></canvas>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import Chart from 'chart.js/auto';

const barchart = ref(null);

onMounted(() => {
  new Chart(barchart.value, {
    type: 'bar',
    data: {
      labels: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun'],
      datasets: [
        {
          label: 'Devolvidos dentro do prazo',
          data: [35, 55, 55, 65, 40, 50],
          backgroundColor: 'rgba(0, 102, 102, 1)',
          borderWidth: 1,
          borderRadius: 15
        },
        {
          label: 'Devolvidos fora do prazo',
          data: [35, 55, 50, 65, 35, 65],
          backgroundColor: 'rgba(0, 64, 128,1)',
          borderWidth: 1,
          borderRadius: 15
        }
      ]
    },
    options: {
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
  background-color: white;
  padding: 0;
}


@media (max-width: 370px) {
  .grafico1 {
    width: 320px;
  }
}
</style>
