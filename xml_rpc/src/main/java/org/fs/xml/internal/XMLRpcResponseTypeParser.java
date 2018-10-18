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

import org.fs.xml.type.Parameter;
import org.fs.xml.net.XMLRpcResponse;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Collection;

class XMLRpcResponseTypeParser implements TypeParser<XMLRpcResponse> {

  private final static String PARAMS = "params";
  private final static String PARAM = "param";
  private final static String VALUE = "value";
  private final static String FAULT = "fault";

  public static XMLRpcResponseTypeParser create() {
    return new XMLRpcResponseTypeParser();
  }

  private XMLRpcResponseTypeParser() {
  }

  @SuppressWarnings("unchecked") @Override public void write(XmlSerializer writer, XMLRpcResponse value) throws IOException {
    writer.startTag(null, Constants.RESPONSE);
    if (value.isSuccess()) {
      writer.startTag(null, PARAMS);
      Collection<Parameter> params = value.response();
      for (Parameter param : params) {
        writer.startTag(null, PARAM);
        writer.startTag(null, VALUE);
        TypeParser converter = Parser.findWriteParser(param.asNil());
        converter.write(writer, param.asNil());//asNil method returns as object regardless if it's null or not
        writer.endTag(null, VALUE);
        writer.endTag(null, PARAM);
      }
      writer.endTag(null, PARAMS);
    } else {
      writer.startTag(null, FAULT);
      writer.startTag(null, VALUE);
      TypeParser converter = Parser.findWriteParser(value.fault());
      converter.write(writer, value.fault());
      writer.endTag(null, VALUE);
      writer.endTag(null, FAULT);
    }
    writer.endTag(null, Constants.RESPONSE);
  }

  @Override public XMLRpcResponse read(XmlPullParser reader)
      throws XmlPullParserException, IOException {
    XMLRpcResponse response = XMLRpcResponse.create();
    boolean isSuccess = false;
    int type = reader.getEventType();
    while (true) {
      if (type == XmlPullParser.START_TAG) {
        String text = reader.getName();
        boolean ignore = Constants.RESPONSE.equalsIgnoreCase(text)
            || PARAM.equalsIgnoreCase(text)
            || VALUE.equalsIgnoreCase(text);
        if (!ignore) {
          if (PARAMS.equalsIgnoreCase(text)) {
            isSuccess = true;
          } else if (FAULT.equalsIgnoreCase(text)) {
            isSuccess = false;
          } else if (text != null) {
            TypeParser converter = Parser.findReadParser(reader);
            Object o = converter.read(reader);
            if (isSuccess) {
              response.addParameter(Parameter.create(o));
            } else {
              response.addFault(o);
            }
            continue;//because sub-converter handle next
          }
        }
      } else if (type == XmlPullParser.END_TAG) {
        String text = reader.getName();
        if (Constants.RESPONSE.equalsIgnoreCase(text)) {
          reader.next();
          break;
        }
      }
      type = reader.next();
    }
    return response;
  }

  @Override public boolean hasRead(XmlPullParser reader) {
    final String nodeName = reader.getName();
    return Constants.RESPONSE.equalsIgnoreCase(nodeName);
  }

  @Override public boolean hasWrite(Object o) {
    return o instanceof XMLRpcResponse;
  }
}
