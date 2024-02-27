package ru.komcity.mobile.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.komcity.mobile.databinding.FragmentConnectionErrorBinding
import ru.komcity.mobile.view.MainActivityView

class ConnectionErrorFragment : Fragment() {
    private var _binding: FragmentConnectionErrorBinding? = null
    private val binding get() = _binding!!

    private var activityListener: MainActivityView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivityView) {
            activityListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentConnectionErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        binding.btTryAgain.setOnClickListener {
            activityListener?.navigateToBack()
        }
    }
}