package com.greenwich.mexpense.network;

/**
 * Package com.greenwich.mexpense.network in
 * <p>
 * Project MExpense
 * <p>
 * Created by Maxwell on 16/11/2021
 */
public abstract class ApiCallHandler {
    protected abstract void done();
    public void success(Object data){
        this.done();
    }
    public void failed(String title, String reason){
        this.done();
    }
    public void logout(){this.done();}
}
