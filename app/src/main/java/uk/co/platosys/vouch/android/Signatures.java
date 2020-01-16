package uk.co.platosys.vouch.android;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/** This is an Android Room definition of the one-to-many relationship between Vouchers and their
 *  Signatures. 
 *
 */

public class Signatures {
    @Embedded public VoucherEntity voucherEntity;
    @Relation(
            parentColumn = "id",
            entityColumn = "value"
    )
    public List<String> signatureValues;
}
