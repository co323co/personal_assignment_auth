import router from '@/router';

export default {
  // 현재 상태들
  state: {
    //현재 보드에 있는 게시글들
    routerName: '',
  },
  getters: {
    routerName(state) {
      return state.routerName;
    },
  },
  mutations: {
    setRouterName(state, payload) {
      state.routerName = payload;
    },
  },
  actions: {
    //현재 라우터 위치 가져오기
    //탭 메뉴들을 바꾸기 위해 알려줌
    getRouterName(context, payload) {
      // let routerName = router.currentRoute.name;
      context.commit('setRouterName', payload);
      console.log('라우터 이름', payload);
    },
  },
};
