package com.djavid.smartsubs.db

import com.squareup.sqldelight.db.SqlDriver

object DatabaseSchema : SqlDriver.Schema by Database.Schema {

    private const val DB_VERSION = 2

    override val version: Int = DB_VERSION

}