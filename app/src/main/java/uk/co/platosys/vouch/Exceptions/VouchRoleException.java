package uk.co.platosys.vouch.Exceptions;

public class VouchRoleException extends VouchException {
    public VouchRoleException(String msg){
        super(msg);
    }
    public VouchRoleException(String msg, Throwable cause){
        super(msg, cause);
    }
}
