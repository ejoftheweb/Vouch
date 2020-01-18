package uk.co.platosys.vouch.android.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SignatureEntity {
    @PrimaryKey
    String signature;
    String voucher_id;
}

