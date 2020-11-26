package uk.co.platosys.vouch;

import org.jdom2.Document;
import org.jdom2.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.minigma.utils.XMLUtils;
import uk.co.platosys.vouch.constants.XML;
import uk.co.platosys.vouch.exceptions.InvalidContentException;
import uk.co.platosys.vouch.exceptions.VouchException;

public class ThreadFactory extends VoucherFactory {


    Thread thread;
    Element threadElement;
    Document threadContentDocument;


    private ThreadFactory(Store store){
        super(store);

    }

    /**To create a Thread, you need a ThreadFactory instance, which you can obtain by
     * calling getInstance(Store).
     * You then initialise the Factory by passing it a VoucherID, which is the VoucherID of its parent.
     *
     *
     * @param store
     * @return
     */
    public static ThreadFactory getInstance(Store store){
        return new ThreadFactory(store);
    }
    public void initialise(VoucherID parentID, Self author){
        super.initialise(author, parentID);
        this.thread=(Thread) voucher;
        threadElement=new Element(XML.THREAD_ELEMENT_NAME, XML.NS);
        threadContentDocument = new Document(threadElement);
    }


    public void addVoucher(Voucher voucher)throws IllegalStateException, VouchException {
        Element threadItemElement=new Element(XML.THREAD_ITEM_ELEMENT_NAME, XML.NS);
        threadItemElement.setAttribute(XML.THREAD_ITEM_ATTRIBUTE_NAME, voucher.getId().toString());
        threadElement.addContent(threadItemElement);
        try {
            thread.setContent(new XmlContent(threadContentDocument));
        }catch(IOException iox){
            throw new VouchException("error setting thread content in addVoucher method", iox);
        }
    }
    public Thread build(char[] passphrase) throws IllegalStateException, BadPassphraseException{
        super.build(passphrase);
        return thread;
    }

    /**Called on ThreadFactory, this method always throws an IllegalStateException. Thread Content is
     * built by adding Vouchers to the Factory instead.
     * @param content
     * @throws IllegalStateException
     * @throws InvalidContentException
     */
    @Override
    public void setContent(Content content) throws IllegalStateException, InvalidContentException {
        throw new IllegalStateException("cannot set thread content, use addVoucher instead");
    }


}
