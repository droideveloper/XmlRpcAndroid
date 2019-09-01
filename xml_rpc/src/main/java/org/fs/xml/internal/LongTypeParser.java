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
import java.util.HashMap;
import java.util.Map;

class LongTypeParser implements TypeParser<Long> {

  private final static Map<String, TypeParser<Long>> cache = new HashMap<>();

  static TypeParser<Long> getInstance(String preferred) {
    synchronized (LongTypeParser.class) {
      if (!cache.containsKey(preferred)) {
        cache.put(preferred, new LongTypeParser(preferred));
      }
      return cache.get(preferred);
    }
  }

  private String preferred;

  LongTypeParser(String preferred) {
    this.preferred = preferred;
  }

  @Override public void write(XmlSerializer writer, Long value) throws IOException {
    writer.startTag(null, preferred);
    writer.text(String.valueOf(value));
    writer.endTag(null, preferred);
  }

  @Override public Long read(XmlPullParser reader) throws XmlPullParserException, IOException {
    int type = reader.getEventType();
    String text = null;
    while (type != XmlPullParser.END_TAG) {
      if (type == XmlPullParser.TEXT) {
        text = reader.getText();
      }
      type = reader.next();
    }
    //go to next START_TAG
    reader.next();
    try {
      if (text != null) {
        return Long.parseLong(text);
      }
    } catch (Exception ignored) {}
    return -1L;
  }

  @Override public boolean hasRead(XmlPullParser reader) {
    final String nodeName = reader.getName();
    return preferred.equalsIgnoreCase(nodeName);
  }

  @Override public boolean hasWrite(Object o) {
    return o instanceof Long;
  }
}
