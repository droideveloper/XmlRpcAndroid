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

import org.fs.xml.net.XMLRpcRequest;
import org.fs.xml.net.XMLRpcResponse;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public final class Parser {

  private final static List<TypeParser<?>> converters;

  static {
    converters = new ArrayList<>();
    converters.add(Base64StringTypeParser.create());
    converters.add(IntegerTypeParser.create(Constants.INTEGER_V1));
    converters.add(IntegerTypeParser.create(Constants.INTEGER_V2));
    converters.add(IntegerTypeParser.create(Constants.INTEGER_V3));
    converters.add(LongTypeParser.create(Constants.LONG_V1));
    converters.add(LongTypeParser.create(Constants.LONG_V2));
    converters.add(CollectionTypeParser.create());
    converters.add(StructTypeParser.create());
    converters.add(DoubleTypeParser.create());
    converters.add(NullTypeParser.create());
    converters.add(FloatTypeParser.create());
    converters.add(XMLRpcRequestTypeParser.create());//request
    converters.add(XMLRpcResponseTypeParser.create());//response
  }

  /**
   * <p>Constructor</p>
   */
  public Parser() {
  }

  /**
   * <p>Adds String converter with style of a) plain b) wrapped</p>
   *
   * @param plain true if plain string read/write action
   */
  public void addStringConverter(boolean plain) {
    if (plain) {
      converters.add(StringTypeParser.create(StringTypeParser.STYLE_NO_WRAP));
    } else {
      converters.add(StringTypeParser.create(StringTypeParser.STYLE_WRAP));
    }
  }

  /**
   * <p>Adds Boolean converter with style of a) binary b) string</p>
   *
   * @param binary true if binary boolean read/write action
   */
  public void addBooleanConverter(boolean binary) {
    if (binary) {
      converters.add(BooleanTypeParser.create(BooleanTypeParser.STYLE_BINARY));
    } else {
      converters.add(BooleanTypeParser.create(BooleanTypeParser.STYLE_STRING));
    }
  }

  /**
   * <p>Date converter default instance provided as "yyyyMMdd'T'HH:mm:ss", Locale#getDefault() and
   * TimeZone#getTimeZone(String)</p>
   */
  public void addDateConverter() {
    converters.add(DateTypeParser.create());
  }

  /**
   * <p>Date converter default instance provided as Locale#getDefault() and
   * TimeZone#getTimeZone(String)</p>
   *
   * @param formatStr dateFormat String
   */
  public void addDateConverter(String formatStr) {
    converters.add(DateTypeParser.create(formatStr));
  }

  /**
   * <p>Date converter default instance provided as TimeZone#getTimeZone(String)</p>
   *
   * @param formatStr dateFormat String
   * @param locale Locale instance
   */
  public void addDateConverter(String formatStr, Locale locale) {
    converters.add(DateTypeParser.create(formatStr, locale));
  }

  /**
   * <p>Date converter default instance provided</p>
   *
   * @param formatStr dateFormat String
   * @param locale Locale instance
   * @param timeZone TimeZone instance
   */
  public void addDateConverter(String formatStr, Locale locale, TimeZone timeZone) {
    converters.add(DateTypeParser.create(formatStr, locale, timeZone));
  }

  /**
   * <p>Writes XMLRpcRequest object instance into OutputStreamWriter instance with given
   * encoding</p>
   *
   * @param writer OutputStreamWriter instance
   * @param request XMLRpcRequest instance
   * @param charSet String representation of charset
   * @throws IOException if IO error occurs
   */
  public void write(OutputStreamWriter writer, XMLRpcRequest request, String charSet)
      throws IOException {
    try {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      XmlSerializer xmlWriter = factory.newSerializer();
      xmlWriter.setOutput(writer);
      xmlWriter.startDocument(charSet, null);//no standalone should be written
      xmlWriter.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);//pretty xml
      TypeParser converter = findWriteParser(request);
      //there is nothing else coming here
      if (converter instanceof XMLRpcRequestTypeParser) {
        XMLRpcRequestTypeParser requestParser = (XMLRpcRequestTypeParser) converter;
        requestParser.write(xmlWriter, request);
      }
      xmlWriter.endDocument();//don't forget to end document
      xmlWriter.flush();//don't forget to flush
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  /**
   * <p>Reads InputStream into xml then parses it into XMLRpcResponse instance for given
   * charset</p>
   *
   * @param in InputStream instance mainly coming from http/s requests
   * @param charSet charset of stream
   * @return XMLRpcResponse instance
   * @throws IOException if IO error occurs
   */
  public XMLRpcResponse read(InputStream in, String charSet) throws IOException {
    try {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      XmlPullParser xmlReader = factory.newPullParser();
      xmlReader.setInput(in, charSet);
      xmlReader.next();//start gone
      TypeParser converter = findReadParser(xmlReader);
      Object o = converter.read(xmlReader);
      return (XMLRpcResponse) o;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  /**
   * <p>Helper method that serialization/deserialization concept of api, reader finder</p>
   *
   * @param reader XmlPullParser reader instance for reading
   * @return TypeParser instance that is fit to be for next read
   * @throws RuntimeException if no typeParser instance found for this reader position
   */
  static TypeParser findReadParser(XmlPullParser reader) {
    for (TypeParser converter : converters) {
      if (converter.hasRead(reader)) {
        return converter;
      }
    }
    throw new RuntimeException("no reader found for @{ " + reader.getName() + " }");
  }

  /**
   * <p>Helper method that serialization/deserialization concept of api, writer finder</p>
   *
   * @param object Object instance that will be required to be written
   * @return TypeParser instance that is fit to be for next write
   * @throws RuntimeException if this object can not be handled by our registered writers
   */
  static TypeParser findWriteParser(Object object) {
    for (TypeParser converter : converters) {
      if (converter.hasWrite(object)) {
        return converter;
      }
    }
    throw new RuntimeException("no writer found for @{ " + object.toString() + " }");
  }
}
