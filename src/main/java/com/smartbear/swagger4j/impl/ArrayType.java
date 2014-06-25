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
 *
 * @author Yann D'Isanto
 */
public abstract class ArrayType implements DataType {
    
    @Override
    public final boolean isArray() {
        return true;
    }

    public final boolean isComplex() {
        return false;
    }

    @Override
    public final boolean isVoid() {
        return false;
    }

}
