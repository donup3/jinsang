package com.samplesecurity.service;

import com.samplesecurity.domain.board.*;
import com.samplesecurity.domain.Member;
import com.samplesecurity.dto.Board.AttachFileDto;
import com.samplesecurity.dto.Board.BoardListDto;
import com.samplesecurity.dto.Board.BoardUpdateDto;
import com.samplesecurity.repository.MemberRepository;
import com.samplesecurity.repository.board.*;
import com.samplesecurity.repository.reply.ReplyRepository;
import com.samplesecurity.dto.Board.BoardMapDto;
import com.samplesecurity.repository.board.AgreeCheckRepository;
import com.samplesecurity.repository.board.AttachFileRepository;
import com.samplesecurity.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Float.*;
import static java.lang.Long.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final AttachFileRepository attachFileRepository;
    private final AgreeCheckRepository agreeCheckRepository;
    private final CategoryRepository categoryRepository;
    private final ReplyRepository replyRepository;
    private final UploadFileRepository uploadFileRepository;
    private final AddressRepository addressRepository;

    public Page<BoardListDto> getBoardList(String boardType, Pageable pageable) {

        return boardRepository.findAllByDto(boardType, pageable);
    }

    //게시물 등록
    public void register(Board board, List<AttachFileDto> fileDtos, String address) {
        if (address != null) {
            String cityName = address.substring(0, 2);
            log.info("cityName: " + cityName);
            Address findAddress = addressRepository.findByCityName(cityName);

            if (findAddress != null) {
                board.setCityNameOfAddress(findAddress);
            }
        }
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
        if (board.getAgreeCount() >= 50 && board.getBoardType().equals("2")) {
            board.setBoardType("1");
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

    public Board getBoardByLoginCheck(Long boardId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Board board = boardRepository.findById(boardId).get();
        if (userDetails.getUsername().equals(board.getMember().getEmail())) {
            return board;
        } else {
            throw new RuntimeException("자신의 글만 수정할 수 있습니다.");
        }
    }

    public void update(Member findMember, Long boardId, BoardUpdateDto boardUpdateDto) {
        Board board = boardRepository.findById(boardId).get();
        Long newCategoryId = parseLong(boardUpdateDto.getCategory());
        Category newCategory = categoryRepository.findById(newCategoryId).get();

        if (findMember.getNickName().equals(board.getMember().getNickName())) {
            attachFileRepository.deleteByBoardId(boardId);
            //게시물 업데이트
            board.setTitle(boardUpdateDto.getTitle());
            board.setContents(boardUpdateDto.getContents());
            //1234타입의 글은 업데이트할때 hidden이 null임
            if (boardUpdateDto.getHidden() != null) {
                board.setHidden(boardUpdateDto.getHidden());
            }
            //주소 위도 경도 업데이트
            if (boardUpdateDto.getAddress() != null && boardUpdateDto.getLatitude() != null && boardUpdateDto.getLongitude() != null) {
                board.setAddress(boardUpdateDto.getAddress());
                board.setLatitude(parseFloat(boardUpdateDto.getLatitude()));
                board.setLongitude(parseFloat(boardUpdateDto.getLongitude()));
            }
            board.setCategory(newCategory);
            //파일 업데이트
            if (boardUpdateDto.getFileDtos() != null) {
                boardUpdateDto.getFileDtos().forEach(attachFile -> {
                    attachFile.setBoard(board);
                    attachFileRepository.save(attachFile);
                });
            }
        }
    }

    public void delete(Long boardId, Member findMember) {
        Board board = boardRepository.findById(boardId).get();
        if (findMember.getNickName().equals(board.getMember().getNickName())) {
            //댓글 삭제
            replyRepository.deleteByBoardId(boardId);
            //파일 삭제
            List<AttachFile> attachFiles = attachFileRepository.findAllByBoardId(boardId);
            deleteFiles(attachFiles);
            attachFileRepository.deleteByBoardId(boardId);
            uploadFileRepository.deleteByBoardId(boardId);
            //게시글 삭제
            boardRepository.deleteById(boardId);
        }
    }

    private void deleteFiles(List<AttachFile> attachList) {
        if (attachList == null || attachList.size() == 0) {
            return;
        }
        attachList.forEach(attach -> {
            try {
                Path file = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName());
                Files.deleteIfExists(file);
                if (Files.probeContentType(file).startsWith("image")) {
                    Path thumbNail = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_" + attach.getFileName());
                    Files.delete(thumbNail);
                }
            } catch (Exception e) {
                log.error("delete file error " + e.getMessage());
            }
        });
    }

    public List<BoardMapDto> getBoardInfoForMap() {
        List<Board> board = boardRepository.findAll();
        List<BoardMapDto> boardMapDtos = new ArrayList<>();

        board.forEach(item -> {
            BoardMapDto boardItemForMap = BoardMapDto.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .latitude(item.getLatitude())
                    .longitude(item.getLongitude())
                    .build();
            boardMapDtos.add(boardItemForMap);
        });

        return boardMapDtos;
    }
}
