<template>
  <div>
    <v-dialog v-model="dialog" fullscreen hide-overlay transition="dialog-bottom-transition">
      <template v-slot:activator="{ on, attrs }">
        <v-btn text v-bind="attrs" v-on="on">
          <span class="mr-2" style="font-family: IBMPlexSansKR !important">관리자 페이지</span>
          <v-icon>mdi-open-in-new</v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-toolbar dark dense color="brown lighten-1">
          <v-btn icon dark @click="dialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>관리자 페이지</v-toolbar-title>
          <v-spacer></v-spacer>
        </v-toolbar>
        <v-list three-line subheader>
          <v-subheader class="mt-5">시리즈</v-subheader>
          <v-list-item>
            <v-list-item-content>
              <div style="width: 100px">
                <SeriresManagement />
              </div>
              <v-list-item-subtitle>블로그 사용자 설정 및 관리 페이지</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-list>
        <v-divider></v-divider>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
import SeriresManagement from '@/components/setting/SeriesManagement';
export default {
  components: {
    SeriresManagement,
  },
  data() {
    return {
      dialog: false,
      notifications: false,
      sound: true,
      widgets: false,
    };
  },
};
</script>
<style lang=""></style>
