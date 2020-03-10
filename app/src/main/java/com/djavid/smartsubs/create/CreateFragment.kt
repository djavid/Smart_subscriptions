package com.djavid.smartsubs.create

import android.os.Bundle
import android.transition.AutoTransition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class CreateFragment : Fragment(), KodeinAware {

    private val presenter: CreateContract.Presenter by instance()
    override lateinit var kodein: Kodein

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        kodein = (activity?.application as Application).createComponent(this)
        presenter.init()
    }

}