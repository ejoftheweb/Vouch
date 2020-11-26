package uk.co.platosys.vouch;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.minigma.Digester;
import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Notation;
import uk.co.platosys.minigma.Signature;
import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.minigma.exceptions.MinigmaException;
import uk.co.platosys.minigma.exceptions.MinigmaOtherException;
import uk.co.platosys.vouch.exceptions.AlreadyPublishedException;
import uk.co.platosys.vouch.exceptions.IDVerificationException;
import uk.co.platosys.vouch.exceptions.VouchException;
import uk.co.platosys.vouch.exceptions.VouchRoleException;
import uk.co.platosys.vouch.exceptions.VoucherNotFoundException;

/**Voucher is a structured container for signed content. A voucher's content is immutable but its associated
 * metadata including tags etc isn't. Voucher is also the root class for other signed content, including Thread, Profile, Self, and Group.
 *
 * **Hierarchy of Voucher Classes**
 *
 *
 */
public class Voucher implements Vouched {
    VoucherID id;
    String title;
    String tweet;
    VoucherID author;
    VoucherID publisher;
    List<VoucherID> recipients;
    List<VoucherID> taggers;
    List<Signature> signatures;
    List<Tag> tags;
    VoucherID parentID;
    VoucherID previousID;
    VoucherID nextID;
    VoucherID advertID;
    Content content;
    Store store;


    /**Constructor typically called by Store implementations to instantiate a Voucher
     * from the store.
     *
     * @param id
     * @param title
     * @param tweet
     * @param author
     * @param publisher

     * @param signatures
     *
     * @param parentID
     * @param previousID
     * @param nextID
     * @param content
     * @param store
     * @throws IDVerificationException
     */
    public Voucher (VoucherID id,
                    String title,
                    String tweet,
                    VoucherID author,
                    VoucherID publisher,

                    List<Signature> signatures,
                    //List<Tag> tags,
                    VoucherID parentID,
                    VoucherID previousID,
                    VoucherID nextID,
                    Content content,
                    Store store)
            throws IDVerificationException
    {

        this.title=title;
        this.tweet=tweet;
        this.author=author;
        this.publisher=publisher;
        this.signatures=signatures;
        //this.tags=tags;
        this.parentID=parentID;
        this.previousID=previousID;
        this.nextID=nextID;
        this.content=content;
        this.store=store;
        if(id==calculateId()) {
            this.id=id;
        }else{
            throw new IDVerificationException("VoucherIDs don't match, content tampered or corrupted");
        }
        /*The tags are encoded into the Signature objects as Notations. So we have to extract them for
        display*/
        for(Signature signature:signatures){
            VoucherID tagger = Vouch.getSignerProfileID(signature);
            for (Notation notation:signature.getNotations()) {
                tags.add(new Tag(tagger, notation));
            }
        }
    }

    /**Protected constructor called by the subclasses and the Factory class
     *
     * @param store
     * @param parent
     */
      protected  Voucher (Store store, VoucherID parent){
            this.store=store;
            this.parentID=parent;
      }


    /**
     * Stores this Voucher in the store. A Voucher cannot be stored until:
     * - its ID has been calculated it is signed.
     *
     *
     * @return
     */
   private  Signature store() throws IllegalStateException {
        if (id==null){throw new IllegalStateException ("id not set");}
        if (author==null){throw new IllegalStateException ("author not set");}
        return store.store(this);
    }

