<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated class="custom-header">
      <q-toolbar>
        <q-btn flat dense round icon="menu" aria-label="Menu" @click="toggleLeftDrawer" />
        <q-toolbar-title>{{ pageTitle }}</q-toolbar-title>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawerOpen" show-if-above bordered class="custom-drawer">
      <q-list class="drawer-content">
        <q-item-label header>
          <q-img class="logo" src="src/assets/logo.png" />
        </q-item-label>
        <q-item v-for="link in linksList" :key="link.title" :to="link.route" clickable>
          <q-item-section avatar>
            <q-icon :name="link.icon" />
          </q-item-section>
          <q-item-section>
            <q-item-label>{{ link.title }}</q-item-label>
            <q-item-label caption>{{ link.caption }}</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>

      <q-separator />

      <q-list class="drawer-footer logout-item">
        <q-item clickable @click="handleLogout">
          <q-item-section avatar>
            <q-icon name="logout" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Logout</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const linksList = [
  { title: 'Home', caption: '', icon: 'home', route: { name: 'home' } },
  { title: 'Editora', caption: '', icon: 'edit', route: { name: 'editora' } },
  { title: 'Livros', caption: '', icon: 'book', route: { name: 'livros' } },
  { title: 'Locatário', caption: '', icon: 'person', route: { name: 'locatario' } },
  { title: 'Aluguel', caption: '', icon: 'import_contacts', route: { name: 'aluguel' } },
  { title: 'Usuário', caption: '', icon: 'settings', route: { name: 'usuario' } }
]

const leftDrawerOpen = ref(false)

function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}

const pageTitle = computed(() => {
  return route.meta.title || 'Página Inicial'
})

function handleLogout() {
  router.push({ name: 'login' })
}
</script>

<style>
.custom-header {
  background-color: white;
  box-shadow: 3px 4px 10px 0px rgba(0, 0, 0, 0.25);
  color: black;
}

.custom-drawer {
  background-color: white;
  box-shadow: 3px 4px 10px 0px rgba(0, 0, 0, 0.25);
  color: black;
  display: flex;
  flex-direction: column;
}

.drawer-content {
  flex: 1;
}

.drawer-footer {
  margin-top: auto;
}

.logout-item {
  border: none;
  box-shadow: none;
}

.logout-item:hover {
  background-color: #f5f5f5;
}

.logo {
  width: 110.62px;
  height: 56px;
  margin: 35px 67px 51px 66px;
}
</style>
