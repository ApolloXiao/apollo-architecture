package com.apollo.architecture.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollo.architecture.model.api.ExceptionHandle
import com.apollo.architecture.model.api.SimpleException
import com.apollo.architecture.model.bean.BaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    val uiEvent: UIEvent by lazy { UIEvent() }

    fun <T> launchResultOnUI(
            block: suspend CoroutineScope.() -> BaseResponse<T>,
            success: (T) -> Unit,
            error: (SimpleException) -> Unit = {
                uiEvent.showErrorMsg.postValue(it.errorMessage)
            },
            complete: () -> Unit = {},
            isShowDialog: Boolean = true
    ) {
        if (isShowDialog) uiEvent.showDialog.postValue(null)
        launchUI {
            handleException(
                    //retrofit请求是在子线程中进行，这里不需要withContext(Dispatchers.IO){block()}
                    { block() },
                    { res ->
                        executeResponse(res) { success(it) }
                    },
                    {
                        error(it)
                    },
                    {
                        uiEvent.dismissDialog.postValue(null)
                        complete()
                    }
            )
        }
    }

    fun launchUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            block()
        }
    }

    private suspend fun <T> executeResponse(
            response: BaseResponse<T>,
            success: suspend CoroutineScope.(T) -> Unit
    ) {
        coroutineScope {
            if (response.errorCode == 0) {
                success(response.data)
            } else {
                throw SimpleException(response.errorCode, response.errorMsg)
            }
        }
    }

    private suspend fun <T> handleException(
            block: suspend CoroutineScope.() -> BaseResponse<T>,
            success: suspend CoroutineScope.(BaseResponse<T>) -> Unit,
            error: suspend CoroutineScope.(SimpleException) -> Unit,
            complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                success(block())
            } catch (e: Throwable) {
                error(ExceptionHandle.handleException(e))
            } finally {
                complete()
            }
        }
    }

    inner class UIEvent {
        val showDialog by lazy { MutableLiveData<String?>() }
        val dismissDialog by lazy { MutableLiveData<Nothing?>() }
        val showErrorMsg by lazy { MutableLiveData<String?>() }
    }
}