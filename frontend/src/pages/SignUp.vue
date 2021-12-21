<template lang="">
  <v-main style="background-color: #7e57c2">
    <v-container class="py-16" style="width: 70%">
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
              <v-row v-if="!isAuthMode">
                <v-col style="padding-right: 0px !important">
                  <v-form ref="emailForm">
                    <v-text-field v-model="email" :rules="emailRules" label="이메일" solo />
                  </v-form>
                </v-col>
                <v-col cols="2" class="text-center">
                  <v-progress-circular
                    v-if="isLoading"
                    class="mt-2"
                    indeterminate
                    color="red"
                  ></v-progress-circular>
                  <v-btn v-else @click="send" dark color="success" height="62%" width="100%"
                    >인증</v-btn
                  >
                </v-col>
              </v-row>
              <div v-else>
                <div class="ml-1 mb-2" style="font-size: 0.8rem; color: red">
                  <div v-if="time != 0">
                    {{ time | timeFilter }}
                  </div>
                  <div v-else>시간 만료</div>
                </div>
                <v-row>
                  <v-col style="padding-right: 0px !important">
                    <v-text-field v-model="code" label="인증 코드" dense solo />
                  </v-col>
                  <v-col v-if="time == 0" cols="2" class="text-center">
                    <v-progress-circular
                      v-if="isLoading"
                      class="mt-1"
                      indeterminate
                      color="red"
                    ></v-progress-circular>

                    <v-btn v-else @click="send" width="100%" dark color="success">재인증</v-btn>
                  </v-col>
                  <v-col v-else cols="2" class="text-center">
                    <v-btn @click="checkAuth" width="100%" dark color="red">확인</v-btn>
                  </v-col>
                </v-row>
              </div>
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
import { signUp, sendEmail } from '@/api/auth';

export default {
  data() {
    return {
      emailRules: [
        (value) => !!value || '이메일을 입력해주세요',
        (v) => /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) || '이메일 형식이 아닙니다',
        (v) => this.isAuth == true || '이메일 인증을 해주세요',
      ],
      nicknameRules: [
        (value) => value.length != '' || '닉네임을 입력해주세요',
        (value) => value.length <= 10 || '1~10글자 사이로 입력해주세요',
        (value) => this.isNicknameDuti == false || '닉네임 중복',
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
      code: '',
      time: null,
      timer: null,
      valid: true,
      isAuthMode: false,
      mailCode: '',
      isAuth: false,
      isLoading: false,
      isNicknameDuti: false,
    };
  },
  created() {},
  methods: {
    checkAuth() {
      if (this.mailCode == this.code) {
        console.log('메일 인증 성공');
        this.isAuthMode = false;
        this.isAuth = true;
        this.resetTimer();
      } else {
        alert('인증 코드가 다릅니다');
      }
    },
    send() {
      // let payload = { email: this.email };
      this.isAuth = false;
      this.code = '';
      const isOk = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(this.email);
      if (!isOk) {
        this.$refs.emailForm.validate();
        return;
      }
      this.isLoading = true;
      let payload = { email: this.email };
      sendEmail(payload)
        .then((res) => {
          console.log('then');
          console.log(res.data.result.auth);
          this.mailCode = res.data.result.auth;
          this.time = 180;
          this.isAuthMode = true;
          this.isLoading = false;
          this.startTimer();
        })
        .catch((error) => {
          console.log('catch');
          if (error.response) {
            this.isLoading = false;
            let code = error.response.data.code;
            let status = error.response.data.status;
            // 이미 존재하는 이메일
            if (code == 404 && status == 400) {
              alert('이미 존재하는 이메일입니다');
              this.isNicknameDuti = true;
            }
            console.error(error.response.data);
          } else console.error(error);
        });
    },
    startTimer() {
      this.timer = setInterval(() => this.countdown(), 1000);
      // this.resetButton = true;
    },
    countdown() {
      if (this.time >= 1) {
        this.time--;
      } else {
        this.time = 0;
        this.resetTimer();
      }
    },
    resetTimer() {
      // this.time = 1 * 60;
      clearInterval(this.timer);
      this.timer = null;
      // this.resetButton = false;
    },
    register() {
      if (!this.validate()) return;
      // if (!this.valid) return;
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
          // 닉네임 중복
          if (error.response.data.code == 405 && error.response.data.status == 400) {
            this.isNicknameDuti = true;
            console.log('닉네임 중복');
            this.validate();
          }
          console.error('auth.module.js', error.response.data);
          throw error;
        });
    },
    validate() {
      return this.isAuth && this.$refs.emailForm.validate() && this.$refs.form.validate();
    },
    reset() {
      this.$refs.emailForm.reset();
      this.$refs.form.reset();
      this.email = '';
      this.nickname = '';
      this.password = '';
      this.rePassword = '';
      this.code = '';
      this.isNicknameDuti = false;
    },
    resetValidation() {
      this.$refs.eamilForm.resetValidation();
      this.$refs.form.resetValidation();
    },
  },
  filters: {
    timeFilter: function (seconds) {
      var min = parseInt((seconds % 3600) / 60);
      var sec = seconds % 60;
      if (sec < 10) sec = '0' + sec;
      return min + ':' + sec;
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
