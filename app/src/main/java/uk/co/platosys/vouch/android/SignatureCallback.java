package uk.co.platosys.vouch.android;

import uk.co.platosys.minigma.Signature;

public interface SignatureCallback{
    void onSuccess(Signature signature);
    void onFailure(Throwable cause);
}