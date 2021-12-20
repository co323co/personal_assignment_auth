package com.assignment.dao;

import com.assignment.dto.admin.selectuser.QSelectUserOutput;
import com.assignment.dto.admin.selectuser.SelectUserInput;
import com.assignment.dto.admin.selectuser.SelectUserOutput;
import com.assignment.entity.QUser;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QUser qUser = QUser.user;

    @Override
    public Page<SelectUserOutput> findByDynamicQuery(SelectUserInput selectUserInput, Pageable pageable) {
        QueryResults<SelectUserOutput> queryResult = queryFactory
                .select(new QSelectUserOutput(
                        qUser.id,
                        qUser.email,
                        qUser.nickname,
                        qUser.grade,
                        qUser.createdAt,
                        qUser.updatedAt
                ))
                .from(qUser)
                .where(eqUserId(selectUserInput.getUserId()), eqUserEmail(selectUserInput.getEmail()),
                        eqUserNickname(selectUserInput.getNickname()), eqUserGrade(selectUserInput.getGrade()))
                .orderBy(qUser.createdAt.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize())
                .fetchResults();

        long totalCount = queryResult.getTotal();
        List<SelectUserOutput> list = queryResult.getResults();

        return new PageImpl<>(list, pageable, totalCount);
    }


    private BooleanExpression eqUserId(Integer userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        return qUser.user.id.eq(userId);
    }

    private BooleanExpression eqUserEmail(String userEmail) {
        if (StringUtils.isEmpty(userEmail)) {
            return null;
        }
        return qUser.user.email.eq(userEmail);
    }

    private BooleanExpression eqUserNickname(String userNickname) {
        if (StringUtils.isEmpty(userNickname)) {
            return null;
        }
        return qUser.user.nickname.eq(userNickname);
    }

    private BooleanExpression eqUserGrade(String userGrade) {
        if (StringUtils.isEmpty(userGrade)) {
            return null;
        }
        return qUser.user.grade.eq(userGrade);
    }

}

