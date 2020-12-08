package com.samplesecurity.repository.board;

import com.samplesecurity.domain.board.Board;
import com.samplesecurity.domain.board.Category;
import com.samplesecurity.domain.Member;
import com.samplesecurity.dto.Board.BoardListDto;
import com.samplesecurity.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;

@SpringBootTest
@Rollback(value = false)
class BoardRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 더미데이터생성() {
        Category category1 = categoryRepository.findById(1L).get();
        Category category2 = categoryRepository.findById(2L).get();
        Category category3 = categoryRepository.findById(3L).get();
        Member member1 = new Member();
        member1.setEmail("test@t.com");
        member1.setPassword("123");
        member1.setNickName("테스트멤버1");
        memberRepository.save(member1);
        Member member2 = new Member();
        member2.setEmail("test@t.com");
        member2.setNickName("테스트멤버2");
        member2.setPassword("123");
        memberRepository.save(member2);


        for (int i = 0; i < 15; i++) {
            Board board = Board.builder()
                    .category(category1)
                    .title("테스트 제목" + i)
                    .createdDate(LocalDate.now())
                    .boardType("2")
                    .member(member1)
                    .agreeCount(0)
                    .build();
            boardRepository.save(board);
        }
        for (int i = 15; i < 30; i++) {
            Board board = Board.builder()
                    .category(category2)
                    .title("테스트 제목" + i)
                    .createdDate(LocalDate.now())
                    .member(member2)
                    .boardType("2")
                    .agreeCount(0)
                    .build();
            boardRepository.save(board);
        }
        for (int i = 30; i < 101; i++) {
            Board board = Board.builder()
                    .category(category3)
                    .title("테스트 제목" + i)
                    .boardType("2")
                    .createdDate(LocalDate.now())
                    .member(member2)
                    .agreeCount(0)
                    .build();
            boardRepository.save(board);
        }
    }

    @Test
    public void 페이징테스트(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC,"id");
        Page<BoardListDto> boards = boardRepository.findAllByDto("T", pageRequest);
        for (BoardListDto board : boards) {
            System.out.println("board = " + board);
        }
    }
}