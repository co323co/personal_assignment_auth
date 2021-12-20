<template>
  <!-- 게시글 -->
  <div>
    <!-- 게시글 조회 Form -->
    <div v-if="!isEditMode" class="view">
      <!-- 시리즈명 라벨  -->
      <v-chip v-if="post.seriesName" color="orange" outlined small class="my-4">
        {{ post.seriesName }}
      </v-chip>
      <v-chip v-else color="gray" outlined small class="my-4"> 미분류 </v-chip>
      <!-- 게시글 타이틀 바 -->
      <v-row class="top">
        <v-col>
          <h2>{{ post.postTitle }}</h2>
        </v-col>
      </v-row>
      <v-row style="margin-top: 0px">
        <v-col class="muted" style="align-self: end">
          {{ postCreated }}
        </v-col>
        <v-col v-if="!isAlertShow" class="text-right">
          <!-- 수정 버튼 -->
          <v-btn @click="changeMode('edit')" class="mr-1" plain small fab color="grey lighten-1">
            <v-icon>mdi-pencil</v-icon>
          </v-btn>
          <!-- 삭제 버튼 -->
          <v-btn
            @click="changeMode('try_delete')"
            class="ml-1"
            plain
            small
            fab
            color="grey lighten-1"
          >
            <v-icon>mdi-delete</v-icon>
          </v-btn>
        </v-col>
      </v-row>
      <v-divider class="my-2"></v-divider>
      <!-- 삭제 알림 -->
      <v-row v-if="isAlertShow" style="margin-top: 20px; z-index: 2">
        <Alert msg="정말로 글을 삭제하시겠어요?" :ok="deleteAlertOk" :no="deleteAlertNo" />
      </v-row>
      <!-- 게시글 내용 -->
      <v-row class="content py-5 ma-1 mb-5" v-html="enterToBr(post.postContent)"> </v-row>
      <!-- 게시글 하단 바 -->
      <div class="bottom">
        <v-row v-if="!isAlertShow">
          <!-- 좌측 -->
          <v-col class="text-left">
            <v-btn
              v-if="!isShowComment"
              @click="clickCommentBtn('open')"
              class="mt-2"
              text
              outlined
            >
              <v-icon class="mr-1" gray small>mdi-message-processing</v-icon>
              댓글 {{ post.commentCount }}
            </v-btn>
            <v-btn
              v-else
              color="red darken-3"
              @click="clickCommentBtn('close')"
              class="mt-2"
              text
              outlined
            >
              <v-icon class="mr-1" gray small>mdi-message-processing</v-icon>
              댓글 {{ post.commentCount }}
            </v-btn>
          </v-col>
        </v-row>
      </div>

      <!-- 댓글 리스트 -->
      <div v-if="isShowComment" class="commentArea">
        <CommentWrite :postId="post.postId" />
        <div v-for="(comment, index) in commentList" :key="index">
          <Comment :comment="comment" />
        </div>
      </div>
    </div>

    <!-- 수정 Form -->
    <div v-else class="edit">
      <v-row class="px-1">
        <v-col cols="2">
          <v-select
            height="42"
            dense
            small-chips
            :items="selectSeriesList"
            outlined
            label="시리즈 이동"
            v-model="selectValue"
            hide-details="auto"
          ></v-select>
        </v-col>
      </v-row>
      <!-- 게시글 타이틀 바 -->
      <v-row class="top pa-1">
        <v-col>
          <v-text-field
            :rules="rules"
            style="font-size: 1.7rem; font-weight: bold"
            class="mt-3"
            hide-details="auto"
            label="제목"
            v-model="editPost.postTitle"
          ></v-text-field>
        </v-col>
      </v-row>
      <!-- 게시글 내용 -->
      <v-row class="content px-1 py-5 ma-1">
        <v-textarea
          :rules="rules"
          clear-icon="mdi-close-circle"
          label="내용"
          v-model="editPost.postContent"
          style="font-size: 1.1rem"
          rows="15"
        ></v-textarea>
      </v-row>
      <!-- 게시글 하단 바 -->
      <div class="bottom mb-2" style="margin-right: -20px">
        <v-row v-if="!isAlertShow">
          <!-- 우측 -->
          <v-col class="text-right">
            <!-- 수정 취소 버튼 -->
            <v-btn
              @click="changeMode('try_cancle')"
              class="mr-1"
              plain
              small
              fab
              color="red darkten-1"
            >
              <v-icon>mdi-reply</v-icon>
            </v-btn>
            <!-- 수정 확인 버튼 -->
            <v-btn
              @click="changeMode('submit')"
              class="mx-1"
              plain
              small
              fab
              color="green darken-3"
            >
              <v-icon>mdi-check</v-icon>
            </v-btn>
          </v-col>
        </v-row>
        <!-- <transition name="slide-fade"> -->
        <v-row v-else style="top: -350px; z-index: 3">
          <Alert
            msg="작성하시던 내용이 사라질 수 있습니다. 취소하시겠습니까?"
            :ok="cancleAlertOk"
            :no="cancleAlertNo"
          />
        </v-row>
        <!-- </transition> -->
      </div>
    </div>

    <v-divider class="my-4"></v-divider>
  </div>
</template>

<script>
import Alert from '@/components/common/WarningAlert';
import Comment from '@/components/board/comment/Comment';
import CommentWrite from '@/components/board/comment/CommentWrite';
import { updatePost, deletePost } from '@/api/post';
import { mapGetters } from 'vuex';

