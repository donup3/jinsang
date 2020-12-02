package com.samplesecurity.repository.reply;

import com.samplesecurity.domain.reply.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> ,ReplyCustomRepository{

    List<Reply>  findAllByBoardId(Long boardId);

    List<Reply> findAllByRefOrderGreaterThanEqualAndRef(int setRefOrder, int ref);
}
