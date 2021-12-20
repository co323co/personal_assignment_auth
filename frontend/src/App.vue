<template>
  <v-app>
    <Header v-if="$store.getters.isLogin" />
    <router-view :key="$route.fullPath"></router-view>
    <Footer />
  </v-app>
</template>

<script>
import Header from '@/components/common/Header.vue';
import Footer from '@/components/common/Footer.vue';
export default {
  name: 'App',
  components: { Header, Footer },
  data: () => ({
    //
  }),
};
</script>
<style>
@import 'assets/css/font.css';
</style>
