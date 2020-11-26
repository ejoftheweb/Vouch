package uk.co.platosys.vouch;

public interface Vouched {
     /** Credibility is subjective. It can only be calculated with reference to the person who has to
      * believe it. Thus this method takes a Self argument.
      * [Perhaps it should just be a Profile?? Logically, it should be a Self. But  the only difference
      * between a Profile and a Self is that the latter has getKey().... & the Key shouldn't be needed
      * to calculate credibility. OTOH, we need to consider the gaming risk of allowing third-party calculations
      * of credibility....
      *
      *
      *
      * @param self
      * @return
      */
     Credibility getCredibility(Self self);
}
