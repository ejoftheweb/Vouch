package uk.co.platosys.vouch;

import uk.co.platosys.minigma.Notation;

/**
 * The Tag class encapsulates Voucher metadata.
 */

public class Tag {
public static final String ROLE="role";
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
