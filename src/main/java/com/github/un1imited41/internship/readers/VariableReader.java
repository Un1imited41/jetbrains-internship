package com.github.un1imited41.internship.readers;

import com.github.un1imited41.internship.dto.VariableWrapperDto;
import com.github.un1imited41.internship.entities.QTemplate;
import com.github.un1imited41.internship.entities.QVariableWrapper;
import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class VariableReader {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ReaderHelper readerHelper;

    private static final QTemplate template = QTemplate.template;
    private static final QVariableWrapper variable = QVariableWrapper.variableWrapper;

    public VariableWrapperDto getVariableById(Long id) {
        return readerHelper.selectDto(VariableWrapperDto.class,
                variableQuery().where(variable.id.eq(id))).stream().findFirst().get();
    }

    public Stream<List<VariableWrapperDto>> getAllVariables() {
        List<Long> ids = variableQuery().select(variable.id).fetch();
        return Lists.partition(ids, 100).stream().map(batch ->
                readerHelper.selectDto(VariableWrapperDto.class, variableQuery()
                        .where(variable.id.in(batch))));
    }

    private JPAQuery<?> variableQuery() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFactory.from(variable)
                .join(template).on(template.templateId.eq(variable.templateId));
    }
}
