package mapitgis.jalnigamk.base

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import mapitgis.jalnigam.BaseActivity

abstract class BaseKActivity<VM : BaseViewModel> : BaseActivity() {

    protected abstract val viewModel: VM

    private var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLoadingDialog()
        observeLoadingState()

        // Observe finish
        viewModel.finishActivity.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                if (it) finish()
            }
        }

        viewModel.launchActivity.observe(this) { event ->
            event.getContentIfNotHandled()?.let { activityClass ->
                val intent = Intent(this, activityClass.java)
                startActivity(intent)
            }
        }
    }

    private fun setupLoadingDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(50, 40, 50, 40)
            gravity = Gravity.CENTER_VERTICAL
        }

        val progressBar = ProgressBar(this).apply {
            isIndeterminate = true
        }

        val textView = TextView(this).apply {
            text = "Please wait..."
            textSize = 14f
            setPadding(32, 0, 0, 0)
        }

        layout.addView(progressBar)
        layout.addView(textView)

        loadingDialog = AlertDialog.Builder(this)
            .setView(layout)
            .setCancelable(false)
            .create()
    }


    fun setupToolbar(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun observeLoadingState() {
        viewModel.loadingLiveData.observe(this) { isLoading ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    private fun showLoading() {
        if (loadingDialog?.isShowing != true) {
            loadingDialog?.show()
        }
    }

    private fun hideLoading() {
        if (loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
        }
    }
}
