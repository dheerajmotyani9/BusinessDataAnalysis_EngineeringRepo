package mapitgis.jalnigamk.util

import android.R
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import mapitgis.jalnigamk.nirmal.view.FormKeyValueText
import java.text.SimpleDateFormat
import java.util.Locale

@BindingAdapter("app:visibilityGone")
fun View.setVisibilityGone(isVisible: Boolean?) {
    visibility = if (isVisible == true) View.VISIBLE else View.GONE
}

@BindingAdapter("imageUri")
fun loadImage(view: ImageView, imageUri: String?) {
    if (!imageUri.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(Uri.parse(imageUri)) // Convert string to URI
            .placeholder(R.drawable.progress_indeterminate_horizontal) // Loading placeholder
            .error(R.drawable.ic_menu_report_image) // Error placeholder
            .into(view)
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@BindingAdapter("app:value")
fun setTextForValue(view: FormKeyValueText, value: String?) {
    view.setValue(value)
}


@BindingAdapter("formattedDate")
fun TextView.setFormattedDate(dateString: String?) {
    if (dateString.isNullOrEmpty()) {
        text = ""
        return
    }

    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        text = outputFormat.format(date!!)
    } catch (e: Exception) {
        text = dateString // fallback if format fails
    }
}


@BindingAdapter(value = ["enabledColorRes", "disabledColorRes"], requireAll = true)
fun setButtonColorRes(
    button: Button,
    @ColorRes enabledColorRes: Int,
    @ColorRes disabledColorRes: Int
) {
    val context = button.context

    fun updateColor() {
        val colorRes = if (button.isEnabled) enabledColorRes else disabledColorRes
        val color = ContextCompat.getColor(context, colorRes)
        button.backgroundTintList = ColorStateList.valueOf(color)
    }

    // Initial color
    updateColor()

    // Update when layout changes (e.g., state updates)
    button.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        updateColor()
    }
}



