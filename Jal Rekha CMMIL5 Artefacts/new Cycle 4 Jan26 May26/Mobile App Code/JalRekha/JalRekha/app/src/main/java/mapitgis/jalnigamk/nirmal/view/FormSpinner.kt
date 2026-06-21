package mapitgis.jalnigamk.nirmal.view


import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import mapitgis.jalnigam.R
import mapitgis.jalnigam.databinding.ViewFormSpinnerBinding

class FormSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewFormSpinnerBinding =
        ViewFormSpinnerBinding.inflate(LayoutInflater.from(context), this)

    private var mandatory: Boolean = false

    init {
        attrs?.let { applyAttributes(context, it) }
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FormSpinner)
        try {
            val label = attributes.getString(R.styleable.FormSpinner_label)
            val radius = attributes.getDimensionPixelSize(R.styleable.FormSpinner_radius, 0)
            mandatory = attributes.getBoolean(R.styleable.FormSpinner_mandatory, false)
            val border = attributes.getBoolean(R.styleable.FormSpinner_border, false)

            setLabel(label, mandatory)
            setRadius(radius)
            setFieldBorder(border)
        } finally {
            attributes.recycle()
        }
    }

    fun setFieldBorder(flag: Boolean) {
        if (flag) {
            //binding.cardView.strokeColor = ContextCompat.getColor(context, R.color.colorPrimary)
            //binding.cardView.strokeWidth = 1
        }
    }

    fun setRadius(radius: Int) {
        if (radius != 0) {
            binding.cardView.radius = radius.toFloat()
        }
    }

    fun setLabel(title: String?, isMandatory: Boolean = false) {
        if (title.isNullOrEmpty()) {
            binding.tvLabel.visibility = GONE
        } else {
            binding.tvLabel.visibility = VISIBLE
            val labelText = if (isMandatory) {
                Html.fromHtml("$title <big><b><font color='#FF0000'>*</font></b></big>")
            } else {
                title
            }
            binding.tvLabel.text = labelText
        }
    }

    fun <T> setAdapterData(
        arrayList: ArrayList<T>,
        selectedValue: String? = null,
        showPrompt: Boolean = false
    ) {
        val adapter = object : ArrayAdapter<T>(context, R.layout.row_spinner_item, arrayList) {
            override fun getDropDownView(
                position: Int, convertView: View?, parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (position == 0 && showPrompt) R.color.gray_color
                        else if (position == binding.viewSpinner.selectedItemPosition) R.color.colorAccent
                        else R.color.black
                    )
                )
                return view
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setSingleLine()
                view.setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (position == 0 && showPrompt) R.color.gray_color else R.color.black
                    )
                )
                return view
            }
        }
        binding.viewSpinner.adapter = adapter

        selectedValue?.let {
            val index = arrayList.indexOfFirst { it.toString() == selectedValue }
            if (index != -1) binding.viewSpinner.setSelection(index)
        }
    }

    fun <T> onItemSelected(onItemSelected: (T, View?, Int, Boolean) -> Unit) {
        binding.viewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val isLast = parent?.count?.minus(1) == position
                onItemSelected.invoke(parent?.getItemAtPosition(position) as T, view, position, isLast)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun getLabel(): String = binding.tvLabel.text.toString()
    fun getSpinner(): Spinner = binding.viewSpinner
    fun getSelectedItem(): Any = binding.viewSpinner.selectedItem
    fun getSelectedPosition(): Int = binding.viewSpinner.selectedItemPosition

    fun setMargin(left: Int, top: Int, right: Int, bottom: Int) {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(left, top, right, bottom)
        layoutParams = params
    }
}
