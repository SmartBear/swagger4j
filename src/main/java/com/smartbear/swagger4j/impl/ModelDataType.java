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

import com.smartbear.swagger4j.DataType;

/**
 * @author Yann D'Isanto
 */
public final class ModelDataType implements DataType {

    private final String name;

    public ModelDataType(String name) {
        this.name = name;
    }

    public String getType() {
        return name;
    }

    public String getRef() {
        return null;
    }

    public String getFormat() {
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isPrimitive() {
        return false;
    }

    public boolean isComplex() {
        return true;
    }

    public boolean isVoid() {
        return false;
    }

    public boolean isRef() {
        return false;
    }

    public boolean isInteger() {
        return false;
    }

    public boolean isLong() {
        return false;
    }

    public boolean isFloat() {
        return false;
    }

    public boolean isDouble() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isByte() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isDate() {
        return false;
    }

    public boolean isDateTime() {
        return false;
    }

}
