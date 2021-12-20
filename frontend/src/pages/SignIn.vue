<template lang="">
  <v-main style="background-color: #7e57c2">
    <v-container class="py-16" style="width: 70%">
      <v-card elevation="7" shaped>
        <v-row class="mb-5">
          <v-col style="align-self: center; text-align: -webkit-center">
            <v-img
              src="@/assets/img/signin.png"
              style="border-radius: 30px 0px 0px 0px; width: 28vw"
            >
            </v-img>
          </v-col>
          <v-col style="background-color: #c5cae9; border-radius: 0px 0px 30px 0px" class="pa-10">
            <div class="pt-3 pb-10">
              <h1 class="f1">로그인</h1>
            </div>
            <v-form ref="form" v-model="valid">
              <v-text-field v-model="email" :rules="emailRules" label="이메일" solo />
              <v-text-field
                v-model="password"
                :rules="passwordRules"
                type="password"
                label="비밀번호"
                solo
              />
              <v-btn @click="login" color="deep-purple lighten-3" dark class="long-btn" rounded
                >로그인</v-btn
              >
              <v-btn
                @click="$router.push('/sign-up')"
                color="pink lighten-1"
                dark
                class="long-btn"
                rounded
                >회원가입</v-btn
              >
            </v-form>
          </v-col>
        </v-row>
      </v-card>
    </v-container>
  </v-main>
</template>
<script>
// import { authComment } from '@/api/comment';

export default {
  data() {
    return {
      passwordRules: [
        (value) => !!value || '비밀번호를 입력해주세요!',
        (value) => value.length >= 5 || '5글자 이상 입력해 주세요',
      ],
      emailRules: [
        (value) => !!value || '이메일을 입력해주세요!',
        (v) => /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) || '이메일 형식이 아닙니다',
      ],
      //댓글 등록용 content
      password: '',
      email: '',
      valid: true,
    };
  },
  methods: {
    login() {
      this.validate();
      if (!this.valid) return;
      let payload = {
        email: this.email,
        password: this.password,
      };
      this.$store
        .dispatch('LOGIN', payload)
        .then(() => {
          this.reset();
          this.$router.push('/');
        })
        .catch((error) => {
          if (error.response) console.log(error.response.data.message);
          else console.error(error);
          alert('로그인 실패');
        });
    },
    validate() {
      this.$refs.form.validate();
    },
    reset() {
      this.$refs.form.reset();
      this.email = '';
      this.password = '';
    },
    resetValidation() {
      this.$refs.form.resetValidation();
    },
  },
};
</script>
<style scoped>
.f1 {
  font-family: BMHANNAPro !important;
}
.long-btn {
  width: 100%;
  margin-top: 10px;
  display: block;
}
</style>
