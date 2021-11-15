package com.greenwich.mexpense.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;

/**
 * Package com.greenwich.mexpense.utils in
 * <p>
 * Project MExpense
 * <p>
 * Created by Maxwell on 15/11/2021
 */
@ActivityScoped
public class DateUtil {

    @Inject
    public DateUtil(){}

    @SuppressLint("ConstantLocale")
    public final SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    public final SimpleDateFormat sqlformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    public final SimpleDateFormat fuldateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public String formatDate(Date date , SimpleDateFormat dateformat){
        if(dateformat == null){
            dateformat = sqlformat;
        }
        if(date != null){
            return dateformat.format(date);
        }
        return "";

    }
}
