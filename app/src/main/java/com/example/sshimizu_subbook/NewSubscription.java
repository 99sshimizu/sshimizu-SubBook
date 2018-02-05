package com.example.sshimizu_subbook;

/**
 * Created by SarahS on 2018/01/23.
 */

public class NewSubscription extends Subscription {
    public NewSubscription(String subName, String subDate, String subCharge, String subComments) {
        super(subName, subDate, subCharge, subComments);
        this.setSubName(subName);
        this.setSubDate(subDate);
        this.setSubCharge(subCharge);
        this.setSubComments(subComments);
    }
}
