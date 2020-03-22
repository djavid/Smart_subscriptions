package com.djavid.smartsubs.db

import android.content.Context
import android.content.SharedPreferences
import com.djavid.smartsubs.SubscriptionEntityQueries
import com.djavid.smartsubs.utils.SharedRepository
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class DatabaseModule(
    private val context: Context
) {
    val kodein = Kodein.Module("database_module") {

        //shared prefs
        bind<SharedPreferences>() with singleton {
            this@DatabaseModule.context.getSharedPreferences("default", Context.MODE_PRIVATE)
        }

        bind<SharedRepository>() with singleton { SharedRepository(instance()) }

        //database
        bind<SqlDriver>() with singleton {
            AndroidSqliteDriver(Database.Schema, instance(), "database.db")
        }

        bind<Database>() with singleton { Database.invoke(instance()) }

        //queries
        bind<SubscriptionEntityQueries>() with singleton {
            Database
            instance<Database>().subscriptionEntityQueries
        }

        //repositories
        bind<SubscriptionsRepository>() with singleton {
            SubscriptionsRepository(instance(), instance(), instance())
        }
    }
}