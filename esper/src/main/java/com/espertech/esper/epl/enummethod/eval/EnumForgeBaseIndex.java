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
package com.espertech.esper.epl.enummethod.eval;

import com.espertech.esper.epl.expression.core.ExprForge;
import com.espertech.esper.event.arr.ObjectArrayEventType;

public abstract class EnumForgeBaseIndex implements EnumForge {

    protected ExprForge innerExpression;
    protected int streamNumLambda;
    protected ObjectArrayEventType indexEventType;

    public EnumForgeBaseIndex(ExprForge innerExpression, int streamNumLambda, ObjectArrayEventType indexEventType) {
        this.innerExpression = innerExpression;
        this.streamNumLambda = streamNumLambda;
        this.indexEventType = indexEventType;
    }

    public int getStreamNumSize() {
        return streamNumLambda + 2;
    }
}
