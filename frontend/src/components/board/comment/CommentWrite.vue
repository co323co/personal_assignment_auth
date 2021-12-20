<template>
  <div class="comment">
    <div></div>
    <v-form ref="form" v-model="valid" class="content">
      <v-row>
        <v-col>
          <div style="margin-top: 23px; margin-left: 2px">
            <v-icon class="mr-1" style="color: steelblue; margin-bottom: 2px" small
              >mdi-pencil
            </v-icon>
            <h4 style="color: steelblue; display: inline-block">새 댓글 달기</h4>
          </div>
        </v-col>
        <v-col class="text-right" cols="2">
          <v-text-field label="닉네임" v-model="nickname" hide-details="auto"></v-text-field>
        </v-col>
        <v-col class="text-right" cols="2">
          <v-text-field
            label="비밀번호"
            v-model="password"
            :rules="passwordRules"
            hide-details="auto"
          ></v-text-field>
        </v-col>
        <v-col cols="1" class="text-right mr-1">
          <v-btn
            fab
            small
            elevation="1"
            style="margin-bottom: -35px"
            @click="regist"
            class="head m1-5"
          >
            <v-icon small>mdi-plus</v-icon>
          </v-btn>
        </v-col>
      </v-row>
      <v-textarea
        :rules="contentRules"
        class="mt-3"
        hide-details="auto"
        v-model="content"
        solo
      ></v-textarea>
    </v-form>
  </div>
</template>

<script>
import axios from '@/utils/axios';
import { mapGetters } from 'vuex';
import { createComment } from '@/api/comment';

export default {
  name: 'commentwrite',
  props: { postId: Number },
  data() {
    return {
      passwordRules: [
        (value) => /[0-9]{4}/.test(value) || '4자리 숫자',

        (value) => value.length == 4 || '4자리',
      ],
      contentRules: [(value) => !!value || '내용을 입력해주세요!'],
      //댓글 등록용 content
      content: '',
      password: '',
      nickname: '',
      valid: true,
    };
  },
  computed: {
    ...mapGetters(['currentUser']),
  },
  methods: {
    validate() {
      this.$refs.form.validate();
    },
    reset() {
      this.$refs.form.reset();
      this.password = '';
      this.nickname = null;
      this.content = null;
    },
    resetValidation() {
      this.$refs.form.resetValidation();
    },
    regist() {
      this.validate();
      if (!this.valid) return;
      let write = this.nickname ? this.nickname : null;
      let payload = {
        writer: write,
        content: this.content,
        password: this.password,
        postId: this.postId,
      };
      createComment(payload).then(() => {
        let payload = { postId: this.postId, page: 1, size: 1000 };
        this.$store.dispatch('getCommentsDic', payload);
      });
      this.reset();
    },
  },
};
</script>

<style scoped>
.comment {
  text-align: left;
  border-radius: 5px;
  /* background-color: #ffffff; */
  padding: 0px 20px 10px 20px;
  margin: 10px;
}
.divider {
  height: 2px;
  border-radius: 50px;
  background-color: steelblue;
}
.orangeColor {
  color: steelblue;
}
.border {
  border-color: black;
  border-width: 1px;
}
.head {
  font-weight: bold;
  /* margin-bottom: 5px; */
}
.cbtn {
  color: steelblue;
}
</style>
