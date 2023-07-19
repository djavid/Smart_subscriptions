package com.djavid.smartsubs.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.common.BackPressListener
import com.djavid.smartsubs.databinding.ActivityRootBinding
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

    private lateinit var binding: ActivityRootBinding

    override lateinit var kodein: Kodein

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        kodein = (application as Application).rootComponent(this, binding)
        lifecycle.addObserver(authHelper)

        val subId = intent.getStringExtra(KEY_SUBSCRIPTION_ID)
        presenter.init(subId)
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