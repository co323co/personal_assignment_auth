import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import auth from './auth.module';
import board from './board.module';
import tab from './tab.module';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    auth,
    board,
    tab,
  },
  plugins: [createPersistedState()],
});
