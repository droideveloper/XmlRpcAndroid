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

class IntegerTypeParser implements TypeParser<Integer> {

  private static Map<String, TypeParser<Integer>> cache = new HashMap<>();

  static TypeParser<Integer> getInstance(String preferred) {
    synchronized (IntegerTypeParser.class) {
      if (!cache.containsKey(preferred)) {
        cache.put(preferred, new IntegerTypeParser(preferred));
      }
      return cache.get(preferred);
    }
  }

  private String preferred;

  IntegerTypeParser(String preferred) {
    this.preferred = preferred;
  }

  @Override public void write(XmlSerializer writer, Integer value) throws IOException {
    writer.startTag(null, preferred);
    writer.text(String.valueOf(value));
    writer.endTag(null, preferred);
  }

  @Override public Integer read(XmlPullParser reader) throws XmlPullParserException, IOException {
    //we hit tag something like this
    //<i4> or <int> or <integer> then we need to call next
    int type = reader.getEventType();
    String text = null;
    while (type != XmlPullParser.END_TAG) {
      if (type == XmlPullParser.TEXT) {
        text = reader.getText();
      }
      type = reader.nextToken();
    }
    //not stay at end tag so
    reader.nextToken();
    try {
      if (text != null) {
        return Integer.parseInt(text);
      }
    } catch (Exception ignored) {}
    return -1;
  }

  @Override public boolean hasRead(XmlPullParser reader) {
    final String nodeName = reader.getName();
    return preferred.equalsIgnoreCase(nodeName);
  }

  @Override public boolean hasWrite(Object o) {
    return o instanceof Integer;
  }
}
