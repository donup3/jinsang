package com.samplesecurity.service;

import com.samplesecurity.domain.Board;
import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.Reply;
import com.samplesecurity.dto.reply.ReplyListDto;
import com.samplesecurity.repository.MemberRepository;
import com.samplesecurity.repository.board.BoardRepository;
import com.samplesecurity.repository.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public List<ReplyListDto> getReplyList(Long boardId) {
        return replyRepository.findAllDtoByBoardId(boardId);
    }

    public void register(Long boardId, Reply reply, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Member findMember = memberRepository.findByEmail(username).get();

        Board board = boardRepository.findById(boardId).get();
        reply.setBoard(board);
        reply.setMember(findMember);
        replyRepository.save(reply);
    }

    public void delete(Long replyId) {
        replyRepository.deleteById(replyId);
    }
}
