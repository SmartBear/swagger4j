/**
 *  Copyright 2013 SmartBear Software, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.smartbear.swagger4j.impl;

import com.smartbear.swagger4j.SwaggerFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.json.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for abstraction of writing an actual format, since json and xml are read in the same way
 */

public abstract class SwaggerGenerator {

    public abstract SwaggerGenerator addString(String name, String value);

    public abstract void finish() throws IOException;

    public abstract SwaggerGenerator addObject(String name);

    public abstract SwaggerGenerator addArrayObject(String name);

    public abstract SwaggerGenerator addBoolean(String name, boolean value);

    public abstract SwaggerGenerator addInt(String name, int value);

    public abstract SwaggerGenerator addArray(String name, String[] values);

    /**
     * Builder for a SwaggerGenerator that can write XML
     */

    public static SwaggerGenerator newXmlGenerator(Writer writer) throws IOException {
        return new SwaggerXmlGenerator(writer);
    }

    /**
     * Builder for a SwaggerGenerator that can write JSON
     */

    public static SwaggerGenerator newJsonGenerator(Writer writer) {
        return new SwaggerJsonGenerator(writer);
    }

    /**
     * Builds a SwaggerGenerator for one of the supported formats
     *
     * @param writer the writer to write to
     * @param format the format
     * @return the SwaggerGenerator
     * @throws IOException
     */

    public static SwaggerGenerator newGenerator(Writer writer, SwaggerFormat format) throws IOException {

        switch (format) {
            case xml:
                return newXmlGenerator(writer);
            case json:
                return newJsonGenerator(writer);
            default:
                throw new RuntimeException("Unknown format: " + format);
        }
    }

    /**
     * SwaggerGenerator implementation that reads JSON
     */

    public static class SwaggerJsonGenerator extends SwaggerGenerator {
        private final JsonObjectBuilder builder;
        private Writer writer;
        private Map<String,SwaggerJsonGenerator> objects;
        private Map<String,List<SwaggerJsonGenerator>> arrayObjects;

        public SwaggerJsonGenerator(Writer writer) {
            this.writer = writer;
            builder = Json.createObjectBuilder();
        }

        public SwaggerJsonGenerator(JsonObjectBuilder objectBuilder) {
            this.builder = objectBuilder;
        }

        @Override
        public SwaggerGenerator addString(String name, String value) {
            assert name != null;

            if (value != null)
                builder.add(name, value);

            return this;
        }

        JsonObjectBuilder getObjectBuilder()
        {
            return builder;
        }

        @Override
        public void finish() throws IOException {
            if( arrayObjects != null )
            {
                for( String name : arrayObjects.keySet() )
                {
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    for( SwaggerJsonGenerator w : arrayObjects.get(name))
                    {
                        w.finish();
                        arrayBuilder.add( w.getObjectBuilder() );
                    }

                    builder.add( name, arrayBuilder );
                }
            }

            if( objects != null )
            {
                for( String name : objects.keySet())
                {
                    SwaggerJsonGenerator w = objects.get(name);
                    w.finish();
                    builder.add( name, w.getObjectBuilder());
                }
            }

            if (writer != null) {
                JsonObject jsonObject = builder.build();
                javax.json.JsonWriter jsonWriter = Json.createWriter(writer);
                jsonWriter.writeObject(jsonObject);
                jsonWriter.close();
            }
        }

        @Override
        public SwaggerGenerator addObject(String name) {
            if( objects == null )
                objects = new HashMap<String, SwaggerJsonGenerator>();

            SwaggerJsonGenerator result = new SwaggerJsonGenerator( Json.createObjectBuilder());
            objects.put(name, result);

            return result;
        }

        @Override
        public SwaggerGenerator addArrayObject(String name) {
            assert name != null;

            if( arrayObjects == null )
            {
                arrayObjects = new HashMap<String, List<SwaggerJsonGenerator>>();
            }

            if( !arrayObjects.containsKey(name))
            {
                arrayObjects.put( name, new ArrayList<SwaggerJsonGenerator>());
            }

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            SwaggerJsonGenerator swaggerJsonGenerator = new SwaggerJsonGenerator(objectBuilder);
            arrayObjects.get(name).add(swaggerJsonGenerator);

            return swaggerJsonGenerator;
        }

        @Override
        public SwaggerGenerator addBoolean(String name, boolean value) {
            builder.add(name, value);
            return this;
        }

        @Override
        public SwaggerGenerator addInt(String name, int value) {
            builder.add(name, value);
            return this;
        }

        @Override
        public SwaggerGenerator addArray(String name, String[] values) {
            assert name != null && values != null;

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (String v : values)
                arrayBuilder.add(v);
            builder.add(name, arrayBuilder);

            return this;
        }
    }

    /**
     * SwaggerGenerator implementation that reads XML
     */

    public static class SwaggerXmlGenerator extends SwaggerGenerator {
        private final Element elm;
        private Writer writer;

        private SwaggerXmlGenerator(Writer writer) throws IOException {
            this.writer = writer;

            try {
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                elm = (Element) document.appendChild(document.createElement(Constants.API_DOCUMENTATION));
            } catch (Exception e) {
                throw new IOException(e);
            }
        }

        private SwaggerXmlGenerator(Element elm) {
            this.elm = elm;
        }

        @Override
        public SwaggerGenerator addString(String name, String value) {

            if (value != null) {
                Document document = elm.getOwnerDocument();
                Node node = elm.appendChild(document.createElement(name));
                node.appendChild(document.createTextNode(value));
            }

            return this;
        }

        @Override
        public void finish() throws IOException {
            if (writer == null)
                throw new RuntimeException("finish can only be called on root writer");

            try {
                Document document = elm.getOwnerDocument();
                DOMSource domSource = new DOMSource(document);
                StreamResult result = new StreamResult(writer);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.transform(domSource, result);
            } catch (TransformerException ex) {
                throw new IOException(ex);
            }
        }

        @Override
        public SwaggerGenerator addObject(String name) {
            return addArrayObject( name );
        }

        @Override
        public SwaggerGenerator addArrayObject(String name) {
            Document document = elm.getOwnerDocument();
            Node node = elm.appendChild(document.createElement(name));

            return new SwaggerXmlGenerator((Element) node);
        }

        @Override
        public SwaggerXmlGenerator addBoolean(String name, boolean value) {
            addString(name, Boolean.toString(value));
            return this;
        }

        @Override
        public SwaggerGenerator addInt(String name, int value) {
            addString(name, Integer.toString(value));
            return this;
        }

        @Override
        public SwaggerGenerator addArray(String name, String[] values) {
            for (String v : values)
                addString(name, v);

            return this;
        }
    }
}
