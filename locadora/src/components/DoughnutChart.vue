<template>
  <div class="grafico2">
    <canvas ref="doughnut"></canvas>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import Chart from 'chart.js/auto';

const doughnut = ref(null);

onMounted(() => {
  new Chart(doughnut.value, {
    type: 'doughnut',
    data: {
      labels: ['Livros Emprestados', 'Livros Atrasados'],
      datasets: [{
        data: [63, 37],
        backgroundColor: ['rgba(0, 64, 128,1)', 'rgba(0, 102, 102, 1)'],
        borderWidth: 1
      }]
    },
    options: {
      plugins: {
        tooltip: {
          callbacks: {
            label: function(context) {
              let label = context.label || '';
              let value = context.raw || 0;
              return `${value} %`;
            }
          }
        },
        legend: {
          labels: {
            usePointStyle: true,
            pointStyle: 'circle'
          }
        }
      }
    }
  });
});
</script>

<style scoped>
.grafico2 {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 380px;
  height: 289px;
  padding: 10px;
  box-shadow: 3px 4px 10px 0px rgba(0, 0, 0, 0.25);
  border-radius: 20px;
  background-color: white;
}

@media (max-width: 1150px) {
  .grafico2 {
    width: 505px;
    height: 299px;
  }
}

@media (max-width: 800px) {
  .grafico2 {
    width: 420px;
  }
}

@media (max-width: 550px) {
  .grafico2 {
    width: 350px;
  }
}

@media (max-width: 370px) {
  .grafico2 {
    width: 320px;
  }
}
</style>
