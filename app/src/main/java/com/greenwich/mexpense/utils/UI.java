package com.greenwich.mexpense.utils;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.greenwich.mexpense.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;

/**
 * Package com.greenwich.mexpense.utils in
 * <p>
 * Project MExpense
 * <p>
 * Created by Maxwell on 14/11/2021
 */
@ActivityScoped
public class UI {

    private final Activity activity;
    private KProgressHUD hud;

    @Inject
    public UI(Activity activity) {
        this.activity = activity;
    }

    public void showOkayDialog(String title, String content, boolean doOnBackPress){
        if (!activity.isFinishing())
            new iOSDialogBuilder(activity)
                    .setTitle(title)
                    .setSubtitle(content)
                    .setBoldPositiveLabel(false)
                    .setCancelable(false)
                    .setPositiveListener(activity.getString(R.string.ok), dialog -> {
                        dialog.dismiss();
                        if (doOnBackPress) activity.onBackPressed();
                    })
                    .build()
                    .show();
    }


    public void showErrorDialog(String title, String message){
        if (!activity.isFinishing())
            new iOSDialogBuilder(activity)
                    .setTitle(title)
                    .setSubtitle(message)
                    .setBoldPositiveLabel(false)
                    .setCancelable(false)
                    .setPositiveListener(activity.getString(R.string.ok), iOSDialog::dismiss)
                    .build()
                    .show();
    }

    public void showInfoDialog(String title, String message){
        if (!activity.isFinishing())
            new iOSDialogBuilder(activity)
                    .setTitle(title)
                    .setSubtitle(message)
                    .setPositiveListener(activity.getString(R.string.ok), iOSDialog::dismiss)
                    .build()
                    .show();
    }

    public void showInfoDialog(String title, String message, String btn_message){
        if (!activity.isFinishing())
            new iOSDialogBuilder(activity)
                    .setTitle(title)
                    .setSubtitle(message)
                    .setPositiveListener(btn_message, iOSDialog::dismiss)
                    .build()
                    .show();
    }

    public void showNonCloseableProgress(String title){
        if (!activity.isFinishing())
            if (title == null || title.isEmpty()){
                hud =  KProgressHUD.create(activity)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel(activity.getString(R.string.load))
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
            }else {
                hud =  KProgressHUD.create(activity)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setDetailsLabel(title)
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
            }

    }

    public void dismissProgress(){
        if (hud != null){
            hud.dismiss();
            hud = null;
        }
    }
}
