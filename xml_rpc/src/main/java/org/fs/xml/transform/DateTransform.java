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
package org.fs.xml.transform;

import org.simpleframework.xml.transform.Transform;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/* @hide */
public class DateTransform implements Transform<Date> {

  private final SimpleDateFormat simpleDateFormat;

  public DateTransform() {
    this("yyyyMMdd'T'HH:mm:ss");
  }

  public DateTransform(String format) {
    this(format, Locale.US);
  }

  public DateTransform(String format, Locale locale) {
    simpleDateFormat = new SimpleDateFormat(format, locale);
  }

  @Override public Date read(String source) throws Exception {
    return simpleDateFormat.parse(source);
  }

  @Override public String write(Date source) throws Exception {
    return simpleDateFormat.format(source);
  }
}
