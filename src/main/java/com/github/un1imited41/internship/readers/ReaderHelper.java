package com.github.un1imited41.internship.readers;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static java.util.stream.Collectors.toList;

@Component
public class ReaderHelper {

    public <T> List<T> selectDto(Class<T> clazz, JPAQuery<?> query) {

        List<EntityPath<?>> joins = query.getMetadata().getJoins()
                .stream().map(e -> (EntityPath<?>) e.getTarget()).collect(toList());

        EntityPath<?> root = joins.get(0);

        Expression<?> id;
        try {
            String idFieldName = ReflectionUtils.getFields(root.getType(), field ->
                    field.getAnnotation(Id.class) != null).stream().findFirst().get().getName();
            id = (Expression<?>) root.getClass().getField(idFieldName).get(root);
        } catch (Exception ignored) {
            throw new RuntimeException("Not found id field");
        }
        return query.transform(groupBy(id)
                .list(Projections.bean(clazz, getBindings(clazz, root, joins))));
    }

    @SneakyThrows
    private <T> Map<String, Expression<?>> getBindings(Class<T> clazz, EntityPath<?> target,
                                                       List<EntityPath<?>> joins) {
        joins.remove(target);
        var bindings = new HashMap<String, Expression<?>>();
        var entityFields = getFieldMap(target);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (entityFields.containsKey(field.getName())) {
                if (field.getType().isAssignableFrom(List.class)) {
                    findEntity(joins, field).ifPresentOrElse(path -> {
                        var type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        bindings.put(field.getName(), list(Projections.bean((Class<?>) type,
                                getBindings((Class<?>) type, path, joins))));
                    }, () -> bindings.put(field.getName(), entityFields.get(field.getName())));
                } else if (entityFields.get(field.getName()) instanceof EntityPath) {
                    findEntity(joins, field).ifPresent(path ->
                            bindings.put(field.getName(), Projections.bean(field.getType(),
                                    getBindings(field.getType(),
                                            path, joins))));
                } else
                    bindings.put(field.getName(), entityFields.get(field.getName()));
            } else {
                findEntity(joins, field).ifPresent(path ->
                        bindings.put(field.getName(), Projections.bean(field.getType(),
                                getBindings(field.getType(), path, joins))));
            }
        }
        return bindings;
    }

    private Optional<EntityPath<?>> findEntity(List<EntityPath<?>> joins, Field field) {
        return joins.stream()
                .filter(j -> field.getName()
                        .startsWith(Introspector.decapitalize(j.getType().getSimpleName()))).findFirst();
    }

    @SneakyThrows
    private Map<String, Expression<?>> getFieldMap(EntityPath<?> entity) {
        Map<String, Expression<?>> map = new HashMap<>();
        ReflectionUtils.getFields(entity.getClass(), field ->
                !Modifier.isStatic(field.getModifiers())).forEach(field -> {
            field.setAccessible(true);
            try {
                if (field.get(entity) instanceof Expression<?>)
                    map.put(field.getName(), (Expression<?>) field.get(entity));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return map;
    }

}
