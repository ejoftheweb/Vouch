package uk.co.platosys.vouch.android.room;

import androidx.room.Entity;

@Entity
public class LockEntity {
    public String user_id;
    public String lock;
    public String fingerprint;
}
