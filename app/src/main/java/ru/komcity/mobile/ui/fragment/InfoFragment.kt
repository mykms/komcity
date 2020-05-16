package ru.komcity.mobile.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import ru.komcity.mobile.R

class InfoFragment : BaseFragment() {
    override fun onCreateInit(clientId: String, context: Context) {
    }

    override fun getArgs(args: Bundle?) {
        //
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_info
    }

    override fun initComponents(view: View) {
        //
    }
}