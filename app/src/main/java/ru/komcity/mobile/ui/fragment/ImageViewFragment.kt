package ru.komcity.mobile.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_image_view.*
import ru.komcity.mobile.R

class ImageViewFragment : Fragment() {

    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_image_view, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view, arguments?.get("Bitmap"))
    }

    private fun initComponents(view: View, bmp: Any?) {
        initToolbar()
        ivImage.setImageBitmap(bmp as? Bitmap)
        mScaleGestureDetector = ScaleGestureDetector(context, object: ScaleGestureDetector.OnScaleGestureListener {
            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector?) {
            }

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                mScaleFactor *= detector?.getScaleFactor() ?: 0f
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
                ivImage.setScaleX(mScaleFactor)
                ivImage.setScaleY(mScaleFactor)
                return true
            }
        })
        view.setOnTouchListener { v, event ->
            mScaleGestureDetector?.onTouchEvent(event)
            true
        }
    }

    private fun initToolbar() = with(toolbar) {
        title = "Просмотр изображения"
        setNavigationIcon(R.drawable.vector_ic_arrow_back_white)
        setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }
}
