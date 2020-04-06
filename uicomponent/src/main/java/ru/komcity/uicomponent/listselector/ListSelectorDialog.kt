package ru.komcity.uicomponent.listselector

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.ui_list_selector_dialog.*
import ru.komcity.uicomponent.R

/**
 * Created by Aleksei Kholoimov on 05.04.2020
 * <p>
 * Выбор из списка в Диалоге (Fragment)
 */
interface ListSelectorDialogListener {
    fun onCloseClick()
    fun onSelectItem(item: String, position: Int)
}

class ListSelectorDialog(private val items: List<String>,
                         private val listener: ListSelectorDialogListener) : BottomSheetDialogFragment() {

    private var pTitle = ""
    private val adapterItems = ListSelectorAdapter(items) { item, position ->
        listener.onSelectItem(item, position)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ui_list_selector_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).background = ColorDrawable(Color.TRANSPARENT)
    }

    private fun initComponents() {
        ivClose.setOnClickListener {
            listener.onCloseClick()
        }
        textTitle.text = pTitle
        initRecyclerView()
    }

    private fun initRecyclerView() = with(rvItems) {
        adapter = adapterItems
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ContextCompat.getDrawable(context, R.drawable.ui_recycler_divider)
                    ?: ShapeDrawable())
        })
    }

    fun setTitle(title: String) {
        pTitle = title
    }
}