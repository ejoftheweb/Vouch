package uk.co.platosys.vouch;

import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.vouch.constants.Data;
import uk.co.platosys.vouch.exceptions.InvalidContentException;

/**ProfileFactory is used to create Profiles in three different circumstances:
 * First, Case A, when the user and the profile subject are the same person and this is their first and only Profile so far;
 * Second, Case B, to create a subsequent (public) Profile for the same person  and
 * Third, Case C, to create a Profile for a different person - a Third Party Profile.
 *
 * It is arguable whether Case C, Third Party Profiles, should be supported at all. Deploying them for anything
 * other than private purposes raises major data protection regulatory issues. *However*, once signed by the
 * profile subject that problem, while it doesn't go away entirely, becomes much more tractable.
 *
 *
 */

public class ProfileFactory extends VoucherFactory {
    private Profile profile;
    Lock lock; //the Lock to be attached to the Profile being created
    Element profileElement;
    Document profileContentDocument;
    String bio;
    VoucherID profileID;

    private ProfileFactory(Store store){
        super(store);
    }
    public static ProfileFactory getInstance (Store store){
        return new ProfileFactory(store);
    }

    /**sets the Factory to create a Profile having only the root Voucher as a parent
     *Use for Case A, when the user and the profile subject are the same person and this is their only Profile
     *  so far.
     *  The Factory is initialised by passing it a username, a Lock and a Key.
     */
    public void initialise (String username, Lock lock){
        initialise(VoucherID.getRootID(), username, lock);
    }

    /**sets the Factory to create a Profile as a child of the given Voucher
     * Use this method for Case B, e.g. to create a public Profile derived from a private one,
     * and for Case C.
     */
    public void initialise(VoucherID parentID, String username, Lock lock){
        this.parentID=parentID;
        this.lock=lock;
        this.voucher=new Voucher(store, parentID);
        this.profile=(Profile) voucher;
        this.tagList=new ArrayList<>();

    }

    /**Overridden method from the superclass, always throws an IllegalStateException. For Profiles, content is the Lock and the
     * bio, setBio() should be called at the same stage of the workflow as setContent for regular Vouchers
     *
      * @param content
     * @throws IllegalStateException
     * @throws InvalidContentException
     */
    @Override
    public void setContent(Content content) throws IllegalStateException, InvalidContentException {
        throw new IllegalStateException("cannot set profile content, use setBio instead");
    }

    /**
     * Sets the bio for this Profile. The bio is part of the immutable content (together with the subject's PGP PublicKey)
     * @param bio
     * @throws IllegalStateException
     */
    public void setBio (String bio) throws IllegalStateException {
        if(this.profile==null){throw new IllegalStateException("Profile Factory must be initialised before calling setBio");}
        profile.setContent(new ProfileContent(lock, bio));
        //once the Content has been set we can calculate the VoucherID (profileID)
        this.profileID=profile.calculateId();
        //and given the profileID we can now populate the tagList with the userid tags extracted from the Lock (PGP PublicKey)
        List<String> userids = lock.getUserIDs();
        for(String userid:userids) {
            if (userid.matches(Data.EMAIL_REGEXP)){
                tagList.add(new Tag (profileID, Tag.EMAIL, userid));
            }else{
                tagList.add(new Tag(profileID, Tag.USERID, userid));
            }
        }
    }
    public Profile build (Self author, char[] passphrase){
        return null;
    }
    public Profile build (char[] passphrase) throws BadPassphraseException{
        super.build(passphrase);
        return profile;
    }
    public Self buildSelf (Key key, char[] passphrase)throws BadPassphraseException, IllegalStateException {
        if (profile==null){throw new IllegalStateException("voucherFactory not initialised");}
        if (profile.getContent()==null){throw new IllegalStateException("ProfileFactory: buildSelf called before setting bio");}
        Self self = (Self) profile;
        self.setKey(key);
        //Sign
        try {
            self.sign(self, tagList, passphrase);
        } catch(BadPassphraseException bpe){
            throw bpe;
        }
        return self;
    }
}

