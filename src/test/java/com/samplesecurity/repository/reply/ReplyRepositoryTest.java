package com.samplesecurity.repository.reply;

import com.samplesecurity.domain.board.Board;
import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.reply.Reply;
import com.samplesecurity.repository.MemberRepository;
import com.samplesecurity.repository.board.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;

@SpringBootTest
@Rollback(value = false)
class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 데이터생성() {
        Board board = boardRepository.findById(101L).get();
        Member member = memberRepository.findById(1L).get();
        for (int i = 0; i < 3; i++) {
            Reply reply = Reply.builder()
                    .board(board)
                    .contents("테스트 내용" + i)
                    .createdDate(LocalDate.now())
                    .member(member)
                    .toMemberName("동그라미")
                    .build();

            replyRepository.save(reply);
        }
    }

}