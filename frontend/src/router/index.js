import Vue from 'vue';
import Router from 'vue-router';
import store from '@/store';

Vue.use(Router);

const router = new Router({
  routes: [
    {
      path: '/',
      redirect: '/main',
    },
    {
      path: '/main',
      name: 'main',
      component: () => import('@/pages/Main.vue'),
      meta: { authRequired: true },
    },
    {
      path: '/sign-in',
      name: 'sign-in',
      component: () => import('@/pages/SignIn.vue'),
    },
    {
      path: '/sign-up',
      name: 'sign-up',
      component: () => import('@/pages/SignUp.vue'),
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('@/pages/Admin.vue'),
      meta: { authRequired: true },
      beforeEnter: (to, from, next) => {
        if (store.getters.userInfo.userGrade != 'admin') {
          alert('관리자 계정이 아닙니다!');
          next('sign-in');
        } else next();
      },
    },
    {
      path: '/:catchAll(.*)*',
      component: () => import('@/pages/Error404.vue'),
    },
  ],

  // #을 제거하기 위해 history 를 모드로 추가한다.
  mode: 'history',
});

export default router;
router.beforeEach((to, from, next) => {
  // 로그인이 필요한 경우
  if (
    to.matched.some(function (routeInfo) {
      return routeInfo.meta.authRequired;
    })
  ) {
    console.log('로그인 필요 페이지');
    store
      .dispatch('verifyAuth')
      .then(() => {
        console.log('인증 성공');
        next();
      })
      .catch((error) => {
        console.log('인증 실패', error);
        alert('Login Please!');
        next('sign-in');
      });
  }
  // 로그인이 필요하지 않은 경우
  else {
    console.log("routing success : '" + to.path + "'");
    next();
  }
});
