package uk.co.platosys.vouch;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.minigma.utils.XMLUtils;
import uk.co.platosys.vouch.constants.XML;
import uk.co.platosys.vouch.exceptions.InvalidContentException;
import uk.co.platosys.vouch.exceptions.VouchException;
import uk.co.platosys.vouch.exceptions.VoucherNotFoundException;

/** A Thread is a Voucher which references a sequence of Vouchers. A Thread's immutable content is a list of references to other
 * Vouchers.
 *
 * A Thread's Content is an instance of ThreadContent which extends XMLContent. We extract the associated Document
 *
 */
public class Thread extends Voucher {

    private List<Voucher> vouchers;

    protected Thread (Store store, VoucherID parent){
        super(store, parent);
    }

    public Iterator<Voucher> getVouchers(){
        return vouchers.iterator();
    }

}
