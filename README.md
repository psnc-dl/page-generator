## Build from scratch

In order to build page-generator from scratch you will need Apache Maven, go to the main
project folder (where the pom.xml is) and invoke: 
```mvn assembly:assembly```
As a result you will get a executable jar with all dependencies stored in ```./target/``` it should be 
named like ```page-generator-0.0.1-SNAPSHOT-jar-with-dependencies.jar```.

## Running page-generator with PAGE XML

Preparation of training data consists of two steps: preparation of glyphs (cutting) and 
creation of training images (generation of images). 

In order to prepare glyphs, you need to invoke:
```java -cp $PAGE_GENERATOR_JAR pl.psnc.synat.a12.aletheia.Cutter --image imageName.png --xml xmlName.xml --output ./zipWithImages.zip```

To generate images you need to:
```java -cp $PAGE_GENERATOR_JAR pl.psnc.synat.a12.generator.CLI --input zipWithImages.zip -w imageWidth -h imageHeight --output output.zip```

As a result of second step you will receive clean training image and box file which might be used to fed Tesseract 
training process. 

Full list of available features for both tools is listed after invocation of tool without any parameters.

## Running page-generator with Cutouts output

Cutouts (http://wlt.synat.pcss.pl/cutouts) application can be used to prepare training material for Tesseract. 
As a result of work with Cutouts user gets set of XML files (one file per glyph) and three images (original, 
binarized and final). XML file contains metadata related to glyph itself, things like: UTF code of a glyph, 
information whether glyph is noised, coordinates of glyph in the original image.

Page generator can produce cleaned images based on the output from Cutouts, in order to do so, 
you need to invoke it:
```java -cp $PAGE_GENERATOR_JAR .psnc.synat.a12.generator.cutouts.CutoutsCLI --input zipWithImages.zip -w imageWidth -h imageHeight --output output.zip```

Full list of available features will be listed after invocation of tool without any parameters

## TODO

* check licenses of used dependencies 
* check with Java 7 
* check with Tesseract 3.0.1
* check with Tesseract 3.0.2
* add disclaimers in code 

## Done
* get rid of System.outs/err use logger (include log4j.properties) 
* make project buildable
* dependency review
* update changes required for workflow launch
* describe building process 