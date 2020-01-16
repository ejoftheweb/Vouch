package uk.co.platosys.vouch.android;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import uk.co.platosys.vouch.Voucher;

@Entity
public class VoucherEntity {
    @PrimaryKey
   String id; //big binary as string
   String title;
   String tweet;
   String author; //profile id. bbastring
   String publisher;//profile id. bbastring
   String content; //content as string.


}
