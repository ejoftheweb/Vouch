package uk.co.platosys.vouch;


import uk.co.platosys.vouch.Exceptions.CredibilityException;

/** Central to the Vouch system is the credibility metric. Credibility is <b>subjective</b>,  that is, there
 * can be no authoritative objective credibility without a central Truth Arbiter, which smacks of dogmatic
 * totalitarianism.  But
 * Vouch will try to calculate a value for the credibility of a statement from the perspective of the reader,
 * being an accumulation of the credibilities of its signatories.
 *
 * Key to this is the notion of a <i>trust algebra</i>, which I have not rigorously developed. Different
 * versions of this class may produce different results, so versioning is important. Versions are changed
 * only when the credibility algorithm changes.
 * <h2>The Vouched interface </h2>
 * Anything vouched has a credibility.
 *
 * <h2>Credibility calculus</h2>
 * A Vouched object derives its credibility from its signatories and <i>their</i> credibility. The overall
 * credibility of an object is thus the credibility attributable to each of its credibilities. Credibility calculus
 * determines how these values are combined. Without a rigorously developed trust algebra, the  operations
 * used to combine credibilities are essentially arbitrary.  However, whatever the underlying arithmetic, we are
 * concerned with two main operations, broadly: summation and multiplication. This is somewhat misleading
 * because arithmetic addition and multiplication will produce wrong answers.
 *<p></p>
 * Consider: an object is signed by two people, one highly trustworthy, one only marginally so. Is it any more or less credible
 * for being signed by the second, marginally-trustworthy person?  A first approximation would suggest
 * that the lesser credibilities should simply be ignored.  Hence, credibility summation is simply a
 * MAX function.
 * However, suppose the second less-trustworthy person assigns a negative credibility to a statement (by
 * asserting that it is false).  Do we simply ignore that assertion, or do we allow the doubt that it
 * introduces to lower the overall credibility of the object?
 *
 */
public class Credibility {

    private float value; //the actual credibility score, between -1 and +1.
    public static final int VERSION= 0;//increment when algorithm changes

    public static final float FALSE=-1;
    public static final float TRUE=1;
    public static final float UNKNOWN=0;

    private Credibility (float value) {
        this.value=value;

    }

    /**
     *  In version 1, this returns as follows:
     *  if both are positive, the largest;
     *  if both are negative, the smallest;
     *  otherwise (different signs; or one or other or both is zero): the sum.
     * @param one
     * @param two
     * @return
     */
    public static Credibility sum(Credibility one, Credibility two){
        if((one.value>0) && (two.value>0)) {
            return new Credibility(Math.max(one.value, two.value));
        }else if((one.value<0)&&(two.value<0)){
            return new Credibility(Math.min(one.value, two.value));
        }else{
            return new Credibility((one.value+two.value));
        }
    }
    public static Credibility accrue(Credibility one, Credibility two){
        //TODO
        return one;
    }
    public float getValue(){
        return value;
    }

}
