import axios from '@/utils/axios';

// 댓글 조회 API
async function getComment(commentId) {
  var url = `/comments/${commentId}`;
  try {
    const { data } = await axios.get(url);
    return data.result;
  } catch (error) {
    console.error(error.response.data.message);
  }
}

// 댓글 리스트 조회 API
async function getCommentList(condition) {
  var url = `/comments?page=${condition.page}&size=${condition.size}`;
  if (condition.postId) url += `&postId=${condition.postId}`;
  try {
    const { data } = await axios.get(url);
    return data.result;
  } catch (error) {
    console.error(error.response.data.message);
  }
}
// 댓글 작성 API
async function createComment(comment) {
  console.log('createComment API', comment);
  try {
    return axios.post('/comments', comment);
  } catch (error) {
    console.error(error.response.data.message);
  }
}
// 댓글 수정 API
async function updateComment(comment, commentId) {
  console.log('updateComment API', comment, commentId);
  try {
    return axios.patch(`/comments/${commentId}`, comment);
  } catch (error) {
    console.error(error.response.data.message);
  }
}
// 댓글 삭제 API
async function deleteComment(commentId) {
  console.log('deleteComment API', commentId);
  try {
    return axios.delete(`/comments/${commentId}`);
  } catch (error) {
    console.log(error);
    console.error(error.response.data.message);
  }
}
export { getComment, getCommentList, createComment, updateComment, deleteComment };
