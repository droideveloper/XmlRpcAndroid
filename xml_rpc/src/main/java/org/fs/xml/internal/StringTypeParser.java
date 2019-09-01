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

import static org.fs.xml.util.C.STRING_NO_WRAP_STYLE;
import static org.fs.xml.util.C.STRING_WRAP_STYLE;

class StringTypeParser implements TypeParser<String> {

  private static StringTypeParser instance;

  static StringTypeParser getInstance(@StringStyle int style) {
    synchronized (StringTypeParser.class) {
      if (instance == null) {
        instance = new StringTypeParser(style);
      }
      instance.setStyle(style);
      return instance;
    }
  }

  @StringStyle  private int style;

  StringTypeParser(@StringStyle int style) {
    this.style = style;
    if (style < STRING_NO_WRAP_STYLE || style > STRING_WRAP_STYLE) {
      final String error = String.format(Locale.getDefault(), "style for %d is not supported please use %d or %d",
              style, STRING_NO_WRAP_STYLE, STRING_WRAP_STYLE);
      throw new IllegalArgumentException(error);
    }
  }

  void setStyle(@StringStyle int style) {
    this.style = style;
    if (style < STRING_NO_WRAP_STYLE || style > STRING_WRAP_STYLE) {
      final String error = String.format(Locale.getDefault(), "style for %d is not supported please use %d or %d",
              style, STRING_NO_WRAP_STYLE, STRING_WRAP_STYLE);
      throw new IllegalArgumentException(error);
    }
  }

  @Override public void write(XmlSerializer writer, String value) throws IOException {
    if (style == STRING_NO_WRAP_STYLE) {
      writer.text(value);
    } else if (style == STRING_WRAP_STYLE) {
      writer.startTag(null, Constants.STRING);
      writer.text(value);
      writer.endTag(null, Constants.STRING);
    } else {
      final String error = String.format(Locale.getDefault(), "style for %d is not supported please use %d or %d",
              style, STRING_NO_WRAP_STYLE, STRING_WRAP_STYLE);
      throw new IOException(error);
    }
  }

  @Override public String read(XmlPullParser reader) throws XmlPullParserException, IOException {
    int type = reader.getEventType();
    if (style == STRING_NO_WRAP_STYLE) {
      if (type != XmlPullParser.TEXT) {
        final String error = String.format(Locale.getDefault(), "style for %d is not fit expected data try %d",
                style, STRING_WRAP_STYLE);
        throw new IOException(error);
      }
      return reader.getText();
    } else if (style == STRING_WRAP_STYLE) {
      String text = null;
      while (type != XmlPullParser.END_TAG) {
        if (type == XmlPullParser.TEXT) {
          text = reader.getText();
        }
        type = reader.next();
      }
      //go on next START_TAG
      reader.next();
      return text;
    } else {
      final String error = String.format(Locale.getDefault(), "style for %d is not supported please use %d or %d",
              style, STRING_NO_WRAP_STYLE, STRING_WRAP_STYLE);
      throw new IOException(error);
    }
  }

  @Override public boolean hasRead(XmlPullParser reader) {
    try {
      if (style == STRING_NO_WRAP_STYLE) {
        return reader.getEventType() == XmlPullParser.TEXT;
      } else if (style == STRING_WRAP_STYLE) {
        final String nodeName = reader.getName();
        return Constants.STRING.equalsIgnoreCase(nodeName);
      } else {
        final String error = String.format(Locale.getDefault(), "style for %d is not supported please use %d or %d",
                style, STRING_NO_WRAP_STYLE, STRING_WRAP_STYLE);
        throw new RuntimeException(error);
      }
    } catch (XmlPullParserException e) {
      throw new RuntimeException(e);
    }
  }

  @Override public boolean hasWrite(Object o) {
    return o instanceof String;
  }
}
