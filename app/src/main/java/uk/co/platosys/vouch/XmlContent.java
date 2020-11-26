package uk.co.platosys.vouch;

import org.jdom2.Document;
import org.jdom2.Element;

import java.io.IOException;

import uk.co.platosys.minigma.utils.XMLUtils;
import uk.co.platosys.vouch.exceptions.InvalidXMLException;

/**In Vouch, all content is by default XmlContent, so this is the only concrete implementation of Content
 * (and binary content can be embedded in XML as Base64 text)
 */
public class XmlContent extends Content {
    Document document;
    public XmlContent (String encodedContent)throws InvalidXMLException {
        super(encodedContent);
        try {
            this.document = XMLUtils.decode(encodedContent);
        }catch(uk.co.platosys.minigma.exceptions.InvalidXMLException ixe){
            throw new InvalidXMLException("invalid xml", ixe );
        }
    }
    public XmlContent (Document document) throws IOException {
        super(XMLUtils.encode(document));
    }

    /**
     * package-protected constructor used by sub-classes
     */
    protected XmlContent(){

    }
    public Document getDocument(){
        return document;
    }
    public Element getRootElement(){
        return document.getRootElement();
    }
}

