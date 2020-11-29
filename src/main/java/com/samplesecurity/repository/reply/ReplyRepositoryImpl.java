package com.samplesecurity.repository.reply;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.samplesecurity.dto.reply.QReplyListDto;
import com.samplesecurity.dto.reply.ReplyListDto;

import javax.persistence.EntityManager;
import java.util.List;

import static com.samplesecurity.domain.QMember.member;
import static com.samplesecurity.domain.QReply.reply;

public class ReplyRepositoryImpl implements ReplyCustomRepository {
    private JPAQueryFactory queryFactory;

    public ReplyRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ReplyListDto> findAllDtoByBoardId(Long boardId) {
        return queryFactory.select(new QReplyListDto(
                reply.id.as("replyId"),
                reply.contents,
                reply.ref,
                reply.level,
                reply.refOrder,
                member.name,
                reply.toMemberName,
                reply.createdDate,
                reply.agreeCount,
                reply.disagreeCount))
                .from(reply)
                .leftJoin(reply.member, member)
                .where(reply.board.id.eq(boardId))
                .orderBy(reply.ref.desc())
                .orderBy(reply.refOrder.asc())
                .fetch();
    }
}
