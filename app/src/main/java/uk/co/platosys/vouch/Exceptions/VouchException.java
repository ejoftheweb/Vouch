package uk.co.platosys.vouch.Exceptions;

public class VouchException extends Exception {
    public VouchException(String msg){
        super(msg);
    }
    public VouchException(String msg, Throwable cause){
        super(msg, cause);
    }
}
