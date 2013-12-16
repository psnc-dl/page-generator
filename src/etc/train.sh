# Path to Tesseract instalation
FONTNAME=test

# paths with tesseract's binaries and shared data
TESS_BIN=/usr/bin
TESS_SHARE=./

# document specific settings
LANG=pl

###############################################
NAME=$LANG.$FONTNAME.exp
TIFNAME=.png

echo "combined 0 0 0 0 1" >font_properties
./unicharset_extractor *.box 

for x in `cat files.txt` 
do
 echo "tesseract training $x$TIFNAME $NAME$x"
 tesseract $x$TIFNAME $NAME$x nobatch box.train.stderr
done

cat *.tr >combined.tr
mftraining -F font_properties -U unicharset -O $NAME.unicharset combined.tr 
cntraining combined.tr

mv pffmtable $NAME.pffmtable
mv normproto $NAME.normproto
mv inttemp $NAME.inttemp
wordlist2dawg words.list $LANGNAME.word-dawg $NAME.unicharset

combine_tessdata $NAME.

#sudo cp $NAME.traineddata /usr/local/share/tessdata

#rm *.tr
#rm Microfeat
#rm $NAME.pffmtable
#rm $NAME.normproto
#rm $NAME.inttemp
#rm $NAME.normproto
#rm $NAME.unicharset
