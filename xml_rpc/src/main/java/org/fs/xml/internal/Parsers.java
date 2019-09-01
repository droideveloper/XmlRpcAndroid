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
import org.fs.xml.util.C;
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

import static org.fs.xml.internal.Constants.INTEGER_V1;
import static org.fs.xml.internal.Constants.INTEGER_V2;
import static org.fs.xml.internal.Constants.INTEGER_V3;
import static org.fs.xml.internal.Constants.LONG_V1;
import static org.fs.xml.internal.Constants.LONG_V2;
import static org.fs.xml.internal.Constants.XML_SCHEMA;
import static org.fs.xml.util.C.BOOLEAN_BINARY_STYLE;
import static org.fs.xml.util.C.BOOLEAN_STRING_STYLE;
import static org.fs.xml.util.C.STRING_NO_WRAP_STYLE;
import static org.fs.xml.util.C.STRING_WRAP_STYLE;

public final class Parsers {

  private static Parsers instance;

  public static Parsers getInstance() {
    synchronized (Parsers.class) {
      if (instance == null) {
        instance = new Parsers();
      }
      return instance;
    }
  }

  private final List<TypeParser<?>> converters;
  private Boolean prettyPrintEnabled;

  /**
   * <p>Constructor</p>
   */
  private Parsers() {
    converters = new ArrayList<>();
    converters.add(Base64StringTypeParser.getInstance());
    converters.add(IntegerTypeParser.getInstance(INTEGER_V1));
    converters.add(IntegerTypeParser.getInstance(INTEGER_V2));
    converters.add(IntegerTypeParser.getInstance(INTEGER_V3));
    converters.add(LongTypeParser.getInstance(LONG_V1));
    converters.add(LongTypeParser.getInstance(LONG_V2));
    converters.add(CollectionTypeParser.getInstance());
    converters.add(StructTypeParser.getInstance());
    converters.add(DoubleTypeParser.getInstance());
    converters.add(NullTypeParser.getInstance());
    converters.add(FloatTypeParser.getInstance());
    converters.add(XMLRpcRequestTypeParser.getInstance()); // request
    converters.add(XMLRpcResponseTypeParser.getInstance()); // response
  }

  /**
   * set whether it is allowed to print pretty xml or not
   * @param prettyPrintEnabled param to enable pretty print on output xml file
   */
  public void setPrettyPrintEnabled(Boolean prettyPrintEnabled) {
    this.prettyPrintEnabled = prettyPrintEnabled;
  }

  /**
   * if pretty printing enabled we do print it that way
   * @return default is false
   */
  public Boolean getPrettyPrintEnabled() {
    return prettyPrintEnabled;
  }

  /**
   * <p>Adds String converter with style of a) plain b) wrapped</p>
   *
   * @param plain true if plain string read/write action
   */
  public void registerStringConverter(boolean plain) {
    if (plain) {
      converters.add(StringTypeParser.getInstance(STRING_NO_WRAP_STYLE));
    } else {
      converters.add(StringTypeParser.getInstance(STRING_WRAP_STYLE));
    }
  }

  /**
   * <p>Adds Boolean converter with style of a) binary b) string</p>
   *
   * @param binary true if binary boolean read/write action
   */
  public void registerBooleanConverter(boolean binary) {
    if (binary) {
      converters.add(BooleanTypeParser.getInstance(BOOLEAN_BINARY_STYLE));
    } else {
      converters.add(BooleanTypeParser.getInstance(BOOLEAN_STRING_STYLE));
    }
  }

  /**
   * <p>Date converter default instance provided as "yyyyMMdd'T'HH:mm:ss", Locale#getDefault() and
   * TimeZone#getTimeZone(String)</p>
   */
  public void registerDateConverter() {
    converters.add(DateTypeParser.getInstance());
  }

  /**
   * <p>Date converter default instance provided as Locale#getDefault() and
   * TimeZone#getTimeZone(String)</p>
   *
   * @param formatStr dateFormat String
   */
  public void registerDateConverter(String formatStr) {
    converters.add(DateTypeParser.getInstance(formatStr));
  }

  /**
   * <p>Date converter default instance provided as TimeZone#getTimeZone(String)</p>
   *
   * @param formatStr dateFormat String
   * @param locale Locale instance
   */
  public void registerDateConverter(String formatStr, Locale locale) {
    converters.add(DateTypeParser.getInstance(formatStr, locale));
  }

  /**
   * <p>Date converter default instance provided</p>
   *
   * @param formatStr dateFormat String
   * @param locale Locale instance
   * @param timeZone TimeZone instance
   */
  public void registerDateConverter(String formatStr, Locale locale, TimeZone timeZone) {
    converters.add(DateTypeParser.getInstance(formatStr, locale, timeZone));
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
      xmlWriter.setFeature(XML_SCHEMA, prettyPrintEnabled);//pretty xml
      TypeParser converter = resolveWrite(request);
      //there is nothing else coming here
      if (converter instanceof XMLRpcRequestTypeParser) {
        XMLRpcRequestTypeParser requestParser = (XMLRpcRequestTypeParser) converter;
        requestParser.write(xmlWriter, request);
      }
      xmlWriter.endDocument(); // don't forget to end document
      xmlWriter.flush(); // don't forget to flush
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
      xmlReader.next(); // start gone
      TypeParser converter = resolveRead(xmlReader);
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
  TypeParser resolveRead(XmlPullParser reader) {
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
  TypeParser resolveWrite(Object object) {
    for (TypeParser converter : converters) {
      if (converter.hasWrite(object)) {
        return converter;
      }
    }
    throw new RuntimeException("no writer found for @{ " + object.toString() + " }");
  }
}
