package uk.co.platosys.vouch;

import androidx.room.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.minigma.BigBinary;
import uk.co.platosys.minigma.Digester;
import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Notation;
import uk.co.platosys.minigma.Signature;
import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.minigma.exceptions.MinigmaException;
import uk.co.platosys.minigma.exceptions.MinigmaOtherException;
import uk.co.platosys.vouch.Exceptions.AlreadyPublishedException;
import uk.co.platosys.vouch.Exceptions.IDVerificationException;
import uk.co.platosys.vouch.Exceptions.VouchException;
import uk.co.platosys.vouch.Exceptions.VouchRoleException;
import uk.co.platosys.vouch.Exceptions.VoucherNotFoundException;


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
    VoucherID parent;
    VoucherID previous;
    VoucherID next;
    VoucherID advert;
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
     * @param parent
     * @param previous
     * @param next
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
                    VoucherID parent,
                    VoucherID previous,
                    VoucherID next,
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
        this.parent=parent;
        this.previous=previous;
        this.next=next;
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

    /**Protected constructor called by the subclasses
     *
     * @param store
     * @param parent
     */
      protected  Voucher (Store store, VoucherID parent){
            this.store=store;
            this.parent=parent;
      }

    /**Static method used to get a Voucher from a store. Alternatively, call getVoucher(VoucherID) on the
     * Store object which is all this method does.
     * @param store
     * @param voucherID
     * @return
     * @throws VoucherNotFoundException
     */
    public static Voucher getVoucher (Store store, VoucherID voucherID) throws VoucherNotFoundException{
            return store.getVoucher(voucherID);
    }

    /**Static method to create a new unpublished Voucher
     *
     * @param store
     * @param parent
     * @param title
     * @param tweet
     * @param author
     * @param content
     * @return
     */
    public static Voucher createVoucher (Store store,
                                         VoucherID parent,
                                         String title,
                                         String tweet,
                                         Self author,
                                         Content content,
                                         char[] passphrase)
    throws BadPassphraseException
    {
            Voucher voucher = new Voucher(store, parent);
            voucher.setTitle(title);
            voucher.setTweet(tweet);
            voucher.setContent(content);
            try {
                voucher.sign(author, Role.AUTHOR, passphrase);
            }catch(VouchRoleException vre){
                //TODO? should not be thrown here! debug log it at best.
            }
            return voucher;
    }
    /**
     * Stores this Voucher in the store. A Voucher cannot be stored until it is signed.
     *
     *
     * @return
     */
   private  Signature store() throws VouchException {
        if (id==null){
            throw new VouchException ("id not set");
        }
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
           if(id==null){
               if(!role.equals(Role.AUTHOR)){
                   throw new VouchRoleException("voucher must be signed by author first");
               }
               this.author=self.id;
           }else if(role.equals(Role.AUTHOR)){
               throw new VouchRoleException("someone has already claimed authorship for this voucher");
           }
           if(id==null){
               id=calculateId();
           }
           Key key = self.getKey();
           Tag tag = new Tag(self.id, Tag.ROLE,role.getName());
           this.tags.add(tag);
           List<Notation> notations = new ArrayList<>();
           notations.add(tag.toNotation());
           Signature signature = key.sign(content.toString(), notations, passphrase);
           this.taggers.add(self.id);
           signatures.add(signature);
           return signature;
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
    public Signature tag(Self self, List<Tag> tags, char[] passphrase) throws BadPassphraseException {
        List<Notation> notations=new ArrayList<>();
        Signature signature=null;
        try {
            for (Tag tag : tags) {
                notations.add(tag.toNotation());
            }
            signature = self.getKey().sign(content.toString(), notations, passphrase);
        }catch(BadPassphraseException bpe){
            throw bpe;
        }catch(MinigmaOtherException moe){
            //TODO
        }
        if (signature!=null){
            signatures.add(signature);
            notations=signature.getNotations();
            for (Notation notation:notations){
                this.tags.add(new Tag(self.id, notation));
            }
        }
        return signature;
    }
    /**
     * Note the algorithm used to generate the id. Might want to tighten this up.
     *
     */
    private VoucherID calculateId() {
        try{
            String digestedContent = parent.toString() + content.toString();
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

    public void setAuthor(VoucherID author) {
        this.author = author;
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
        return getVoucher(store, parent);
    }

   public Voucher getPreviousVoucher() throws VoucherNotFoundException {
        return getVoucher(store, previous);
    }

    public void setPrevious(VoucherID previous) {
        this.previous = previous;
    }

    public Voucher getNextVoucher() throws VoucherNotFoundException {
        return getVoucher(store, next);
    }

    public void setNext(VoucherID next) {
        this.next = next;
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

    public void setAdvert(VoucherID advert, Self publisher, char[] passphrase){
        this.advert=advert;
        //TODO
    }
}
