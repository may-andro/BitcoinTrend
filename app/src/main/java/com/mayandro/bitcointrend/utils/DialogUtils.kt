package com.mayandro.bitcointrend.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mayandro.bitcointrend.R
import com.mayandro.bitcointrend.databinding.BottomSheetGeneralNotificationBinding
import com.mayandro.bitcointrend.databinding.BottomSheetListBinding
import com.mayandro.bitcointrend.databinding.ItemBottomSheetListBinding

//This class is a helper class to show alert messages for the app
class DialogUtils {
    var bottomSheetDialog: BottomSheetDialog? = null

    fun showAlertMessage(
        context: Context,
        title: String,
        message: String,
        positiveButton: String,
        negativeButton: String = "",
        positiveButtonClickListener: ((View) -> Unit)? = null,
        negativeButtonClickListener: ((View) -> Unit)? = null,
        isCancellable: Boolean = true
    ) {

        dismissDialog()

        val binding = BottomSheetGeneralNotificationBinding.inflate(LayoutInflater.from(context))

        binding.apply {
            textViewDialogTitle.text = title
            textViewDialogTitle.isVisible = message.isNotEmpty()
            textViewDialogMessage.text = message

            buttonPositive.apply {
                text = positiveButton
                if (positiveButton.isNotEmpty()) this.visibility = View.VISIBLE
                setOnClickListener {
                    dismissDialog()
                    positiveButtonClickListener?.invoke(it)
                }
            }

            buttonNegative.apply {
                text = negativeButton
                if (negativeButton.isNotEmpty()) this.visibility = View.VISIBLE
                setOnClickListener {
                    dismissDialog()
                    negativeButtonClickListener?.invoke(it)
                }
            }
        }

        bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog?.setContentView(binding.root)
        bottomSheetDialog?.setCanceledOnTouchOutside(false)
        bottomSheetDialog?.setCancelable(isCancellable)
        val bottomSheet = bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        bottomSheet?.let {
            val bottomSheetBehavior = BottomSheetBehavior.from(it)
            bottomSheetDialog?.setOnShowListener {
                bottomSheetBehavior.setPeekHeight(binding.root.height)
            }
        }
        bottomSheetDialog?.show()
    }

    fun showListBottomSheet(
        context: Context,
        title: String,
        message: String,
        list: List<String>,
        selectedItem: Int?,
        onItemSelected: ((Int) -> Unit)? = null,
        isCancellable: Boolean = true
    ) {
        dismissDialog()

        val binding = BottomSheetListBinding.inflate(LayoutInflater.from(context))

        binding.apply {
            textViewDialogTitle.text = title
            textViewDialogTitle.isVisible = message.isNotEmpty()
            textViewDialogMessage.text = message

            val dropDownAdapter = DropDownAdapter(list, selectedItem)
            dropDownAdapter.selectionListener = object : DropDownAdapter.OnItemSelectedListener{
                override fun onItemSelected(position: Int) {
                    onItemSelected?.invoke(position)
                    dismissDialog()
                }
            }
            bottomSheetList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = dropDownAdapter
            }
        }

        bottomSheetDialog =  BottomSheetDialog(context)
        bottomSheetDialog?.apply {
            setContentView(binding.root)
            setCanceledOnTouchOutside(false)
            setCancelable(isCancellable)
            val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            bottomSheet?.let {
                val bottomSheetBehavior = BottomSheetBehavior.from(it)
                bottomSheetDialog?.setOnShowListener {
                    bottomSheetBehavior.setPeekHeight(binding.root.height)
                }
            }
            show()
        }
    }

    fun dismissDialog(): Boolean {
        bottomSheetDialog?.let {
            if (it.isShowing) {
                it.dismiss()
                return true
            }
        }
        return false
    }

}


class DropDownAdapter(private val list: List<String>,private val selectedItem: Int?)
    : RecyclerView.Adapter<DropDownAdapter.ViewHolder>() {

    var selectionListener: OnItemSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_bottom_sheet_list, parent,
            false
        )
        return ViewHolder(ItemBottomSheetListBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int)
    }

    inner class ViewHolder(private val binding: ItemBottomSheetListBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                selectionListener?.onItemSelected(adapterPosition)
            }
        }

        fun render(data: String) {
            binding.textValue.text = data
            val color = if(selectedItem == adapterPosition) {
                R.color.colorPrimaryVariant
            } else {
                R.color.textColorGrey
            }
            binding.textValue.setTextColor(ContextCompat.getColor(binding.root.context, color))
        }
    }
}