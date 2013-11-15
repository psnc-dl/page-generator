package pl.psnc.synat.a12.generator.cutouts;

import java.io.File;
import javax.xml.bind.JAXBContext;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class CutoutsXmlReader {

    private static final String CONTEXT_NAME = "pl.psnc.synat.a12.generator.utils.jaxb";
    protected final JAXBContext context;
    private final File resource;
    
    public CutoutsXmlReader(String filename)
            throws JAXBException {
        context = JAXBContext.newInstance(CONTEXT_NAME);
        resource = new File(filename);
    }
    
    public pl.psnc.synat.a12.generator.utils.jaxb.LetterBox read()
            throws JAXBException {
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        return (pl.psnc.synat.a12.generator.utils.jaxb.LetterBox) unmarshaller.unmarshal(resource);
    }
    
}
