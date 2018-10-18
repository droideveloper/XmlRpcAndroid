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

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* @hide */
@Root(name = TypeDefinitions.TYPE_ARRAY)
public class XMLRpcArray {

  @Path(TypeDefinitions.PATH_DATA) @ElementList(inline = true) private List<XMLRpcObject> values;

  public XMLRpcArray() {
  }

  public XMLRpcArray(List<XMLRpcObject> values) {
    this.values = values;
  }

  public List<XMLRpcObject> getValues() {
    return values;
  }

  public void setValues(List<XMLRpcObject> values) {
    this.values = values;
  }

  @Override public String toString() {
    StringBuilder b = new StringBuilder();
    b.append("@Array {\n");
    for (XMLRpcObject o : values) {
      b.append(String.format(Locale.US, "@Element { value=%s }\n", o.toString()));
    }
    return b.append("\n}").toString();
  }

  public XMLRpcArray add(XMLRpcObject value) {
    if (values != null) {
      values.add(value);
    } else {
      return withEmpty().add(value);
    }
    return this;
  }

  public XMLRpcArray withEmpty() {
    if (values == null) {
      values = new ArrayList<>();
    } else {
      values.clear();
    }
    return this;
  }
}
