package uk.co.platosys.vouch.exceptions;

public class VoucherNotFoundException extends VouchException {
    public VoucherNotFoundException(String msg){
        super(msg);
    }
    public VoucherNotFoundException(String msg, Throwable cause){
        super(msg, cause);
    }
}

