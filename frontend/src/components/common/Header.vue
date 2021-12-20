<template>
  <v-app-bar app absolute color="deep-purple lighten-2" dark dense>
    <v-app-bar-title class="ml-5 mb-3"> </v-app-bar-title>

    <div>안녕하세요 😊{{ userInfo.userNickname }} 님, 반가워요!</div>
    <v-spacer></v-spacer>
    <span>[ {{ userInfo.userEmail }} ]</span>
    <v-btn class="ml-3" text @click="logout">로그아웃</v-btn>
  </v-app-bar>
</template>

<script>
import { mapGetters } from 'vuex';
export default {
  name: 'Header',
  components: {},
  data() {
    return {};
  },
  computed: {
    ...mapGetters(['userInfo']),
  },
  watch: {},
  created() {},
  methods: {
    logout() {
      this.$store.dispatch('LOGOUT').then(() => {
        console.log('로그아웃 성공');
        this.$router.push('/sign-in').catch(() => {});
      });
    },
  },
};
</script>

<style scoped>
h2 {
  font-family: BMHANNAPro;
}
</style>
