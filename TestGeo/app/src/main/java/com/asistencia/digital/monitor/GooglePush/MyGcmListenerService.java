package com.asistencia.digital.monitor.GooglePush;

import android.os.Bundle;

import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by dam2-yeray on 19/04/2016.
 */
public class  MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "PUSH";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String messageTitle = data.getBundle("notification").getString("title") ;
        String messagebody = data.getBundle("notification").getString("body") ;
        Log.d(TAG,  data.toString());
        Log.d(TAG, "PUSH FROM: " + from);
        Log.d(TAG, "PUSH MESSAJE: " + messageTitle);

        /** EN UN FUTURO EL CODIGO EJECUTARA UNA ORDEN PARA LANZAR EL INTENTO */
        if (from.startsWith("/topics/corregir_incidencia")) {

      //  Intent intent= new Intent(this, AlertaRespuesta.class);
         //   startActivity(intent);
        } else {
            // normal downstream message.
        }

      //  sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]



}


