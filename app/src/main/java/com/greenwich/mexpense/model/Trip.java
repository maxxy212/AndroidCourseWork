package com.greenwich.mexpense.model;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Package com.greenwich.mexpense.model in
 * <p>
 * Project MExpense
 * <p>
 * Created by Maxwell on 14/11/2021
 */
public class Trip extends RealmObject {
    @PrimaryKey public int id;
    public String name;
    public String destination;
    public String date_of_trip;
    public String req_risk;
    public String description;
    public String mode_of_transport;
    public String amt_expense;
    public String starting_point;
    public RealmList<String> expense;
    public String time_of_expense;
    public String comment;
}
