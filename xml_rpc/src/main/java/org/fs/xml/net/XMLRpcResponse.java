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
package org.fs.xml.net;

import org.fs.xml.type.Parameter;

import java.util.ArrayList;
import java.util.Collection;

public final class XMLRpcResponse {

  private Object fault;
  private Collection<Parameter> parameters;

  public static XMLRpcResponse create() {
    return new XMLRpcResponse();
  }

  private XMLRpcResponse() { }

  public void addParameter(Parameter parameter) {
    if (parameters == null) {
      parameters = new ArrayList<>();
    }
    parameters.add(parameter);
  }

  public void addFault(Object fault) {
    this.fault = fault;
  }

  public boolean isSuccess() {
    return fault == null;
  }

  public Collection<Parameter> response() {
    return parameters;
  }

  public Object fault() {
    return fault;
  }
}
