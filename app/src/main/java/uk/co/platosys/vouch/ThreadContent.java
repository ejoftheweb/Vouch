package uk.co.platosys.vouch;

import org.jdom2.Element;

import java.util.List;

import uk.co.platosys.vouch.exceptions.InvalidXMLException;
import uk.co.platosys.vouch.constants.XML;

/**ThreadContent is XMLContent, specifically, Threads are represented as XML Documents as follows:
 * <Thread>
 *     <Voucher id="43characterVoucherID"/>
 * </Thread>
 *
 */
public class ThreadContent extends XmlContent {
    List<Element> voucherElements;
    public ThreadContent (String encodedContent)throws InvalidXMLException {
        super(encodedContent);
        try{
            voucherElements = document.getRootElement().getChildren(XML.THREAD_ITEM_ELEMENT_NAME, XML.NS);
        }catch(Exception x){
            throw new InvalidXMLException("error getting voucher elements", x);
        }
    }
    public List<Element> getVoucherElements(){
            return voucherElements;
    }
}
