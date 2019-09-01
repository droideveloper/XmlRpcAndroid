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

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

class DoubleTypeParser implements TypeParser<Double> {

  private static TypeParser<Double> instance;

  static TypeParser<Double> getInstance() {
    synchronized (DoubleTypeParser.class) {
      if (instance == null) {
        instance = new DoubleTypeParser();
      }
      return instance;
    }
  }

  DoubleTypeParser() { }

  @Override public void write(XmlSerializer writer, Double value) throws IOException {
    writer.startTag(null, Constants.DOUBLE);
    writer.text(String.valueOf(value));
    writer.endTag(null, Constants.DOUBLE);
  }

  @Override public Double read(XmlPullParser reader) throws XmlPullParserException, IOException {
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
        return Double.parseDouble(text);
      }
    } catch (Exception ignored) { }
    return Double.NaN;
  }

  @Override public boolean hasRead(XmlPullParser reader) {
    final String nodeName = reader.getName();
    return Constants.DOUBLE.equalsIgnoreCase(nodeName);
  }

  @Override public boolean hasWrite(Object o) {
    return o instanceof Double;
  }
}
