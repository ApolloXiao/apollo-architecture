package com.apollo.architecture.ui.base

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer

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

    private fun showLoading(msg: String) {
        //fixme UI待完善
        if (dialog == null) {
            dialog = Dialog(this)
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        dialog?.show()

    }

    private fun dismissLoading() {
        dialog?.run { if (isShowing) dismiss() }
    }

}