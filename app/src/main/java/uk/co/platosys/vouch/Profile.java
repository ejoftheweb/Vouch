package uk.co.platosys.vouch;


import uk.co.platosys.minigma.BigBinary;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.Signature;
import uk.co.platosys.minigma.exceptions.LockNotFoundException;
import uk.co.platosys.minigma.exceptions.MinigmaException;
import uk.co.platosys.minigma.votes.Officer;
import uk.co.platosys.minigma.votes.Voter;


/** A Profile is a Voucher that relates to a person (sole or corporate)
 *
 *
 */
public class Profile extends Voucher implements Voter {
  Lock lock;
  String userID;

  public Profile(){
      super();
  }

    @Override
    public Lock getLock() {
      if(lock!=null) {
          return lock;
      }else{
          try {
              lock = lockstore.getLock(userID);
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
}
