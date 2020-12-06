package com.samplesecurity.repository.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.board.Address;
import com.samplesecurity.domain.board.Board;
import com.samplesecurity.dto.Board.BoardListDto;
import com.samplesecurity.dto.Board.QBoardListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.samplesecurity.domain.QMember.member;
import static com.samplesecurity.domain.board.QAddress.address;
import static com.samplesecurity.domain.board.QBoard.board;
import static com.samplesecurity.domain.board.QCategory.category;

public class BoardRepositoryImpl implements BoardCustomRepository {
    private JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardListDto> findAllByDto(String boardType, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (boardType != null) {
            switch (boardType) {
                case "1":
                    builder.and(board.boardType.eq("1"));
                    break;
                case "2":
                    builder.and(board.boardType.eq("1").or(board.boardType.eq("2")));
                    break;
                case "3":
                    builder.and(board.boardType.eq("3"));
                    break;
                case "4":
                    builder.and(board.boardType.eq("4"));
                    break;
                case "5":
                    builder.and(board.boardType.eq("5"));
                    break;
                default:
                    break;
            }
        }

        QueryResults<BoardListDto> result = queryFactory
                .select(
                        new QBoardListDto(board.id,
                                board.id,
                                category.name.as("category"),
                                board.title,
                                board.createdDate,
                                member.nickName.as("writer"),
                                board.agreeCount,
                                board.replies.size(),
                                board.hidden))
                .from(board)
                .orderBy(board.id.desc())
                .leftJoin(board.category, category)
                .leftJoin(board.member, member)
                .offset(pageable.getOffset())
                .where(builder)
                .limit(pageable.getPageSize())
                .fetchResults();

        List<BoardListDto> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Long findPreBoardId(Long boardId, String boardType) {
        return queryFactory
                .select(board.id.max())
                .from(board)
                .where(board.id.lt(boardId)
                        .and(board.boardType.eq(boardType)))
                .fetchOne();
    }

    @Override
    public Long findNextBoardId(Long boardId, String boardType) {
        return queryFactory
                .select(board.id.min())
                .from(board)
                .where(board.id.gt(boardId)
                        .and(board.boardType.eq(boardType)))
                .fetchOne();
    }

    @Override
    public List<Board> findAllByType1() {
        return queryFactory.selectFrom(board)
                .where(board.boardType.eq("1"))
                .orderBy(board.id.desc())
                .limit(4)
                .fetch();
    }

    @Override
    public List<Board> findAllByType2() {
        return queryFactory.selectFrom(board)
                .where(board.boardType.eq("2").or(board.boardType.eq("1")))
                .orderBy(board.id.desc())
                .limit(4)
                .fetch();
    }

    @Override
    public List<Board> findAllByType3() {
        return queryFactory.selectFrom(board)
                .where(board.boardType.eq("3"))
                .orderBy(board.id.desc())
                .limit(4)
                .fetch();
    }

    @Override
    public List<Board> findAllByType4() {
        return queryFactory.selectFrom(board)
                .where(board.boardType.eq("4"))
                .orderBy(board.id.desc())
                .limit(4)
                .fetch();
    }

    @Override
    public List<Address> findCountByAddress() {
        return queryFactory.selectFrom(address)
                .fetch();
    }
}
