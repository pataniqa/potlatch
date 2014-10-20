# Building the code

# Quick Intro

The simplest way to build the client and deploy it to a device, build the server, run the tests and run the server, and generate the Javadoc is via Gradle. See a later section for how to get the code to work in an IDE. But configuring an IDE is hard so it is suggested at first you should use Gradle.

## Gradle

I have used Gradle to build the applications. Note that specific versions of the Android Gradle plugin [require specific versions of Gradle](http://tools.android.com/tech-docs/new-build-system/version-compatibility). I have used plugin 0.12 so this means you need Gradle 1.10 to 1.12. Therefore I have prepared a [Gradle Wrapper](http://www.gradle.org/docs/current/userguide/gradle_wrapper.html) for you using version 1.12. You should use the Gradle wrapper rather than a local version of Gradle if it is a different version. If you are on Linux you call the Gradle wrapper with `./gradlew`.

On Windows (I think!) you need `gradlew.bat` not `./gradlew`. 

## Android SDK

The build targets version 19.1.0 of the Android SDK so it is essential you have installed that version locally [via the SDK manager](http://developer.android.com/tools/help/sdk-manager.html). You also need to ensure you have set the [`ANDROID_HOME` environment variable](http://books.sonatype.com/mvnref-book/reference/android-dev-sect-config-build.html) so that the Gradle Android plugin can find the SDK. On my machine I do this by:

    export ANDROID_HOME=/home/mark/apps/adt-bundle-linux-x86_64-20131030/sdk/

Note if you have installed multiple versions of ADT it is possible that the SDK manager is using the first one to store the SDKs. If you use Eclipse, you can check the location of your SDKs by opening the Android SDK manager and checking the **SDK_PATH** at the top the window.

## Building the server

Note the `#` indicates a comment so you can understand what is going on - please don't type it in! For a more comprehensive list of build targets see below.

    cd PotlatchServer
    ./gradlew test          # run the Spock integration tests
    ./gradlew run           # run the server ready to connect a client to it.
                            # note the server drops its database everytime you restart it

## Building the client

    cd PotlatchClient
    ./gradlew installDebug  # build and install client. Note there needs to be a connected handset 
                            # or a running emulator for this task to succeed

Once you have the server and client running, you need to find out the IP address of the server - if you run Linux

    ifconfig

Then at the login screen on the client enter that address and the port e.g.:

    192.168.1.71:8443

## Hardcoded user / passwords

The server does not implement dynamic user sign-up. There are four hardcoded user / password combinations on the server for testing purposes:

    mark       one
    peter      two
    sophia     three 
    olivia     four

# In More detial!

## PotlatchCommon

To build the common code - although strictly this isn't necessary:

    cd PotlatchCommon
    ./gradlew tasks
    ./gradlew tasks       # see the build tasks available
    ./gradlew clean       # clean the build
    ./gradlew compileJava # compile the code
    ./gradlew javadoc     # generate Javadoc. 

## PotlatchServer

To build the server code:

    cd PotlatchServer
    ./gradlew tasks       # see the build tasks available
    ./gradlew clean       # clean the build
    ./gradlew compileJava # compile the code
    ./gradlew test        # run the server tests
    ./gradlew javadoc     # generate Javadoc. 
    ./gradlew run         # start the server

## PotlatchClient

To build the client code:

    cd PotlatchClient
    ./gradlew tasks             # see the build tasks
    ./gradlew clean             # clean the build
    ./gradlew compileJava       # compile the Java code
    ./gradlew assembleDebug     # create the debug APK
    ./gradlew installDebug	    # install the code into a handset or (running) emulator

# Configuring an IDE

If you wish to set up an IDE, this is slightly more difficult. I use Eclipse so it is possible. I do not know about IDEA/IntelliJ although I have included the [IDEA plugin](http://www.gradle.org/docs/current/userguide/idea_plugin.html) in the Gradle build scripts.

## Eclipse plugin

If you want to use Eclipse, the first step is to call the [eclipse task](http://www.gradle.org/docs/current/userguide/eclipse_plugin.html) from Gradle in PotlatchCommon, PotlatchClient and PotlatchServer modules. This will generate some files project for Eclipse. Then you can import the modules into Eclipse.

## Lombok

Next, because of the use of [Lombok](http://projectlombok.org/), you need to [Install Lombok](http://standardofnorms.wordpress.com/2013/05/10/reducing-java-boilerplate-code-with-lombok-with-eclipse-installation/). I have included the jar in the distribution in the top level lib directory: 

    java -jar lib/lombok.jar

Then add the Lombok jar as an external jar to all three projects.  If you do this, it should be straightforward to set up the PotlatchCommon and PotlatchServer modules. 

## Groovy and Spock

If you want to run the [Spock](https://code.google.com/p/spock/) unit tests in the Server from the IDE as you will need [Groovy](http://groovy.codehaus.org/). If so, you will need to install the [Eclipse Groovy plugin](http://groovy.codehaus.org/Eclipse+Plugin) and install [a compatible version of Groovy](https://code.google.com/p/spock/wiki/SpockVersionsAndDependencies). Specifically I have been using Groovy version 2.1.8. 

Alternatively you can run the tests at the command line as described before.

## Building Potlatch Client in the IDE

Building the Android application in Eclipse is more challenging. If you want to build the APK, I recommend you do it using Gradle, because in Eclipse / IDEA it is easy to get a broken APK. 

Specifically I found even though I was specifying in Gradle that I needed certain libraries, Eclipse / the Eclipse Android tools did not include them so it was necessary to manually include those jars. Therefore I have included a directory in PotlatchClient called libs that contain the necessary jars. So you may need to manually add this lib directory to the PotlatchClient project in Eclipse.

### Configuring Butterknife

To configure [Butterknife](http://jakewharton.github.io/butterknife/) it is necessary to configure the [Eclipse compiler to use Butterknife annotations](http://jakewharton.github.io/butterknife/ide-eclipse.html). 

[I had some problems](http://stackoverflow.com/questions/24724866/android-eclipse-cannot-see-annotation-processing-option) doing this so I had to re-install the Eclipse Java Development tools into the ADT version of Eclipse. If you have further problems then please [refer to this post on StackOverflow](http://stackoverflow.com/questions/23420143/eclipse-doesnt-generate-the-apt-generated-folder-for-butter-knife). 

Next I could not find a way to get the Eclipse Android tools to include the PotlatchCommon code into the APK if I used an Eclipse dependent project. Therefore I found it was necessary to go to `Project Properties / Java Build Path / Source` and manually add the code from the common project as a new Source Folder in PotlatchClient. If there is a better way to do this I would be interested to know?

It is also necessary for the PotlatchClient to have the Lombok jar, **but** it is better if you can to avoid it ending up in the “Android Private Libraries” as then it will be put in the APK where it will take up unnecessary space. Instead, I have generated a lomok-api.jar that only contains the annotations that I have placed in PotlatchClient lib directory as described in the [Lombok Android instructions](http://projectlombok.org/setup/android.html). Again, if someone has some insight into how to reliably configure this I would be interested to know?

But, to re-iterate, the **best way to build the client is probably at the command line**.

Good luck with all this and have fun!

# Libraries used:

This is an area which could do with further work to reduce the size of the APK. For example, I could not get the client to work without both GSON and Jackson even though I tried to get rid of this dependency. I switched to using Jackson with Retrofit because the server uses Jackson and otherwise I found there are problems round tripping dates but I couldn't get it to work. Similiarly the Apache HttpComponents jar is the `libs` directory, but it shouldn't need to be there. 

- Apache Commons Codec http://commons.apache.org/proper/commons-codec/
- Apache Commons IO http://commons.apache.org/proper/commons-io/
- GSON https://code.google.com/p/google-gson/ 
- Jackson http://wiki.fasterxml.com/JacksonHome
- Picasso http://square.github.io/picasso/
- OkHttp http://square.github.io/okhttp/ and OkHttpUrlConnection
- OkIo http://square.github.io/okio/ 
- Retrofit- http://square.github.io/retrofit/ 
- Rxjava https://github.com/ReactiveX/RxJava 
- Spring Boot http://projects.spring.io/spring-boot/ 
- Spring Data REST http://projects.spring.io/spring-data-rest/ 
