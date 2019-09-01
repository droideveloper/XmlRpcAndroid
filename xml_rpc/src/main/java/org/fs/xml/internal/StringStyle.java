package org.fs.xml.internal;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.fs.xml.util.C.STRING_NO_WRAP_STYLE;
import static org.fs.xml.util.C.STRING_WRAP_STYLE;

@Retention(RetentionPolicy.RUNTIME)
@IntDef(value = { STRING_NO_WRAP_STYLE, STRING_WRAP_STYLE })
public @interface StringStyle { }
