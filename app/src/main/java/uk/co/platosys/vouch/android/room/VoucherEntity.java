package uk.co.platosys.vouch.android.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

import uk.co.platosys.vouch.Voucher;

@Entity
public class VoucherEntity {

   /*VoucherID id;
   String title;
   String tweet;
   VoucherID author;
   VoucherID publisher;
   List<VoucherID> recipients;
   List<VoucherID> taggers;
   List<Signature> signatures;
   List<Tag> tags;
   VoucherID parent;
   VoucherID previous;
   VoucherID next;
   Content content;
   Store store;*/

   @PrimaryKey
   public String id; //big binary as string
   public String title;
   public String tweet;
   public String author; //profile id. bbastring
   public String publisher;//profile id. bbastring
   public List<String> recipients;
   public List<String> taggers;
   public List<String> signatures;
   public String parent;
   public String previous;
   public String next;
   public String content; //content as string.


}
