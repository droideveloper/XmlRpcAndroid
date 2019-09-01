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

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class LegacyXMLConverterFactory extends Converter.Factory {

  private final static String ISO_DATE = "yyyyMMdd'T'HH:mm:ss";
  private final static String GMT_TIMEZONE = "GMT";

  private Parsers parser;
  private Converter<?, RequestBody> requestBodyConverter;
  private Converter<ResponseBody, ?> responseBodyConverter;

  public static LegacyXMLConverterFactory create() {
    return new LegacyXMLConverterFactory();
  }

  public static LegacyXMLConverterFactory create(Parsers parser) {
    final LegacyXMLConverterFactory factory = new LegacyXMLConverterFactory();
    factory.setParser(parser);
    return factory;
  }

  /**
   * Defaults initialized by api
   */
  private LegacyXMLConverterFactory() {
    parser = Parsers.getInstance();
    parser.registerBooleanConverter(true); // <boolean>1</boolean>
    parser.registerStringConverter(false); // wrapped <string>text</string>
    parser.registerDateConverter(ISO_DATE, Locale.getDefault(), TimeZone.getTimeZone(GMT_TIMEZONE));

    requestBodyConverter = LegacyXMLRequestBodyConverter.create(parser);
    responseBodyConverter = LegacyXMLResponseBodyConverter.create(parser);
  }

  public void setParser(Parsers parser) {
    this.parser = parser;
    // parser can not be null
    if (parser == null) {
      throw new IllegalArgumentException("parser can not be null");
    }
    // re init
    requestBodyConverter = LegacyXMLRequestBodyConverter.create(parser);
    responseBodyConverter = LegacyXMLResponseBodyConverter.create(parser);
  }

  @Override public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    if (!(type instanceof Class)) return null;
    return requestBodyConverter;
  }

  @Override public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    if (!(type instanceof Class)) return null;
    return responseBodyConverter;
  }
}
