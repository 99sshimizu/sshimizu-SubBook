package com.example.sshimizu_subbook;

/**
 * Created by SarahS on 2018/01/23.
 */

/**Represents a NewSubscription
 *
 * @author sshimizu
 * @version 1.0
 * @see Subscription
 */
public class NewSubscription extends Subscription {
    /**Constructs NewSubscription
     *
     * @param subName subscription Name
     * @param subDate subscription Date
     * @param subCharge subscription Charges
     * @param subComments subscription Comments
     */
    public NewSubscription(String subName, String subDate, String subCharge, String subComments) {
        super(subName, subDate, subCharge, subComments);
        this.setSubName(subName);
        this.setSubDate(subDate);
        this.setSubCharge(subCharge);
        this.setSubComments(subComments);
    }
}
