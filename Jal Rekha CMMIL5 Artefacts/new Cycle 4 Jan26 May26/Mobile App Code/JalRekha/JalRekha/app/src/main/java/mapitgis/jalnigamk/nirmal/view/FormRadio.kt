package mapitgis.jalnigamk.nirmal.view

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import com.google.android.material.radiobutton.MaterialRadioButton
import mapitgis.jalnigam.R
import mapitgis.jalnigam.databinding.ViewFormRadioBinding

@RequiresApi(Build.VERSION_CODES.N)
class FormRadio @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var binding: ViewFormRadioBinding =
        ViewFormRadioBinding.inflate(LayoutInflater.from(context), this)

    private var onValueChangeListener: OnValueChangeListener? = null
    private var entries: List<String> = emptyList()

    private var mandatory: Boolean = false

    init {
        attrs?.let { applyAttributes(context, it) }
        setRadioGroupListener()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun applyAttributes(context: Context, attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FormRadio)
        try {
            val label = attributes.getString(R.styleable.FormRadio_label)
            val groupOrientation = attributes.getInt(R.styleable.FormRadio_groupOrientation,
                HORIZONTAL
            )
            val entriesString = attributes.getString(R.styleable.FormRadio_entries)
            mandatory = attributes.getBoolean(R.styleable.FormLabel_mandatory, false)

            if (entriesString.isNullOrEmpty()) {
                throw IllegalArgumentException("No Radio Group Entries provided")
            }

            entries = entriesString.split("|")
            setLabel(label,mandatory)
            setRadioGroupOrientation(groupOrientation)
            setRadioGroupEntries(entries)
        } finally {
            attributes.recycle()
        }
    }

    private fun setRadioGroupListener() {
        binding.radioOption.setOnCheckedChangeListener { _, checkedId ->
            val selectedButton = findViewById<RadioButton>(checkedId)
            selectedButton?.let {
                val selectedValue = it.text.toString()
                val index = entries.indexOf(selectedValue)
                onValueChangeListener?.onValueChange(selectedValue, index)
            }
        }
    }

    fun getSelectedValue(): String? {
        return binding.radioOption.findViewById<RadioButton>(binding.radioOption.checkedRadioButtonId)?.text?.toString()
    }

    fun getSelectedIndex(): Int {
        return entries.indexOf(getSelectedValue()).takeIf { it >= 0 } ?: -1
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setLabel(title: String?, isMandatory: Boolean = false) {
        binding.tvLabel.visibility = if (title.isNullOrEmpty()) GONE else VISIBLE
        binding.tvLabel.text = if (isMandatory) {
            Html.fromHtml("$title <big><b><font color='#FF0000'>*</font></b></big>", Html.FROM_HTML_MODE_LEGACY)
        } else {
            title
        }
    }

    fun setRadioGroupOrientation(orientation: Int) {
        binding.radioOption.orientation = orientation
    }

    fun setRadioGroupEntries(entries: List<String>) {
        this.entries = entries
        binding.radioOption.removeAllViews()

        val layoutParam = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT,
            RadioGroup.LayoutParams.WRAP_CONTENT, 1f
        )
        layoutParam.setMargins(0,-32,0,0)

        entries.forEach { value ->
            val radioButton = MaterialRadioButton(context).apply {
                text = value
                id = generateViewId()
                textSize = 14.0f
                //setButtonDrawable(R.drawable.radio_selector)
                layoutParams = layoutParam
            }
            binding.radioOption.addView(radioButton)
        }
    }

    fun clearSelection() {
        binding.radioOption.clearCheck()
    }

    fun setSelectedValue(selectedValue: String) {
        entries.indexOf(selectedValue).takeIf { it >= 0 }?.let { index ->
            setSelectedIndex(index)
        }
    }

    fun setSelectedIndex(index: Int) {
        (binding.radioOption.getChildAt(index) as? RadioButton)?.isChecked = true
    }

    fun setOnChangeListener(listener: OnValueChangeListener) {
        onValueChangeListener = listener
    }

    fun setMargin(left: Int, top: Int, right: Int, bottom: Int) {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        //params.setMargins(left.dp, top.dp, right.dp, bottom.dp)
        layoutParams = params
    }
}

interface OnValueChangeListener {
    fun onValueChange(selectedValue: String, selectedIndex: Int)
}
