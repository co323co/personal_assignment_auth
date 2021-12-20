<template lang="">
  <div class="mx-16 px-16 py-10 back" style="margin-top: 100px; text-align: center">
    <div class="pt-1">
      <span class="bold">{{ userInfo.userNickname }}</span
      >님 안녕하세요
    </div>
    <div class="pt-1">
      {{ userInfo.userNickname }}
      님의 e-mail은 📧 <span class="bold">{{ userInfo.userEmail }}</span> 입니다.
    </div>
    <div class="pt-1">
      <span class="bold">{{ userInfo.userCreatedAt | dateFilter }}</span
      >에 가입하셨군요!
    </div>
    <div class="pt-1">
      현재 <span class="bold">{{ userInfo.userGrade }}</span> 등급입니다
    </div>
    <v-btn
      v-if="userInfo.userGrade == 'admin'"
      @click="$router.push('/admin')"
      class="mt-5"
      dark
      color="indigo lighten-1"
      >관리자 페이지 이동</v-btn
    >
  </div>
</template>
<script>
import { mapGetters } from 'vuex';
export default {
  components: {},
  data() {
    return {};
  },
  created() {},
  computed: {
    ...mapGetters(['userInfo']),
  },
  methods: {},
  filters: {
    dateFilter: function (value) {
      let date = new Date(value);
      return date.getFullYear() + '년 ' + (date.getMonth() + 1) + '월 ' + date.getDate() + '일';
    },
  },
};
</script>
<style scoped>
.font {
  font-family: IBMPlexSansKR !important;
}
.bold {
  font-weight: bold;
  font-size: 0.9rem;
}
.back {
  background-color: rgb(247, 243, 243);
  border-radius: 20px;
}
</style>
