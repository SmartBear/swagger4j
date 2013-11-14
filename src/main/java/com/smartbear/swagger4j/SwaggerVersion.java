package com.smartbear.swagger4j;

/**
 * Enum for the Swagger Version
 */

public enum SwaggerVersion
{
    V1_1, V1_2;

    public String getIdentifier() {
        switch ( this )
        {
            case V1_1: return "1.1";
            case V1_2: return "1.2";
        }

        throw new RuntimeException( "Unexpected Swagger version: " + this.name());
    }

    public final static SwaggerVersion DEFAULT_VERSION = V1_2;

    public static SwaggerVersion fromIdentifier(String string) {
        if( V1_1.getIdentifier().equals( string ))
            return V1_1;
        if( V1_2.getIdentifier().equals( string ))
            return V1_2;

        throw new RuntimeException( "Unknown Swagger Version: " + string );
    }
}
