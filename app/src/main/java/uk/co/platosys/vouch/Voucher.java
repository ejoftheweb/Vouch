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
import uk.co.platosys.vouch.Exceptions.VouchException;
import uk.co.platosys.vouch.Exceptions.VouchRoleException;
import uk.co.platosys.vouch.Exceptions.VoucherNotFoundException;


public class Voucher  {
    BigBinary id;
    String title;
    String tweet;
    Profile author;
    Profile publisher;
    List<Profile> recipients;
    List<Profile> taggers;
    List<Signature> signatures;
    List<Tag> tags;
    Voucher parent;
    Voucher previous;
    Voucher next;
    String content;
    Store store;

    /**
     * Instantiates an empty Voucher
     * @param store
     */
    public Voucher(Store store, Voucher parent){
        this.store=store;
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
    public Signature sign(Self self, Role role,  char[] passphrase)throws VouchRoleException, BadPassphraseException {
      try {
           if(id==null){
               if(!role.equals(Role.AUTHOR)){
                   throw new VouchRoleException("voucher must be signed by author first");
               }
               this.author=self;
           }else if(role.equals(Role.AUTHOR)){
               throw new VouchRoleException("someone has already claimed authorship for this voucher");
           }
           setId();//method does nothing if the id has already been set.
           Key key = self.getKey();
           Tag tag = new Tag(Tag.ROLE,role.getName());
           this.tags.add(tag);
           List<Notation> notations = new ArrayList<>();
           notations.add(tag.toNotation());
           Signature signature = key.sign(content, notations, passphrase);
           this.taggers.add(self);
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
    public Signature tag(Self self, List<Tag> tags, char[] passphrase) throws BadPassphraseException {
        List<Notation> notations=new ArrayList<>();
        Signature signature=null;
        try {
            for (Tag tag : tags) {
                notations.add(tag.toNotation());
            }
            signature = self.getKey().sign(content, notations, passphrase);
        }catch(BadPassphraseException bpe){
            throw bpe;
        }catch(MinigmaOtherException moe){
            //TODO
        }
        if (signature!=null){
            signatures.add(signature);
            notations=signature.getNotations();
            for (Notation notation:notations){
                this.tags.add(new Tag(notation));
            }
        }
        return signature;
    }
    /**
     * Note the algorithm used to generate the id. Might want to tighten this up.
     *
     */
    private void setId() {
        if (id==null) {
            try {
                String digestedContent = parent.getId().toString() + content;
                this.id = Digester.digest(digestedContent);
            } catch (MinigmaException mx) {
                //TODO
            }
        }//else it's already set, we don't need to do anything. Don't waste cycles.
    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Profile getAuthor() {
        return author;
    }

    public void setAuthor(Profile author) {
        this.author = author;
    }

    public Profile getPublisher() {
        return publisher;
    }

    public void setPublisher(Profile publisher) {
        this.publisher = publisher;
    }

    public List<Profile> getTaggers() {
        return taggers;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Signature addTag(Tag tag, Key key, char[] passphrase){
        return null;
    }

    public Voucher getParent() {
        return parent;
    }

    public void setParent(Voucher parent) {
        this.parent = parent;
    }

    public Voucher getPrevious() {
        return previous;
    }

    public void setPrevious(Voucher previous) {
        this.previous = previous;
    }

    public Voucher getNext() {
        return next;
    }

    public void setNext(Voucher next) {
        this.next = next;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigBinary getId(){
           return id;
    }

}
