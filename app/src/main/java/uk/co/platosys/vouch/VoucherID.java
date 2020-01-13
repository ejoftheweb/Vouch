package uk.co.platosys.vouch;

import java.text.ParseException;

import uk.co.platosys.minigma.BigBinary;

/**
 * A Voucher's ID can be deduced from its immutable content, of which it is a hash.
 * Underneath, it is therefore just a big binary number, from which it is instantiated although
 * it can also be instantiated from a String, in which form it is frequently stored in
 * storage systems which do not natively support the storage of arbitrary-precision big integers.
 */
public class VoucherID {
    private BigBinary bigBinary;
    public VoucherID(BigBinary bigBinary){
        this.bigBinary=bigBinary;
    }
    public VoucherID(String string) throws ParseException {
        this.bigBinary=new BigBinary(string);
    }
    @Override
    public String toString(){
        return bigBinary.toString();
    }
}
