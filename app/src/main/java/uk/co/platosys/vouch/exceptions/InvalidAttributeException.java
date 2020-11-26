package uk.co.platosys.vouch.exceptions;

/**
 * Exception thrown when attempting to set an invalid Voucher attribute
 */
public class InvalidAttributeException extends Exception {
    public InvalidAttributeException(String msg){
        super(msg);
    }
}
