#!/bin/bash

if [ $1 = '']; then
  echo "in case of cutter: ./generate.sh -c filename"
  echo "in case of cli: ./generate.sh -g filename image_width image_height" 
  exit
fi 

PAGE_GENERATOR_JAR=./page-generator.jar
CUTTER=pl.psnc.synat.a12.aletheia.Cutter
GENERATE=pl.psnc.synat.a12.generator.CLI

if [ $1 = '-c' ]; then
  java -cp $PAGE_GENERATOR_JAR $CUTTER --image $2.png --xml $2.xml --output ./$2.zip
elif [ $1 = '-g' ]; then
  java -cp $PAGE_GENERATOR_JAR $GENERATE --input $2.zip -w $3 -h $4 --output ./$2_out.zip
fi
