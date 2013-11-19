package com.smartbear.swagger4j;

import junit.framework.TestCase;

public class SwaggerVersionTestCase extends TestCase  {
    public void testSwaggerVersion() throws Exception
    {
        assertTrue( SwaggerVersion.V1_2.isGreaterThan( SwaggerVersion.V1_1 ));
        assertTrue( SwaggerVersion.V1_2.isGreaterThan( SwaggerVersion.V1_0 ));
        assertTrue( SwaggerVersion.V1_1.isGreaterThan( SwaggerVersion.V1_0 ));

        assertFalse( SwaggerVersion.V1_0.isGreaterThan( SwaggerVersion.V1_1));
        assertFalse( SwaggerVersion.V1_0.isGreaterThan( SwaggerVersion.V1_2));
        assertFalse( SwaggerVersion.V1_1.isGreaterThan( SwaggerVersion.V1_2));
    }
}
