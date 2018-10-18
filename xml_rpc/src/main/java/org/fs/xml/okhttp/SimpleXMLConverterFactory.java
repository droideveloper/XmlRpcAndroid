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

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.fs.xml.transform.XMLRpcMatcher;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import retrofit2.Converter;
import retrofit2.Retrofit;


public class SimpleXMLConverterFactory extends Converter.Factory {

  public static SimpleXMLConverterFactory create() {
    return create(new Persister(new XMLRpcMatcher(), new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>")));
  }

  public static SimpleXMLConverterFactory create(Serializer serializer) {
    return new SimpleXMLConverterFactory(serializer, true);
  }

  public static SimpleXMLConverterFactory createNonStrict() {
    return create(new Persister(new XMLRpcMatcher(), new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>")));
  }

  public static SimpleXMLConverterFactory createNonStrict(Serializer serializer) {
    return new SimpleXMLConverterFactory(serializer, false);
  }

  private final Serializer serializer;
  private final boolean strict;

  private SimpleXMLConverterFactory(Serializer serializer, boolean strict) {
    this.serializer = serializer;
    this.strict = strict;
  }

  public boolean isStrict() {
    return strict;
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    if (!(type instanceof Class)) return null;
    return new SimpleXmlResponseBodyConverter<>((Class<?>) type, serializer, strict);
  }

  @Override public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    if (!(type instanceof Class)) return null;
    return new SimpleXmlRequestBodyConverter<>(serializer);
  }
}
