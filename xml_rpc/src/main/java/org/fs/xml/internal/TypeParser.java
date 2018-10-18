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

interface TypeParser<V> {

  /**
   * <p>Writes V type of object to xml</p>
   *
   * @param writer Writer instance to write objects into xml
   * @param value V type of value to be written
   * @throws IOException if IO error occurs
   */
  void write(XmlSerializer writer, V value) throws IOException;

  /**
   * <p>Reads V type of object from xml</p>
   *
   * @param reader XmlPullParser instance for read
   * @return V instance
   * @throws XmlPullParserException if illegal read action occurs
   * @throws IOException if IO error occurs
   */
  V read(XmlPullParser reader) throws XmlPullParserException, IOException;

  /**
   * <p>Checks whether reader can be read from this TypeParser or not</p>
   *
   * @param reader XmlPullParser instance that holds xml data
   * @return true if we can read this TypeParser false otherwise
   */
  boolean hasRead(XmlPullParser reader);

  /**
   * <p>Checks whether object can be written by this TypeParser instance or not</p>
   *
   * @param o Object that will be written
   * @return true if this parser fits it to write false otherwise
   */
  boolean hasWrite(Object o);
}
