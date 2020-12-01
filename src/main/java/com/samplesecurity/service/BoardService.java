package com.samplesecurity.service;

import com.samplesecurity.domain.board.AgreeCheck;
import com.samplesecurity.domain.board.AttachFile;
import com.samplesecurity.domain.board.Board;
import com.samplesecurity.domain.Member;
import com.samplesecurity.dto.Board.AttachFileDto;
import com.samplesecurity.dto.Board.BoardListDto;
import com.samplesecurity.repository.board.AgreeCheckRepository;
import com.samplesecurity.repository.board.AttachFileRepository;
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
        if (fileDtos != null) {
            for (AttachFileDto fileDto : fileDtos) {
                AttachFile attachFile = fileDto.toEntity();
                AttachFile saveFile = attachFileRepository.save(attachFile);
                saveFile.setBoard(saveBoard);
            }
        }
    }

    public Board getBoard(Long id) {
        if (id != null) {
            return boardRepository.findById(id).get();
        }
        return null;
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

    public Long getPreBoard(Long boardId, String boardType) {
        if (boardId != null) {
            return boardRepository.findPreBoardId(boardId, boardType);
        }
        return null;
    }

    public Long getNextBoard(Long boardId, String boardType) {
        if (boardId != null) {
            return boardRepository.findNextBoardId(boardId, boardType);
        }
        return null;
    }
}
