package pl.psnc.synat.a12.aletheia.pagexml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import pl.psnc.synat.a12.generator.utils.jaxb.GlyphType;
import pl.psnc.synat.a12.generator.utils.jaxb.PageType;
import pl.psnc.synat.a12.generator.utils.jaxb.PcGtsType;
import pl.psnc.synat.a12.generator.utils.jaxb.PointType;
import pl.psnc.synat.a12.generator.utils.jaxb.ReadingOrderType;
import pl.psnc.synat.a12.generator.utils.jaxb.RegionRefIndexedType;
import pl.psnc.synat.a12.generator.utils.jaxb.TextLineType;
import pl.psnc.synat.a12.generator.utils.jaxb.TextRegionType;
import pl.psnc.synat.a12.generator.utils.jaxb.TextTypeSimpleType;
import pl.psnc.synat.a12.generator.utils.jaxb.WordType;

public class PageXmlReader extends XmlBindingReader {

    private static final String CONTEXT_NAME = "pl.psnc.synat.a12.generator.utils.jaxb";

    private static final int REPLACEMENT_CODEPOINT = 65533;

    private List<Glyph> glyphs = new ArrayList<Glyph>();

    private String[][][] orderedRegions;

    private String[][][] unOrderedRegions;

    private File resource;

    private PageType page;

    private List<String> noiseWords = Collections.emptyList();

    private List<String> tabuTypes;

    private Map<String, Integer> readingOrder = new HashMap<String, Integer>();

    private String[][] header;

    private String[][] signature;

    private int unknownCounter;


    public PageXmlReader(String filename)
            throws JAXBException {
        super(CONTEXT_NAME);
        resource = new File(filename);
    }


    @Override
    protected void doLoadXml()
            throws JAXBException {
        PcGtsType gtSource = loadXmlBinding(resource);
        page = gtSource.getPage();
    }


    @Override
    protected void doInit() {
        ReadingOrderType readingOrderType = page.getReadingOrder();

        if (readingOrderType != null) {
            process(readingOrderType);
        }

        List<Object> rawRegions = page.getTextRegionOrImageRegionOrLineDrawingRegion();
        orderedRegions = new String[readingOrder.size()][][];
        unOrderedRegions = new String[rawRegions.size() - readingOrder.size()][][];

        for (Object region : rawRegions) {
            if (region instanceof TextRegionType) {
                process((TextRegionType) region);
            }
        }
    }


    private void process(ReadingOrderType readingOrderType) {
        for (Object order : readingOrderType.getOrderedGroup()
                .getRegionRefIndexedOrOrderedGroupIndexedOrUnorderedGroupIndexed()) {

            if (order instanceof RegionRefIndexedType) {
                process((RegionRefIndexedType) order);
            }
        }
    }


    private void process(RegionRefIndexedType order) {
        int index = order.getIndex();
        Object ref = order.getRegionRef();

        if (!(ref instanceof TextRegionType)) {
            System.err.println("ERR: unknown referenced type in given reading order group, type: " + ref.getClass());
            return;
        }
        TextRegionType region = (TextRegionType) ref;
        String id = region.getId();
        readingOrder.put(id, index);
    }


    private void process(TextRegionType region) {
        TextTypeSimpleType type = region.getType();
        if (type == null) {
            System.err.println("ERR: null region type, id:" + region.getId());
            return;
        }

        if (tabuTypes.contains(type.value())) {
            System.err.println("Tabu region: " + type);
            return;
        }

        ArrayList<String[]> content = new ArrayList<String[]>();
        TextLineType[] orderedLines = orderLines(region.getTextLine(), region.getTextEquiv().getUnicode());

        for (TextLineType line : orderedLines) {
            content.add(process(line));
        }
        storeRegion(region, content.toArray(new String[][] {}));
    }


    private TextLineType[] orderLines(List<TextLineType> lines, String fulltext) {
        TextLineType[] result = new TextLineType[lines.size()];
        List<String> rightOrder = Arrays.asList(getLines(clean(fulltext)));

        for (TextLineType line : lines) {
            int index = rightOrder.indexOf(clean(line.getTextEquiv().getUnicode()));

            if (index == -1) {
                System.err.println("FATAL: cannot find right order for line id:" + line.getId());
                System.err.println(line.getTextEquiv().getUnicode());
                System.err.println(rightOrder);
                continue;
            }
            result[index] = line;
        }
        return result;
    }


    private void storeRegion(TextRegionType region, String[][] lines) {
        String regionId = region.getId();
        Integer order = readingOrder.get(regionId);
        String[][] content = getAvailableContent(region, lines);

        if (order == null) {
            switch (region.getType()) {
                case HEADER:
                    header = content;
                    break;
                case SIGNATURE_MARK:
                    signature = content;
                    break;
                default:
                    System.err.println("WARN: unknown reading order for region id: " + regionId + " type: "
                            + region.getType());
                    unOrderedRegions[unknownCounter++] = content;
            }
        } else {
            orderedRegions[order] = content;
        }
    }


