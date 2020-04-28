package com.apollo.architecture.ui.base

import android.app.Dialog
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.apollo.architecture.R
import kotlinx.android.synthetic.main.dialog_loading.view.*

abstract class BaseActivity<VM : BaseViewModel>(userDataBinding: Boolean = false) : AppCompatActivity() {
    private val _userDataBinding = userDataBinding
    private lateinit var mBinding: ViewDataBinding
    lateinit var mViewModel: VM
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = initViewModel()
        addLiveDataObserve()
        observerBaseUIEvent()
        if (_userDataBinding) {
            mBinding = DataBindingUtil.setContentView(this, getContentViewId())
        } else {
            setContentView(getContentViewId())
        }
        initView()
        initData()
    }

    override fun onDestroy() {
        dismissLoading()
        super.onDestroy()
    }

    abstract fun getContentViewId(): Int
    abstract fun initViewModel(): VM
    abstract fun addLiveDataObserve()
    abstract fun initView()
    abstract fun initData()

    private fun observerBaseUIEvent() {
        mViewModel.uiEvent.showDialog.observe(this, Observer {
            showLoading(it)
        })
        mViewModel.uiEvent.dismissDialog.observe(this, Observer {
            dismissLoading()
        })
        mViewModel.uiEvent.showErrorMsg.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun showLoading(msg: String?) {
        if (dialog == null) {
            dialog = Dialog(this)
            dialog?.run {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                val loadingView = layoutInflater.inflate(R.layout.dialog_loading, null)
                loadingView.loading_text.text = msg?:"加载中..."
                loadingView.loading_progress.indeterminateDrawable.colorFilter =
                        PorterDuffColorFilter(
                                ContextCompat.getColor(this.context, R.color.colorPrimaryDark),
                                PorterDuff.Mode.MULTIPLY)
                setContentView(loadingView)
            }
        }
        dialog?.show()
    }

    private fun dismissLoading() {
        dialog?.run { if (isShowing) dismiss() }
    }

}