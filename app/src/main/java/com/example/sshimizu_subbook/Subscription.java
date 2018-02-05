package com.example.sshimizu_subbook;

/**
 * Created by SarahS on 2018/01/23.
 */

public class Subscription {
    private String subName;
    private String subCharge;
    private String subDate;
    private String subComments;
    // private Date date;
    //protected Date date;

    public Subscription(String subName, String subDate, String subCharge, String subComments) throws SubNameTooLongException {
        this.setSubName(subName);
        this.setSubDate(subDate);
        this.setSubCharge(subCharge);
        this.setSubComments(subComments);
    }

    public String getSubName(){
        return subName;
    }
    public String getSubDates(){
        return subDate;
    }
    public String getSubCharges(){
        return subCharge;
    }
    public String getSubComments(){
        return subComments;
    }

    public void setSubName(String subName) throws  SubNameTooLongException{
        if (subName.length() <= 20){
            this.subName = subName;
        }else{
            throw  new  SubNameTooLongException();
        }
    }

    public void setSubComments(String subComments) throws  SubNameTooLongException{
        if (subName.length() <= 30){
            this.subComments = subComments;
        }else{
            throw  new  SubNameTooLongException();
        }
    }

    public void setSubDate(String subDate){
        this.subDate = subDate;
    }

    public void setSubCharge(String subCharge){
        this.subCharge = subCharge;
    }

    public String toString() {
        return subName + "\n" + "$" + subCharge + "\n" + subDate ;
    }
}
