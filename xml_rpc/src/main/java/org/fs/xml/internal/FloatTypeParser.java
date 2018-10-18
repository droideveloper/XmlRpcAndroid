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

class FloatTypeParser implements TypeParser<Float> {

  public static FloatTypeParser create() {
    return new FloatTypeParser();
  }

  private FloatTypeParser() {}

  @Override public void write(XmlSerializer writer, Float value) throws IOException {
    writer.startTag(null, Constants.FLOAT);
    writer.text(String.valueOf(value));
    writer.endTag(null, Constants.FLOAT);
  }

  @Override public Float read(XmlPullParser reader) throws XmlPullParserException, IOException {
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
    try {
      if (text != null) {
        return Float.parseFloat(text);
      }
    } catch (Exception ignored) {}
    return Float.NaN;
  }

  @Override public boolean hasRead(XmlPullParser reader) {
    final String nodeName = reader.getName();
    return Constants.FLOAT.equalsIgnoreCase(nodeName);
  }

  @Override public boolean hasWrite(Object o) {
    return o instanceof Float;
  }
}
