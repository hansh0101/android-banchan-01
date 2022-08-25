package co.kr.woowahan_banchan.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.presentation.ui.order.OrderActivity
import co.kr.woowahan_banchan.presentation.worker.OrderUpdateWorker

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)
        deliverNotification(notificationManager, context)

        val workManager = WorkManager.getInstance(context)

        val orderId = intent?.extras?.getLong("orderId") ?: -1
        orderUpdate(orderId,workManager)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(
            PRIMARY_CHANNEL_ID,
            "DELIVERY_FINISH",
            NotificationManager.IMPORTANCE_HIGH
        )
        with(notificationChannel) {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            description = "ALARM_DELIVERY"
            notificationManager.createNotificationChannel(this)
        }
    }

    private fun deliverNotification(notificationManager: NotificationManager, context: Context) {
        val contentIntent = Intent(context, OrderActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("쓩~")
            .setContentText("주문하신 반찬이 배달됐어요!")
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun orderUpdate(orderId: Long, workManager: WorkManager) {
        val inputData = Data.Builder().putLong(OrderUpdateWorker.ORDER_ID, orderId).build()
        val orderUpdateRequest = OneTimeWorkRequestBuilder<OrderUpdateWorker>()
            .setInputData(inputData).build()
        workManager.enqueue(orderUpdateRequest)
    }

    companion object {
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"

        fun getIntent(context: Context, orderId: Long): Intent =
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra("orderId", orderId)
            }
    }
}