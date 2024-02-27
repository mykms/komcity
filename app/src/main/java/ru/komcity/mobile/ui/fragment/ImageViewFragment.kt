package ru.komcity.mobile.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.komcity.mobile.R
import ru.komcity.mobile.databinding.FragmentImageViewBinding

class ImageViewFragment : Fragment() {
    private var _binding: FragmentImageViewBinding? = null
    private val binding get() = _binding!!

    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentImageViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view, arguments?.get("Bitmap"))
    }

    private fun initComponents(view: View, bmp: Any?) {
        initToolbar()
        binding.ivImage.setImageBitmap(bmp as? Bitmap)
        mScaleGestureDetector = ScaleGestureDetector(view.context, object: ScaleGestureDetector.OnScaleGestureListener {


            override fun onScale(detector: ScaleGestureDetector): Boolean {
                mScaleFactor *= detector?.getScaleFactor() ?: 0f
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
                binding.ivImage.setScaleX(mScaleFactor)
                binding.ivImage.setScaleY(mScaleFactor)
                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
            }
        })
        view.setOnTouchListener { v, event ->
            mScaleGestureDetector?.onTouchEvent(event)
            true
        }
    }

    private fun initToolbar() = with(binding.toolbar) {
        title = "Просмотр изображения"
        setNavigationIcon(R.drawable.vector_ic_arrow_back_white)
        setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }
}
