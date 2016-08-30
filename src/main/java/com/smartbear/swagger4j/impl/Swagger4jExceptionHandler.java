package com.smartbear.swagger4j.impl;

/**
 * Created by ole on 07/03/14.
 */
public class Swagger4jExceptionHandler {

    private static Swagger4jExceptionHandler instance;

    public synchronized static Swagger4jExceptionHandler get() {

        if (instance == null) {
            String handler = System.getProperty("swagger4j.exceptionhandler", Swagger4jExceptionHandler.class.getName());
            try {
                instance = (Swagger4jExceptionHandler) Class.forName(handler).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                instance = new Swagger4jExceptionHandler();
            }
        }

        return instance;
    }

    public void onException(Exception e) {
        e.printStackTrace();
    }
}
