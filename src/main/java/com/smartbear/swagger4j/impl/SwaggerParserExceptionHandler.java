package com.smartbear.swagger4j.impl;

/**
 * Created by ole on 07/03/14.
 */
public class SwaggerParserExceptionHandler {

    private static SwaggerParserExceptionHandler instance;


    public synchronized static SwaggerParserExceptionHandler get() {

        if( instance == null )
        {
            String handler = System.getProperty( "swagger.parser.exceptionhandler", SwaggerParserExceptionHandler.class.getName());
            try {
                instance = (SwaggerParserExceptionHandler) Class.forName( handler ).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                instance = new SwaggerParserExceptionHandler();
            }
        }

        return instance;
    }

    public void onParseError(RuntimeException e) {
        throw e;
    }
}
