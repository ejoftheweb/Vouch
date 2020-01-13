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
 /**
  *
  * @param voucher
  * @return
  */
 Signature store (Voucher voucher);

 /**
  *
  * @param profile
  * @return
  */
     Signature store(Profile profile);

 /**
  *
  * @param group
  * @return
  */
 Signature store (Group group);
     Voucher getVoucher(VoucherID voucherID) throws VoucherNotFoundException;
     Profile getProfile(VoucherID voucherID) throws VoucherNotFoundException;
     Group getGroup(VoucherID voucherID) throws VoucherNotFoundException;
     List<Profile> getProfiles(String name);
     List<Group> getGroups(String name);
     List<Voucher> findVouchers(String[] searchTerms);
     List<Signature> getSignatures(VoucherID voucherID);
     Lock getLock();

}
