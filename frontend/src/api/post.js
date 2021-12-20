import axios from '@/utils/axios';

// 게시글 리스트 조회 API
async function getPostList(condition) {
  var url = `/posts?page=${condition.page}&size=${condition.size}`;
  if (condition.seriesId) url += `&seriesId=${condition.seriesId}`;
  if (condition.hasSeries) url += `&hasSeries=${condition.hasSeries}`;
  if (condition.search) url += `&search=${condition.search}`;
  try {
    const { data } = await axios.get(url);
    return data.result;
  } catch (error) {
    console.error(error);
  }
}
// 게시글 작성 API
async function createPost(post) {
  console.log('createPost API', post);
  try {
    return axios.post('/posts', post);
  } catch (error) {
    console.error(error.response.data.message);
  }
}
// 게시글 수정 API
async function updatePost(post, postId) {
  console.log('updatePost API', post, postId);
  try {
    return axios.patch('/posts/' + postId, post);
  } catch (error) {
    console.error(error.response.data.message);
  }
}
// 게시글 삭제 API
async function deletePost(postId) {
  console.log('deletePost API', postId);
  try {
    return axios.delete('/posts/' + postId);
  } catch (error) {
    console.log(error);
    console.error(error.response.data.message);
  }
}
export { getPostList, createPost, updatePost, deletePost };
