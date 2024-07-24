<template>
  <q-page class="flex flex-center">
    <q-card style="width: 400px; max-width: 90%">
      <q-card-section>
        <div class="text-h6">Login</div>
      </q-card-section>

      <q-card-section>
        <q-form class="form" @submit.prevent="login">
          <q-input
            v-model="username"
            label="Usuário"
            outlined
            required
          />
          <q-input
            v-model="password"
            label="Senha"
            type="password"
            outlined
            required
          />
          <q-btn
            type="submit"
            label="Entrar"
            color="primary"
            class="full-width"
          />
        </q-form>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const username = ref('');
const password = ref('');
const router = useRouter();

const login = () => {
  if (username.value === 'admin' && password.value === 'admin') {
    alert('Login bem-sucedido!');
    router.push({ name: 'home' });
  } else {
    alert('Usuário ou senha incorretos.');
  }
};

</script>

<style scoped>
.q-page {
  background-color: #f5f5f5;
}
.form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style>
