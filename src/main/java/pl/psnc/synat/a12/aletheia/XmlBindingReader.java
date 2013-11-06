package pl.psnc.synat.a12.aletheia;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;

public abstract class XmlBindingReader {

    protected static Logger logger = Logger.getLogger(XmlBindingReader.class);

    protected final JAXBContext context;


    protected XmlBindingReader(String jaxbContextName)
            throws JAXBException {
        context = JAXBContext.newInstance(jaxbContextName);
    }


    protected void init() {
        try {
            doLoadXml();
        } catch (JAXBException e) {
            logger.error("Cannot load Xml configuration file", e);
            throw new IllegalStateException(e);
        }
        doInit();
    }


    /**
     * Load JAXB binding from specified file.
     * 
     * @param resource
     *            file
     * @throws JAXBException
     */
    protected <T> T loadXmlBinding(File resource)
            throws JAXBException {
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        @SuppressWarnings("unchecked")
        final JAXBElement<T> element = (JAXBElement<T>) unmarshaller.unmarshal(resource);
        return element.getValue();
    }


    abstract protected void doInit();


    abstract protected void doLoadXml()
            throws JAXBException;
}
