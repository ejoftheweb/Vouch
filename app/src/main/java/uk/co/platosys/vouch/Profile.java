package uk.co.platosys.vouch;


import uk.co.platosys.minigma.BigBinary;
import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.Signature;
import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.minigma.exceptions.LockNotFoundException;
import uk.co.platosys.minigma.exceptions.MinigmaException;
import uk.co.platosys.minigma.votes.Officer;
import uk.co.platosys.minigma.votes.Voter;
import uk.co.platosys.vouch.Exceptions.VoucherNotFoundException;


/** A Profile is a Voucher that relates to a person (sole or corporate)
 *
 *
 */
public class Profile extends Voucher implements Voter {
  Lock lock;
  String userID;

    /**The constructor is private.
     *
     * @param store
     * @param parent
     */
  protected Profile(Store store, Voucher parent){
      super( store,  parent);
  }

    @Override
    public Lock getLock() {
      if(lock!=null) {
          return lock;
      }else{
          try {
              lock = store.getLock(userID);
          }catch (MinigmaException mx) {
              //TODO
              return null;
          }catch(LockNotFoundException lnfx){
              //TODO
              return null;
          }
      }
      return lock;
    }

    @Override
    public BigBinary getPaper() {
        return null;
    }

    @Override
    public Signature getPollSignature() {
        return null;
    }

    @Override
    public void notify(BigBinary paper, Officer officer, Signature signature) {

    }

    /**Use this static method to create a new profile. A profile should but need not be created and signed by
     * its subject.
     *
     * @param store
     * @param parent
     * @param name
     * @param content
     * @param lock
     * @param key
     * @param passphrase
     * @return
     * @throws BadPassphraseException
     */
    public static Profile createProfile(Store store,
                                        Voucher parent,
                                        String name,
                                        String content,
                                        Lock lock,
                                        Key key,
                                        char[] passphrase)
    throws BadPassphraseException {
       Profile profile = new Profile(store, parent);
       profile.lock=lock;
       profile.title=name;
       //TODO
       return profile;
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
