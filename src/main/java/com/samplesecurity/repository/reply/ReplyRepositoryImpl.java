package com.samplesecurity.repository.reply;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.samplesecurity.domain.reply.Reply;
import com.samplesecurity.dto.reply.QReplyListDto;
import com.samplesecurity.dto.reply.ReplyListDto;

import javax.persistence.EntityManager;
import java.util.List;

import static com.samplesecurity.domain.QMember.member;
import static com.samplesecurity.domain.reply.QReply.reply;


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
                .orderBy(reply.ref.asc())
                .orderBy(reply.refOrder.asc())
                .fetch();
    }

    //    @SuppressWarnings("ConstantConditions") //int -> integer로 바꿔야안전 null일수도 있어서 warning 표시가 뜸
    @Override
    public int findBottom(Reply parentReply) {
        Integer minRef = queryFactory.select(reply.refOrder.min())
                .from(reply)
                .where(reply.level.loe(parentReply.getLevel())
                        .and(reply.refOrder.gt(parentReply.getRefOrder()))
                        .and(reply.ref.eq(parentReply.getRef())))
                .fetchOne();

        if (minRef != null) {
            return minRef;
        }
        return -1;
    }

    @Override
    public int findMaxRefOrder(Reply parentReply) {
        Integer maxRef = queryFactory.select(reply.refOrder.max())
                .from(reply)
                .where(reply.level.goe(parentReply.getLevel())
                        .and(reply.refOrder.goe(parentReply.getRefOrder()))
                        .and(reply.ref.eq(parentReply.getRef())))
                .fetchOne();
        if (maxRef != null) {
            return maxRef;
        }
        return -1;
    }

    @Override
    public void deleteReply(int level, int ref) {
        queryFactory.delete(reply)
                .where(reply.level.goe(level).and(reply.ref.eq(ref)))
                .execute();
    }
}
