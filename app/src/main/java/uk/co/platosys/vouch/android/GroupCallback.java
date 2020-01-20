package uk.co.platosys.vouch.android;

import uk.co.platosys.vouch.Group;
import uk.co.platosys.vouch.Voucher;

public interface GroupCallback {
    void onSuccess(Group group);
    void onFailure(Throwable reason);
}
