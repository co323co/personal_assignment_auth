<template>
  <!-- 게시글 -->
  <div class="write">
    <v-row class="px-1">
      <v-col cols="2">
        <v-select
          class="ml-2"
          :items="selectSeriesList"
          chips
          label="시리즈"
          v-model="selectValue"
          dense
          hide-details="auto"
        ></v-select>
      </v-col>
    </v-row>
    <!-- 게시글 타이틀 바 -->
    <v-row class="top pa-1">
      <v-col>
        <v-text-field
          style="font-size: 1.6rem; font-weight: bold"
          background-color="white"
          class="mt-3 px-1"
          hide-details="auto"
          label="제목"
          filled
          dense
          :rules="rules"
          v-model="editPost.postTitle"
        ></v-text-field>
      </v-col>
    </v-row>
    <!-- 게시글 내용 -->
    <v-row class="content px-1 py-5 ma-1">
      <v-textarea
        label="내용"
        v-model="editPost.postContent"
        style="font-size: 1.1rem"
        background-color="white"
        filled
        :rules="rules"
        rows="15"
      ></v-textarea>
    </v-row>
    <!-- 게시글 하단 바 -->
    <div class="bottom">
      <v-row v-if="!isAlertShow">
        <!-- 우측 -->
        <v-col class="text-right">
          <!-- 작성 확인 버튼 -->
          <v-btn @click="changeMode('submit')" class="mx-1" plain small fab color="green darken-3">
            <v-icon>mdi-check</v-icon>
          </v-btn>
        </v-col>
      </v-row>

      <!-- <transition name="slide-fade"> -->
      <v-row v-else style="top: -80px; z-index: 3">
        <Alert
          msg="작성하시던 내용이 사라질 수 있습니다. 취소하시겠습니까?"
          :ok="cancleAlertOk"
          :no="cancleAlertNo"
        />
      </v-row>
      <!-- </transition> -->
    </div>
  </div>
</template>

<script>
import Alert from '@/components/common/WarningAlert';
import { createPost, updatePost, deletePost } from '@/api/post';
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
      selectValue: null,
    };
  },
  props: {
    seriesList: Array,
  },
  components: {
    Alert,
  },
  created() {
    this.setSeriesSelectList();
    console.log('시리즈리스트, 와치', this.seriesList);
  },
  computed: {
    ...mapGetters(['seriesId']),
  },
  watch: {
    // 비동기 처리 늦게 됐을 때
    seriesList: function () {
      this.setSeriesSelectList();
    },
    // 글이 삭제되어 리스트가 변경됐을 때 초기호
    post: function () {
      this.isEditMode = false;
    },
  },
  methods: {
    setSeriesSelectList() {
      this.selectSeriesList = this.seriesList.map((series) => {
        return { text: series.name, value: series.id };
      });
      this.selectSeriesList.push({ text: '미분류', value: null });
      this.selectValue = this.seriesId;
    },
    async submitPost(seriesId, title, content) {
      try {
        await createPost(seriesId, title, content);
      } catch (error) {
        console.error(error.response.data.message);
        console.error(error);
      }
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
      // 글 수정 제출
      if (mode == 'submit') {
        this.isEditMode = false;
        let newPost = {
          seriesId: this.selectValue,
          title: this.editPost.postTitle,
          content: this.editPost.postContent,
        };
        if (newPost.title.length == 0 || !newPost.title) return;
        if (newPost.content.length == 0 || !newPost.content) return;
        createPost(newPost).then(() => this.refreshBoard());
        this.$router.go();
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
      this.$store.ch('getBoard', payload);
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
  font-size: 1.1rem;
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

.write {
  padding-top: 40px;
  padding-bottom: 30px;
  padding-right: 20px;
  padding-left: 20px;
  margin-top: 30px;
  background-color: rgba(219, 219, 219, 0.336);
  border-radius: 20px;
}
</style>
