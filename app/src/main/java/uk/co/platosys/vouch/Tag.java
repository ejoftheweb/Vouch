package uk.co.platosys.vouch;

import uk.co.platosys.minigma.Notation;

/**
 * The Tag class encapsulates Voucher metadata.
 * Voucher metadata are signed -  you have to know *who* has rated a particular object, to give credibility
 * to the tagging. Thus a Tag has three fields: tagger, name, value. The tagger is the person who allocated
 * the tag and then there's a conventional name-value pair, which is encoded as OpenPGP NotationData into the
 * signature. A Minigma Notation object wraps name-value pairs for use in Minigma signatures.
 *
 */

public class Tag {
    public static final String ROLE="role";
    public static final String TITLE="title";
    public static final String PROFILE="profile";
    public static final String EMAIL="email";
    public static final String USERID="userid";

    VoucherID tagger;
    String name;
    String value;

    public Tag(VoucherID tagger, String name, String value){
        this.tagger=tagger;
        this.name=name;
        this.value=value;
    }
    public Tag (VoucherID tagger, Notation notation){
        this.tagger=tagger;
        this.name=notation.getName();
        this.value=notation.getValue();
    }
    public Notation toNotation(){
        return  new Notation(name, value);
    }


}
