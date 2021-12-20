<template lang="">
  <div>
    <v-hover v-slot="{ hover }">
      <v-card
        :elevation="hover ? 16 : 2"
        :class="{ 'on-hover': hover }"
        shaped
        class="mx-auto text-center"
        justify="center"
        align="center"
      >
        <v-card-title
          class="mt-2 pt-5"
          style="
            min-width: 200px;
            font-family: BMYEONSUNG !important;
            font-size: 1.6rem;
            font-weight: bord;
            place-content: center;
          "
        >
          {{ content.title }}
        </v-card-title>
        <v-card-subtitle class="pa-5">
          <div
            style="
              font-family: IBMPlexSansKR !important;
              color: black;
              margin-top: auto;
              margin-bottom: auto;
            "
            v-html="content.content"
          ></div>
        </v-card-subtitle>
      </v-card>
    </v-hover>
  </div>
</template>
<script>
export default {
  props: {
    content: Object,
  },
  data() {
    return {};
  },
};
</script>
<style></style>
