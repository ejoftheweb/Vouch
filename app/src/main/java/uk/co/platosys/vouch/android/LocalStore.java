package uk.co.platosys.vouch.android;


import android.database.sqlite.SQLiteDatabase;

import androidx.room.RoomDatabase;

import java.util.Iterator;
import java.util.List;

import uk.co.platosys.minigma.Fingerprint;
import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.Signature;
import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.minigma.exceptions.LockNotFoundException;
import uk.co.platosys.minigma.exceptions.MinigmaException;
import uk.co.platosys.vouch.Exceptions.VouchRoleException;
import uk.co.platosys.vouch.Exceptions.VoucherNotFoundException;
import uk.co.platosys.vouch.Group;
import uk.co.platosys.vouch.Profile;
import uk.co.platosys.vouch.Role;
import uk.co.platosys.vouch.Self;
import uk.co.platosys.vouch.Store;
import uk.co.platosys.vouch.Voucher;
import uk.co.platosys.vouch.VoucherID;
import uk.co.platosys.vouch.android.VouchConstants;


/**This is an implementation of the Vouch Store interface working on the Android device itself. Data
 * is stored in a SQLite database on the device, using the Room object persistence library.
 *
 */
public class LocalStore  implements Store  {
    private char[] passphrase;
    private Lock lock;
    private Key key;
    private Self self;
    private SQLiteDatabase database;

    /**
     * Self is the profile of the operator of this Store. It should be a separate Profile with a separate
     * Lock and Key to any user of the system.
     * @param self
     * @param lock
     * @param key
     * @param passphrase
     */
    public LocalStore(Self self, Lock lock, Key key, char[] passphrase){
        this.passphrase=passphrase;
        this.self=self;

    }
    @Override
    public Signature store(Voucher voucher) {
        try {

            return voucher.sign(self, Role.STORE, passphrase);
        }catch(VouchRoleException vre){
            //TODO
            return null;
        }catch (BadPassphraseException bpe){
            //TODO
            return null;
        }
    }

    @Override
    public Signature store(Profile profile) {
        return null;
    }

    @Override
    public Signature store(Group group) {
        return null;
    }

    @Override
    public Profile getProfile(VoucherID voucherID) throws VoucherNotFoundException {
        return null;
    }

    @Override
    public Group getGroup(VoucherID voucherID) throws VoucherNotFoundException {
        return null;
    }

    @Override
    public List<Profile> getProfiles(String name) {
        return null;
    }

    @Override
    public List<Group> getGroups(String name) {
        return null;
    }

    @Override
    public Voucher getVoucher(VoucherID voucherID) throws VoucherNotFoundException {
        return null;
    }

    @Override
    public List<Voucher> findVouchers(String[] searchTerms) {
        return null;
    }

    @Override
    public List<Signature> getSignatures(VoucherID voucherID) {
        return null;
    }

    /**
     * Returns this store's Lock.
     * @return
     */
    @Override
    public Lock getLock() {
        return null;
    }

    @Override
    public boolean addLock(Lock lock) {
        return false;
    }

    @Override
    public boolean removeLock(Fingerprint fingerprint) {
        return false;
    }

    @Override
    public Lock getLock(Fingerprint fingerprint) {
        return null;
    }

    @Override
    public Iterator<Lock> iterator() throws MinigmaException {
        return null;
    }

    @Override
    public Lock getLock(String userID) throws MinigmaException, LockNotFoundException {
        return null;
    }

    @Override
    public boolean contains(String userID) {
        return false;
    }

    @Override
    public long getStoreId() {
        return 0;
    }

    @Override
    public String getUserID(Fingerprint keyID) {
        return null;
    }

    @Override
    public String getUserID(long keyID) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
