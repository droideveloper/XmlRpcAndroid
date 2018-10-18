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

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

import java.util.Date;

/* @hide */
public class XMLRpcMatcher implements Matcher {

  private final String formatString;

  public XMLRpcMatcher() {
    this(null);
  }

  public XMLRpcMatcher(String formatString) {
    this.formatString = formatString;
  }

  @Override public Transform match(Class type) throws Exception {
    if (Boolean.class.equals(type) || boolean.class.equals(type)) {
      return new BooleanTransform();
    } else if (Date.class.equals(type)) {
      if (null != formatString) return new DateTransform(formatString);
      return new DateTransform();
    }
    return null;
  }
}
