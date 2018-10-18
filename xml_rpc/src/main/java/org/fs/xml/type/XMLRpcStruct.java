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

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* @hide */
@Root(name = TypeDefinitions.TYPE_STRUCT)
public class XMLRpcStruct {

  @ElementMap(entry = TypeDefinitions.ENTRY_MAP,
      key = TypeDefinitions.KEY_MAP,
      inline = true, required = false) private Map<String, XMLRpcObject> values;

  public XMLRpcStruct() { }

  public XMLRpcStruct(Map<String, XMLRpcObject> values) {
    this.values = values;
  }

  public void setValues(Map<String, XMLRpcObject> values) {
    this.values = values;
  }

  public Map<String, XMLRpcObject> getValues() {
    return values;
  }

  @Override public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("@Struct {\n");
    for (Map.Entry<String, XMLRpcObject> entry : values.entrySet()) {
      str.append(String.format(Locale.US, "@Entry { key=%s, value=%s }\n", entry.getKey(),
          entry.getValue().toString()));
    }
    str.append("\n }");
    return str.toString();
  }

  public XMLRpcStruct add(String key, XMLRpcObject value) {
    if (values != null) {
      values.put(key, value);
    } else {
      return withEmptyMap().add(key, value);
    }
    return this;
  }

  public XMLRpcObject getValue(String key) {
    if (values != null && key != null) {
      return values.get(key);
    } else {
      return null;
    }
  }

  public XMLRpcStruct withEmptyMap() {
    if (values != null) {
      values.clear();
    } else {
      values = new HashMap<>();
    }
    return this;
  }
}
