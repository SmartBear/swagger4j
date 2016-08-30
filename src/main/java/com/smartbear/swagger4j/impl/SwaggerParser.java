/**
 * Copyright 2013 SmartBear Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartbear.swagger4j.impl;

import com.smartbear.swagger4j.SwaggerFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for abstraction of reading actual format, since json and xml are read in the same way
 */

public abstract class SwaggerParser {

    public abstract String getString(String name);

    public abstract List<SwaggerParser> getChildren(String name);

    public abstract boolean getBoolean(String name);

    public abstract String getString();

    public abstract SwaggerFormat getFormat();

    public abstract int getInteger(String name);

    public abstract Number getNumber(String name);

    public abstract List<String> getArray(String name);

    public abstract SwaggerParser getChild(String name);

    public abstract String[] getChildNames();

    /**
     * Builder for a SwaggerParser that can read XML
     */

    public static SwaggerParser newXmlParser(Reader reader) throws IOException {
        return new SwaggerXmlParser(reader);
    }

    /**
     * Builder for a SwaggerParser that can read json
     */

    public static SwaggerParser newJsonParser(Reader reader) {
        return new SwaggerJsonParser(reader);
    }

    /**
     * Builder for a SwaggerParser for one of the supported formats
     */

    public static SwaggerParser newParser(Reader reader, SwaggerFormat format) throws IOException {
        switch (format) {

            case json:
                return SwaggerParser.newJsonParser(reader);
            case xml:
                return SwaggerParser.newXmlParser(reader);
        }

        throw new RuntimeException("Unknown format: " + format);
    }

    /**
     * SwaggerParser implementation that reads XML
     */

    public static class SwaggerXmlParser extends SwaggerParser {
        private final Element elm;

        private SwaggerXmlParser(Element elm) {
            this.elm = elm;
        }

        private SwaggerXmlParser(Reader reader) throws IOException {
            assert reader != null;

            try {
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(reader));
                elm = document.getDocumentElement();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }

        @Override
        public String getString(String name) {
            NodeList nl = elm.getElementsByTagName(name);
            if (nl.getLength() > 0) {
                return nl.item(0).getTextContent();
            }

            return null;
        }

        @Override
        public List<SwaggerParser> getChildren(String name) {
            List<SwaggerParser> result = new ArrayList<SwaggerParser>();

            NodeList nl = elm.getElementsByTagName(name);
            for (int c = 0; c < nl.getLength(); c++) {
                result.add(new SwaggerXmlParser((Element) nl.item(c)));
            }

            return result;
        }

        @Override
        public boolean getBoolean(String name) {
            return Boolean.getBoolean(getString(name));
        }

        @Override
        public String getString() {
            return elm.getTextContent();
        }

        @Override
        public SwaggerFormat getFormat() {
            return SwaggerFormat.xml;
        }

        @Override
        public int getInteger(String name) {
            return Integer.parseInt(getString(name));
        }

        @Override
        public Number getNumber(String name) {
            String stringValue = getString(name);
            return stringValue == null || stringValue.isEmpty()
                ? null
                : new BigDecimal(stringValue);
        }

        @Override
        public List<String> getArray(String name) {
            List<String> result = new ArrayList<String>();

            NodeList nl = elm.getElementsByTagName(name);
            for (int c = 0; c < nl.getLength(); c++) {
                result.add(nl.item(c).getTextContent());
            }

            return result;
        }

        @Override
        public SwaggerParser getChild(String name) {
            NodeList nl = elm.getElementsByTagName(name);
            if (nl.getLength() > 0) {
                return new SwaggerXmlParser((Element) nl.item(0));
            }

            return null;
        }

        @Override
        public String[] getChildNames() {
            List<String> names = new ArrayList<String>();
            NodeList nl = elm.getChildNodes();
            for (int c = 0; c < nl.getLength(); c++) {
                Node n = nl.item(c);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    names.add(n.getNodeName());
                }
            }

            return names.toArray(new String[names.size()]);
        }
    }

    /**
     * SwaggerParser implementation that reads JSON
     */

    public static class SwaggerJsonParser extends SwaggerParser {
        private final JsonObject jsonObject;

        private SwaggerJsonParser(JsonObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        private SwaggerJsonParser(Reader reader) {
            final javax.json.JsonReader jsonReader = javax.json.Json.createReader(reader);
            jsonObject = jsonReader.readObject();
        }

        @Override
        public String getString(String name) {
            try {
                return jsonObject.getString(name, null);
            } catch (RuntimeException e) {
                Swagger4jExceptionHandler.get().onException(e);
                return null;
            }
        }

        @Override
        public List<SwaggerParser> getChildren(String name) {
            List<SwaggerParser> result = new ArrayList<SwaggerParser>();
            if (!jsonObject.containsKey(name) || jsonObject.isNull(name)) {
                return result;
            }

            JsonArray jsonArray = jsonObject.getJsonArray(name);
            for (int c = 0; jsonArray != null && c < jsonArray.size(); c++) {
                result.add(new SwaggerJsonParser(jsonArray.getJsonObject(c)));
            }

            return result;
        }

        @Override
        public boolean getBoolean(String name) {
            assert name != null : "name can not be null";
            JsonValue value = jsonObject.get(name);
            return value == null ? false : Boolean.valueOf(value.toString());
        }

        @Override
        public String getString() {
            return jsonObject.toString();
        }

        @Override
        public SwaggerFormat getFormat() {
            return SwaggerFormat.json;
        }

        @Override
        public int getInteger(String name) {
            JsonValue value = jsonObject.get(name);
            return Integer.parseInt(value.toString());
        }

        @Override
        public Number getNumber(String name) {
            JsonValue value = jsonObject.get(name);
            if (value == null) {
                return null;
            }
            switch (value.getValueType()) {
                case NUMBER:
                    return ((JsonNumber) value).bigDecimalValue();
                case STRING:
                    return new BigDecimal(((JsonString) value).getString());
                default:
                    throw new IllegalArgumentException("not a number");
            }
        }

        @Override
        public List<String> getArray(String name) {
            List<String> result = new ArrayList<String>();
            if (!jsonObject.containsKey(name) || jsonObject.isNull(name)) {
                return result;
            }
            JsonArray jsonArray = jsonObject.getJsonArray(name);
            if (jsonArray != null) {
                for (int c = 0; c < jsonArray.size(); c++) {
                    result.add(jsonArray.getString(c));
                }
            }
            return result;
        }

        @Override
        public SwaggerParser getChild(String name) {
            JsonValue value = jsonObject.get(name);
            if (value == JsonValue.NULL) {
                return null;
            }
            JsonObject child = (JsonObject) value;
            return child == null ? null : new SwaggerJsonParser(child);
        }

        @Override
        public String[] getChildNames() {
            return jsonObject.keySet().toArray(new String[jsonObject.size()]);
        }
    }
}
