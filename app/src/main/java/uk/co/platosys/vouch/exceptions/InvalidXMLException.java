package uk.co.platosys.vouch.exceptions;

public class InvalidXMLException extends Exception {
    public InvalidXMLException (String msg){
        super(msg);

    }
    public InvalidXMLException (String msg, Throwable cause){
        super(msg, cause);

    }
}
