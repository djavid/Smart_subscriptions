package com.djavid.smartsubs.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.djavid.core.ui.databinding.ActivityRootBinding
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.common.BackPressListener
import com.djavid.smartsubs.utils.Constants
import com.djavid.smartsubs.data.FirebaseAuthHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class RootActivity : AppCompatActivity(), DIAware {

    private val coroutineScope: CoroutineScope by instance()
    private val presenter: RootContract.Presenter by instance()
    private val authHelper: FirebaseAuthHelper by instance()

    private lateinit var binding: ActivityRootBinding

    override lateinit var di: DI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        di = (application as Application).rootComponent(this, binding)
        lifecycle.addObserver(authHelper)

        val subId = intent.getStringExtra(Constants.KEY_SUBSCRIPTION_ID)
        presenter.init(subId)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    @Deprecated("Deprecated in Java")
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