    /** A Voucher is ephemeral until it is signed. Signing causes it to be stored. It can be signed
     *  any number of times by different people.
     *
     * @param self
     * @param role
     * @param passphrase
     * @return
     */
    public Signature sign(Self self,
                          Role role,
                          char[] passphrase)
            throws  VouchRoleException,
                    BadPassphraseException {
        try {
            if (id == null) {
                if (!role.equals(Role.AUTHOR)) {
                    throw new VouchRoleException("voucher must be signed by author first");
                }
                this.author = self.id;
            } else if (role.equals(Role.AUTHOR)) {
                throw new VouchRoleException("someone has already claimed authorship for this voucher");
            }
            if (id == null) {
                id = calculateId();
            }
            Key key = self.getKey();
            Tag tag = new Tag(self.id, Tag.ROLE, role.getName());
            this.tags.add(tag);
            List<Notation> notations = new ArrayList<>();
            notations.add(tag.toNotation());
            Signature signature = key.sign(content.toString(), notations, passphrase);
            this.taggers.add(self.id);
            signatures.add(signature);
            signatures.add(store());//adds the signature returned by the store. NB we should do this asynchronously (but possibly in the local store implementation??)
            return signature;
        }catch(IllegalStateException ise){//we already controlled for the condition which could throw this exception here, so it should never be thrown
            return null;
        }catch(BadPassphraseException bpe) {
          throw bpe;
        }catch (VouchRoleException vrx){
          throw vrx;
        }catch (MinigmaException mx){
          //TODO
          return null;
      }
    }

    /**
     * Tags a Voucher. Tags are always signed by the tagger.
     * @param self
     * @param tags
     * @param passphrase
     * @return
     * @throws BadPassphraseException
     */
    public Signature sign (Self self, List<Tag> tags, char[] passphrase) throws BadPassphraseException {
        List<Notation> notations=new ArrayList<>();
        Signature signature=null;
        try {
            for (Tag tag : tags) {
                if((tag.value==Tag.ROLE)&&(tag.name==Role.AUTHOR.getName())){
                    //it's an Author tag!
                    if (author!=null){
                        throw new VouchRoleException("voucher already has an author");
                    }else{
                        this.author=self.getId();
                    }
                }
                notations.add(tag.toNotation());
            }
            signature = self.getKey().sign(content.toString(), notations, passphrase);
        }catch(BadPassphraseException bpe){
            throw bpe;
        }catch (VouchRoleException vre){

        } catch(MinigmaOtherException moe){
            //TODO
        }
        if (signature!=null){
            signatures.add(signature);
            notations=signature.getNotations();
            for (Notation notation:notations){
                this.tags.add(new Tag(self.id, notation));
            }
        }
        signatures.add(store());
        return signature;
    }
    /**
     * Note the algorithm used to generate the id. Might want to tighten this up.
     *
     */
    protected VoucherID calculateId() {
        try{
            String digestedContent = parentID.toString() + content.toString();
            return new VoucherID(Digester.digest(digestedContent));
        }catch (MinigmaException mx) {
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

   protected void setTitle(String title) {
        this.title = title;
    }

    public String getTweet() {
        return tweet;
    }

   protected void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Profile getAuthorProfile()  throws VoucherNotFoundException{
        return store.getProfile(author);
    }
    public Profile getPublisherProfile()throws VoucherNotFoundException {
        return store.getProfile(publisher);
    }

    public Signature publish(Self publisher, char[] passphrase) throws AlreadyPublishedException, BadPassphraseException {
        if (this.publisher!=null)throw new AlreadyPublishedException("voucher already published");
        Signature signature=null;
        try {
            signature = this.sign(publisher, Role.PUBLISHER, passphrase);
        }catch(VouchRoleException vre){

        }catch (BadPassphraseException bpe) {
            throw bpe;
        }
        this.publisher=publisher.id;
        return signature;
    }

    public List<VoucherID> getTaggers() {
        return taggers;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Signature addTag(Tag tag, Key key, char[] passphrase){

        return null;
    }

    public Voucher getParentVoucher() throws VoucherNotFoundException {
        return store.getVoucher(parentID);
    }

   public Voucher getPreviousVoucher() throws VoucherNotFoundException {
        return store.getVoucher(previousID);
    }

    public void setPrevious(VoucherID previousID) {
        this.previousID = previousID;
    }

    public Voucher getNextVoucher() throws VoucherNotFoundException {
        return store.getVoucher(nextID);
    }

    public void setNext(VoucherID nextID) {
        this.nextID = nextID;
    }

    public Content getContent() {
        return content;
    }
    protected void setContent(Content content){
        this.content=content;
    }

    public VoucherID getId(){
           return id;
    }

    @Override
    public Credibility getCredibility(Self self) {
        return null;
    }

    public void setAdvert(VoucherID advertID, Self publisher, char[] passphrase){
        this.advertID=advertID;
        //TODO
    }
}
