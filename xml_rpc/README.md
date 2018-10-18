# XmlRpcAndroid
XmlRpcAndroid re-styled for android since SimpleXML library mainly uses reflection now XmlPullParser and XmlSerializer used for serialization and deserialization
processes this improves process less trouble at all and naming values not required the way before.

Code Samples;

if you are using retrofit
```java
Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("YOUR_ENDPOINT_URL")    
                                .addConverterFactory(LegacyConverterFactory.create())//or send parser with #create(Parser)
                                ....
                                .build();
                                
                                
interface MyServiceEndpoint {
    
    @POST
    Call<XMLRpcResponse> method(@Body XMLRpcRequest request);
}                                
```

in order to achieve this request
```xml
<?xml version="1.0" encoding="utf-8"?>
<methodCall>
   <methodName>SampleMethodName</methodName>
   <params>
      <param>
         <value>
            <string>stringParam</string>
         </value>
      </param>
   </params>
</methodCall>
```

follow in java code
```java
XMLRpcRequest request = XMLRpcRequest.create("SampleMethodName")
                                     .addParameter(Parameter.create("StringParam"));
                
Parser parser = new Parser(); //create new Parser
parser.addBooleanConverter(true); //set style of boolean true means <code>1 or 0</code> false means <code>true or false</code>
parser.addStringConverter(false); //set string style wrap with <string> tag or not
parser.addDateConverter("yyyy-MM-dd'T'HH:mm:ss", //this provides advenced level of date read and write
                        Locale.getDefault(),
                        TimeZone.getTimeZone("GMT"));
                        
parser.write(OutputStreamWriter, requst, "utf-8"); 
OutputStreamWriter#flush(); //flush outputStreamWriter                        
```

another example with different types
```xml
<?xml version='1.0' encoding='utf-8' ?>
<methodCall>
  <methodName>SampleMethodName</methodName>
  <params>
    <param>
      <value>
        <string>param1</string>
      </value>
    </param>
    <param>
      <value>
        <double>1.0</double>
      </value>
    </param>
    <param>
      <value>
        <boolean>1</boolean>
      </value>
    </param>
    <param>
      <value>
        <dateTime.iso8601>2016-07-12T08:27:52</dateTime.iso8601>
      </value>
    </param>
    <param>
      <value>
        <i4>1</i4>
      </value>
    </param>
  </params>
</methodCall>
```

follow in java code
```java
XMLRpcRequest request = XMLRpcRequest.create("SampleMethodName")
                                     .addParameter(Parameter.create("param1"))
                                     .addParameter(Parameter.create(1d))
                                     .addParameter(Parameter.create(true))
                                     .addParameter(Parameter.create(new java.util.Date()))
                                     .addParameter(Parameter.create(1));
   
Parser parser = new Parser(); //create new Parser
parser.addBooleanConverter(true); //set style of boolean true means <code>1 or 0</code> false means <code>true or false</code>
parser.addStringConverter(false); //set string style wrap with <string> tag or not
parser.addDateConverter("yyyy-MM-dd'T'HH:mm:ss", //this provides advenced level of date read and write
                        Locale.getDefault(),
                        TimeZone.getTimeZone("GMT"));
                        
parser.write(OutputStreamWriter, requst, "utf-8"); 
OutputStreamWriter#flush(); //flush outputStreamWriter   
```
