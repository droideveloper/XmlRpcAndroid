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
import org.fs.xml.net.XMLRpcRequest;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Collection;

class XMLRpcRequestTypeParser implements TypeParser<XMLRpcRequest> {

  private final static String METHOD_NAME = "methodName";
  private final static String PARAMS = "params";
  private final static String PARAM = "param";
  private final static String VALUE = "value";

  public static XMLRpcRequestTypeParser create() {
    return new XMLRpcRequestTypeParser();
  }

  private XMLRpcRequestTypeParser() {
  }

  @SuppressWarnings("unchecked") @Override public void write(XmlSerializer writer, XMLRpcRequest value) throws IOException {
    writer.startTag(null, Constants.REQUEST);
    //methodName
    writer.startTag(null, METHOD_NAME);
    writer.text(value.methodName());
    writer.endTag(null, METHOD_NAME);
    //params
    Collection<Parameter> parameters = value.parameters();
    writer.startTag(null, PARAMS);
    for (Parameter param : parameters) {
      writer.startTag(null, PARAM);
      writer.startTag(null, VALUE);
      TypeParser converter = Parser.findWriteParser(param.asNil());
      converter.write(writer, param.asNil());//unchecked says yet it's the fitting one for write
      writer.endTag(null, VALUE);
      writer.endTag(null, PARAM);
    }
    writer.endTag(null, PARAMS);
    writer.endTag(null, Constants.REQUEST);
  }

  @Override public XMLRpcRequest read(XmlPullParser reader)
      throws XmlPullParserException, IOException {
    XMLRpcRequest request = null;
    int type = reader.getEventType();
    while (true) {
      if (type == XmlPullParser.START_TAG) {
        String text = reader.getName();
        boolean ignored = Constants.REQUEST.equalsIgnoreCase(text)
            || PARAMS.equalsIgnoreCase(text)
            || PARAM.equalsIgnoreCase(text)
            || VALUE.equalsIgnoreCase(text);
        if (!ignored) {
          if (METHOD_NAME.equalsIgnoreCase(text)) {
            type = reader.next();
            if (type == XmlPullParser.TEXT) {
              request = XMLRpcRequest.create(reader.getText());
              continue;
            }
          } else if (text != null) {
            TypeParser converter = Parser.findReadParser(reader);
            Object object = converter.read(reader);
            if (request != null) {
              request.addParameter(Parameter.create(object));
            }
            continue;
          }
        }
      } else if (type == XmlPullParser.END_TAG) {
        String text = reader.getName();
        if (Constants.REQUEST.equalsIgnoreCase(text)) {
          reader.next();
          break;
        }
      }
      type = reader.next();
    }
    return request;
  }

  @Override public boolean hasRead(XmlPullParser reader) {
    final String nodeName = reader.getName();
    return Constants.REQUEST.equalsIgnoreCase(nodeName);
  }

  @Override public boolean hasWrite(Object o) {
    return o instanceof XMLRpcRequest;
  }
}
