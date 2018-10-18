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

import java.io.IOException;
import java.io.OutputStreamWriter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import org.simpleframework.xml.Serializer;
import retrofit2.Converter;

public class SimpleXmlRequestBodyConverter<T> implements Converter<T, RequestBody> {

  private final static MediaType MEDIA_TYPE = MediaType.parse("application/xml; charset=UTF-8");
  private final static String UTF_8 = "UTF-8";

  private final Serializer serializer;

  public SimpleXmlRequestBodyConverter(Serializer serializer) {
    this.serializer = serializer;
  }

  @Override public RequestBody convert(T value) throws IOException {
    Buffer buffer = new Buffer();
    try {
      OutputStreamWriter out = new OutputStreamWriter(buffer.outputStream(), UTF_8);
      serializer.write(value, out);
      out.flush();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
  }
}

