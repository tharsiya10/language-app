package fr.uparis.languesapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class NotifDismissedReceiver : BroadcastReceiver() {

    var words : Array<String> = arrayOf()
    var dst : Array<String> = arrayOf()

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("projetnotif", "DISMISSED")
        val url = intent.data.toString()

        val serviceIntent = Intent(context, MyService::class.java).apply {
            action = "add_notif"
        }
        serviceIntent.putExtra("url", url)
        // attendre un temps
        context.startService(serviceIntent)
    }
}