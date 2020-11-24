package com.samplesecurity.repository.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.samplesecurity.dto.Board.BoardListDto;
import com.samplesecurity.dto.Board.QBoardListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.samplesecurity.domain.QBoard.board;
import static com.samplesecurity.domain.QCategory.category;
import static com.samplesecurity.domain.QMember.member;

public class BoardRepositoryImpl implements BoardCustomRepository {
    private JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardListDto> findAllByDto(String boardType, Pageable pageable) {
        BooleanBuilder builder=new BooleanBuilder();
        if(boardType!=null){
            switch (boardType){
                case "2":
                    builder.and(board.boardType.eq(boardType));
                    break;
                default:
                    break;
            }
        }

        QueryResults<BoardListDto> result = queryFactory
                .select(
                        new QBoardListDto(board.id,
                                category.name.as("category"),
                                board.title,
                                board.createdDate,
                                member.name.as("writer"),
                                board.agreeCount))
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
}
