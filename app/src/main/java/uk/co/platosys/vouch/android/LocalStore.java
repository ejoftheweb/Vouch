package uk.co.platosys.vouch.android;


import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.co.platosys.minigma.Fingerprint;
import uk.co.platosys.minigma.Key;
import uk.co.platosys.minigma.Lock;
import uk.co.platosys.minigma.Signature;
import uk.co.platosys.minigma.exceptions.BadPassphraseException;
import uk.co.platosys.minigma.exceptions.LockNotFoundException;
import uk.co.platosys.minigma.exceptions.MinigmaException;
import uk.co.platosys.vouch.Content;
import uk.co.platosys.vouch.Exceptions.IDVerificationException;
import uk.co.platosys.vouch.Exceptions.VouchRoleException;
import uk.co.platosys.vouch.Exceptions.VoucherNotFoundException;
import uk.co.platosys.vouch.Group;
import uk.co.platosys.vouch.Profile;
import uk.co.platosys.vouch.Role;
import uk.co.platosys.vouch.Self;
import uk.co.platosys.vouch.Store;
import uk.co.platosys.vouch.Voucher;
import uk.co.platosys.vouch.VoucherID;
import uk.co.platosys.vouch.android.room.LockEntity;
import uk.co.platosys.vouch.android.room.LockStoreDao;
import uk.co.platosys.vouch.android.room.Signatures;
import uk.co.platosys.vouch.android.room.StoreDao;
import uk.co.platosys.vouch.android.room.VoucherEntity;


/**This is an implementation of the Vouch Store interface working on the Android device itself. Data
 * is stored in a SQLite database on the device, using the Room object persistence library.
 *
 * In addition it provides asynchronous versions of all the store/retrieve  methods which should be used in
 * place of the synchronous ones for all calls from the UI thread. These have a void return type but
 * take a callback argument,
 *
 */
public class LocalStore  implements Store  {
    private char[] passphrase;
    private Lock lock;
    private Key key;
    private Self self;
    private SQLiteDatabase database;
    private List<Store> remoteStores;
    private StoreDao storeDao;
    private LockStoreDao lockStoreDao;

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
        this.lock=lock;
        this.key=key;

    }
    @Override
    public Signature store(Voucher voucher) {
        try {

            return voucher.sign(self, Role.STORE, passphrase);
        }catch(VouchRoleException vre){
            //TODO log it
            return null;
        }catch (BadPassphraseException bpe){
            //TODO log it
            return null;
        }
    }
    public void storeAsync(Voucher voucher, SignatureCallback callback){
        try{
            callback.onSuccess(voucher.sign(self, Role.STORE, passphrase));
        }catch(Exception ex) {
            callback.onFailure(ex);
        }
    }

    @Override
    public Signature store(Profile profile) {
        //TODO
        return null;
    }
    public void storeAsync(Profile profile, SignatureCallback callback){
        //TODO
    }
    @Override
    public Signature store(Group group) {
        //TODO
        return null;
    }
    public void storeAsync(Group group, SignatureCallback callback){
        //TODO
    }
    @Override
    public Profile getProfile(VoucherID voucherID) throws VoucherNotFoundException {
        //TODO
        return null;
    }
    public void getProfileAsync(VoucherID voucherID, ProfileCallback profileCallback){
        //TODO
    }

    @Override
    public Group getGroup(VoucherID voucherID) throws VoucherNotFoundException {
        //TODO
        return null;
    }

    @Override
    public List<Profile> getProfiles(String name) {
        //TODO
        return null;
    }

    @Override
    public List<Group> getGroups(String name) {
        //TODO
        return null;
    }

    @Override
    public Voucher getVoucher(VoucherID voucherID) throws VoucherNotFoundException {
        try{
            VoucherEntity voucherEntity = storeDao.getVoucherEntity(voucherID.toString());
            List<String> signatureStrings = storeDao.getSignatures(voucherID.toString());
            List<Signature> signatures = new ArrayList<>();
            for(String signatureString:signatureStrings){signatures.add(new Signature(signatureString));}
            if(voucherEntity==null) {
                throw new VoucherNotFoundException("voucher not found locally");
            }
            return new Voucher(voucherID,
                    voucherEntity.title,
                    voucherEntity.tweet,
                    new VoucherID (voucherEntity.author),
                    new VoucherID (voucherEntity.publisher),
                    signatures,
                    new VoucherID (voucherEntity.parent),
                    new VoucherID (voucherEntity.previous),
                    new VoucherID (voucherEntity.next),
                    new Content (voucherEntity.content),
                    this);

        }catch(VoucherNotFoundException vnx){
             for(Store remoteStore:remoteStores) {
                 try{
                     return remoteStore.getVoucher(voucherID);
                 }catch (VoucherNotFoundException vnxe) {
                     //do nothing
                 }
             }
             throw new VoucherNotFoundException("voucher not found remotely or locally", vnx);
        }catch(IDVerificationException idve){
            throw new VoucherNotFoundException("voucher failed verification", idve);
        }catch(ParseException px) {
            throw new VoucherNotFoundException("parse error remaking voucher or one of its components", px);
        }
    }
     public void getVoucherAsync(VoucherID voucherID, VoucherCallback voucherCallback){
        try{
           voucherCallback.onSuccess(getVoucher(voucherID));
        }catch(VoucherNotFoundException vne){
            voucherCallback.onFailure(vne);
        }
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
        return lock;
    }

    @Override
    public boolean addLock(Lock lock) {
        return false;
    }

    @Override
    public boolean removeLock(Fingerprint fingerprint) {
        try {
            lockStoreDao.deleteLock(fingerprint.toString());
            return true;
        }catch(Exception exception){
            return false;
        }
    }

    @Override
    public Lock getLock(Fingerprint fingerprint) {
        LockEntity lockEntity = lockStoreDao.getLockEntityByFingerprint(fingerprint.toString());
        try {
            return new Lock(lockEntity.lock);
        }catch(MinigmaException mx){
            //TODO: log it
            return null;
        }
    }

    @Override
    public Iterator<Lock> iterator() throws MinigmaException {
        return null;
    }

    @Override
    public Lock getLock(String userID) throws MinigmaException, LockNotFoundException {
        LockEntity lockEntity = lockStoreDao.getLockEntityByUser(userID);
        try {
            return new Lock(lockEntity.lock);
        }catch(MinigmaException mx){
            //TODO: log it
            return null;
        }
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
    @Override
    public void addRemoteStore(Store store){
        remoteStores.add(store);
    }
}
