package com.asistencia.digital.monitor.GooglePush;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.asistencia.digital.monitor.R;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by dam2-yeray on 19/04/2016.
 */
public class RegistrationIntentService extends IntentService {

    public RegistrationIntentService() {super("SERVICIOPUSH");}

    // ...


    @Override
    public void onHandleIntent(Intent intent) {
        // ...
        InstanceID instanceID = InstanceID.getInstance(this);

        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d("IDINSTANCE", token);
            GcmPubSub subscription = GcmPubSub.getInstance(this);
            subscription.subscribe(token, "/topics/corregir_incidencia", null);

           // PostArray array = MyFunctions.crearPost("Login",this);
           // array.setValue("cod_incidencia", MyFunctions.INC_AYUD);
          //  MyFunctions.webServiceConnect(array,  MyFunctions.URL_INSERT_LOCATION);

        } catch (IOException e) {
            e.printStackTrace();
        }


        // ...
    }

    // ...
}