export default {
  data() {
    return {
      rules: [(value) => !!value || '입력해주세요!'],
      postCreated: '',
      isEditMode: false,
      isAlertShow: false,
      editPost: {},
      selectSeriesList: [],
      selectValue: this.post.seriesId,
      isShowComment: false,
      commentList: [],
    };
  },
  props: {
    post: Object,
    seriesList: Array,
  },
  components: {
    Alert,
    Comment,
    CommentWrite,
  },
  computed: {
    ...mapGetters(['seriesId', 'commentsDic']),
  },
  created() {
    // console.log('시리즈아이디', this.post.seriesId);
    // console.log('포스트', this.post);
    let date = new Date(this.post.postCreatedAt);
    this.postCreated =
      date.getFullYear() + '년 ' + (date.getMonth() + 1) + '월 ' + date.getDate() + '일';
    this.setSeriesSelectList();
  },
  watch: {
    commentsDic: function () {
      let currentPostId = this.post.postId;
      this.commentList = this.commentsDic[currentPostId];
    },
    // 비동기 처리 늦게 됐을 때
    seriesList: function () {
      this.setSeriesSelectList();
    },
    // 보드리스트가 늦게 불러지거나 글이 삭제되어 리스트가 변경됐을 때 초기호
    post: function () {
      this.isEditMode = false;
      this.selectValue = this.post.seriesId;
    },
  },
  methods: {
    clickCommentBtn(mode) {
      let currentPostId = this.post.postId;
      //댓글 리스트 열기
      if (mode == 'open') {
        //현재 페이지의 댓글 리스트 얻기
        let payload = { postId: currentPostId, page: 1, size: 1000 };
        this.$store.dispatch('getCommentsDic', payload);
        this.isShowComment = true;
      }
      // 댓글 리스트 닫기
      if (mode == 'close') {
        this.$store.dispatch('deleteCommentsDic', currentPostId);
        this.isShowComment = false;
      }
    },
    setSeriesSelectList() {
      this.selectSeriesList = this.seriesList.map((series) => {
        return { text: series.name, value: series.id };
      });
      this.selectSeriesList.push({ text: '미분류', value: -1 });
    },
    deleteAlertOk() {
      return this.changeMode('delete_alert_ok');
    },
    deleteAlertNo() {
      return this.changeMode('delete_alert_no');
    },
    cancleAlertOk() {
      return this.changeMode('cancle_alert_ok');
    },
    cancleAlertNo() {
      return this.changeMode('cancle_alert_no');
    },
    changeMode(mode) {
      console.log(mode);
      // 글 수정 모드
      if (mode == 'edit') {
        this.isEditMode = true;
        let seriesId = this.post.seriedId;
        this.selectValue = seriesId;
        this.editPost.postTitle = this.post.postTitle;
        this.editPost.postContent = this.post.postContent;
        console.log(this.selectValue);
      }
      // 글 작성 중 취소버튼
      else if (mode == 'try_cancle') {
        this.isAlertShow = true;
      }
      // 정말로 취소하시겠습니까? -> 예
      else if (mode == 'cancle_alert_ok') {
        this.isAlertShow = false;
        this.isEditMode = false;
      }
      // 정말로 취소하시겠습니까? -> 아니요
      else if (mode == 'cancle_alert_no') {
        this.isAlertShow = false;
      }
      // 글 조회 모든
      else if (mode == 'view') {
        this.isEditMode = true;
      }
      // 글 삭제 버튼
      else if (mode == 'try_delete') {
        this.isAlertShow = true;
      }
      // 정말로 삭제하시겠습니까? -> 예
      else if (mode == 'delete_alert_ok') {
        this.isAlertShow = false;
        this.isEditMode = false;
        deletePost(this.post.postId).then(() => this.refreshBoard());
      }
      // 정말로 삭제하시겠습니까? -> 아니요
      else if (mode == 'delete_alert_no') {
        this.isAlertShow = false;
      }
      // 글 수정 제출
      else if (mode == 'submit') {
        let newPost = {
          seriesId: this.selectValue,
          title: this.editPost.postTitle,
          content: this.editPost.postContent,
        };
        console.log(newPost, 'newPost');
        if (newPost.title.length == 0 || !newPost.title) return;
        if (newPost.content.length == 0 || !newPost.content) return;
        this.isEditMode = false;
        updatePost(newPost, this.post.postId).then(() => this.refreshBoard());
      }
    },
    refreshBoard() {
      //글 생성했으니 보드 다시 얻어오기
      let currentSeriesId = this.$store.state.board.seriesId;
      let hasSeries = null;
      if (!currentSeriesId) hasSeries = false;
      let payload = {
        seriesId: currentSeriesId,
        hasSeries: hasSeries,
        search: null,
        page: 1,
        size: 1000,
      };
      this.$store.dispatch('getBoard', payload);
    },
    enterToBr(str) {
      if (str) return str.replace(/(?:\r\n|\r|\n)/g, '<br/>');
    },
  },
};
</script>

<style scoped>
.top {
  font-size: 1.25rem;
}
.content {
  min-height: 300px;
}
.bottom {
  position: relative;
}
.bottom button {
  font-size: 0.95rem;
}
.bottom > .row {
  top: -30px;
  position: absolute;
  width: 100%;
}
.muted {
  color: gray;
  /* font-weight: bold; */
  font-size: 0.92rem;
}
.view {
  padding: 20px;
  /* background-color: rgb(250, 242, 215); */
  /* border-radius: 10px; */
}
.edit {
  padding: 20px;
  border-radius: 20px;
  background-color: rgba(238, 238, 238, 0.329);
  /* background-color: rgb(250, 242, 215); */
  /* border-radius: 10px; */
}
.commentArea {
  margin-top: 50px;
  width: 100%;
  padding: 3px;
  border-radius: 5px;
  background-color: rgb(238, 238, 238);
}
</style>
