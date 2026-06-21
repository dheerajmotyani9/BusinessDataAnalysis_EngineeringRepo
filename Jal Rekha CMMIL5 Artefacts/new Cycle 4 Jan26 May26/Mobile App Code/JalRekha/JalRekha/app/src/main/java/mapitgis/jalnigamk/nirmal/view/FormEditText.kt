package mapitgis.jalnigamk.nirmal.view


import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import mapitgis.jalnigam.R
import mapitgis.jalnigam.databinding.ViewFormEdittextBinding

class FormEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewFormEdittextBinding =
        ViewFormEdittextBinding.inflate(LayoutInflater.from(context), this)

    private var _error: String? = null
    private var mandatory: Boolean = false

    init {
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.FormEditText)
            val label = attributes.getString(R.styleable.FormEditText_label)
            _error = attributes.getString(R.styleable.FormEditText_error)
            val hint = attributes.getString(R.styleable.FormEditText_hint)
            val inputType = attributes.getInt(R.styleable.FormEditText_android_inputType, EditorInfo.TYPE_NULL)
            val radius = attributes.getDimensionPixelSize(R.styleable.FormEditText_radius, 0)
            val imeOptions = attributes.getInt(R.styleable.FormEditText_android_imeOptions, EditorInfo.IME_ACTION_NEXT)
            val drawable = attributes.getDrawable(R.styleable.FormEditText_icon)
            mandatory = attributes.getBoolean(R.styleable.FormEditText_mandatory, false)
            val singleLine = attributes.getBoolean(R.styleable.FormEditText_android_singleLine, true)
            val editable = attributes.getBoolean(R.styleable.FormEditText_android_enabled, true)
            val border = attributes.getBoolean(R.styleable.FormEditText_border, false)
            val length = attributes.getInt(R.styleable.FormEditText_android_maxLength, 0)
            val lines = attributes.getInt(R.styleable.FormEditText_android_lines, 1)
            val gravity = attributes.getInt(R.styleable.FormEditText_android_gravity,Gravity.LEFT)

            attributes.recycle()

            setSingleLine(singleLine)
            setLabel(label, mandatory)
            setHint(hint)
            setInputType(inputType)
            setRadius(radius)
            setLeftIcon(drawable)
            setFieldBorder(border)
            setLength(length)
            setEnable(editable)
            setIMEIOption(imeOptions)
            setLines(lines)
            setFieldGravity(gravity)
        }
    }

    fun setIMEIOption(option: Int = EditorInfo.IME_ACTION_NEXT) {
        binding.etField.imeOptions = option
    }

    fun setLines(line: Int) {
        binding.etField.setLines(line)
    }

    fun setFieldGravity(gravity: Int) {
        binding.etField.gravity = gravity
    }



    fun setSingleLine(singleLine: Boolean) {
        binding.etField.isSingleLine = singleLine
    }

    fun setLength(length: Int) {
        if (length > 0) binding.etField.filters = arrayOf(InputFilter.LengthFilter(length))
    }

    fun setFieldBorder(flag: Boolean) {
        if (flag) {
            //binding.cardView.strokeColor = ContextCompat.getColor(context, R.color.colorPrimary)
            //binding.cardView.strokeWidth = 1
        }
    }

    fun setText(string: String?) {
        binding.etField.setText(string ?: "")
    }

    fun setEnable(isEnabled: Boolean) {
        binding.etField.isEnabled = isEnabled
    }

    fun afterTextChanged(afterTextChanged: (String) -> Unit) {
        binding.etField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }


    fun setOnClickListener(onClick: (View) -> Unit) {
        binding.etField.isEnabled = true // Prevent direct text input
        binding.etField.isFocusable = false
        binding.etField.isCursorVisible = false

        binding.etField.setOnClickListener {
            onClick.invoke(binding.etField)
        }
    }

    fun setLeftIcon(drawable: Drawable?) {
        drawable?.let {
            binding.etField.setCompoundDrawablesWithIntrinsicBounds(it, null, null, null)
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
            binding.tvLabel.text = if (isMandatory) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml("$title <big><b><font color='#FF0000'>*</font></b></big>", Html.FROM_HTML_MODE_LEGACY)
                } else {
                    title
                }
            } else {
                title
            }
        }
    }

    fun setHint(hint: String?) {
        binding.etField.hint = hint ?: ""
    }

    fun showError(error: String? = _error) {
        error?.let {
            binding.etField.error = it
            binding.etField.requestFocus()
        }
    }

    fun setInputType(inputType: Int) {
        if (inputType != EditorInfo.TYPE_NULL) {
            val typeface: Typeface = binding.etField.typeface
            binding.etField.inputType = inputType
            binding.etField.typeface = typeface
        }
    }

    fun isEmpty(): Boolean = binding.etField.text.toString().trim().isEmpty()

    fun isMandatory(): Boolean = mandatory

    fun getLabel(): String = binding.tvLabel.text.toString()

    fun getFieldValue(): String = binding.etField.text.toString().trim()

    fun getEditText(): EditText = binding.etField

    fun setMargin(left: Int, top: Int, right: Int, bottom: Int) {
        val mParam = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        mParam.setMargins(left, top, right, bottom)
        layoutParams = mParam
    }

    fun isValidMobileNumber(): Boolean {
        val regex = Regex("^[6-9][0-9]{9}$")
        return getFieldValue().matches(regex)
    }

}
