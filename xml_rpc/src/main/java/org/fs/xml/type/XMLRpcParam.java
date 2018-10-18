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
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/* @hide */
@Root(name = TypeDefinitions.KEY_PARAM)
public class XMLRpcParam {

  @Element(required = false) private XMLRpcObject value;

  public XMLRpcParam() {
  }

  public XMLRpcParam(XMLRpcObject value) {
    this.value = value;
  }

  public XMLRpcObject getValue() {
    return value;
  }

  public void setValue(XMLRpcObject value) {
    this.value = value;
  }

  /**
   * Create Param object from Object instnace
   */
  public static XMLRpcParam withObject(XMLRpcObject obj) {
    return new XMLRpcParam(obj);
  }

  /**
   *
   * @param objs
   * @return
   */
  public static List<XMLRpcParam> withObjects(XMLRpcObject... objs) {
    return withObjects(Arrays.asList(objs));
  }

  /**
   *
   * @param objs
   * @return
   */
  public static List<XMLRpcParam> withObjects(List<XMLRpcObject> objs) {
    List<XMLRpcParam> XMLRpcParams = new ArrayList<>();
    for (XMLRpcObject obj : objs) {
      XMLRpcParams.add(withObject(obj));
    }
    return XMLRpcParams;
  }

  @Override public String toString() {
    return String.format(Locale.US, "\n@Param { \n%s\n }", value.toString());
  }
}
