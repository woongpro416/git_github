package com.example.Logitech.controller;

import com.example.Logitech.domain.Qna;
import com.example.Logitech.dto.MemberResponseDto;
import com.example.Logitech.service.QnaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {

    public final QnaService qnaService;

    //전체 목록
    @GetMapping("/list")
    public List<Qna> getQnaList() {
        return qnaService.getQnaList();
    }

    //제목 검색
    @GetMapping("/search")
    public List<Qna> findByTitle(@RequestParam String keyword) {
        return qnaService.findByTitle(keyword);
    }

    //질문,답변 상세보기
    @GetMapping("/detail/{id}")
    public List<Qna> detailQna(@PathVariable Long id) {
        return qnaService.detail(id);
    }

    //질문 등록
    @PostMapping("/question")
    public Qna addQuestion(@RequestBody Qna qna) {
        return qnaService.addQuestion(qna);
    }

    //답변 등록 (관리자)
    @PostMapping("/answer/{parentId}")
    public Qna addAnswer(@PathVariable Long parentId, @RequestBody Qna qna, HttpSession session) {
        MemberResponseDto loginUser = (MemberResponseDto) session.getAttribute("loginUser");
        qna.setWriter(qna.getWriter());
        return qnaService.addAnswer(parentId, qna);
    }

    //qna 수정
    @PutMapping("/update/{id}")
    public Qna update(@RequestParam Long id,@RequestBody Qna qna, HttpSession session) {
        MemberResponseDto loginUser = (MemberResponseDto) session.getAttribute("loginUser");
        boolean isAdmin = loginUser.getRole().name().equals("ADMIN");
        return qnaService.updateQna(id, qna, loginUser.getName(), isAdmin);
    }



    //qna 삭제
    @DeleteMapping("/delete/{id}")
    public void deleteQna(@PathVariable Long id, HttpSession session) {
        MemberResponseDto loginUser = (MemberResponseDto) session.getAttribute("loginMember");
        boolean isAdmin = loginUser.getRole().name().equals("ADMIN");
        qnaService.delete(id, loginUser.getName(), isAdmin);
    }
}
