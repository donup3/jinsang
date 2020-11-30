package com.samplesecurity.repository.reply;

import com.samplesecurity.domain.reply.Reply;
import com.samplesecurity.dto.reply.ReplyListDto;

import java.util.List;

public interface ReplyCustomRepository {
    List<ReplyListDto> findAllDtoByBoardId(Long boardId);
    int findBottom(Reply reply);
    int findMaxRefOrder(Reply reply);
}
