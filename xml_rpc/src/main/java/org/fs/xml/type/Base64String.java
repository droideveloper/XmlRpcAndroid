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

import android.text.TextUtils;
import android.util.Base64;

import java.nio.charset.Charset;

public final class Base64String {

  private final String str;

  public Base64String(String str) {
    this.str = str;
    if (TextUtils.isEmpty(str)) {
      throw new IllegalArgumentException("string is null");
    }
  }

  public String decode() {
    return decode("UTF-8", Base64.NO_WRAP);
  }

  public String decode(String enc, int flags) {
    byte[] buffer = str.getBytes(Charset.forName(enc));
    buffer = Base64.decode(buffer, flags);
    return new String(buffer, Charset.forName(enc));
  }

  public String encode() {
    return encode("UTF-8", Base64.NO_WRAP);
  }

  public String encode(String enc, int flags) {
    byte[] buffer = str.getBytes(Charset.forName(enc));
    buffer = Base64.encode(buffer, flags);
    return new String(buffer, Charset.forName(enc));
  }
}
