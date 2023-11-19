package fr.uparis.languesapp

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings.Global
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import android.view.MotionEvent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class MyService : Service() {

    // mix of : https://stackoverflow.com/questions/65971302/notification-in-kotlin-repeating-every-day-at-the-same-time
    // and https://www.appsloveworld.com/kotlin/100/72/how-to-fire-a-service-every-2-minutes

    private val CHANNEL_ID = "channelId"
    private lateinit var alarmManager : AlarmManager
    private lateinit var alarmPendingIntent: PendingIntent
    private lateinit var notifManager : NotificationManager
    lateinit var allWords : Array<Array<String>>
    val dao by lazy {
        (application as WordApplication).database.myDao()
    }

    lateinit var livedata : LiveData<List<Word>>
    lateinit var observer : androidx.lifecycle.Observer<List<Word>>
    val insertInfo = MutableLiveData<Int>()
    var listInd : MutableList<Int> = mutableListOf()

    private var n = 3
    private var id = 1
    private var freq = 1
    private var totalWords = n

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        Log.d("projet", "onCreate")
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Langues",
            NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "words_to_learn"

            notifManager = getSystemService(NotificationManager::class.java)
            notifManager.createNotificationChannel(channel)

            alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            val intent = Intent(this, AlarmReceiver::class.java)
            alarmPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        }
    }

    fun createOnDismissedIntent(context: Context, mot : String, langue_dst: String, url: String) : PendingIntent{
        val intentNotif = Intent(context, NotifDismissedReceiver::class.java)
        intentNotif.putExtra("dismissMot", mot)
        intentNotif.putExtra("dismiss", langue_dst)
        intentNotif.setData(Uri.parse(url))
        val pendingIntent = PendingIntent.getBroadcast(context.applicationContext, 0, intentNotif, PendingIntent.FLAG_MUTABLE)
        return pendingIntent
    }

    fun initNotification(mot : String, langue_dst : String, url : String) : Notification {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("langues")
            .setContentText(mot + " en " +langue_dst)
            .setSmallIcon(R.drawable.ic_baseline_abc_24)
            .setContentIntent(pendingIntent)
            .setDeleteIntent(createOnDismissedIntent(this, mot,langue_dst, url))
            .setAutoCancel(true)
            .setOngoing(false)
            .build()
        return notification
    }


    @SuppressLint("NotificationId0")
    fun showNotifications(){
        var ind : Int = -1
        // notif foreground
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("langues")
            .setContentText("mots à apprendre")
            .setSmallIcon(R.drawable.ic_baseline_abc_24)
            .setAutoCancel(true)
            .build()
        startForeground(0, notification)


        observer = androidx.lifecycle.Observer<List<Word>> {
            while(n > 0 && it.isNotEmpty() && listInd.size < it.size) {
                ind = (0..(it.size-1)).random()
                Log.d("projetdao", it[ind].count.toString())
                if(!(ind in listInd)) {
                    listInd += ind
                    startForeground(
                        id,
                        initNotification(it[ind].mot, it[ind].langue_dst, it[ind].adr_url)
                    )
                    stopForeground(STOP_FOREGROUND_DETACH)
                    id++
                    n--
                }
            }

        }
        val listLanguages = getLanguageOfTheDay()
        val time = System.currentTimeMillis()

        if(listLanguages[0] != "" && listLanguages[1] != ""){
            listLanguages[0]?.let { listLanguages[1]?.let { it1 ->
                livedata = loadWordsToShowLg(time, it,
                    it1
                )
                livedata.observeForever(observer)
            } }
        }
        else {
            livedata = loadWordsToShow(time)
            livedata.observeForever(observer)
        }
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if(intent.action == "stop"){
            stopSelf()
            return START_NOT_STICKY
        }

        if(intent.action == "add_notif"){
            n++
            intent.getStringExtra("url")?.let {
                val time = System.currentTimeMillis()
                updateWordKnown(time, it)
            }
        }

        if(intent.action == "setting") {
            n = getSharedInt("nb_mots", 3)
            freq = getSharedInt("frequence", 1)
            totalWords = n
            listInd.clear()
            notifManager.cancelAll()
        }

        if(intent.action != "start" && intent.action != "add_notif" && intent.action != "setting"){
            return START_NOT_STICKY
        }

        showNotifications()
        triggerAlarm(2)

        return START_STICKY
    }

    fun triggerAlarm(duration : Long){
        Log.d("projet", "Alarm triggered")
        val everyXMin = TimeUnit.MINUTES.toMillis(duration)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+everyXMin, alarmPendingIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(livedata.hasObservers()) livedata.removeObserver(observer)
        listInd.clear()
    }

    fun updateWordKnown(timestamp : Long, url : String) {
        Thread {
            val l = dao.updateWordKnown(timestamp, url)
            insertInfo.postValue(if (l == 0) 0 else 1)
        }.start()
    }

    fun loadWordsToShow(currentTime : Long) = dao.loadWordsToShow(currentTime)

    fun loadWordsToShowLg(currentTime: Long, src : String, dst : String) = dao.loadWordsToShow(currentTime, src, dst)

    private fun getSharedInt(key : String, default : Int) : Int {
        val sharePref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharePref.getInt(key, default)
    }

    private fun getSharedString(key : String) : String? {
        val sharePref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharePref.getString(key, "language")
    }

    private fun getLanguageOfTheDay() : List<String?> {
        val list : MutableList<String?> = mutableListOf()
        var src = ""
        var dst = ""
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_WEEK)
        if(day == Calendar.MONDAY) {
            src = ("lundi_src")
            dst = ("lundi_dst")
        }
        else if(day == Calendar.TUESDAY) {
            src = "mardi_src"
            dst = "mardi_dst"
        }
        else if(day == Calendar.WEDNESDAY) {
            src = "mercr_src"
            dst = "mercr_dst"
        }
        else if(day == Calendar.THURSDAY){
            src = "jeudi_src"
            dst = "jeudi_dst"
        }
        else if(day == Calendar.FRIDAY){
            src = "vendr_src"
            dst = "vendr_dst"
        }
        else if(day == Calendar.SATURDAY){
            src = "samedi_src"
            dst = "samedi_dst"
        }
        else {
            src = "dim_src"
            dst = "dim_dst"
        }
        list += getSharedString(src)
        list += getSharedString(dst)
        return list
    }


}