package uk.co.platosys.vouch;

import uk.co.platosys.minigma.Notation;

/**
 * The Tag class encapsulates Voucher metadata.
 */

public class Tag {
public static final String ROLE="role";

    String name;
    String value;

    public Tag(String name, String value){
        this.name=name;
        this.value=value;
    }
    public Tag (Notation notation){
        this.name=notation.getName();
        this.value=notation.getValue();
    }
    public Notation toNotation(){
        return  new Notation(name, value);
    }
}
