package ru.komcity.uicomponent.listselector

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.ui_list_selector_dialog.*
import ru.komcity.uicomponent.DividerWithRemoveDecorator
import ru.komcity.uicomponent.R
import java.io.Serializable

/**
 * Created by Aleksei Kholoimov on 05.04.2020
 * <p>
 * Выбор из списка в Диалоге (Fragment)
 */
interface ListSelectorDialogListener: Serializable {
    fun onCloseClick()
    fun onSelectItem(item: String, position: Int)
}

class ListSelectorDialog: BottomSheetDialogFragment() {

    private var pTitle = ""
    private var items: List<String>? = null
    private var listener: ListSelectorDialogListener? = null
    private val keyItems = "items"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Design_BottomSheetDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        items = arguments?.getStringArrayList(keyItems)
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
        listener?.let {
            initComponents(items ?: emptyList(), it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).background = ColorDrawable(Color.TRANSPARENT)
    }

    fun setParams(items: List<String>, listener: ListSelectorDialogListener) {
        this.items = items
        this.listener = listener
        this.arguments = bundleOf(keyItems to items)
    }

    private fun initComponents(items: List<String>, listener: ListSelectorDialogListener) {
        ivClose.setOnClickListener {
            listener.onCloseClick()
        }
        textTitle.text = pTitle
        initRecyclerView(items, listener)
    }

    private fun initRecyclerView(items: List<String>, listener: ListSelectorDialogListener) = with(rvItems) {
        adapter = ListSelectorAdapter(items) { item, position ->
            listener.onSelectItem(item, position)
        }
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        addItemDecoration(DividerWithRemoveDecorator(context, R.drawable.ui_recycler_divider, 0, 1))
    }

    fun setTitle(title: String) {
        pTitle = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener = null
    }
}