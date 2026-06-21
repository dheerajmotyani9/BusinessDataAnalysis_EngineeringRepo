package mapitgis.jalnigamk.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mapitgis.jalnigam.BuildConfig
import kotlin.reflect.KClass

abstract class BaseViewModel(app: Application) : AndroidViewModel(app){

    private val context = app as Context

    // Finish Activity LiveData
    private val _finishActivity = MutableLiveData<Event<Boolean>>()
    val finishActivity: LiveData<Event<Boolean>> get() = _finishActivity

    protected fun finishCurrentActivity() {
        _finishActivity.postValue(Event(true))
    }

    // LiveData for launching an Activity once
    private val _launchActivity = MutableLiveData<Event<KClass<out Activity>>>()
    val launchActivity: LiveData<Event<KClass<out Activity>>> get() = _launchActivity

    // Trigger activity launch
    protected fun launchActivity(target: KClass<out Activity>) {
        _launchActivity.postValue(Event(target))
    }

    private val _loading = MutableLiveData(false)
    val loadingLiveData: LiveData<Boolean>
        get() = _loading

    protected fun showLoading(){
        _loading.postValue(true)
    }

    protected fun hideLoading(){
        _loading.postValue(false)
    }
    //---------------------------------------------//


    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(block = block)
    }

    protected fun launchLoading(block: suspend () -> Unit) {
        launch {
            encapsulateLoading(block)
        }
    }

    private suspend fun encapsulateLoading(block: suspend () -> Unit) {
        _loading.encapsulate(block)
    }


    protected fun toast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    init {
        if(BuildConfig.DEBUG)Log.d("BaseViewModel", "${this::class.simpleName} initialized")
    }

    override fun onCleared() {
        super.onCleared()
        if(BuildConfig.DEBUG)Log.d("BaseViewModel", "${this::class.simpleName} cleared")
    }


}


suspend fun MutableLiveData<Boolean>.encapsulate(block: suspend () -> Unit) {
    postValue(true)
    block()
    postValue(false)
}



open class Event<out T>(private val content: T) {

    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) null else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content even if it's already been handled.
     */
    fun peekContent(): T = content
}