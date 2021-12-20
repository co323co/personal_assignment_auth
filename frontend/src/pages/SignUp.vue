<template lang="">
  <v-main style="background-color: #7e57c2">
    <v-container class="py-16">
      <v-card elevation="7" shaped>
        <v-row class="mb-5">
          <v-col style="align-self: center; text-align: -webkit-center">
            <v-img
              src="@/assets/img/signup.png"
              style="border-radius: 30px 0px 0px 0px; width: 35vw"
            >
            </v-img>
          </v-col>
          <v-col
            style="background-color: #c5cae9; border-radius: 0px 0px 30px 0px"
            class="px-10 pt-10 pb-9"
          >
            <div class="pt-3 pb-10">
              <h1 class="f1">회원가입</h1>
            </div>
            <v-form ref="form" v-model="valid">
              <v-text-field v-model="email" :rules="emailRules" label="이메일" solo />
              <v-text-field v-model="nickname" :rules="nicknameRules" label="닉네임" solo />
              <v-text-field
                v-model="password"
                :rules="passwordRules"
                type="password"
                label="비밀번호"
                solo
              />
              <v-text-field
                v-model="rePassword"
                :rules="rePasswordRules"
                type="password"
                label="비밀번호 재확인"
                solo
              />
              <v-btn @click="register" color="pink lighten-1" dark class="long-btn" rounded
                >회원가입</v-btn
              >
              <v-btn
                @click="$router.push('/sign-in')"
                text
                class="long-btn"
                rounded
                color="grey darken-2"
                style="font-weight: bold"
                >로그인 페이지로 돌아가기</v-btn
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
import { signUp } from '@/api/auth';

export default {
  data() {
    return {
      emailRules: [
        (value) => !!value || '이메일을 입력해주세요',
        (v) => /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) || '이메일 형식이 아닙니다',
      ],
      nicknameRules: [
        (value) => value.length != '' || '닉네임을 입력해주세요',
        (value) => value.length <= 10 || '1~10글자 사이로 입력해주세요',
      ],
      passwordRules: [
        (value) => !!value || '비밀번호를 입력해주세요',
        (value) => value.length >= 5 || '5글자 이상 입력해 주세요',
      ],
      rePasswordRules: [(value) => value == this.password || '비밀번호가 일치하지 않습니다'],
      //댓글 등록용 content
      email: '',
      nickname: '',
      password: '',
      rePassword: '',
      valid: true,
    };
  },
  methods: {
    register() {
      this.validate();
      if (!this.valid) return;

      let payload = {
        email: this.email,
        nickname: this.nickname,
        password: this.password,
        grade: 'user', //기본 유저 등급
      };
      signUp(payload)
        .then(({ data }) => {
          alert('회원가입 성공');
          console.log(data.result);
          this.reset();
          this.$router.push('/sign-in');
        })
        .catch((error) => {
          alert('회원가입 실패');
          console.error('auth.module.js', error.response.data.message);
          throw error;
        });
    },
    validate() {
      this.$refs.form.validate();
    },
    reset() {
      this.$refs.form.reset();
      this.email = '';
      this.nickname = '';
      this.password = '';
      this.rePassword = '';
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
