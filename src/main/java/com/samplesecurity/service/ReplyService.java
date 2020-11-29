package com.samplesecurity.service;

import com.samplesecurity.domain.Board;
import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.Reply;
import com.samplesecurity.dto.reply.ReplyListDto;
import com.samplesecurity.repository.MemberRepository;
import com.samplesecurity.repository.board.BoardRepository;
import com.samplesecurity.repository.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public List<ReplyListDto> getReplyList(Long boardId) {
        return replyRepository.findAllDtoByBoardId(boardId);
    }

    public void register(Long boardId, Reply reply, Authentication authentication) {
        Member findMember = findMember(authentication);
        Board board = boardRepository.findById(boardId).get();
        //가장 큰 ref 값을 찾아서 +1
        int max = 0;
        List<Reply> all = replyRepository.findAll();
        for (Reply findReply : all) {
            if (findReply.getRef() > max) {
                max = findReply.getRef();
            }
        }
        reply.setRef(max + 1);
        reply.setBoard(board);
        reply.setMember(findMember);
        replyRepository.save(reply);
    }

    public void subRegister(Long boardId, Reply reply, Authentication authentication, Long parentId) {
        Member findMember = findMember(authentication);
        Board board = boardRepository.findById(boardId).get();
        Reply parentReply = replyRepository.findById(parentId).get();
        parentReply.setAnswerNum(parentReply.getAnswerNum() + 1);

        //level , reforder  로직 처리
        System.out.println("reply.getLevel() = " + reply.getLevel());
        List<Reply> findReplyList = replyRepository.findAllByLevelAndRefAndIdGreaterThan(reply.getLevel(), reply.getRef(), parentId);
        for (Reply reply1 : findReplyList) {
            System.out.println("reply1 = " + reply1);
        }

        // 사이에 들어오는 것을 체크 해줘야함
        // 1-1, 1-2 사이에 1-1-1 이 중간에 들어올 경우
        //1-1-1의 reforder를 1-2의 것으로 바꾸고 1-2부터 같은 레벨을 가진 reply의 reforder를 모두 +1 해준다.

        if (findReplyList.size() > 0) {
            log.info("@@");
            Reply firstReply = findReplyList.get(0);

            reply.setRefOrder(firstReply.getRefOrder());

            for (Reply reply1 : findReplyList) {
                reply1.setRefOrder(reply1.getRefOrder() + 1);
            }

        }
        // 1-2 들어올때를 체크해줘야함
        //1-1, 1-1-1 다음으로 1-2가 올경우 1-1의 1-1 level 이상의  자식댓글을 가진 reply의 댓글 수+1만큼 1-2 refOrder에 더해준다.
        else {
            List<Reply> allByLevel = replyRepository.findAllByLevelGreaterThanEqualAndRef(reply.getLevel(), reply.getRef());
            int answerCount = 0;
            if (allByLevel.size() > 0) {
                for (Reply findReply : allByLevel) {
                    answerCount += findReply.getAnswerNum();
                }
                System.out.println("answerCount = " + answerCount);
                reply.setRefOrder(reply.getRefOrder() + answerCount);
            } else {
                reply.setRefOrder(reply.getRefOrder() + 1);
            }
        }

        reply.setLevel(reply.getLevel() + 1);
        reply.setBoard(board);
        reply.setMember(findMember);
        log.info("replyService subReply: " + reply);
        replyRepository.save(reply);
    }

    public void delete(Long replyId) {
        replyRepository.deleteById(replyId);
    }

    private Member findMember(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return memberRepository.findByEmail(username).get();
    }
}
