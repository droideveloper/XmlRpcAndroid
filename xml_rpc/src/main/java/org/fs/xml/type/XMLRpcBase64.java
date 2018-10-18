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
package org.fs.xml.type;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Text;

/* @hide */
@Element(name = TypeDefinitions.TYPE_BASE64)
public class XMLRpcBase64 {

  @Text private String value;

  public XMLRpcBase64() {
  }

  public XMLRpcBase64(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  /**
   * static method to encode string to base64 string
   *
   * @return encoded String
   * @throws Exception
   */
  public static String encode(String value) throws Exception {
    byte[] data = value.getBytes("UTF-8");
    return android.util.Base64.encodeToString(data, android.util.Base64.DEFAULT);
  }

  /**
   * static method to decode base64 string to string
   *
   * @return decoded string
   * @throws Exception
   */
  public static String decode(String value) throws Exception {
    byte[] data = android.util.Base64.decode(value, android.util.Base64.DEFAULT);
    return new String(data, "UTF-8");
  }
}
