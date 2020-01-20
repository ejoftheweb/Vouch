package uk.co.platosys.vouch;


import java.util.List;

import uk.co.platosys.minigma.Signature;

public class Vouch {
    /**
     * Gets a list of Profiles trusted by the user.
     * @return
     */
    public static List<Profile> getTrustedProfiles(Self self){
        return null;
    }

    /**Returns the Profile ID of the signer of a given signature, or null if it cannot
     * be found - e.g. it is a PGP signature from outwith Vouch
     *
     * @param signature
     * @return
     */
    public static VoucherID getSignerProfileID(Signature signature){
        //TODO
        return null;
    }

}
