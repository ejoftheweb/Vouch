package uk.co.platosys.vouch.android;

import uk.co.platosys.vouch.Voucher;

public interface VoucherCallback {
    void onSuccess(Voucher voucher);
    void onFailure(Throwable reason);
}
