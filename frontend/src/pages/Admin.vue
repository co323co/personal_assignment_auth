<template lang="">
  <div class="mx-16 px-16 py-10 back" style="margin-top: 100px; text-align: center">
    <div class="mb-5">
      <h3>관리자 페이지</h3>
    </div>
    <v-data-table
      v-if="!isDelteUserListMode"
      :headers="headers"
      :header-props="headerProps"
      :items="userList"
      :items-per-page="5"
      :search="search"
      item-key="userId"
      class="elevation-1"
      no-data-text="게시글이 없습니다"
      no-results-text="검색 결과가 없습니다"
      :options="{
        itemsPerPage: 5,
      }"
      :footer-props="{
        itemsPerPageText: '쪽수',
      }"
    >
      <template v-slot:footer.prepend>
            <div class="mt-1">
              <v-btn @click="isDelteUserListMode = true" text color="red lighten-1">
                <v-icon small>mdi-close</v-icon>
                회원탈퇴</v-btn
              >
            </div>
          </template>
    </v-data-table>

      <v-data-table
          v-else
          show-select
          v-model="selectedRows"
          :headers="headers"
          :header-props="headerProps"
          :items="userList"
          item-key="userId"
          :search="search"
          no-data-text="게시글이 없습니다"
          no-results-text="검색 결과가 없습니다"
          :options="{
            itemsPerPage: 5,
          }"
          :footer-props="{
            itemsPerPageText: '쪽수',
          }"
        >
          <template v-slot:footer.prepend>
            <div class="mt-3 mb-2">
              <v-btn @click="deleteUserList" text color="green lighten-1">
                <v-icon small>mdi-check</v-icon>
                확인</v-btn
              >
              <v-btn @click="isDelteUserListMode = false" text color="grey lighten-1">
                <v-icon small>mdi-close</v-icon>
                취소</v-btn
              >
            </div>
          </template>
        </v-data-table>
  </div>
</template>
<script>
import { mapGetters } from 'vuex';
import { getUserList, deleteUserList } from '@/api/admin';

export default {
  components: {},
  data() {
    return {
      userList: [],
      headers: [
        { text: '이메일', value: 'userEmail' },
        { text: '닉네임', value: 'userNickname' },
        { text: '등급', value: 'userGrade' },
        { text: '가입일', value: 'userCreatedAt' },
      ],
      headerProps: {
        sortByText: '정렬',
      },
      search: null,
      isDetailMode: false,
      isDelteUserListMode: false,
      selectedRows: [],
    };
  },
  created() {
    this.refreshUserList();

  },
  computed: {
    ...mapGetters([ 'userInfo']),
  },
  methods: {
     refreshUserList(){
     let condition = {
      page: 1,
      size: 1000,
      };
      getUserList(condition).then((res) => {
        console.log(res);
        for (let user of res) {
          user.userCreatedAt = this.dateFilter(user.userCreatedAt);
        }
        this.userList = res;
      });
    },
    deleteUserList() {
      console.log(this.selectedRows);
      let userIdList = [];
      for (let row of this.selectedRows) {
        userIdList.push({"userId" : row.userId});
      }
      deleteUserList(userIdList)
        .then(() => {
          this.refreshUserList();
          this.isDeltePostListMode = false;
          this.selectedRows = [];
        }).catch((error)=>{console.error(error);});
    },
    dateFilter(value) {
      let date = new Date(value);
      return (
        date.getFullYear() +
        '년 ' +
        (date.getMonth() + 1) +
        '월 ' +
        date.getDate() +
        '일 ' +
        date.getHours() +
        '시 ' +
        date.getMinutes() +
        '분'
      );
    },
  },
};
</script>
<style scoped>
.font {
  font-family: IBMPlexSansKR !important;
}

.back {
  background-color: rgb(247, 243, 243);
  border-radius: 20px;
}
</style>
