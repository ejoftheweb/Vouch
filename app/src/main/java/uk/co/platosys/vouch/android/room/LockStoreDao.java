package uk.co.platosys.vouch.android.room;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface LockStoreDao {
    @Query("SELECT lock FROM LockEntity WHERE (user_id =:userid)")
    public LockEntity getLockEntityByUser(String userid);

    @Query("SELECT lock FROM LockEntity WHERE (fingerprint=:fingerprint)")
    public LockEntity getLockEntityByFingerprint(String fingerprint);

    @Query("DELETE  FROM LockEntity WHERE (fingerprint = :fingerprint)")
    public void deleteLock (String fingerprint);
}
