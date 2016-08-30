## swagger4j

A simple java library for reading and writing [Swagger 1.X definitions](https://swagger.io). Supports both JSON and XML formats. The current version is
reasonably in line with the latest swagger core release except that it doesn't support any of the JSON Schema/Data Model constructs yet. Apache 2.0 licensed.

If you're interested in reading Swagger 2.0 definitions you're better off using the official [swagger-parser](https://github.com/swagger-api/swagger-parser)

### Getting started

Clone this repo from GitHub and build it yourself with maven (with "mvn install"). 

The latest version is also available in the SmartBear maven repository at soapui.org, add this to your pom with

```xml
<repositories>
    <repository>
        <id>soapUI Repository</id>
        <url>http://www.soapui.org/repository/maven2</url>
    </repository>
</repositories>
```

and add the corresponding dependency:

```xml
<dependency>
    <groupId>com.smartbear</groupId>
    <artifactId>swagger4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

swagger4j has a runtime dependency
on jsonp ([https://java.net/projects/jsonp/](https://java.net/projects/jsonp/)) which you can add to your maven pom with.

```xml
<dependency>
    <groupId>org.glassfish</groupId>
    <artifactId>javax.json</artifactId>
    <version>1.0-b06</version>
</dependency>
```

Once added to your classpath you can start reading swagger definitions, for example

```java
ResourceListing resourceListing = Swagger.readSwagger( "http://petstore.swagger.wordnik.com/api/api-docs.json" )
for( ResourceListingApi apiRef : resourceListing.getApis() )
{
   ApiDeclaration apiDeclaration = apiRef.getDeclaration();
   for( Api api : apiDeclaration.getApis())
   {
       ...
   }
}
```

and of course you can write them as well

```java
SwaggerFactory factory = Swagger.createSwaggerFactory();

ApiDeclaration apiDeclaration = factory.createApiDeclaration( "http://api.mycompany.com/apis", "/user" );
Api api = apiDeclaration.addApi( "{id}" );
Operation op = api.addOperation( "getuserbyid", Operation.Method.GET );
op.addParameter( "id", Parameter.ParamType.path );

ResourceListing rl = factory.createResourceListing( "http://api.mycompany.com/apis" );
rl.setApiVersion( "1.0" );
rl.addApi( apiDeclaration, "user-doc.{format}" );

Swagger.writerSwagger( rl, "api-docs" );
```

The API is closely modeled after the Swagger specification, if you are familiar with that it should be a breeze to use.
If you aren't familiar with Swagger and its spec head right over to the swagger website at 
[https://github.com/wordnik/swagger-core/wiki](https://github.com/wordnik/swagger-core/wiki) to learn all about it.

Javadoc is available in the zip at sourceforge.

### Library Design

Swagger4j uses a standard Factory/Builder approach with interfaces defining the entire Swagger object model and a
default implementation implementing them. If you have any suggestions on how to improve the actual API please don't
hesitate to get in touch by adding tickets here at GitHub for bugs, issues, feature requests - Thank you!

Please note the following:
- the whole DataModel/DataType part of Swagger is not yet supported
- paths to APIs referred to in a ResourceListing are resolved by adding the path of the api to the basePath defined
in the resourceListing. If the basePath is relative - it is resolved relatively to the host/root of the ResourceListing
URI.

### Usages

* Version 0.2+ of the SoapUI-Swagger-Plugin use swagger4j to parse and generate Swagger definitions.

### Future improvements

* None planned at this point!

### Release History

* 20130527 - Initial beta1 release
* 20131119 - beta2 release, adds support for Swagger 1.2 and 1.0
* 20140505 - beta3 bugfixes
* 20140512 - beta4 more bugfixes
* 20151029 - 1.0.0 release - more bugfixes
