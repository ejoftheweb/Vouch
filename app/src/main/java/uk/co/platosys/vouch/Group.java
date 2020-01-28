package uk.co.platosys.vouch;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.platosys.minigma.BigBinary;
import uk.co.platosys.minigma.Signature;
import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.minigma.exceptions.MinigmaException;
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
    Set<Profile> members=new HashSet<>();
    List<Signature> signatures = new ArrayList<>();
    Set<Profile> admins=new HashSet<>();
    boolean needsApproval = false;

    private Group(Store store, VoucherID founder){
        super (store, founder);
    }

    /**Use this static method to instantiate an existing Group. If the group has not been created, it
     * will throw a VoucherNotFoundException
     *
     * @param store
     * @param groupID
     * @return
     * @throws VoucherNotFoundException
     */
    public static Group getGroup(Store store, VoucherID groupID) throws VoucherNotFoundException {
        return store.getGroup(groupID);
    }

    /** Use this static method to create a new Group.
     *
     * @param store the Store where the Group is to be held
     * @param founder the (Self) Profile of the person creating it
     * @param title the Group's title
     * @param tweet the Tweet relating to the Group
     * @param content the Group's content (typically its contract/constitution)
     * @param needsApproval whether new members need the approval of the Group admins
     * @param passphrase the founder's passphrase (so it can be signed)
     * @return the newly-created Group object.
     * @throws BadPassphraseException
     */
    public static Group createGroup(Store store,
                                    VoucherID founder,
                                    String title,
                                    String tweet,
                                    Content content,
                                    boolean needsApproval,
                                    char[] passphrase) throws BadPassphraseException{
        Group group = new Group(store, founder);
        group.setAuthor(founder);
        group.setTitle(title);
        group.setTweet(tweet);
        group.setContent(content);
        group.needsApproval=needsApproval;
        group.create(founder, store, passphrase);
        return group;

    }

    private Signature create(VoucherID selfid, Store store, char[] passphrase) throws BadPassphraseException {
        Signature signature=null;
        try {
            Self self = store.getSelf(selfid);
            signatures.add(self.getKey().sign(getContent().toString(), passphrase));
            members.add(self);
            admins.add(self);

            return store.store(this);
        }catch(VoucherNotFoundException vnfe) {
            //TODO
            return null;
        }catch(MinigmaException moe){
            //TODO
            return null;
        }
    }

    public Signature join(Self self, Store store, char[] passphrase) throws BadPassphraseException {
        Signature signature=null;
        try {
            signatures.add(self.getKey().sign(getContent().toString(), passphrase));
            members.add(self);
            return store.store(this);
        }catch(MinigmaException moe){
            return null;
        }

    }
}
