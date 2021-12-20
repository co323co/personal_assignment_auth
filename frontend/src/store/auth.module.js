// import axios from '@/utils/axios.js';
import {} from '@/utils/cookies';
import { signIn, getProfile, reissueAccessToken } from '@/api/auth';
const jwt = module.require('jsonwebtoken');
export default {
  state: {
    accessToken: '',
    refreshToken: '',
    userInfo: '',
  },
  getters: {
    isLogin(state) {
      return state.userInfo != '';
    },
    userInfo(state) {
      return state.userInfo;
    },
    accessToken(state) {
      return state.accessToken;
    },
    refreshToken(state) {
      return state.refreshToken;
    },
  },
  mutations: {
    setAccessToken(state, accessToken) {
      state.accessToken = accessToken;
    },
    setRefreshToken(state, refreshToken) {
      state.refreshToken = refreshToken;
    },
    setUserInfo(state, userInfo) {
      state.userInfo = userInfo;
    },

    clearAccessToken(state) {
      state.accessToken = '';
    },
    clearRefreshToken(state) {
      state.refreshToken = '';
    },
    clearUserInfo(state) {
      state.userInfo = '';
    },
  },
  actions: {
    verifyAuth: ({ state, commit }) => {
      return new Promise((resolve, reject) => {
        let accessToken = state.accessToken;
        let refreshToken = state.refreshToken;
        if (accessToken == '') accessToken = null;
        if (refreshToken == '') refreshToken = null;
        console.log('accessToken', accessToken);
        // 모든 토큰이 없는 경우
        if (!accessToken && !refreshToken) reject('모든 TOKEN이 없음');
        // ACCESS TOEKN만 있는 경우
        if (accessToken) {
          console.log('ACCESS 토큰 존재');
          try {
            const { userId, exp } = jwt.decode(accessToken);
            // 토큰이 변형됨
            if (state.userInfo.userId != userId) {
              console.log('토큰 정보가 일치하지 않음');
              console.log(state.userInfo.userId + ',' + userId);
              commit('clearAccessToken');
              commit('clearRefreshToken');
              commit('clearUserInfo');
              reject('토큰 정보가 일치하지 않음');
            }
            // ACCESS TOKEN 만료
            if (Date.now() >= exp * 1000) {
              console.log('ACCESS TOKEN 만료');
              let rToken = jwt.decode(refreshToken);
              // REFRESH TOKEN도 만료되었을 경우
              if (Date.now() >= rToken.exp * 1000) {
                commit('clearAccessToken');
                commit('clearRefreshToken');
                commit('clearUserInfo');
                reject('REFRESH TOKEN 만료');
              }
              // REFRESH TOKEN은 유효함 -> ACCESS TOKEN 재발급
              else {
                reissueAccessToken()
                  .then(({ data }) => {
                    console.log(data);
                    console.log('ACCESS TOKEN 재발급 완료');
                    commit('setAccessToken', data.result.accessToken);
                    resolve();
                  })
                  .catch((error) => {
                    console.error(error);
                    commit('clearAccessToken');
                    commit('clearRefreshToken');
                    commit('clearUserInfo');
                    reject(error);
                  });
              }
            }
            //ACCESS TOKEN 기간이 유효함
            else {
              resolve();
            }
          } catch (error) {
            commit('clearAccessToken');
            commit('clearRefreshToken');
            commit('clearUserInfo');
            reject('토큰이 유효하지 않음');
          }
        }
        // REFRESH TOKEN만 있는 경우
        else if (refreshToken) {
          console.log('REFRESH TOKEN만 존재');
          reissueAccessToken(refreshToken)
            .then(({ data }) => {
              console.log('ACCESS TOKEN 재발급 완료');
              commit('setAccessToken', data.result.accessToken);
              resolve();
            })
            .catch((error) => {
              console.error(error);
              reject(error);
            });
        }
      });
    },
    LOGIN: ({ commit }, payload) => {
      return new Promise((resolve, reject) => {
        signIn(payload)
          .then(({ data }) => {
            console.log(data);
            // 토큰 셋팅
            let accessToken = data.result.accessToken;
            let refreshToken = data.result.refreshToken;
            commit('setAccessToken', accessToken);
            commit('setRefreshToken', refreshToken);
            // 로그인 유저 셋팅
            getProfile()
              .then(({ data }) => {
                console.log(data.result);
                commit('setUserInfo', data.result);
                resolve();
              })
              .catch((error) => {
                reject(error);
              });
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    LOGOUT({ commit }) {
      console.log('로그아웃');
      commit('clearAccessToken');
      commit('clearRefreshToken');
      commit('clearUserInfo');
    },
  },
};
