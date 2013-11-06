## Build from scratch

In order to build page-generator from scratch you will need Apache Maven, go to the main
project folder (where the pom.xml is) and invoke: 
```mvn assembly:assembly```
As a result you will get a executable jar with all dependencies stored in ```./target/``` it should be 
named like ```page-generator-0.0.1-SNAPSHOT-jar-with-dependencies.jar```.

In order to run page-generator you need to invoke:
```java -jar page-generator-0.0.1-SNAPSHOT-jar-with-dependencies.jar````

## TODO

* get rid of System.outs/err use logger (include log4j.properties) 
* dependency review
* check licenses of used dependencies 
* check with Java 7 
* check with Tesseract 3.0.1 
* check with Tesseract 3.0.2
* describe building process and add disclaimers in code 

## Done
* make project buildable
* update changes required by Clemens
