package uk.co.platosys.vouch;


import java.util.Iterator;
import java.util.List;

/** A thread is a sequence of Vouchers. A thread's immutable content is a list of references to other
 * Vouchers.
 *
 */
public class Thread extends Voucher {


    private List<Voucher> thread;
    Iterator<Voucher> vouchers;

    public Thread (Store store, VoucherID parent){
        super(store, parent);
    }
    public Iterator<Voucher> getVouchers(){
        return vouchers;
    }
}
