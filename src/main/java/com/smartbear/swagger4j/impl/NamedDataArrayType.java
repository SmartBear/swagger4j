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

/**
 *
 * @author Yann D'Isanto
 */
public final class NamedDataArrayType extends ArrayType {

    private final String name;

    public NamedDataArrayType(String name) {
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
        return new StringBuilder("array[")
            .append(name)
            .append(']')
            .toString();
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isRef() {
        return false;
    }
}
