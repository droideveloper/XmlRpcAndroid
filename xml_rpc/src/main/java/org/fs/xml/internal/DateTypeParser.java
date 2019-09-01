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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.fs.xml.internal.Constants.GMT_TIMEZONE;
import static org.fs.xml.internal.Constants.ISO_DATE;

class DateTypeParser implements TypeParser<Date> {

  public static DateTypeParser create(String str, Locale locale, TimeZone zone) {
    return new DateTypeParser(str, locale, zone);
  }

  private static DateTypeParser instance;

  static DateTypeParser getInstance() {
    return getInstance(ISO_DATE);
  }

  static DateTypeParser getInstance(String objFormat) {
    return getInstance(objFormat, Locale.getDefault(), TimeZone.getTimeZone(GMT_TIMEZONE));
  }

  static DateTypeParser getInstance(String objFormat, Locale locale) {
    return getInstance(objFormat, locale, TimeZone.getTimeZone(GMT_TIMEZONE));
  }

  static DateTypeParser getInstance(String objFormat, Locale locale, TimeZone zone) {
    synchronized (DateTypeParser.class) {
      if (instance == null) {
        instance = new DateTypeParser(objFormat, locale, zone);
      }
      instance.setObjParser(objFormat, locale, zone);
      return instance;
    }
  }

  private SimpleDateFormat objParser;

  DateTypeParser(String objFormat, Locale locale, TimeZone zone) {
    this.objParser = new SimpleDateFormat(objFormat, locale);
    this.objParser.setTimeZone(zone);
  }

  void setObjParser(String objFormat, Locale locale, TimeZone zone) {
    this.objParser = new SimpleDateFormat(objFormat, locale);
    this.objParser.setTimeZone(zone);
  }

  @Override public void write(XmlSerializer writer, Date value) throws IOException {
    writer.startTag(null, Constants.DATE);
    writer.text(objParser.format(value));
    writer.endTag(null, Constants.DATE);
  }

  @Override public Date read(XmlPullParser reader) throws XmlPullParserException, IOException {
    int type = reader.getEventType();
    String text = null;
    while (type != XmlPullParser.END_TAG) {
      if (type == XmlPullParser.TEXT) {
        text = reader.getText();
      }
      type = reader.next();
    }
    //go on next START_TAG
    reader.next();
    try {
      return objParser.parse(text);
    } catch (ParseException e) {
      //wrap it with IOException, yahoo
      throw new IOException(e);
    }
  }

  @Override public boolean hasRead(XmlPullParser reader) {
    final String nodeName = reader.getName();
    return Constants.DATE.equalsIgnoreCase(nodeName);
  }

  @Override public boolean hasWrite(Object o) {
    return o instanceof Date;
  }
}
