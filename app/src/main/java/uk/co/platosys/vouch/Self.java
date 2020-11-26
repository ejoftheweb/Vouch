package uk.co.platosys.vouch;

import java.util.List;

import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Signature;
import uk.co.platosys.vouch.exceptions.IDVerificationException;

/**
 * The Self class is a Profile that relates to the currently-authenticated user of
 * a Vouch app.
 */
public class Self extends Profile {
    Key key;

    public Self (
            VoucherID id,
            String title,
            String tweet,
            VoucherID author,
            VoucherID publisher,
            List<Signature> signatures,
            VoucherID parent,
            VoucherID previous,
            VoucherID next,
            Content content,
            Store store,
            String userID,
            Key key)
            throws IDVerificationException {

        super(
                id,
                title,
                tweet,
                author,
                publisher,
                signatures,
                parent,
                previous,
                next,
                content,
                store,
                userID
        );
        this.key=key;
    }
    protected void setKey(Key key) {
        this.key = key;
    }
    public Key getKey() {
        return key;
    }



}
