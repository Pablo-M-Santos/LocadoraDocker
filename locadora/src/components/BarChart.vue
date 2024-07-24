<template>
  <div>
    <Bar :data="data" :options="options" />
  </div>
</template>

<script setup>
import { defineComponent } from 'vue';
import { Bar } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js';

// Registrar os elementos necessários do Chart.js
ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale);

const data = {
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
};

const options = {
  scales: {
    y: {
      beginAtZero: true
    }
  }
};
</script>
