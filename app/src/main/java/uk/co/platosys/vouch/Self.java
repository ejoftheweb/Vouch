package uk.co.platosys.vouch;

import uk.co.platosys.minigma.Key;

/**
 * The Self class is a Profile that relates to the currently-authenticated user of
 * a Vouch app.
 */
public class Self extends Profile {
    Key key;

    public Self(){
        super();
    }

    public Key getKey() {
        return key;
    }



}
