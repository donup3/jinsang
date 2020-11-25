package com.samplesecurity.service;

import com.samplesecurity.domain.AgreeCheck;
import com.samplesecurity.domain.AttachFile;
import com.samplesecurity.domain.Board;
import com.samplesecurity.domain.Member;
import com.samplesecurity.dto.Board.AttachFileDto;
import com.samplesecurity.dto.Board.BoardListDto;
import com.samplesecurity.repository.AgreeCheckRepository;
import com.samplesecurity.repository.AttachFileRepository;
import com.samplesecurity.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final AttachFileRepository attachFileRepository;
    private final AgreeCheckRepository agreeCheckRepository;

    public Page<BoardListDto> getBoardList(String boardType, Pageable pageable) {
        return boardRepository.findAllByDto(boardType, pageable);
    }

    public void register(Board board, List<AttachFileDto> fileDtos) {
        Board saveBoard = boardRepository.save(board);

        for (AttachFileDto fileDto : fileDtos) {
            AttachFile attachFile = fileDto.toEntity();
            AttachFile saveFile = attachFileRepository.save(attachFile);
            saveFile.setBoard(saveBoard);
        }
    }

    public Board getBoard(Long id) {
        return boardRepository.findById(id).get();
    }

    public int addAgree(Member findMember, Long boardId) {
        AgreeCheck agreeCheck;
        Board board = boardRepository.findById(boardId).get();

        AgreeCheck findAgreeCheck = agreeCheckRepository.findByMemberIdAndBoardId(findMember.getId(), boardId);
        agreeCheck = findAgreeCheck;

        if (findAgreeCheck == null) {
            agreeCheck = createAgreeCheck(findMember, board);
        }

        int count = board.getAgreeCount();
        if (!agreeCheck.isAgreeChecked()) {
            count++;
            agreeCheck.setAgreeChecked(true);
            board.setAgreeCount(count);
        } else {
            count--;
            agreeCheck.setAgreeChecked(false);
            board.setAgreeCount(count);
        }

        return count;
    }

    private AgreeCheck createAgreeCheck(Member findMember, Board board) {
        AgreeCheck agreeCheck = AgreeCheck
                .builder()
                .board(board)
                .member(findMember)
                .agreeChecked(false)
                .build();
        return agreeCheckRepository.save(agreeCheck);
    }
}
