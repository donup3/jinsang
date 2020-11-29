package com.samplesecurity.repository.reply;

import com.samplesecurity.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> ,ReplyCustomRepository{

    List<Reply> findAllByLevel(int level);

    List<Reply>  findAllByBoardId(Long boardId);

    List<Reply> findAllByLevelAndIdGreaterThan(int level,Long replyId);

    List<Reply> findAllByLevelAndRef(int level, int ref);

    List<Reply> findAllByLevelAndRefAndIdGreaterThan(int level, int ref, Long parentId);

    List<Reply> findAllByLevelLessThanEqualAndRef(int level, int ref);

    List<Reply> findAllByLevelGreaterThanEqualAndRef(int level, int ref);

    List<Reply> findAllByRefOrderGreaterThanAndRef(int setRefOrder, int ref);

    List<Reply> findAllByRefOrderGreaterThanEqualAndRef(int setRefOrder, int ref);
}
