<template>
  <div>
    <Doughnut :data="data" :options="options" />
  </div>
</template>

<script setup>
import { defineComponent } from 'vue';
import { Doughnut } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement } from 'chart.js';

// Registrar os elementos necessários do Chart.js
ChartJS.register(Title, Tooltip, Legend, ArcElement);

const data = {
  labels: ['Livros Emprestados', 'Livros Atrasados'],
  datasets: [{
    data: [63, 37],
    backgroundColor: ['rgba(0, 64, 128,1)','rgba(0, 102, 102, 1)'],
    borderWidth: 1
  }]
};

const options = {
  plugins: {
    tooltip: {
      callbacks: {
        label: function(context) {
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
};
</script>
