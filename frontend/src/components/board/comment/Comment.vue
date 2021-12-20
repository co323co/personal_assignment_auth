<template lang="">
  <div>
    <!-- 댓글 보여주는 form -->
    <div v-if="!isModifyShow" class="comment">
      <div class="head">{{ comment.commentWriter }}</div>
      <div class="content mt-2" v-html="enterToBr(comment.commentContent)"></div>
      <v-row class="mt-3">
        <v-col
          v-if="comment.commentCreatedAt == comment.commentUpdatedAt"
          d-inline-block
          class="date"
        >
          {{ comment.commentCreatedAt | dateFilter }}
        </v-col>
        <v-col v-else d-inline-block class="date">
          {{ comment.commentUpdatedAt | dateFilter }} 수정됨
        </v-col>
        <v-col class="mb-1 text-right">
          <v-btn text @click="isModifyShow = true">수정</v-btn> |
          <v-btn text @click="remove">삭제</v-btn>
        </v-col>
      </v-row>
      <div class="divider"></div>
    </div>
    <!-- 댓글 수정하는 form -->
    <div v-if="isModifyShow" class="comment">
      <v-form ref="form" v-model="valid">
        <v-row>
          <v-col class="head" style="margin-top: 20px"
            >{{ comment.commentWriter }}
            <v-chip v-if="isWrongPassword" x-small class="ma-2" color="red" outlined>
              비밀번호가 틀렸습니다!
            </v-chip>
          </v-col>
          <v-col class="text-right" cols="3">
            <v-text-field
              class="mb-2"
              label="비밀번호"
              v-model="password"
              :rules="passwordRules"
              hide-details="auto"
            ></v-text-field>
          </v-col>
        </v-row>
        <div class="content">
          <v-textarea :rules="contentRules" hide-details="auto" v-model="content" solo></v-textarea>
        </div>
        <v-row offset-lg11>
          <v-col
            v-if="comment.commentCreatedAt == comment.commentUpdatedAt"
            d-inline-block
            class="date"
          >
            {{ comment.commentCreatedAt | dateFilter }}
          </v-col>
          <v-col v-else d-inline-block class="date">
            {{ comment.commentUpdatedAt | dateFilter }} 수정됨
          </v-col>
          <v-col class="mb-1 cbtn text-right">
            <v-btn text @click="modify">확인</v-btn> |
            <v-btn text @click="cancle">취소</v-btn>
          </v-col>
        </v-row>
      </v-form>
      <div class="divider"></div>
    </div>
  </div>
</template>
<script>
// import { mapGetters } from 'vuex';
import { getComment, updateComment, deleteComment } from '@/api/comment';
export default {
  props: ['comment'],
  component: {},
  computed: {},
  data() {
    return {
      //현재 댓글이 보기 상태인지 수정 상태인지를 나타낸다
      isModifyShow: false,
      //copy_comment의 약자, props가 변경되는 걸 막기 위해 현재 .vue에서 사용할 comment 복사
      // copyComment: this.copy(),
      passwordRules: [
        (value) => /[0-9]{4}/.test(value) || '4자리 숫자',
        (value) => value.length == 4 || '4자리',
      ],
      contentRules: [(value) => !!value || '내용을 입력해주세요!'],
      password: '',
      content: this.comment.commentContent,
      valid: true,
      isWrongPassword: false,
    };
  },
  methods: {
    // 댓글 수정
    modify() {
      this.isModifyShow = true;
      this.validate();
      if (!this.valid) return;
      let payload = {
        content: this.content,
        password: this.password,
        postId: this.comment.postId,
      };
      console.log(payload);
      updateComment(payload, this.comment.commentId)
        .then(() => {
          //수정했으니 댓글 다시 얻기
          let payload = { postId: this.comment.postId, page: 1, size: 1000 };
          this.$store.dispatch('getCommentsDic', payload).then(() => {
            this.reset();
            this.refreshComment();
          });
          this.isModifyShow = false;
        })
        .catch(() => {
          this.isWrongPassword = true;
          console.log('오류');
        });
      //수정 폼 다시 되돌리기
      //   });
    },
    remove() {
      if (confirm('정말로 삭제할까요?')) {
        deleteComment(this.comment.commentId).then(() => {
          let payload = { postId: this.comment.postId, page: 1, size: 1000 };
          this.$store.dispatch('getCommentsDic', payload);
        });
      }
    },
    cancle() {
      this.isModifyShow = false;
      this.refreshComment();
      this.reset();
    },
    enterToBr(str) {
      if (str) return str.replace(/(?:\r\n|\r|\n)/g, '<br />');
    },
    validate() {
      this.$refs.form.validate();
    },
    reset() {
      this.$refs.form.reset();
      this.isWrongPassword = false;
    },
    resetValidation() {
      this.$refs.form.resetValidation();
    },
    refreshComment() {
      this.password = '';
      getComment(this.comment.commentId).then((res) => {
        this.content = res.commentContent;
        this.nickname = res.writer;
      });
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
  watch: {
    comment: function () {},
  },
  filters: {
    dateFilter(dateStr) {
      let date = new Date(dateStr);
      return date.getFullYear() + '년 ' + (date.getMonth() + 1) + '월 ' + date.getDate() + '일';
    },
  },
};
</script>

<style>
.comment {
  text-align: left;
  border-radius: 5px;
  /* background-color: #ffffff; */
  padding: 10px 20px;
  margin: 10px;
}
.head {
  font-weight: bold;
  margin-bottom: 5px;
}
.content {
  padding-top: 5px;
  padding-bottom: 5px;
  margin-bottom: 10px;
}
.cbtn {
  color: steelblue;
}
.divider {
  background-color: darkgray;
  height: 1px;
  width: 100%;
}
.date {
  align-self: center;
  font-size: 0.85rem;
  color: gray;
}
</style>
