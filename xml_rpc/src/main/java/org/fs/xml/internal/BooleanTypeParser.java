/*
 * XmlRpc Copyright (C) 2016 Fatih.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fs.xml.internal;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Locale;

import static org.fs.xml.util.C.BOOLEAN_BINARY_STYLE;
import static org.fs.xml.util.C.BOOLEAN_STRING_STYLE;

class BooleanTypeParser implements TypeParser<Boolean> {

  private static BooleanTypeParser instance;

  static BooleanTypeParser getInstance(@BooleanStyle int style) {
    synchronized (BooleanTypeParser.class) {
      if (instance == null) {
        instance = new BooleanTypeParser(style);
      }
      instance.setStyle(style);
      return instance;
    }
  }

  @BooleanStyle private int style;

  BooleanTypeParser(@BooleanStyle int style) {
    this.style = style;
    if (style < BOOLEAN_BINARY_STYLE || style > BOOLEAN_STRING_STYLE) {
      final String error = String.format(Locale.getDefault(), "style for %d is not supported please use %d or %d",
              style, BOOLEAN_BINARY_STYLE, BOOLEAN_STRING_STYLE);
      throw new IllegalArgumentException(error);
    }
  }

  void setStyle(@BooleanStyle int style) {
    this.style = style;
    if (style < BOOLEAN_BINARY_STYLE || style > BOOLEAN_STRING_STYLE) {
      final String error = String.format(Locale.getDefault(), "style for %d is not supported please use %d or %d",
              style, BOOLEAN_BINARY_STYLE, BOOLEAN_STRING_STYLE);
      throw new IllegalArgumentException(error);
    }
  }

  @Override public void write(XmlSerializer writer, Boolean value) throws IOException {
    writer.startTag(null, Constants.BOOLEAN);
    if (style == BOOLEAN_BINARY_STYLE) {
      writer.text(String.valueOf(value ? 1 : 0));
    } else if (style == BOOLEAN_STRING_STYLE) {
      writer.text(String.valueOf(value));
    } else {
      final String error = String.format(Locale.getDefault(), "style for %d is not supported please use %d or %d",
              style, BOOLEAN_BINARY_STYLE, BOOLEAN_STRING_STYLE);
      throw new IOException(error);
    }
    writer.endTag(null, Constants.BOOLEAN);
  }

  @Override public Boolean read(XmlPullParser reader) throws XmlPullParserException, IOException {
    int type = reader.getEventType();
    String text = null;
    while (type != XmlPullParser.END_TAG) {
      if (type == XmlPullParser.TEXT) {
        text = reader.getText();
      }
      type = reader.next();
    }
    //go on next START_TAG
    reader.next();
    if (style == BOOLEAN_BINARY_STYLE) {
      return text != null && Integer.parseInt(text) == 1;
    } else if (style == BOOLEAN_STRING_STYLE) {
      return text != null && Boolean.parseBoolean(text);
    } else {
      final String error = String.format(Locale.getDefault(), "style for %d is not supported please use %d or %d",
              style, BOOLEAN_BINARY_STYLE, BOOLEAN_STRING_STYLE);
      throw new IOException(error);
    }
  }

  @Override public boolean hasRead(XmlPullParser reader) {
    final String nodeName = reader.getName();
    return Constants.BOOLEAN.equalsIgnoreCase(nodeName);
  }

  @Override public boolean hasWrite(Object o) {
    return o instanceof Boolean;
  }
}
