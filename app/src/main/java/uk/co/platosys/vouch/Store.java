package uk.co.platosys.vouch;

import java.util.List;

import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.LockStore;
import uk.co.platosys.minigma.Signature;
import uk.co.platosys.vouch.Exceptions.VoucherNotFoundException;

/** Stores handle transport storage and retrieval of Vouchers**/
public interface Store extends LockStore {
    /**Store implementations should notify voucher recipients when a voucher is stored
     *
     * @param voucher
     */
     Signature store (Voucher voucher);
     Voucher getVoucher(String voucherID) throws VoucherNotFoundException;
     Profile getProfile(String voucherID) throws VoucherNotFoundException;
     Group getGroup(String voucherID) throws VoucherNotFoundException;
     List<Voucher> findVouchers(String[] searchTerms);
     Lock getLock();

}
