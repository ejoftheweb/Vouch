package uk.co.platosys.vouch;


import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.minigma.BigBinary;
import uk.co.platosys.minigma.Signature;
import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.minigma.exceptions.MinigmaOtherException;
import uk.co.platosys.vouch.Exceptions.VoucherNotFoundException;

/** Vouch groups are conceptually extremely powerful.
 *
 * Note that a Group is NOT analagous to a Thread. In a Thread, the "members" are references to other Vouchers,
 * and form the immutable content.
 * In a Group, the "members" are signatories,  The Group content is immutable, and signed by the members.
 *
 * Groups are democratic! - using Minigma Votes!
 *
 */
public class Group extends Profile {
    List<Profile> members=new ArrayList<>();
    Group admins;

    private Group(Store store, Profile founder){
        super (store, founder);
    }

    public static Group getGroup(Store store, String groupID) throws VoucherNotFoundException {
        return store.getGroup(groupID);
    }

    public static Group createGroup(Store store,
                                    Self founder,
                                    String title,
                                    String tweet,
                                    String content,
                                    char[] passphrase){
        Group group = new Group(store, founder);
        group.setAuthor(founder);
        group.setTitle(title);
        group.setTweet(tweet);
        group.setContent(content);
        //TODO Sign and store it
        return group;

    }

    public Signature join(Self self, char[] passphrase) throws BadPassphraseException {
        Signature signature=null;
        try {
            signature = self.getKey().sign(getContent(), passphrase);
            members.add(self);
        }catch(MinigmaOtherException moe){
            return null;
        }
        return signature;
    }
}