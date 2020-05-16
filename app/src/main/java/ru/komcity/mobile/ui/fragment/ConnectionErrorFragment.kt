package ru.komcity.mobile.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_connection_error.*
import ru.komcity.mobile.R
import ru.komcity.mobile.view.MainActivityView

class ConnectionErrorFragment : Fragment() {

    private var activityListener: MainActivityView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivityView) {
            activityListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_connection_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        btTryAgain.setOnClickListener {
            activityListener?.navigateToBack()
        }
    }
}