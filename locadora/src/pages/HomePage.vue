<template>
  <q-page class="graficos">
    <q-card flat bordered class="q-mb-md">
      <q-card-section>
        <q-banner class="bg-primary text-white q-mb-md">
          <span class="text-h6">Gráficos</span>
        </q-banner>
        <div class="q-gutter-md row">
          <div class="col-12 col-md-6">
            <q-card class="my-card">
              <q-card-section>
                <canvas id="barchart" width="463" height="237"></canvas>
              </q-card-section>
            </q-card>
          </div>
          <div class="col-12 col-md-6">
            <q-card class="my-card">
              <q-card-section>
                <canvas id="doughnut" width="290" height="201"></canvas>
              </q-card-section>
            </q-card>
          </div>
        </div>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script>
import { onMounted } from 'vue';
import { Chart, registerables } from 'chart.js';

export default {
  name: 'GraficosPage',
  setup() {
    onMounted(() => {
      Chart.register(...registerables);

      new Chart(document.getElementById('barchart'), {
        type: 'bar',
        data: {
          labels: ['A', 'B', 'C', 'D'],
          datasets: [{
            label: 'Exemplo de Barras',
            data: [12, 19, 3, 5],
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'top',
            },
            tooltip: {
              callbacks: {
                label: function(tooltipItem) {
                  return tooltipItem.dataset.label + ': ' + tooltipItem.raw;
                }
              }
            }
          }
        }
      });

      new Chart(document.getElementById('doughnut'), {
        type: 'doughnut',
        data: {
          labels: ['Red', 'Blue', 'Yellow'],
          datasets: [{
            label: 'Exemplo de Donut',
            data: [300, 50, 100],
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56']
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'top',
            },
            tooltip: {
              callbacks: {
                label: function(tooltipItem) {
                  return tooltipItem.dataset.label + ': ' + tooltipItem.raw;
                }
              }
            }
          }
        }
      });
    });
  }
}
</script>

<style scoped>
.graficos {
  padding: 20px;
}

.my-card {
  box-shadow: none;
}

.q-banner {
  margin-bottom: 20px;
}

.q-card-section {
  padding: 16px;
}
</style>
