<template>
  <q-page>
    <div class="graficos">
      <div class="grafico1">
        <BarChart />
      </div>
      <div class="grafico2">
        <DoughnutChart />
      </div>
    </div>
    <div class="topLivrosContainer">
      <top-livros />
      <MaisAlugados />
    </div>
  </q-page>
</template>

<script>
import BarChart from 'components/BarChart.vue';
import DoughnutChart from 'components/DoughnutChart.vue';
import TopLivros from 'src/components/TopLivros.vue';
import MaisAlugados from 'src/components/MaisAlugados.vue';

export default {
  components: {
    BarChart,
    DoughnutChart,
    TopLivros,
    MaisAlugados
  }
}
</script>

<style scoped>
.graficos {
  display: flex;
  justify-content: center;
  align-items: center;
  justify-content: space-around;
  margin-top: 20px;
}

.topLivrosContainer{
  margin-top: 20px;
}

@media (max-width: 1350px) {
  .graficos {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    gap: 40px;
  }

  .livros-alugados {
    flex-direction: column;
    width: 800px;
    height: 500px;
  }

  .grafico2 {
    width: 505px;
    height: 299px;
  }
}

@media (max-width: 550px) {
  .grafico1 {
    width: 420px;
  }

  .mais-alugados h1 {
    font-size: 22px;
  }

  .grafico2 {
    width: 420px;
  }

  .livros-alugados {
    width: 420px;
    height: auto;
  }

  .mais-alugados {
    width: 420px;
  }
}

@media (max-width: 450px) {
  .grafico1 {
    width: 350px;
  }

  .grafico2 {
    width: 350px;
  }

  .livros-alugados {
    width: 350px;
    height: auto;
  }

  .mais-alugados {
    width: 350px;
  }
}

@media (max-width: 370px) {
  .grafico1 {
    width: 320px;
  }

  .grafico2 {
    width: 320px;
  }

  .livros-alugados {
    width: 320px;
    height: auto;
  }

  .livros-alugados h1 {
    font-size: 22px;
  }

  .mais-alugados {
    width: 320px;
  }
}
</style>
