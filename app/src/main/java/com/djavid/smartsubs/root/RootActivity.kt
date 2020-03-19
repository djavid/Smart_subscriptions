package com.djavid.smartsubs.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class RootActivity: AppCompatActivity(), KodeinAware {

    private val coroutineScope: CoroutineScope by instance()
    private val presenter: RootContract.Presenter by instance()

    override lateinit var kodein: Kodein

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        kodein = (application as Application).rootComponent(this)
        presenter.init()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    //    override fun onBackPressed() {
//        if (supportFragmentManager.backStackEntryCount > 0) {
//            supportFragmentManager.popBackStack()
//        } else {
//            super.onBackPressed()
//        }
//    }

}