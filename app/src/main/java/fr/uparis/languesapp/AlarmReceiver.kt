package fr.uparis.languesapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        /*
        if (context != null) {
            context.startService(Intent(context, MyService::class.java))
        }
        */
        val serviceIntent = Intent(context, MyService::class.java).apply {
            action = "start"
        }
        context?.startService(serviceIntent)


    }
}