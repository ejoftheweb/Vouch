package uk.co.platosys.vouch;

import org.jdom2.Document;
import org.jdom2.Element;

import java.io.IOException;

import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.utils.XMLUtils;
import uk.co.platosys.vouch.constants.XML;
import uk.co.platosys.vouch.exceptions.InvalidXMLException;

/**
 * Content is stored as a Base-64 String
 * Profile content is specifically-structured to contain:
 * (a) the subject's PGP Public Key material (as a BigBinary Base64 String) and
 * (b) a free-text description - eg bio - of the subject.
 */


public class ProfileContent extends XmlContent {
    Element rootElement;
    Element lockElement;
    Element bioElement;
    public ProfileContent (String encodedContent) throws InvalidXMLException {
        super(encodedContent);
        rootElement=getRootElement();
    }
    public ProfileContent (Document profileDocument) throws  IOException {
        super(profileDocument);
        rootElement=getRootElement();
    }
    protected ProfileContent(Lock lock, String bio){
        super();
        rootElement = new Element(XML.PROFILE_ELEMENT_NAME, XML.NS);
        lockElement = new Element(XML.LOCK_ELEMENT_NAME, XML.NS);
        bioElement = new Element(XML.BIO_ELEMENT_NAME, XML.NS);
        lockElement.setText(lock.toArmoredString());
        bioElement.setText(bio);
        rootElement=rootElement.addContent(lockElement);
        rootElement=rootElement.addContent(bioElement);
        this.document=new Document(rootElement);
        try {
            encodedContent = XMLUtils.encode(document);
        }catch (IOException iox){
            //do something about it
        }
    }
}
