package com.mayandro.bitcointrend.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mayandro.bitcointrend.R
import com.mayandro.bitcointrend.utils.DialogUtils

//This class is responsible for abstracting out common code from all fragments like showing alert dialog, setting view etc.
abstract class BaseFragment<B : ViewBinding>() : Fragment(){
    abstract fun getViewBinding(): B

    lateinit var binding: B

    val dialogUtils: DialogUtils by lazy {
        DialogUtils()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        return binding.root
    }

    fun clearDialogMessage() {
        dialogUtils.dismissDialog()
    }

    fun showDialogMessage(
        title: String,
        message: String,
        positiveButton: String = getString(R.string.close),
        negativeButton: String = "",
        positiveButtonClickListener: ((View) -> Unit)? = null,
        negativeButtonClickListener: ((View) -> Unit)? = null,
        isCancellable: Boolean = true
    ) {
        dialogUtils.showAlertMessage(
            title = title,
            message = message,
            positiveButton = positiveButton,
            positiveButtonClickListener = positiveButtonClickListener,
            negativeButton = negativeButton,
            negativeButtonClickListener = negativeButtonClickListener,
            isCancellable = isCancellable,
            context = requireContext()
        )
    }
}
