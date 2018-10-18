package org.fs.xmlrpcclient;

import org.fs.xml.transform.XMLRpcMatcher;
import org.fs.xml.type.XMLRpcObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import java.io.StringWriter;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Fatih on 17/11/15.
 * as org.fs.xmlrpcclient.ObjectSerializationTest
 */
public class XMLRpcObjectSerializationTest {

    private Serializer persist;

    @Before
    public void start() {
        persist = new Persister(new XMLRpcMatcher(),
                                new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>"));
    }

    @After
    public void finish() {
        persist = null;
    }

    @Test
    public void testObjectWithString() throws Exception {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                          "<value>" +
                            "<string>" + "text" + "</string>" +
                          "</value>";

        testAny(expected, XMLRpcObject.withValue("text"));
    }

    @Test
    public void testObjectWithInteger() throws Exception {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<value>" +
                    "<int>" + "1" + "</int>" +
                "</value>";

        testAny(expected, XMLRpcObject.withValue(1));
    }

    @Test
    public void testObjectWithLong() throws Exception {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<value>" +
                "<long>" + "1" + "</long>" +
                "</value>";

        testAny(expected, XMLRpcObject.withValue(1L));
    }

    @Test
    public void testObjectWithBoolean() throws Exception {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<value>" +
                "<boolean>" + "1" + "</boolean>" +
                "</value>";
        testAny(expected, XMLRpcObject.withValue(true));
    }


    @Test
    public void testObjectWithDate() throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        Date date = new Date();

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<value>" +
                "<dateTime.iso8601>" + formatter.format(date) + "</dateTime.iso8601>" +
                "</value>";

        testAny(expected, XMLRpcObject.withValue(date));
    }

    void testAny(String expected, XMLRpcObject value) throws Exception {
        StringWriter srtWriter = new StringWriter();
        persist.write(value, srtWriter);
        Assert.assertEquals(escapeWhiteSpace(expected), escapeWhiteSpace(srtWriter.toString()));
    }

    //no white space here
    static String escapeWhiteSpace(String str) {
        return str.replaceAll("\\s+", "");
    }
}
