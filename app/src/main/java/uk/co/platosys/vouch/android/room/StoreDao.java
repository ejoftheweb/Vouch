package uk.co.platosys.vouch.android.room;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface StoreDao {

    @Query("SELECT signature FROM SignatureEntity WHERE (voucher_id =:voucherID )")
    public List<String> getSignatures(String voucherID);

    @Query("SELECT * FROM VoucherEntity WHERE (id = :vouchid)")
    public VoucherEntity getVoucherEntity(String vouchid);

    @Query("SELECT * FROM ProfileEntity WHERE(user_id =:userID)")
    public ProfileEntity getProfileEntity (String userID);
}
