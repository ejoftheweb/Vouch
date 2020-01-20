package uk.co.platosys.vouch.android;

import uk.co.platosys.vouch.Profile;
import uk.co.platosys.vouch.Voucher;

public interface ProfileCallback {
    void onSuccess(Profile profile);
    void onFailure(Throwable reason);
}
