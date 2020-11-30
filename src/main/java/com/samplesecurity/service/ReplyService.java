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
        reply.setToMemberName(parentReply.getMember().getName());

        replyRepository.save(reply);
    }

    public void delete(Long replyId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Reply reply = replyRepository.findById(replyId).get();
        if (userDetails.getUsername().equals(reply.getMember().getEmail())) {
            replyRepository.deleteById(replyId);
        } else {
            throw new RuntimeException(("자신의 댓글만 삭제할 수 있습니다."));
        }
    }

    private Member findMember(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return memberRepository.findByEmail(username).get();
    }
}
