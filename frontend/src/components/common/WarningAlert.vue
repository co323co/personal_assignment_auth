<template lang="">
  <v-alert
    style="width: 100%"
    class="mx-5"
    shaped
    dark
    outlined
    type="warning"
    prominent
    elevation="2"
  >
    <v-row>
      <v-col style="align-self: center">
        {{ msg }}
      </v-col>
      <v-col class="text-right" style="display: inline-block">
        <v-btn color="red darken-1" class="ma-1" fab text @click="ok">네</v-btn>
        <v-btn color="green darken-1" class="ma-1" fab text @click="no">아니요</v-btn>
      </v-col>
    </v-row>
  </v-alert>
</template>
<script>
export default {
  props: {
    ok: Function,
    no: Function,
    msg: String,
  },
};
</script>
<style scoped>
.v-alert {
  background-color: white !important;
}
</style>
