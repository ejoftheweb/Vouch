package uk.co.platosys.vouch;

import java.text.ParseException;

import uk.co.platosys.minigma.BigBinary;
import uk.co.platosys.vouch.constants.Data;

/**
 * A Voucher's ID can be deduced from its immutable content and its parent's ID, the concantenation of which it is a hash.
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

    public static VoucherID getRootID(){
        byte[] bytes = {0};
        BigBinary vid=new BigBinary(bytes);
        try{
            vid=new BigBinary(Data.ROOT_ID);
        }catch (ParseException  px){

        }
        return new VoucherID(vid);
    }
}
