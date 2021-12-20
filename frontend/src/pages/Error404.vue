<template>
  <div class="fullscreen bg-primary text-white text-center flex flex-center">
    <div style="font-size: 20vh">404</div>
    <div class="text-h2" style="opacity: 0.8">PAGE NOT FOUND</div>
    <v-btn class="ma-5" color="primary" @click="$router.push({ path: `/` })">GO HOME</v-btn>
  </div>
</template>

<script>
export default {
  name: 'Error404',
};
</script>
