package com.djavid.smartsubs.db

import android.content.Context
import android.content.SharedPreferences
import com.djavid.smartsubs.NotificationEntityQueries
import com.djavid.smartsubs.SubscriptionEntityQueries
import com.djavid.smartsubs.data.storage.CloudStorageRepository
import com.djavid.smartsubs.data.storage.RealTimeRepository
import com.djavid.smartsubs.data.storage.SharedRepository
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class DatabaseModule(
    private val context: Context
) {
    val di = DI.Module("database_module") {

        //shared prefs
        bind<SharedPreferences>() with singleton {
            this@DatabaseModule.context.getSharedPreferences("default", Context.MODE_PRIVATE)
        }
        bind<SharedRepository>() with singleton {
            SharedRepository(instance())
        }

        //database
        bind<SqlDriver>() with singleton {
            AndroidSqliteDriver(Database.Schema, instance(), "database.db")
        }
        bind<Database>() with singleton { Database.invoke(instance()) }

        //queries
        bind<SubscriptionEntityQueries>() with singleton {
            instance<Database>().subscriptionEntityQueries
        }
        bind<NotificationEntityQueries>() with singleton {
            instance<Database>().notificationEntityQueries
        }

        //repositories
        bind<SubscriptionsRepository>() with singleton {
            SubscriptionsRepository(instance(), instance(), instance())
        }
        bind<NotificationsRepository>() with singleton {
            NotificationsRepository(instance(), instance(), instance())
        }
        bind<RealTimeRepository>() with singleton {
            RealTimeRepository(instance(), instance(), instance(), instance())
        }
        bind<CloudStorageRepository>() with singleton {
            CloudStorageRepository()
        }
    }
}