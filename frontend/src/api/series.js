import axios from '@/utils/axios';

// 모든 시리즈 목록 조회 API
async function getSeriesList() {
  try {
    const { data } = await axios.get('/series');
    return data.result;
  } catch (error) {
    console.error(error);
  }
}

// 시리즈 이름 조회
async function getSeriesName(seriesId) {
  try {
    const { data } = await axios.get(`/series/${seriesId}`);
    return data.result;
  } catch (error) {
    console.error(error);
  }
}

// 시리즈 추가
async function createSeries(seriesName) {
  try {
    const { data } = await axios.post(`/series/`, { name: seriesName });
    return data.result;
  } catch (error) {
    console.error(error);
  }
}

// 시리즈 이름 수정
async function updateSeriesName(seriesName, seriesId) {
  try {
    const { data } = await axios.patch(`/series/${seriesId}`, { name: seriesName });
    return data.result;
  } catch (error) {
    console.error(error);
  }
}

//시리즈 삭제
async function deleteSeries(seriesId) {
  console.log('deleteSeries API', seriesId);
  try {
    return axios.delete('/series/' + seriesId);
  } catch (error) {
    console.log(error);
    console.error(error.response.data.message);
  }
}

export { getSeriesList, getSeriesName, createSeries, updateSeriesName, deleteSeries };
