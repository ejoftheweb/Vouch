package uk.co.platosys.vouch.android.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SignatureEntity {
    @PrimaryKey @NonNull
    String signature;
    String voucher_id;
}

