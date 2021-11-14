package com.greenwich.mexpense;

import androidx.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;

import dagger.hilt.android.HiltAndroidApp;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Package com.greenwich.mexpense in
 * <p>
 * Project MExpense
 * <p>
 * Created by Maxwell on 13/11/2021
 */
@HiltAndroidApp
public class BaseApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        AndroidNetworking.initialize(this);
        //304 for not created
    }
}
