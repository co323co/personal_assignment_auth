<template lang="">
  <div>
    <div>시리즈 등록</div>
    <v-row class="pa-3 mt-1 mb-3" style="width: 600px">
      <v-col style="align-self: center">
        <v-text-field v-model="createName" dense solo hide-details></v-text-field>
      </v-col>
      <v-col class="text-right" style="align-self: center">
        <v-btn @click="create" fab x-small color="brown lighten-3">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </v-col>
    </v-row>
    <div>시리즈 편집</div>
    <v-container
      id="scroll-target"
      style="height: 400px; width: 600px"
      class="overflow-y-auto ma-0"
    >
      <div v-scroll:#scroll-target="onScroll" align="center" justify="center">
        <div v-for="(item, index) in seriesList" :key="index">
          <div v-if="!isEditModeList[index]">
            <v-row class="pa-2">
              <v-col class="text-left" style="align-self: center">
                <h4>{{ item.name }}</h4>
              </v-col>
              <v-col class="text-right" style="align-self: center">
                <v-btn @click="changeMode(index)" fab x-small color="brown lighten-3">
                  <v-icon>mdi-pencil</v-icon>
                </v-btn>
                <v-btn @click="remove(item.id)" class="ml-3" fab x-small color="red lighten-3">
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </v-col>
            </v-row>
          </div>
          <div v-else>
            <v-row class="pa-2">
              <v-col style="align-self: center">
                <v-text-field v-model="editNameList[index]" dense solo hide-details></v-text-field>
              </v-col>
              <v-col class="text-right" style="align-self: center">
                <v-btn @click="edit(item.id, index)" fab x-small color="brown lighten-3">
                  <v-icon>mdi-check</v-icon>
                </v-btn>
                <v-btn @click="changeMode(index)" class="ml-3" fab x-small color="red lighten-3">
                  <v-icon>mdi-close</v-icon>
                </v-btn>
              </v-col>
            </v-row>
          </div>
          <v-divider />
        </div>
        <div></div>
      </div>
    </v-container>
  </div>
</template>
<script>
import { getSeriesList, createSeries, updateSeriesName, deleteSeries } from '@/api/series';
import { mapGetters } from 'vuex';

export default {
  data() {
    return {
      isEditModeList: [],
      editNameList: [],
      createName: '',
    };
  },
  created() {
    this.reset();
  },
  computed: {
    ...mapGetters(['seriesList']),
  },
  watch: {
    seriesList: function () {
      for (let series of this.seriesList) {
        this.isEditModeList.push(false);
        this.editNameList.push(series.name);
      }
    },
  },
  methods: {
    create() {
      if (this.createName == null || this.createName.length == 0) return;
      createSeries(this.createName).then(() => {
        this.reset();
        this.createName = '';
      });
    },
    reset() {
      this.isEditModeList = [];
      this.editNameList = [];
      this.$store.dispatch('getSeries');
    },
    getIsEditMode(index) {
      return this.isEditModeList[index];
    },
    changeMode(index) {
      this.isEditModeList[index] = !this.isEditModeList[index];
      this.isEditModeList = [...this.isEditModeList];
      console.log(this.isEditModeList[index]);
    },
    edit(id, index) {
      // console.log(this.editNameList);
      updateSeriesName(this.editNameList[index], id).then(() => {
        this.reset();
        console.log('수정완료');
      });
    },
    remove(id) {
      deleteSeries(id).then(() => {
        this.$store.dispatch('getSeries');
      });
    },
    onScroll(e) {
      this.offsetTop = e.target.scrollTop;
    },
  },
};
</script>
<style scoped>
.box {
  border-width: 1px;
  border-color: black;
}
</style>
