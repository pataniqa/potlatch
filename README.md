# Building the code

# Quick Intro

To keep it short - here are the commands you need to know. These instructions are for Linux - I think you need `gradlew.bat` not `./gradlew` on Windows:

  cd PotlatchServer
  ./gradlew test          # run the Spock integration tests
	./gradlew run			      # run the server ready to connect a client to it.
                          # note the server drops its database everytime you restart it

  cd PotlatchClient
	./gradlew installDebug	# build and install client. Note there needs to be a connected handset 
                          # or a runnign emulator for this task to succeed

Once you have the server and client running, you need to find out the IP address of the server - if you run Linux

  ifconfig

Then at the login screen on the client enter that address and the port e.g.:

  192.168.1.71:8443

# In more detail 

## PotlatchCommon

To build the common code - although strictly this isn't necessary unless you want to inspect the javadoc - if you do you will need [GraphViz](http://www.graphviz.org/) installed because the build file uses [UMLGraph](http://www.umlgraph.org/) to generate UML from the Java code / Javadoc. 

    cd PotlatchCommon
    ./gradlew tasks
    ./gradlew tasks       # see the build tasks available
    ./gradlew clean       # clean the build
    ./gradlew compileJava # compile the code
    ./gradlew javadoc     # generate Javadoc. You will need Graphviz installed
                          # to do this due to the use of UMLGraph 

## PotlatchServer

To build the server code (same rules apply about Javadoc):

    cd PotlatchServer
    ./gradlew tasks       # see the build tasks available
    ./gradlew clean       # clean the build
    ./gradlew compileJava # compile the code
    ./gradlew test        # run the server tests
    ./gradlew javadoc     # generate Javadoc. You will need Graphviz installed
                          # to do this due to the use of UMLGraph 
    /gradlew run          # start the server

## PotlatchClient

To build the client code

	cd PotlatchClient
	./gradlew tasks             # see the build tasks
  ./gradlew clean             # clean the build
  ./gradlew compileJava       # compile the Java code
  ./gradlew assembleDebug     # create the debug APK
  ./gradlew installDebug	    # install the code into a handset 
                              # or (running) emulator

## Gradle concerns

You will notice I have included a [Gradle](http://www.gradle.org/) wrapper. This provides a specific version of Gradle, because a particular version of the Android Gradle plugin will only work with certain versions of Gradle. Therefore, in theory, you should not need Gradle installed, or it should not matter if you have a different version of Gradle installed, because the wrapper should handle it. However, this is only the theory, and I had quite a bit of trouble getting Gradle to work, and various StackOverflow posts warned that Gradle wrappers may not work, and you might need to install a specific version of Gradle. If so, you need version 1.12.

# Configuring an IDE

If you wish to set up an IDE, this is slightly more difficult. I use Eclipse so it is possible. I do not know about IDEA/IntelliJ although I have included the IDEA plugin. 

If you want to use Eclipse, the first step is to call the eclipse task from Gradle in PotlatchCommon, PotlatchClient and PotlatchServer modules. This will generate some files project for Eclipse. Then you can import the modules into Eclipse.

Next, because of the use of [Lombok](http://projectlombok.org/), you need to add the Lombok jar as an external jar to all three projects. I have included the jar in the distribution in the top level lib directory. If you do this, it should be straightforward to set up the PotlatchCommon and PotlatchServer modules. 

The only problem is if you want to run the [Spock](https://code.google.com/p/spock/) unit tests in the Server from the IDE as you will need [Groovy](http://groovy.codehaus.org/). If so, you will need to install the [Eclipse Groovy plugin](http://groovy.codehaus.org/Eclipse+Plugin) and install [a compatible version of Groovy](https://code.google.com/p/spock/wiki/SpockVersionsAndDependencies). Specifically I have been using Groovy version 2.1.8. 

Alternatively you can run the tests at the command line as described before.

Building the Android application in Eclipse is more challenging. If you want to build the APK, I recommend you do it using Gradle, because in Eclipse / IDEA it is easy to get a broken APK. 

Specifically I found even though I was specifying in Gradle that I needed certain libraries, Eclipse / the Eclipse Android tools did not include them so it was necessary to manually include those jars. I have included a directory in PotlatchClient called libs that contain the necessary jars. So you may need to manually add this lib directory to the PotlatchClient project in Eclipse.

Next [Butterknife](http://jakewharton.github.io/butterknife/): it is necessary to configure the [Eclipse compiler to use Butterknife annotations](http://jakewharton.github.io/butterknife/ide-eclipse.html). [I had some problems](http://stackoverflow.com/questions/24724866/android-eclipse-cannot-see-annotation-processing-option) doing this and [had to refer to StackOverflow](http://stackoverflow.com/questions/23420143/eclipse-doesnt-generate-the-apt-generated-folder-for-butter-knife). 

Next the Eclipse Android tools do not seem to include the PotlatchCommon code into the APK if you use a dependent project so I found it was necessary to go to project properties / source files and include the Java source files manually into PotlatchClient. 

It is also necessary for the PotlatchClient to have the Lombok jar, but it is better if you can to avoid it ending up in the “Android Private Libraries” as then it will be put in the APK where it will take up unnecessary space. Instead, I have generated a lomok-api.jar that I have placed in PotlatchClient lib directory as described in the [Lombok Android instructions](http://projectlombok.org/setup/android.html).

But, to re-iterate, the best way to build the client is probably at the command line.
Good luck with all this – all I sorry it so challenging – but I found it quite a battle to get the various tools to work together.

Good luck with it and have fun!

# Other libraries used:

-Picasso http://square.github.io/picasso/
-OkHttp http://square.github.io/okhttp/ 
-OkIo http://square.github.io/okio/ 
-Retrofit- http://square.github.io/retrofit/ 
-Rxjava https://github.com/ReactiveX/RxJava 
-Spring Boot http://projects.spring.io/spring-boot/ 
-Spring Data REST http://projects.spring.io/spring-data-rest/ 
