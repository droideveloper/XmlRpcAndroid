package org.fs.xml.internal;

import android.support.annotation.IntDef;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.fs.xml.util.C.BOOLEAN_BINARY_STYLE;
import static org.fs.xml.util.C.BOOLEAN_STRING_STYLE;


@Retention(RetentionPolicy.RUNTIME)
@IntDef(value = { BOOLEAN_BINARY_STYLE, BOOLEAN_STRING_STYLE })
public @interface BooleanStyle { }
