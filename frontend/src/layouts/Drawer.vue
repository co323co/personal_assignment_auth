<template lang="">
  <div>
    <v-card>
      <v-navigation-drawer app permanent expand-on-hover>
        <v-list>
          <v-list-item class="px-2 pt-1">
            <v-list-item-avatar>
              <v-img src="@/assets/img/me.png"></v-img>
            </v-list-item-avatar>
          </v-list-item>

          <v-list-item link>
            <v-list-item-content>
              <v-list-item-title> <h2>김민정</h2></v-list-item-title>
              <v-list-item-subtitle>
                <div style="font-size: 0.9rem">co323co@gmail.com</div>
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-list>

        <v-divider></v-divider>

        <v-list nav dense>
          <v-list-item link @click="move('main')">
            <v-list-item-icon>
              <v-icon>mdi-home</v-icon>
            </v-list-item-icon>
            <v-list-item-title>
              <div style="font-size: 1rem">홈</div>
            </v-list-item-title>
          </v-list-item>
          <v-list-item link @click="move('board')">
            <v-list-item-icon>
              <v-icon>mdi-bookshelf</v-icon>
            </v-list-item-icon>
            <v-list-item-title>
              <div style="font-size: 1rem">시리즈</div>
            </v-list-item-title>
          </v-list-item>
        </v-list>

        <v-divider></v-divider>

        <v-list nav dense>
          <v-list-item link @click="open('github')">
            <v-list-item-icon>
              <v-icon>mdi-github</v-icon>
            </v-list-item-icon>
            <v-list-item-title>
              <div style="font-size: 1rem">Github</div>
            </v-list-item-title>
          </v-list-item>
          <v-list-item link @click="open('notion')">
            <v-list-item-icon>
              <v-icon>mdi-notebook</v-icon>
            </v-list-item-icon>
            <v-list-item-title>
              <div style="font-size: 1rem">Notion</div>
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-navigation-drawer>
    </v-card>
  </div>
</template>
<script>
export default {
  methods: {
    move(path) {
      //탭 메뉴들을 바꾸기 위해 알려줌
      if (path == 'main') this.$router.push({ path: `/main` }).catch(() => {});
      else if (path == 'board') this.$router.push({ path: `/board` }).catch(() => {});
    },
    open(to) {
      if (to == 'github') {
        window.open('https://github.com/co323co');
      } else if (to == 'notion') {
        window.open('https://cocoj.notion.site/MinJeong-Kim-39dea25333424982afec7d9ddf091f4c');
      }
    },
  },
};
</script>
<style lang=""></style>
