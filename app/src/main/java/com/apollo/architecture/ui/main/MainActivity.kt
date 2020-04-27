package com.apollo.architecture.ui.main

import androidx.lifecycle.Observer
import com.apollo.architecture.R
import com.apollo.architecture.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainActivity : BaseActivity<MainViewModel>() {
    override fun getContentViewId(): Int {
        return R.layout.activity_main
    }

    override fun initViewModel(): MainViewModel {
        return getViewModel()
    }

    override fun addLiveDataObserve() {
        mViewModel.articleList.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                text.text = it[0].name
            }
        })
    }

    override fun initView() {

    }

    override fun initData() {
        mViewModel.fetchArticleLists()
    }

}
