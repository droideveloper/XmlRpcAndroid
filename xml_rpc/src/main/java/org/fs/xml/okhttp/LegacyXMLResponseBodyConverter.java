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
import org.fs.xml.net.XMLRpcResponse;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class LegacyXMLResponseBodyConverter implements Converter<ResponseBody, XMLRpcResponse> {

  private static final String UTF_8 = "UTF-8";
  private final Parsers parser;

  public static LegacyXMLResponseBodyConverter create(Parsers parser) {
    return new LegacyXMLResponseBodyConverter(parser);
  }

  private LegacyXMLResponseBodyConverter(Parsers parser) {
    this.parser = parser;
  }

  @Override public XMLRpcResponse convert(ResponseBody response) throws IOException {
    try (InputStream in = response.byteStream()) {
      return parser.read(in, UTF_8);
    }
  }
}
