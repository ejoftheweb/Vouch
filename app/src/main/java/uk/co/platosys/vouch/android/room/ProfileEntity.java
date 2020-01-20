package uk.co.platosys.vouch.android.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProfileEntity {
    @PrimaryKey
    public String userID;
    public String voucherID;
    public String lock;
}
