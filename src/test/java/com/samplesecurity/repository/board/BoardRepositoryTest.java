package com.samplesecurity.repository.board;

import com.samplesecurity.domain.board.Address;
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
    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void 더미데이터생성() {
        Category category1 = categoryRepository.findById(1L).get();
        Category category2 = categoryRepository.findById(2L).get();
        Category category3 = categoryRepository.findById(3L).get();
        Address address1 = addressRepository.findById(3L).get();
        Address address2 = addressRepository.findById(6L).get();
        Address address3 = addressRepository.findById(9L).get();
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
                    .cityNameOfAddress(address1)
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
                    .cityNameOfAddress(address2)
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
                    .cityNameOfAddress(address3)
                    .createdDate(LocalDate.now())
                    .member(member2)
                    .agreeCount(0)
                    .build();
            boardRepository.save(board);
        }
    }

    @Test
    public void 페이징테스트() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        Page<BoardListDto> boards = boardRepository.findAllByDto("T", pageRequest);
        for (BoardListDto board : boards) {
            System.out.println("board = " + board);
        }
    }

    @Test
    public void 테스트비밀글생성() {
        Category category3 = categoryRepository.findById(3L).get();
        Board board = Board.builder()
                .category(category3)
                .title("변호사 상담 비밀글 테스트")
                .boardType("2")
                .createdDate(LocalDate.now())
                .member(memberRepository.findById(1L).get())
                .boardType("5")
                .hidden("Y")
                .agreeCount(0)
                .build();
        boardRepository.save(board);

        Board board2 = Board.builder()
                .category(category3)
                .title("심리 상담 비밀글 테스트")
                .boardType("2")
                .createdDate(LocalDate.now())
                .member(memberRepository.findById(1L).get())
                .boardType("6")
                .hidden("Y")
                .agreeCount(0)
                .build();
        boardRepository.save(board2);

        Board board3 = Board.builder()
                .category(category3)
                .title("CS 상담 비밀글 테스트")
                .boardType("2")
                .createdDate(LocalDate.now())
                .member(memberRepository.findById(1L).get())
                .boardType("7")
                .hidden("Y")
                .agreeCount(0)
                .build();
        boardRepository.save(board3);
    }
}