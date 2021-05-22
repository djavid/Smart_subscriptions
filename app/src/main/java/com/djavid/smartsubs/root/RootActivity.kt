package com.djavid.smartsubs.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.common.BackPressListener
import com.djavid.smartsubs.utils.KEY_SUBSCRIPTION_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class RootActivity : AppCompatActivity(), KodeinAware {

    private val coroutineScope: CoroutineScope by instance()
    private val presenter: RootContract.Presenter by instance()
    private val authHelper: FirebaseAuthHelper by instance()

    override lateinit var kodein: Kodein

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        kodein = (application as Application).rootComponent(this)
        lifecycle.addObserver(authHelper)

        val subId = intent.getLongExtra(KEY_SUBSCRIPTION_ID, -1)
        presenter.init(if (subId != -1L) subId else null)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.fragments
                .filterIsInstance(BackPressListener::class.java)
                .last()
                .onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

}