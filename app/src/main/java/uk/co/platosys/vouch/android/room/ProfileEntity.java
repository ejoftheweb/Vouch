package uk.co.platosys.vouch.android.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProfileEntity {
    @PrimaryKey @NonNull
    public String user_id;
    public String voucherID;
    public String lock;
}
