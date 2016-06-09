/*
 * Copyright 2014 Yann D'Isanto.
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
package com.smartbear.swagger4j;

import com.smartbear.swagger4j.DataType;

/**
 *
 * @author Yann D'Isanto
 */
public enum PrimitiveType implements DataType {

    INTEGER(Type.INTEGER, Format.INT32),
    LONG(Type.INTEGER, Format.INT64),
    FLOAT(Type.NUMBER, Format.FLOAT),
    DOUBLE(Type.NUMBER, Format.DOUBLE),
    NUMBER( Type.NUMBER, Format.UNDEFINED),
    STRING(Type.STRING, Format.UNDEFINED),
    BYTE(Type.STRING, Format.BYTE),
    BOOLEAN(Type.BOOLEAN, Format.UNDEFINED),
    DATE(Type.STRING, Format.DATE),
    DATE_TIME(Type.STRING, Format.DATE_TIME);

    public static enum Type {

        INTEGER,
        NUMBER,
        STRING,
        BOOLEAN;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        public static Type parse(String string) {
            return valueOf(string.toUpperCase());
        }
    }

    public static enum Format {

        UNDEFINED,
        INT32,
        INT64,
        FLOAT,
        DOUBLE,
        BYTE,
        DATE,
        DATE_TIME;

        @Override
        public String toString() {
            if (this == UNDEFINED) {
                return "";
            }
            return name().toLowerCase().replace("_", "-");
        }

        public static Format parse(String string) {
            return string != null
                ? valueOf(string.toUpperCase().replace("-", "_"))
                : UNDEFINED;
        }
    }

    private final Type type;

    private final Format format;

    private PrimitiveType(Type type, Format format) {
        this.type = type;
        this.format = format;
    }

    public String getType() {
        return type.toString();
    }

    public String getRef() {
        return null;
    }

    public String getFormat() {
        return format.toString();
    }

    @Override
    public String toString() {
        if (this == DATE_TIME) {
            return "dateTime";
        }
        return name().toLowerCase();
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    public boolean isComplex() {
        return false;
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public boolean isRef() {
        return false;
    }

    public boolean isInteger() {
        return this == INTEGER;
    }

    public boolean isLong() {
        return this == LONG;
    }

    public boolean isFloat() {
        return this == FLOAT;
    }

    public boolean isDouble() {
        return this == DOUBLE;
    }

    public boolean isString() {
        return this == STRING;
    }

    public boolean isByte() {
        return this == BYTE;
    }

    public boolean isBoolean() {
        return this == BOOLEAN;
    }

    public boolean isDate() {
        return this == DATE;
    }

    public boolean isDateTime() {
        return this == DATE_TIME;
    }

    public boolean isNumber() {
        // according to swagger specs byte is not a number but a string
        return this == INTEGER 
            || this == DOUBLE 
            || this == LONG 
            || this == FLOAT;
    }
    
    public boolean isTemporal() {
        return this == DATE 
            || this == DATE_TIME;
    }
    
    /**
     * Gets the primitive type for the given type and format.
     *
     * @param type the type
     * @param format the format
     * @return a PrimitiveType instance.
     * @throws IllegalArgumentException if the specified type or format is
     * invalid
     * @throws UnsupportedOperationException if the type/format association is
     * not supported
     */
    public static PrimitiveType get(String type, String format) throws IllegalArgumentException, UnsupportedOperationException {
        return get(Type.parse(type), Format.parse(format));
    }

    /**
     * Gets the primitive type for the given type and format.
     *
     * @param type the type
     * @param format the format
     * @return a PrimitiveType instance.
     * @throws UnsupportedOperationException if the type/format association is
     * not supported
     */
    public static PrimitiveType get(Type type, Format format) throws UnsupportedOperationException {
        for (PrimitiveType primitive : values()) {
            if (primitive.type == type && primitive.format == format) {
                return primitive;
            }
        }
        throw new UnsupportedOperationException("invalid primitive type/format");
    }
}
