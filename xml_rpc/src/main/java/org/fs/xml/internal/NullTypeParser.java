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

class NullTypeParser implements TypeParser<Object> {

  public static NullTypeParser create() {
    return new NullTypeParser();
  }

  private NullTypeParser() {
  }

  @Override public void write(XmlSerializer writer, Object value) throws IOException {
    writer.startTag(null, Constants.NIL);
    writer.endTag(null, Constants.NIL);
  }

  @Override public Object read(XmlPullParser reader) throws XmlPullParserException, IOException {
    //fact is <nil /> fits both START_TAG and END_TAG events so calling twice needed
        /*<nil /> START_TAG*/
    reader.next();
        /*<nil /> END_TAG*/
    reader.next(); //go on next START_TAG
    return null;
  }

  @Override public boolean hasRead(XmlPullParser reader) {
    final String nodeName = reader.getName();
    return Constants.NIL.equalsIgnoreCase(nodeName);
  }

  @Override public boolean hasWrite(Object o) {
    return o == null;
  }
}
