import VueAxios from 'axios';
import store from '@/store';
import router from '@/router';

const axios = VueAxios.create({
  baseURL: process.env.VUE_APP_API_URL + '/api',
  headers: {
    'Content-type': 'application/json',
  },
});

// axios.interceptors.request.use(async function (config) {
//   config.headers['X-ACCESS-TOKEN'] = 'hi';
// });

axios.interceptors.request.use(
  function (config) {
    // 요청을 보내기 전에 수행할 일
    config.headers['X-ACCESS-TOKEN'] = store.getters.accessToken;
    config.headers['X-REFRESH-TOKEN'] = store.getters.refreshToken;
    console.log('store.getters.accessToken', store.getters.accessToken);
    // ...
    return config;
  },
  function (error) {
    // 오류 요청을 보내기전 수행할 일
    // ...
    return Promise.reject(error);
  }
);
// 응답 인터셉터 추가
axios.interceptors.response.use(
  function (response) {
    // 응답 데이터를 가공
    // ...
    return response;
  },
  function (error) {
    // 오류 응답을 처리
    // ...
    const rq = error.response.config; //요청했던 request 정보

    //유효하지 않은 ACCESS 토큰입니다
    if (error.response && error.response.data.code == 402) {
      console.log(error.response.data.message);
      store.dispatch('verifyAuth').catch(() => {
        alert('로그인이 만료되었습니다');
        router.push('/sign-in');
      });
      return axios(rq);
    }
    return Promise.reject(error);
  }
);
export default axios;

// if (error.response && error.response.data.code == 402) {
//   store.dispatch('verifyAuth').then(()=>{}).catch(() => {
//     alert('로그인이 만료되었습니다');
//     router.push('/sign-in');
//   });
// }
