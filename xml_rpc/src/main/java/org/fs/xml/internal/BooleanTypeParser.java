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

class BooleanTypeParser implements TypeParser<Boolean> {

  public final static int STYLE_BINARY = 0x01;
  public final static int STYLE_STRING = 0x02;

  private final int style;

  public static BooleanTypeParser create(int style) {
    return new BooleanTypeParser(style);
  }

  private BooleanTypeParser(int style) {
    this.style = style;
    if (style < STYLE_BINARY || style > STYLE_STRING) {
      throw new IllegalArgumentException("style is not supported");
    }
  }

  @Override public void write(XmlSerializer writer, Boolean value) throws IOException {
    writer.startTag(null, Constants.BOOLEAN);
    if (style == STYLE_BINARY) {
      writer.text(String.valueOf(value ? 1 : 0));
    } else if (style == STYLE_STRING) {
      writer.text(String.valueOf(value));
    } else {
      throw new IOException("style and data are no good");
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
    if (style == STYLE_BINARY) {
      return Integer.parseInt(text) == 1;
    } else if (style == STYLE_STRING) {
      return Boolean.parseBoolean(text);
    } else {
      throw new IOException("style and data are no good");
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
