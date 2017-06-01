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
package com.espertech.esper.avro.getter;

import com.espertech.esper.avro.core.AvroEventPropertyGetter;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.PropertyAccessException;
import com.espertech.esper.codegen.core.CodegenBlock;
import com.espertech.esper.codegen.core.CodegenContext;
import com.espertech.esper.codegen.model.expression.CodegenExpression;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import static com.espertech.esper.codegen.model.expression.CodegenExpressionBuilder.*;

public class AvroEventBeanGetterDynamicPoly implements AvroEventPropertyGetter {
    private final AvroEventPropertyGetter[] getters;

    public AvroEventBeanGetterDynamicPoly(AvroEventPropertyGetter[] getters) {
        this.getters = getters;
    }

    public Object getAvroFieldValue(GenericData.Record record) {
        return getAvroFieldValuePoly(record, getters);
    }

    public Object get(EventBean eventBean) throws PropertyAccessException {
        GenericData.Record record = (GenericData.Record) eventBean.getUnderlying();
        return getAvroFieldValue(record);
    }

    public boolean isExistsProperty(EventBean eventBean) {
        return true;
    }

    public Object getFragment(EventBean eventBean) throws PropertyAccessException {
        return null;
    }

    public Object getAvroFragment(GenericData.Record record) {
        return null;
    }

    public boolean isExistsPropertyAvro(GenericData.Record record) {
        return getAvroFieldValuePolyExists(record, getters);
    }

    public CodegenExpression codegenEventBeanGet(CodegenExpression beanExpression, CodegenContext context) {
        return codegenUnderlyingGet(castUnderlying(GenericData.Record.class, beanExpression), context);
    }

    public CodegenExpression codegenEventBeanExists(CodegenExpression beanExpression, CodegenContext context) {
        return constantTrue();
    }

    public CodegenExpression codegenEventBeanFragment(CodegenExpression beanExpression, CodegenContext context) {
        return constantNull();
    }

    public CodegenExpression codegenUnderlyingGet(CodegenExpression underlyingExpression, CodegenContext context) {
        return localMethod(getAvroFieldValuePolyCodegen(context, getters), underlyingExpression);
    }

    public CodegenExpression codegenUnderlyingExists(CodegenExpression underlyingExpression, CodegenContext context) {
        return constantTrue();
    }

    public CodegenExpression codegenUnderlyingFragment(CodegenExpression underlyingExpression, CodegenContext context) {
        return constantNull();
    }

    static boolean getAvroFieldValuePolyExists(GenericData.Record record, AvroEventPropertyGetter[] getters) {
        if (record == null) {
            return false;
        }
        record = navigatePoly(record, getters);
        return record != null && getters[getters.length - 1].isExistsPropertyAvro(record);
    }

    static String getAvroFieldValuePolyExistsCodegen(CodegenContext context, AvroEventPropertyGetter[] getters) {
        return context.addMethod(boolean.class, GenericData.Record.class, "record", AvroEventBeanGetterDynamicPoly.class)
                .ifRefNullReturnFalse("record")
                .assignRef("record", localMethod(navigatePolyCodegen(context, getters), ref("record")))
                .ifRefNullReturnFalse("record")
                .methodReturn(getters[getters.length - 1].codegenUnderlyingExists(ref("record"), context));
    }

    static Object getAvroFieldValuePoly(GenericData.Record record, AvroEventPropertyGetter[] getters) {
        if (record == null) {
            return null;
        }
        record = navigatePoly(record, getters);
        if (record == null) {
            return null;
        }
        return getters[getters.length - 1].getAvroFieldValue(record);
    }

    static String getAvroFieldValuePolyCodegen(CodegenContext context, AvroEventPropertyGetter[] getters) {
        return context.addMethod(Object.class, GenericData.Record.class, "record", AvroEventBeanGetterDynamicPoly.class)
                .ifRefNullReturnNull("record")
                .assignRef("record", localMethod(navigatePolyCodegen(context, getters), ref("record")))
                .ifRefNullReturnNull("record")
                .methodReturn(getters[getters.length - 1].codegenUnderlyingGet(ref("record"), context));
    }

    static Object getAvroFieldFragmentPoly(GenericData.Record record, AvroEventPropertyGetter[] getters) {
        if (record == null) {
            return null;
        }
        record = navigatePoly(record, getters);
        if (record == null) {
            return null;
        }
        return getters[getters.length - 1].getAvroFragment(record);
    }

    static String getAvroFieldFragmentPolyCodegen(CodegenContext context, AvroEventPropertyGetter[] getters) {
        return context.addMethod(Object.class, GenericData.Record.class, "record", AvroEventBeanGetterDynamicPoly.class)
                .ifRefNullReturnNull("record")
                .assignRef("record", localMethod(navigatePolyCodegen(context, getters), ref("record")))
                .ifRefNullReturnNull("record")
                .methodReturn(getters[getters.length - 1].codegenUnderlyingFragment(ref("record"), context));
    }

    private static GenericData.Record navigatePoly(GenericData.Record record, AvroEventPropertyGetter[] getters) {
        for (int i = 0; i < getters.length - 1; i++) {
            Object value = getters[i].getAvroFieldValue(record);
            if (!(value instanceof GenericData.Record)) {
                return null;
            }
            record = (GenericData.Record) value;
        }
        return record;
    }

    private static String navigatePolyCodegen(CodegenContext context, AvroEventPropertyGetter[] getters) {
        CodegenBlock block = context.addMethod(GenericData.Record.class, GenericData.Record.class, "record", AvroEventBeanGetterDynamicPoly.class);
        block.declareVar(Object.class, "value", constantNull());
        for (int i = 0; i < getters.length - 1; i++) {
            block.assignRef("value", getters[i].codegenUnderlyingGet(ref("record"), context))
                    .ifRefNotTypeReturnConst("value", GenericData.Record.class, null)
                    .assignRef("record", cast(GenericData.Record.class, ref("value")));
        }
        return block.methodReturn(ref("record"));
    }
}
