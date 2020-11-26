package uk.co.platosys.vouch;

import uk.co.platosys.vouch.exceptions.InvalidContentException;

public class BlankVoucher {
    String content;

    public BlankVoucher(Store store, VoucherID parent){}

    public  void setContent(String content) throws InvalidContentException {
        for (char test:content.toCharArray()) {
            //TODO
            //test that it's a legit Base64 character, if it isn't, throw the exception.
        }
        this.content=content;
    }
}
