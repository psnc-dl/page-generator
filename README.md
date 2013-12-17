## Build from scratch

In order to build page-generator from scratch you will need Apache Maven, go to the main
project folder (where the pom.xml is) and invoke: 
```mvn assembly:assembly```
As a result you will get a executable jar with all dependencies stored in ```./target/``` it should be 
named like ```page-generator-jar-with-dependencies.jar```.

## Running page-generator with PAGE XML

Preparation of training data consists of two steps: preparation of glyphs (cutting) and 
creation of training images (generation of images). In the examples below ```$PAGE_GENERATOR_JAR``` 
is instead of real jar name.

In order to prepare glyphs, you need to invoke:
```java -cp $PAGE_GENERATOR_JAR pl.psnc.synat.a12.aletheia.Cutter --image imageName.png --xml xmlName.xml --output ./zipWithImages.zip```

To generate images you need to:
```java -cp $PAGE_GENERATOR_JAR pl.psnc.synat.a12.generator.CLI --input zipWithImages.zip -w imageWidth -h imageHeight --output ./output.zip```

As a result of second step you will get zip archive with cleaned training image and box file which might be used to fed Tesseract 
training process. 

Full list of available features for both tools is listed after invocation of tool without any parameters.

## Running page-generator with Cutouts output

Cutouts (http://wlt.synat.pcss.pl/cutouts) application can be used to prepare training material for Tesseract. 
As a result of work with Cutouts user gets set of XML files (one file per glyph) and three images (original, 
binarized and final). XML file contains metadata related to glyph itself, things like: UTF code of a glyph, 
information whether glyph is noised, coordinates of glyph in the original image.

Page generator can produce cleaned images based on the output from Cutouts, in order to do so, 
you need to invoke it:
```java -cp $PAGE_GENERATOR_JAR pl.psnc.synat.a12.generator.cutouts.CutoutsCLI --input zipWitCutoutsOutput.zip --output output```

Full list of available features will be listed after invocation of tool without any parameters

## TODO

* check with Tesseract 3.0.1
* check with Tesseract 3.0.2
* describe LetterBrowser
* document usage of pl.psnc.synat.a12.evaluation.Diff
* document usage of pl.psnc.synat.a12.generator.custom.CustomTextCLI
* document usage of pl.psnc.synat.a12.gui.LettersBrowser
* document usage of pl.psnc.synat.a12.aletheia.XmlToTxt

## Done
* check with Java 7 
* get rid of System.outs/err use logger (include log4j.properties) 
* make project buildable
* dependency review
* update changes required for workflow launch
* describe building process 
* check licenses of used dependencies 


## Acknowledgements
The tool is developed by [Digital Libraries Team](http://dl.psnc.pl/) of 
[Poznań Supercomputing and Networking Center](http://www.man.poznan.pl/).

This project was initiated and partially funded as a part of the 
[Synat](www.synat.pl) project, funded by Polish National Centre 
of Research and Development.

This project was partially funded as a part of the 
[Succeed](succeed-project.eu) project, funded by the European Union.

## Licence
Copyright (c) 2013 Poznań Supercomputing and Networking Center  
Licensed under the [EUPL, Version 1.1](https://joinup.ec.europa.eu/software/page/eupl/licence-eupl). 

"Compatible Licences" according to article 5 EUPL are: GNU General Public License (GNU GPL) v. 2, Open Software License (OSL) v. 2.1, v. 3.0, Common Public License v. 1.0, Eclipse Public License v. 1.0, Cecill v. 2.0.
