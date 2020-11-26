package uk.co.platosys.vouch;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.vouch.exceptions.InvalidAttributeException;
import uk.co.platosys.vouch.exceptions.InvalidContentException;
import uk.co.platosys.vouch.exceptions.VouchRoleException;

/**VoucherFactory and its subclasses produce Vouchers. Factory instances are reusable, lightweight objects,
 * attached to an underlying Store instance.
 *
 * Factory classes are not thread safe.
 *
 * A Voucher is produced first by initialising the Factory class with the parentID of the voucher to
 * be produced (every Voucher has a parent, except the root Voucher), adding content and metadata, then
 * calling the build method which returns the signed Voucher and stores it.
 *
 * The methods in this class are listed in the order in which they should be called. Note that
 * initialise(), setContent() and build() MUST be called; setTitle(), addTag() and setTweet() MAY be called.
 *
 * VoucherFactory will throw an IllegalStateException if called out of sequence.
 */
public class VoucherFactory {
    Store store; //the Store instance where this Factory stores the Vouchers it creates
    VoucherID parentID; //the VoucherID of the parent of the Voucher this Factory is making
    Voucher voucher;//the Voucher this Factory is making
    String title;//The title of the Voucher this Factory is making
    List<Tag> tagList;
    String tweet;
    Content content;
    Self author;
    protected VoucherFactory(Store store){
        this.store=store;
    }
    //initialise first
    public void initialise(Self author, VoucherID parentID){
        this.parentID=parentID;
        this.author=author;
        this.voucher=new Voucher(store, parentID);
        this.tagList=new ArrayList<>();
        tagList.add(new Tag(author.getId(), Tag.ROLE, Role.AUTHOR.getName()));
    }
    public void setTitle(String title)throws IllegalStateException, InvalidAttributeException{
        if (voucher==null){throw new IllegalStateException("voucherFactory not initialised");}
        tagList.add(new Tag(author.getId(), Tag.TITLE, title));
        voucher.setTitle(title);
    }

    public void setContent (Content content) throws IllegalStateException, InvalidContentException {
        if (voucher==null){throw new IllegalStateException("voucherFactory not initialised");}
        voucher.setContent(content);
    }
    public void addTag(Tag tag) throws IllegalStateException {
        if (voucher==null){throw new IllegalStateException("voucherFactory not initialised");}
        tagList.add(tag);
    }
    public void setTweet(String tweet)throws IllegalStateException, InvalidAttributeException {
        if (voucher==null){throw new IllegalStateException("voucherFactory not initialised");}
        voucher.setTweet(tweet);
    }
    public Voucher build (char[] passphrase)throws BadPassphraseException, IllegalStateException {
        if (voucher==null){throw new IllegalStateException("voucherFactory not initialised");}
        if (voucher.getContent()==null){throw new IllegalStateException("voucherFactory: build called before setting voucher content");}
        //Calculate ID
        VoucherID voucherID = voucher.calculateId();
        //Sign
            try {
                voucher.sign(author, tagList, passphrase);
            } catch(BadPassphraseException bpe){
                throw bpe;
            }
        return voucher;
    }
}
