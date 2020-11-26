package uk.co.platosys.vouch;

import java.util.List;

import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.LockStore;
import uk.co.platosys.minigma.Signature;
import uk.co.platosys.vouch.exceptions.VoucherNotFoundException;

/** Stores handle transport storage and retrieval of Vouchers**/
public interface Store extends LockStore {
    /**Store implementations should notify voucher recipients when a voucher is stored
     *
     * @param voucher
     */
 /**
  * Stores the Voucher and returns a Signature, being the Store's
  * signature of the Voucher
  * @param voucher
  * @return
  */
 Signature store (Voucher voucher);

 /**
  * Stores the Profile.
  * @param profile
  * @return
  */
     Signature store(Profile profile);

 /**
  * Stores the Group and its associated list of members.
  * @param group
  * @return
  */
 Signature store (Group group);

    /**
     *  Retrieves a voucher with the given VoucherID
     * @param voucherID
     * @return
     * @throws VoucherNotFoundException
     */
     Voucher getVoucher(VoucherID voucherID) throws VoucherNotFoundException;

    /**
     * Retrieves a Profile with the given VoucherID
     * @param voucherID
     * @return
     * @throws VoucherNotFoundException
     */
    Profile getProfile(VoucherID voucherID) throws VoucherNotFoundException;
    Self getSelf(VoucherID voucherID) throws VoucherNotFoundException;
    /**
     * Retrieves the Group with the given ID
      * @param voucherID
     * @return
     * @throws VoucherNotFoundException
     */
    Group getGroup(VoucherID voucherID) throws VoucherNotFoundException;

    /**
     * Returns a List of Profiles having the given name. Often there will only be one, but Profile names are
     * not guaranteed to be unique.
     * @param name
     * @return
     */
     List<Profile> getProfiles(String name);

    /**
     * Returns a List of Groups having the given name.
     * @param name
     * @return
     */
     List<Group> getGroups(String name);

    /**
     * Returns the Vouchers matching the given search terms.
     * @param searchTerms
     * @return
     */
     List<Voucher> findVouchers(String[] searchTerms);

    /**
     * Returns a list of Signatures of the supplied VoucherID
     * @param voucherID
     * @return
     */
     List<Signature> getSignatures(VoucherID voucherID);

    /**
     * Returns the Lock with which this Store signs the Vouchers it stores.
     * @return
     */
    Lock getLock();
    /**
     *
     */
    void addRemoteStore(Store store);

}
