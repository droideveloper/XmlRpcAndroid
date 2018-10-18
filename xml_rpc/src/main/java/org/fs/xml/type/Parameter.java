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

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public final class Parameter {

  private final Object value;

  public static Parameter create(Object value) {
    return new Parameter(value);
  }

  private Parameter(Object value) {
    this.value = value;
  }

  public Base64String asBase64String() {
    return type(value);
  }

  public Boolean asBoolean() {
    return type(value);
  }

  public Collection asCollection() {
    return type(value);
  }

  public Date asDate() {
    return type(value);
  }

  public Double asDouble() {
    return type(value);
  }

  public Float asFloat() {
    return type(value);
  }

  public Integer asInteger() {
    return type(value);
  }

  public Long asLong() {
    return type(value);
  }

  public Object asNil() {
    return type(value);
  }

  public String asString() {
    return type(value);
  }

  public Map asMap() {
    return type(value);
  }

  @SuppressWarnings("unchecked") private static <C> C type(Object o) {
    return (C) o;
  }
}
