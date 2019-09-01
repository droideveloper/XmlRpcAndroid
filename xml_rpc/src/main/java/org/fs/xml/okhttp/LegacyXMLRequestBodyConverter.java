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
package org.fs.xml.okhttp;

import org.fs.xml.internal.Parsers;
import org.fs.xml.net.XMLRpcRequest;

import java.io.IOException;
import java.io.OutputStreamWriter;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

public class LegacyXMLRequestBodyConverter implements Converter<XMLRpcRequest, RequestBody> {

  private final static MediaType MEDIA_TYPE = MediaType.parse("text/xml; charset=UTF-8");
  private final static String UTF_8 = "UTF-8";

  private final Parsers parser;

  public static LegacyXMLRequestBodyConverter create(final Parsers parser) {
    return new LegacyXMLRequestBodyConverter(parser);
  }

  private LegacyXMLRequestBodyConverter(final Parsers parser) {
    this.parser = parser;
  }

  @Override public RequestBody convert(XMLRpcRequest request) throws IOException {
    Buffer buffer = new Buffer();
    try {
      OutputStreamWriter out = new OutputStreamWriter(buffer.outputStream(), UTF_8);
      parser.write(out, request, UTF_8);
      out.flush();
    } catch (Exception actual) {
      throw new IOException(actual);
    }
    return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
  }
}
