package uk.co.platosys.vouch.constants;

import org.jdom2.Namespace;

public class XML {
    public static final String NAMESPACE_NAME="http://vouchproject.org";
    public static final Namespace NS =  Namespace.getNamespace(NAMESPACE_NAME);
    public static final String THREAD_ELEMENT_NAME = "Thread";
    public static final String THREAD_ITEM_ELEMENT_NAME="Voucher";
    public static final String THREAD_ITEM_ATTRIBUTE_NAME="VoucherID";
    public static final String PROFILE_ELEMENT_NAME="Profile";
    public static final String TEXT_ELEMENT_NAME="Text";
    public static final String ROOT_PROFILE_TEXT="This is the Vouch root profile from which all Vouch profiles are descended";
    public static final String LOCK_ELEMENT_NAME="Lock";
    public static final String BIO_ELEMENT_NAME="Bio";

}
