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
package com.espertech.esper.epl.enummethod.dot;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.codegen.base.CodegenClassScope;
import com.espertech.esper.codegen.base.CodegenMethodScope;
import com.espertech.esper.codegen.model.expression.CodegenExpression;
import com.espertech.esper.epl.rettype.EPType;
import com.espertech.esper.epl.rettype.EPTypeHelper;

import java.util.Arrays;
import java.util.Collection;

import static com.espertech.esper.codegen.model.expression.CodegenExpressionBuilder.staticMethod;

public class ExprDotStaticMethodWrapEventBeanArr implements ExprDotStaticMethodWrap {
    private EventType type;

    public ExprDotStaticMethodWrapEventBeanArr(EventType type) {
        this.type = type;
    }

    public EPType getTypeInfo() {
        return EPTypeHelper.collectionOfEvents(type);
    }

    public Collection convertNonNull(Object result) {
        if (!result.getClass().isArray()) {
            return null;
        }
        return Arrays.asList((EventBean[]) result);
    }

    public CodegenExpression codegenConvertNonNull(CodegenExpression result, CodegenMethodScope codegenMethodScope, CodegenClassScope codegenClassScope) {
        return staticMethod(Arrays.class, "asList", result);
    }
}
