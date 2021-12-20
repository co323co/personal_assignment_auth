import axios from '@/utils/axios';

// 유저 리스트 조회 API
async function getUserList(condition) {
  var url = `/admin/users?page=${condition.page}&size=${condition.size}`;
  if (condition.userId) url += `&postId=${condition.userId}`;
  if (condition.email) url += `&email=${condition.email}`;
  if (condition.nickname) url += `&nickname=${condition.nickname}`;
  if (condition.grade) url += `&grade=${condition.grade}`;
  try {
    const { data } = await axios.get(url);
    return data.result;
  } catch (error) {
    if (error.response) console.error('msg', error.response.data.message);
    else console.log(error);
    throw error;
  }
}

// 유저 리스트 삭제 API
async function deleteUserList(userIdList) {
  console.log('deleteUserList API', userIdList);
  try {
    return axios.delete(`/admin/users`, {
      data: userIdList,
    });
  } catch (error) {
    let code = error.response.data.code;
    if (error.response) console.error('msg', error.response.data.message);
    else console.error('error', error);
    throw error;
  }
}
export { getUserList, deleteUserList };
