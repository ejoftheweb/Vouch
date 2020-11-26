package uk.co.platosys.vouch;

/**Class representing the Content element of a Voucher.
 * A Voucher's content is *immutable*.
 *
 * A voucher's content consists of  an arbitrary amount
 * of binary content as a base-64 String.
 *
 * This binary content, when decoded, is xml. But the canonical version is always the base64 text encoding
 * it. This approach sidesteps the car-crash that is XML-canonicalisation
 *
 *
 */

public  abstract class Content {
    private String text;
     Content (){}
    String encodedContent;

    /**Takes a base-64 encoded String representation of the Content as
     * typically stored in a database. This is the method used by Store
     * implementations to return Vouchers.
     *
     * @param encodedContent
     */
    public Content (String encodedContent)  {

         this.encodedContent=encodedContent;
    }

    private void setText(String text){
        this.text=text;
    }
    public String getText(){
        return text;
    }
    @Override
    public String toString(){
        //TODO This method should return a Base64-encoding of the compressed content.
        return encodedContent;
    }
    /*
    public static final String TEXT="AAAAAA";
    public static final String THREAD="BBBBBB";
    public static final String PROFILE="CCCCCC";
    public static final String GROUP="DDDDDD";
*/
}
