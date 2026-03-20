package com.example.Logitech.service;

import com.example.Logitech.domain.Qna;
import com.example.Logitech.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaService {

    public final QnaRepository qnaRepository;

    //질문 및 답변 목록
    public List<Qna> getQnaList() {
        return qnaRepository.findAllGrouped();
    }

    //제목으로 질문 검색
    public List<Qna> findByTitle(String keyword) {
        return qnaRepository.findByTitle(keyword);
    }

    //질문/답변 상세보기
    @Transactional
    public List<Qna> detail(Long id) {
        Qna question = qnaRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException ("존재하지 않는 글입니다."));
        question.setViewCount(question.getViewCount()+1);
        qnaRepository.save(question);
        return qnaRepository.findByParentIdOrderByLevelAscCreatedAtAsc(id);
    }

    //질문 등록
    @Transactional
    public Qna addQuestion(Qna qna) {
        qna.setLevel(1);
        Qna q = qnaRepository.save(qna);
        q.setParentId(q.getId());
        return qnaRepository.save(q);
    }

    //답변 등록 ( 관리자 )
    @Transactional
    public Qna addAnswer(Long parentId, Qna qna) {
        qna.setLevel(2);
        qna.setParentId(parentId);
        return qnaRepository.save(qna);
    }

    @Transactional
    //질문,답변 수정
    public Qna updateQna(Long id, Qna qna, String loginUser, boolean isAdmin) {
        Qna origin = qnaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        if (!origin.getWriter().equals(loginUser) && !isAdmin) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }
        origin.setTitle(qna.getTitle());
        origin.setContent(qna.getContent());
        return qnaRepository.save(origin);
    }

    //질문,답변 삭제 + 질문 삭제시 답변도 같이 삭제
    @Transactional
    public void delete(Long id, String loginUser, boolean isAdmin) {
        Qna origin = qnaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        if (!origin.getWriter().equals(loginUser) && !isAdmin) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        if(origin.getLevel() == 1) {
            List<Qna> list = qnaRepository.findByParentIdOrderByLevelAscCreatedAtAsc(origin.getId());
            qnaRepository.deleteAll(list);
        } else {
            qnaRepository.deleteById(id);
        }
    }
}
