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
package com.smartbear.swagger4j.impl;

import com.smartbear.swagger4j.PrimitiveType;

/**
 * @author Yann D'Isanto
 */
public final class PrimitiveArrayType extends ArrayType {

    private final PrimitiveType itemsType;

    public PrimitiveArrayType(PrimitiveType itemsType) {
        this.itemsType = itemsType;
    }

    public String getType() {
        return itemsType.getType();
    }

    public String getRef() {
        return null;
    }

    public String getFormat() {
        return itemsType.getFormat();
    }

    @Override
    public String toString() {
        return new StringBuilder("array[")
            .append(itemsType)
            .append(']')
            .toString();
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public boolean isRef() {
        return false;
    }

    public boolean isInteger() {
        return itemsType.isInteger();
    }

    public boolean isLong() {
        return itemsType.isLong();
    }

    public boolean isFloat() {
        return itemsType.isFloat();
    }

    public boolean isDouble() {
        return itemsType.isDouble();
    }

    public boolean isString() {
        return itemsType.isString();
    }

    public boolean isByte() {
        return itemsType.isByte();
    }

    public boolean isBoolean() {
        return itemsType.isBoolean();
    }

    public boolean isDate() {
        return itemsType.isDate();
    }

    public boolean isDateTime() {
        return itemsType.isDateTime();
    }

}
