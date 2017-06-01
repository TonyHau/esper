/*
 ***************************************************************************************
 *  Copyright (C) 2006 EsperTech, Inc. All rights reserved.                            *
 *  http://www.espertech.com/esper                                                     *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 ***************************************************************************************
 */
package com.espertech.esper.codegen.model.expression;

import java.util.Map;
import java.util.Set;

public class CodegenExpressionLocalMethodNoParams implements CodegenExpression {
    private final String method;

    public CodegenExpressionLocalMethodNoParams(String method) {
        this.method = method;
    }

    public void render(StringBuilder builder, Map<Class, String> imports) {
        builder.append(method).append("(");
        builder.append(")");
    }

    public void mergeClasses(Set<Class> classes) {
    }
}
