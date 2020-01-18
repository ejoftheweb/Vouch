package uk.co.platosys.vouch;

/**Class representing the Content element of a Voucher.
 * A Voucher's content is *immutable*.
 * For now, this class just wraps some String text, but creating a separate class will
 * let us implement options for more richly-structured content for later on.
 *
 *
 */
public class Content {
    private String text;
     Content (){}

    /**Takes a base-64 encoded String representation of the Content as
     * typically stored in a database.
     *
     * @param encodedContent
     */
    public Content (String encodedContent){
         //TODO
    }
    public static Content createContent(String text){
        Content content=new Content();
        content.setText(text);
        return content;
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
        return text;
    }

}
