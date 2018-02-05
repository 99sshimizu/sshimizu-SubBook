package com.example.sshimizu_subbook;

/**
 * Created by SarahS on 2018/01/23.
 */

/**Represents a Subscription
 *
 * @author sshimizu
 * @version 1.0
 * @see MainActivity
 * @see NewSubscription
 */
public class Subscription {
    private String subName;
    private String subCharge;
    private String subDate;
    private String subComments;

    /**Constructs Subscription
     *
     * @param subName subscription Name
     * @param subDate subscription Date
     * @param subCharge subscription Charges
     * @param subComments subscription Comments
     * @throws SubNameTooLongException
     */
    public Subscription(String subName, String subDate, String subCharge, String subComments) throws SubNameTooLongException {
        this.setSubName(subName);
        this.setSubDate(subDate);
        this.setSubCharge(subCharge);
        this.setSubComments(subComments);
    }

    /**Gets Names
     *
     * @return Name
     */
    public String getSubName(){
        return subName;
    }

    /**Gets Date
     *
     * @return Date
     */
    public String getSubDates(){
        return subDate;
    }

    /**Gets Charges
     *
     * @return Charges
     */
    public String getSubCharges(){
        return subCharge;
    }

    /**Gets Comments
     *
     * @return Comments
     */
    public String getSubComments(){
        return subComments;
    }

    /**Sets Name throws exception when exceeds 20 characters
     *
     * @param subName subscription Name
     * @throws SubNameTooLongException
     */
    public void setSubName(String subName) throws  SubNameTooLongException{
        if (subName.length() <= 20){
            this.subName = subName;
        }else{
            throw  new  SubNameTooLongException();
        }
    }

    /**Sets Comments throws exception when exceeds 30 characters
     *
     * @param subComments subscription Comments
     * @throws SubNameTooLongException
     */
    public void setSubComments(String subComments) throws  SubCommentTooLongException{
        if (subName.length() <= 30){
            this.subComments = subComments;
        }else{
            throw  new  SubNameTooLongException();
        }
    }

    /**Set Date
     *
     * @param subDate subscription Dates
     */
    public void setSubDate(String subDate){
        this.subDate = subDate;
    }

    /**Set Charges
     *
     * @param subCharge subscription Charges
     */
    public void setSubCharge(String subCharge){
        this.subCharge = subCharge;
    }

    /**Converts to string
     *
     * @return in form: subName + "\n" + "$" + subCharge + "\n" + subDate
     */
    public String toString() {
        return subName + "\n" + "$" + subCharge + "\n" + subDate ;
    }
}