    private String[][] getAvailableContent(TextRegionType region, String[][] lines) {
        if (lines.length > 0) {
            return lines;
        }
        String fulltext = region.getTextEquiv().getUnicode();
        String[] fulltextLines = getLines(fulltext);
        String[][] content = new String[fulltextLines.length][];
        int line = 0;

        for (String string : fulltextLines) {
            content[line++] = getWords(string);
        }
        return content;
    }


    private String[] process(TextLineType line) {
        List<String> selected = new ArrayList<String>();

        for (WordType word : line.getWord()) {
            final String id = word.getId();
            final boolean isNoise = noiseWords.contains(id);

            if (isNoise) {
                System.err.println("Noise word: " + id + ": " + word.getTextEquiv().getUnicode());
            } else {
                selected.add(word.getTextEquiv().getUnicode());
            }

            for (GlyphType glyph : word.getGlyph()) {
                process(glyph, isNoise);
            }
        }
        return orderSelectedWords(line.getTextEquiv().getUnicode(), selected);
    }


    private String[] orderSelectedWords(String line, List<String> selected) {
        String[] result = new String[selected.size()];
        String[] words = getWords(line);
        int count = 0;

        for (String word : words) {
            if (!selected.contains(word)) {
                continue;
            }
            result[count++] = removeReplacement(removeSurrogate(word));
        }
        if (count != result.length) {
            System.err.println("FATAL: words missing: " + Arrays.toString(result) + selected.toString());
        }
        return result;
    }


    private String[] getLines(String text) {
        return text.split("\n");
    }


    private String[] getWords(String line) {
        return line.split(" ");
    }


    private String removeReplacement(String word) {
        return word.replaceAll(new String(new int[] { REPLACEMENT_CODEPOINT }, 0, 1), "");
    }


    private String removeSurrogate(String word) {
        return word.replaceAll(new String(new int[] { 0x7a, 0x0304 }, 0, 2), "ż");
    }


    private String clean(String word) {
        return word.replace(new String(new int[] { 0x0304 }, 0, 1), "");
    }


    private void process(GlyphType glyph, boolean isNoise) {
        String value = glyph.getTextEquiv().getUnicode();
        int unicode = value.codePointAt(0);

        if (Character.codePointCount(value, 0, value.length()) != 1) {
            int secondUnicode = value.codePointAt(Character.charCount(unicode));
            System.err.println("ERR: number of code points exceeds one, id: " + glyph.getId());
            System.err.println("ERR: unicode points: " + unicode + " " + secondUnicode);

            if (unicode == 0x7a && secondUnicode == 0x0304) {
                unicode = 0x017c;
                System.err.println("WARN: x7a x0304 codepoints replaced with x017c codepoint (ż)");
            }
        }

        if (Character.isSupplementaryCodePoint(unicode)) {
            System.err.println("WARN: Supplementary code point found: " + unicode);
        }

        if (REPLACEMENT_CODEPOINT != unicode) {
            try {
                Glyph glyphDescription = createGlyphDescription(glyph, unicode, isNoise);
                glyphs.add(glyphDescription);
            } catch (IllegalArgumentException e) {
                System.err.println("ERR: cannot create glyph for id: " + glyph.getId());
                System.err.println("Reason: " + e.getMessage());
            }
        }
    }


    private Glyph createGlyphDescription(GlyphType glyph, int unicode, boolean isNoise) {
        List<PointType> coords = glyph.getCoords().getPoint();
        int[] xCoord = new int[coords.size()];
        int[] yCoord = new int[coords.size()];
        int counter = 0;

        for (PointType pointType : coords) {
            xCoord[counter] = pointType.getX();
            yCoord[counter] = pointType.getY();
            counter++;
        }
        return new Glyph(glyph.getId(), unicode, isNoise, xCoord, yCoord);
    }


    public void read(List<String> tabuWords, List<String> tabuTypes) {
        this.noiseWords = tabuWords;
        this.tabuTypes = tabuTypes;
        super.init();
    }


    public List<Glyph> getGlyphs() {
        return glyphs;
    }


    public String[][][] getOrderedRegions() {
        return orderedRegions;
    }


    public String[][][] getUnOrderedRegions() {
        return unOrderedRegions;
    }


    public int glyphsCount() {
        return glyphs.size();
    }


    public class Glyph {

        private final String id;
        private final int[] xCoordinates;
        private final int[] yCoordinates;
        private final int unicode;
        private final boolean noise;


        public Glyph(String id, int unicode, boolean noise, int[] xCoord, int[] yCoord) {
            if (xCoord.length != yCoord.length) {
                throw new IllegalArgumentException("Number of coordinates for each axis must be equal");
            }
            if (xCoord.length < 3) {
                throw new IllegalArgumentException("There should be at least three point given");
            }

            this.id = id.substring(1);
            this.unicode = unicode;
            this.noise = noise;
            this.xCoordinates = xCoord;
            this.yCoordinates = yCoord;
        }


        public String getId() {
            return id;
        }


        public int[] xCoord() {
            return xCoordinates;
        }


        public int[] yCoord() {
            return yCoordinates;
        }


        public int getUnicodePoint() {
            return unicode;
        }


        public boolean isNoise() {
            return noise;
        }
    }


    public String[][] getHeader() {
        return header;
    }


    public String[][] getSignature() {
        return signature;
    }
}
