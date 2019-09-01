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

import org.fs.xml.util.C;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class StructTypeParser implements TypeParser<Map<String, ?>> {

  private final static String MEMBER = "member";
  private final static String NAME = "name";
  //for this we gone hijack StringTypeConverter internally, yahoo
  private final static String VALUE = "value";

  private static TypeParser<Map<String, ?>> instance;

  static TypeParser<Map<String, ?>> getInstance() {
    synchronized (StringTypeParser.class) {
      if (instance == null) {
        instance = new StructTypeParser();
      }
      return instance;
    }
  }

  private final Parsers parsers;
  private final TypeParser<String> stringTypeParser = new StringTypeParser(C.STRING_WRAP_STYLE);

  StructTypeParser() {
    parsers = Parsers.getInstance();
  }

  @SuppressWarnings("unchecked") @Override public void write(XmlSerializer writer, Map<String, ?> value) throws IOException {
    writer.startTag(null, Constants.STRUCT);
    for (Map.Entry<String, ?> entry : value.entrySet()) {
      writer.startTag(null, MEMBER);
      //write key
      writer.startTag(null, NAME);
      writer.text(entry.getKey());
      writer.endTag(null, NAME);
      //writer value
      writer.startTag(null, VALUE);
      Object o = entry.getValue();
      TypeParser converter = parsers.resolveWrite(o);
      converter.write(writer, o);
      writer.endTag(null, VALUE);

      writer.endTag(null, MEMBER);
    }
    writer.endTag(null, Constants.STRUCT);
  }

  @Override public Map<String, ?> read(XmlPullParser reader)
      throws XmlPullParserException, IOException {
    Map<String, Object> map = new HashMap<>();
    String key = null;
    int type = reader.getEventType();
    while (true) {
      if (type == XmlPullParser.START_TAG) {
        String text = reader.getName();
        boolean ignore = Constants.STRUCT.equalsIgnoreCase(text) || MEMBER.equalsIgnoreCase(text) || VALUE.equalsIgnoreCase(text);
        if (!ignore) {
          if (NAME.equalsIgnoreCase(text)) {
            key = stringTypeParser.read(reader);//we read key
            continue;//internal next called
          } else if (text != null) {//I don't know why we getting name null
            TypeParser converter = parsers.resolveRead(reader);
            Object object = converter.read(reader);
            if (key != null) {
              map.put(key, object);
            }
            key = null;//change it just in case
            continue;//internal next called
          }
        }
      } else if (type == XmlPullParser.END_TAG) {
        String text = reader.getName();
        if (Constants.STRUCT.equalsIgnoreCase(text)) {
          //go on next START_TAG
          reader.next();
          break;
        }
      }
      type = reader.next();
    }
    return map;
  }

  @Override public boolean hasRead(XmlPullParser reader) {
    final String nodeName = reader.getName();
    return Constants.STRUCT.equalsIgnoreCase(nodeName);
  }

  @Override public boolean hasWrite(Object o) {
    return o instanceof Map && !((Map) o).isEmpty();
  }
}
