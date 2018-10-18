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

/** @hide */
public class TypeDefinitions {

  /**
   * primitive type definitions
   */
  public final static String TYPE_STRING = "string";
  public final static String TYPE_DOUBLE = "double";
  public final static String TYPE_INTEGER = "int";
  public final static String TYPE_INTEGER_V2 = "i4";
  public final static String TYPE_LONG = "long";
  public final static String TYPE_LONG_V2 = "i8";
  public final static String TYPE_BOOLEAN = "boolean";

  /**
   * object type definitions
   */
  public final static String TYPE_DATE = "dateTime.iso8601";
  public final static String TYPE_ARRAY = "array";
  public final static String TYPE_STRUCT = "struct";
  public final static String TYPE_BASE64 = "base64";

  /**
   * Definitions used in Serialization process
   */
  public final static String ENTRY_MAP = "member";
  public final static String KEY_MAP = "name";
  public final static String PATH_DATA = "data";
  public final static String KEY_VALUE = "value";
  public final static String KEY_PARAMS = "params";
  public final static String METHOD_CALL = "methodCall";
  public final static String METHOD_RESPONSE = "methodResponse";
  public final static String KEY_PARAM = "param";
}
