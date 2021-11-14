package com.greenwich.mexpense.model;

import io.realm.RealmObject;

/**
 * Package com.greenwich.mexpense.model in
 * <p>
 * Project MExpense
 * <p>
 * Created by Maxwell on 14/11/2021
 */
public class Trip extends RealmObject {
    public String name;
    public String destination;
    public String date_of_trip;
    public String req_risk;
    public String description;
    public String mode_of_transport;
    public double amt_expense;
}
