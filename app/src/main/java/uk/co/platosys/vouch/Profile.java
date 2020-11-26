package uk.co.platosys.vouch;


import java.util.List;

import uk.co.platosys.minigma.BigBinary;
import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.Signature;
import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.minigma.exceptions.LockNotFoundException;
import uk.co.platosys.minigma.exceptions.MinigmaException;
import uk.co.platosys.minigma.votes.Officer;
import uk.co.platosys.minigma.votes.Voter;
import uk.co.platosys.vouch.exceptions.IDVerificationException;
import uk.co.platosys.vouch.exceptions.VoucherNotFoundException;


/** A Profile is a Voucher that relates to a person (sole or corporate)
 * A Profile has a userID which is a String. and should  be an email address.
 *
 *
 * A Profile also has a Lock (that is, a PGP public key). An application can instantiate a Profile, call its
 * getLock() method and then (a) encrypt content so that it can only be decrypted by the Self corresponding to
 * the Profile object; and (b) more significantly from the Vouch perspective, verify a digital signature against
 * the given Lock.
 *
 */
public class Profile extends Voucher implements Voter {
  Lock lock;
  String userID;
  BigBinary paper;

  /**Public constructor typically called by Store implementations to instantiate a Profile
     * from the store.
     *
     * @param id
     * @param title
     * @param tweet
     * @param author
     * @param publisher

     * @param signatures
     *
     * @param parent
     * @param previous
     * @param next
     * @param content
     * @param store
     * @param userID
     * @throws IDVerificationException
     */
    public Profile (VoucherID id,
                    String title,
                    String tweet,
                    VoucherID author,
                    VoucherID publisher,

                    List<Signature> signatures,
                    VoucherID parent,
                    VoucherID previous,
                    VoucherID next,
                    Content content,
                    Store store,
                    String userID)
            throws IDVerificationException {
        super(  id,
                title,
                tweet,
                author,
                publisher,

                signatures,
                parent,
                previous,
                next,
                content,
                store
        );
        this.userID=userID;
        this.lock=getLock();
    }



  /**The constructor is protected, it is called by the Factory class
     *
     * @param store
     * @param parent
     */
  protected Profile(Store store, VoucherID parent){
      super( store,  parent);
  }

    @Override
    public Lock getLock() {
      if(lock!=null) {
          return lock;
      }else{
          try {
              lock = store.getLock(userID);

          }catch(LockNotFoundException lnfx){
              //TODO
              return null;
          }catch (MinigmaException mx) {
              //TODO
              return null;
          }
      }
      return lock;
    }

    @Override
    public BigBinary getPaper() {
        return paper;
    }

    @Override
    public Signature getPollSignature() {
        return null;
    }

    @Override
    public void notify(BigBinary paper, Officer officer, Signature signature) {

      this.paper=paper;

    }



    /**
     * Use this static method to instantiate a profile when you know its ID
     * @param store
     * @param profileID
     * @return
     * @throws VoucherNotFoundException
     */
    public static Profile getProfile (Store store,
                                      VoucherID profileID)
    throws VoucherNotFoundException {
      return store.getProfile(profileID);
    }

}
