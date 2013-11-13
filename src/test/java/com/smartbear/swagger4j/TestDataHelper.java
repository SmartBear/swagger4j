package com.smartbear.swagger4j;

import junit.framework.TestCase;

/**
 * Helper for differences in the swagger version samples
 */

public interface TestDataHelper {
    String getApiVersion();

    void validateUserApiDeclaration(ApiDeclaration apiDeclaration) throws Exception;

    void validatePetApiDeclaration(ApiDeclaration apiDeclaration) throws Exception;


    public static class V1_TestData implements TestDataHelper
    {

        @Override
        public String getApiVersion() {
            return "0.2";
        }

        @Override
        public void validateUserApiDeclaration(ApiDeclaration apiDeclaration) throws Exception {

            for (int c = 0; c < 6; c++) {
                Api api = apiDeclaration.getApis().get(0);
                TestCase.assertNotNull(api);
                TestCase.assertEquals(1, api.getOperations().size());
            }


            TestCase.assertEquals(2, apiDeclaration.getApis().get(4).getOperations().get(0).getParameters().size());
            TestCase.assertEquals(2, apiDeclaration.getApis().get(3).getOperations().get(0).getResponseMessages().size());

        }

        @Override
        public void validatePetApiDeclaration(ApiDeclaration apiDeclaration) throws Exception {

            for (int c = 0; c < 3; c++) {
                Api api = apiDeclaration.getApis().get(0);
                TestCase.assertNotNull(api);
                TestCase.assertEquals(1, api.getOperations().size());
            }

            TestCase.assertEquals(1, apiDeclaration.getApis().get(1).getOperations().get(0).getParameters().size());
            TestCase.assertEquals(2, apiDeclaration.getApis().get(0).getOperations().get(0).getResponseMessages().size());

        }
    }

    public static class V2_TestData implements TestDataHelper
    {

        @Override
        public String getApiVersion() {
            return "1.0.0";
        }

        @Override
        public void validateUserApiDeclaration(ApiDeclaration apiDeclaration) throws Exception {

        }

        @Override
        public void validatePetApiDeclaration(ApiDeclaration apiDeclaration) throws Exception {

        }
    }

}
