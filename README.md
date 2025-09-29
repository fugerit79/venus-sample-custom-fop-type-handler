# venus-sample-custom-fop-type-handler

This project shows how to add custom behaviors to a [Venus Fugerit Doc Framework](https://github.com/fugerit-org/fj-doc) Type Handler.

## Override configuration property 'fop-suppress-events'

When generating a PDF using [Apache FOP](https://xmlgraphics.apache.org/fop/), FOUserAgent can log some events like : 

```shell
2025-09-29 23:03:20,667 WARN  [org.apa.fop.app.FOUserAgent] (executor-thread-1) Font "Symbol,normal,700" not found. Substituting with "Symbol,normal,400".
2025-09-29 23:03:20,667 WARN  [org.apa.fop.app.FOUserAgent] (executor-thread-1) Font "ZapfDingbats,normal,700" not found. Substituting with "ZapfDingbats,normal,400".
2025-09-29 23:03:20,681 INFO  [org.apa.fop.app.FOUserAgent] (executor-thread-1) Rendered page #1.
```

It is possible to use the config property 'fop-suppress-events' to suppress those events logging (especially in production it could be quite verbose), and we did in this project : 

```xml
<docHandler id="pdf-fop" info="pdf" type="org.fugerit.java.doc.mod.fop.PdfFopTypeHandler">
    <docHandlerCustomConfig charset="UTF-8" fop-config-mode="classloader" fop-config-classloader-path="venus-sample-custom-fop-type-handler/fop-config.xml" fop-suppress-events="1"/>
</docHandler>
```

Now let's imagine we want, for debug purpose, to be able to enable FOP event logging without a new build.

This can be achieved by creating a custom doc type handler : 

```java
package org.fugerit.java.demo;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.w3c.dom.Element;

@Slf4j
public class CustomFopPdfTypeHandler extends PdfFopTypeHandler {

    public final static String SYS_PROP_OVERRIDE_FOP_SUPPRESS_EVENTS = "override-for-suppress-events";

    protected void handleConfigTag(Element config) throws ConfigException {
        String overrideFopSuppressEvents = System.getProperty(SYS_PROP_OVERRIDE_FOP_SUPPRESS_EVENTS);
        if (StringUtils.isNotEmpty(overrideFopSuppressEvents)) {
            log.info("Override for suppressed events: {} -> {}",
                    config.getAttribute( PdfFopTypeHandler.ATT_FOP_SUPPRESS_EVENTS ),
                    overrideFopSuppressEvents);
            config.setAttribute(PdfFopTypeHandler.ATT_FOP_SUPPRESS_EVENTS, overrideFopSuppressEvents);
        }
        super.handleConfigTag(config);
    }

}
```

Now we can run our application with event logging enabled by simply adding : 

```shell
mvn quarkus:dev -Doverride-for-suppress-events=0
```

## Original project README

Here starts the original project readme as created by command :

## Quickstart

Requirement :

* maven 3.9.x
* java 21+ (GraalVM for native version)

1. Verify the app

```shell
mvn verify
```

2. Start the app

```shell
mvn quarkus:dev
```

3. Try the app

Open the [swagger-ui](http://localhost:8080/q/swagger-ui/)

Test available paths (for instance : [/doc/example.md](http://localhost:8080/doc/example.md))

NOTE:

* Powered by Quarkus 3.28.1
* Using Fugerit Venus Doc 8.16.5 (extensions : base,freemarker,mod-fop)

## Native version

If you picked only native modules, you should be able to build and run the AOT version (GraalVM 21+ needed).

Further documentation :

* [List of modules and native support](https://venusdocs.fugerit.org/guide/#available-extensions)
* [Fugerit Venus Doc native support introduction](https://venusdocs.fugerit.org/guide/#doc-native-support)

1. Build and verify

```shell
mvn package -Dnative
```

2. Start

```shell
./target/venus-sample-custom-fop-type-handler-1.0.0-SNAPSHOT-runner
```

## Overview

This project has been initialized using [fj-doc-maven-plugin init goal](https://venusdocs.fugerit.org/guide/#maven-plugin-goal-init).

The quarkus 3 structure is similar to running the quarkus create goal : 

```shell
mvn io.quarkus.platform:quarkus-maven-plugin:3.28.1:create \
-DprojectGroupId=org.fugerit.java.demo \
-DprojectArtifactId=venus-sample-custom-fop-type-handler \
-Dextensions='rest,rest-jackson,config-yaml,smallrye-openapi'
```

## Quarkus readme

From here on, this is the original quarkus readme.

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/getting-started-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- YAML Configuration ([guide](https://quarkus.io/guides/config-yaml)): Use YAML to configure your Quarkus application

## Provided Code

### YAML Config

Configure your application with YAML

[Related guide section...](https://quarkus.io/guides/config-reference#configuration-examples)

The Quarkus application configuration is located in `src/main/resources/application.yml`.

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
