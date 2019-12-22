package uk.co.platosys.vouch;


import java.util.List;

/** Vouch groups are conceptually extremely powerful.
 *
 * Note that a Group is NOT analagous to a Thread. In a Thread, the "members" are references to other Vouchers,
 * and form the immutable content.
 * In a Group, the "members" are signatories,  The Group content is immutable, and signed by the members.
 *
 * Groups are democratic!
 *
 */
public class Group extends Profile {
    List<Profile> members;
}
