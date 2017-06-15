package com.example.dam2_yeray.modulomovil.Asyntask;

import android.os.AsyncTask;

import com.example.dam2_yeray.modulomovil.MySqlNiko.MyFunctions;
import com.example.dam2_yeray.modulomovil.PostPhp.PostArray;


public class AsynInsert extends AsyncTask<Void, Void, Void> {

    private PostArray sendParams;
    private String mUrl;


    public AsynInsert(PostArray sendParams, String url) {

        this.sendParams = sendParams;
        this.mUrl = url;

    }

    @Override
    protected Void doInBackground(Void... params) {




        MyFunctions.webServiceConnect(sendParams, mUrl);
        return null;
    }

}
