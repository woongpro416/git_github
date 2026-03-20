import api from "@/api/axios.js"

//상품별 리뷰 목록
export const getReviewsByItem = (itemId) => {
    return api.get(`/reviews/item/${itemId}`).catch(e => e.response)
}

//회원별 리뷰 목록
export const getReviewsByMember = (memberId) => {
    return api.get(`/reviews/member/${memberId}`).catch(e => e.response)
}

//전체 리뷰 목록(관리자)
export const getAllReviews = () => {
    return api.get('/reviews/list').catch(e => e.response)
}

//리뷰 등록
export const saveReview = (data) => {
    return api.post('/reviews/new', data).catch(e => e.response)
}
//리뷰 수정
export const updateReview = (reviewId, data) => {
    return api.put(`/reviews/edit/${reviewId}`, data).catch(e => e.response)
}

//리뷰 삭제
export const deleteReview = (reviewId) => {
    return api.delete(`/reviews/delete/${reviewId}`).catch(e => e.response)
}

