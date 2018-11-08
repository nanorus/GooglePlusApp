package com.official.nanorus.googleplusapp.model.data;

public class Utils {

    public static boolean checkNetWorkError(Throwable throwable) {
        String error = throwable.toString();
        return error.contains("Unable to resolve host") || error.contains("Failed to connect to");
    }

}
