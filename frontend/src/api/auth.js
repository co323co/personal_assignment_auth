import axios from '@/utils/axios';

// 회원가입 API
async function signUp(userData) {
  console.log('signUp API', userData);
  try {
    return axios.post('/users/signup', userData);
  } catch (error) {
    if (error.response) console.error(error.response.data.message);
    else console.error(error);
    throw error;
  }
}

// 로그인 API
async function signIn(userData) {
  try {
    return await axios.post('/users/signin', userData);
  } catch (error) {
    throw error;
  }
}

// 이메일 인증번호전송 API
function sendEmail(userData) {
  console.log('send email api');
  try {
    return axios.post('/users/email', userData);
  } catch (error) {
    throw error;
  }
}

// 프로필 조회 API
async function getProfile() {
  console.log('get user Profile API');
  try {
    return await axios.get('/users/me');
  } catch (error) {
    // console.error('user.js', error.response.data.message);
    throw error;
  }
}
// ACCESS TOKEN 재발행 API
async function reissueAccessToken() {
  console.log('reissue Access Token API');
  // console.log('refreshToken', refreshToken);
  try {
    return await axios.get('/users/reissue');
  } catch (error) {
    console.error('auth.js', error.response.data.message);
    throw error;
  }
}
// 회원탈퇴 API
function deleteUser(userData) {
  return axios.delete('/users', userData);
}

// 회원정보 수정 API
function editUser(userData) {
  return axios.patch('/users', userData);
}

export { signUp, signIn, sendEmail, getProfile, deleteUser, editUser, reissueAccessToken };
