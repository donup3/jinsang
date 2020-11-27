package com.samplesecurity.repository.reply;

import com.samplesecurity.dto.reply.ReplyListDto;

import java.util.List;

public interface ReplyCustomRepository {
    List<ReplyListDto> findAllDtoByBoardId(Long boardId);
}
