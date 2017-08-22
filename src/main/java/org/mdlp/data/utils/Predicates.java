package org.mdlp.data.utils;

import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public class Predicates {

    public static Predicate xpathExists(@NotNull CriteriaBuilder cb, Expression expression, String xpath) {
        return cb.isTrue(cb.function(
            "xpath_exists",
            Boolean.class,
            cb.literal(xpath),
            expression
        ));
    }

}
