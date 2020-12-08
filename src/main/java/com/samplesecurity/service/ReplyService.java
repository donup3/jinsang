package com.samplesecurity.service;

import com.samplesecurity.domain.board.Board;
import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.reply.Reply;
import com.samplesecurity.domain.reply.ReplyAgreeCheck;
import com.samplesecurity.dto.reply.ReplyListDto;
import com.samplesecurity.repository.MemberRepository;
import com.samplesecurity.repository.board.BoardRepository;
import com.samplesecurity.repository.reply.ReplyAgreeCheckRepository;
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
    private final ReplyAgreeCheckRepository replyAgreeCheckRepository;

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

        //level , reforder  로직 처리
        //자신이 들어갈 곳의 밑바닥 구하기
        int minRefOfBottom = replyRepository.findBottom(reply);

        if (minRefOfBottom != -1) {
            reply.setRefOrder(minRefOfBottom);
            List<Reply> allReply = replyRepository.findAllByRefOrderGreaterThanEqualAndRef(minRefOfBottom, reply.getRef());
            for (Reply findReply : allReply) {
                findReply.setRefOrder(findReply.getRefOrder() + 1);
            }
        } else {
            int maxRefOrder = replyRepository.findMaxRefOrder(reply);
            reply.setRefOrder(maxRefOrder + 1);
        }

        reply.setLevel(reply.getLevel() + 1);
        reply.setBoard(board);
        reply.setMember(findMember);
        reply.setToMemberName(parentReply.getMember().getNickName());

        replyRepository.save(reply);
    }

    public void delete(Long replyId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Reply reply = replyRepository.findById(replyId).get();
        if (userDetails.getUsername().equals(reply.getMember().getEmail())) {
            replyRepository.deleteReply(reply.getLevel(), reply.getRef());
        } else {
            throw new RuntimeException(("자신의 댓글만 삭제할 수 있습니다."));
        }
    }

    public int addAgree(Long replyId, Authentication authentication) {
        ReplyAgreeCheck agreeCheck;
        Reply reply = replyRepository.findById(replyId).get();
        Member findMember = findMember(authentication);

        ReplyAgreeCheck findAgreeCheck = replyAgreeCheckRepository.findByMemberIdAndReplyId(findMember.getId(), replyId);
        agreeCheck = findAgreeCheck;

        if (findAgreeCheck == null) {
            agreeCheck = createAgreeCheck(findMember, reply);
        }

        int count = reply.getAgreeCount();
        if (!agreeCheck.isAgreeChecked()) {
            count++;
            agreeCheck.setAgreeChecked(true);
        } else {
            count--;
            agreeCheck.setAgreeChecked(false);
        }
        reply.setAgreeCount(count);

        return count;
    }

    public int disAgree(Long replyId, Authentication authentication) {
        ReplyAgreeCheck agreeCheck;
        Reply reply = replyRepository.findById(replyId).get();
        Member findMember = findMember(authentication);

        ReplyAgreeCheck findAgreeCheck = replyAgreeCheckRepository.findByMemberIdAndReplyId(findMember.getId(), replyId);
        agreeCheck = findAgreeCheck;

        if (findAgreeCheck == null) {
            agreeCheck = createAgreeCheck(findMember, reply);
        }

        int count = reply.getDisagreeCount();
        if (!agreeCheck.isDisAgreeChecked()) {
            count++;
            agreeCheck.setDisAgreeChecked(true);
        } else {
            count--;
            agreeCheck.setDisAgreeChecked(false);
        }
        reply.setDisagreeCount(count);

        return count;
    }

    private Member findMember(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return memberRepository.findByEmail(username).get();
    }

    private ReplyAgreeCheck createAgreeCheck(Member findMember, Reply reply) {
        ReplyAgreeCheck replyAgreeCheck = ReplyAgreeCheck
                .builder()
                .reply(reply)
                .member(findMember)
                .agreeChecked(false)
                .agreeChecked(false)
                .disAgreeChecked(false)
                .build();

        return replyAgreeCheckRepository.save(replyAgreeCheck);
    }

}
