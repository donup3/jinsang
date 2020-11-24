package com.samplesecurity.service;

import com.samplesecurity.domain.AttachFile;
import com.samplesecurity.domain.Board;
import com.samplesecurity.dto.Board.AttachFileDto;
import com.samplesecurity.dto.Board.BoardListDto;
import com.samplesecurity.repository.board.AttachFileRepository;
import com.samplesecurity.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final AttachFileRepository attachFileRepository;

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
}
