package com.djavid.smartsubs.db

import com.djavid.smartsubs.SubscriptionEntityQueries
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class DatabaseModule {
    val kodein = Kodein.Module("database_module") {
        bind<SqlDriver>() with singleton {
            AndroidSqliteDriver(Database.Schema, instance(), "database.db")
        }

        //queries
        bind<SubscriptionEntityQueries>() with singleton {
            instance<Database>().subscriptionEntityQueries
        }

        //repositories
        bind<SubscriptionsRepository>() with singleton {
            SubscriptionsRepository(instance(), instance(), instance())
        }
    }
}