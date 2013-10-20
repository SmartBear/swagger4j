package com.smartbear.swagger4j;

public enum SwaggerFormat
{
    xml, json;

    public String getExtension() {
        return this.name().toLowerCase();
    }
}