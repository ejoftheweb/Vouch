package uk.co.platosys.vouch.android.room;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(version =1, entities={VoucherEntity.class, SignatureEntity.class})
public abstract class VouchDatabase extends RoomDatabase {
}
