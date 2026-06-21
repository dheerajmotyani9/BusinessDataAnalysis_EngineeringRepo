package mapitgis.jalnigamk.fhtc.view


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import mapitgis.jalnigam.R
import androidx.core.content.withStyledAttributes

class FHTCDashboardButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val icon: ImageView
    private val title: TextView
    private val subtitle: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_fhtc_dashboard_button, this, true)

        icon = findViewById(R.id.iv_icon)
        title = findViewById(R.id.tv_title)
        subtitle = findViewById(R.id.tv_subtitle)

        // Load custom attributes
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.FHTCDashboardButton, 0, 0) {

                getDrawable(R.styleable.FHTCDashboardButton_ucv_icon)?.let { d ->
                    icon.setImageDrawable(d)
                }
                title.text = getString(R.styleable.FHTCDashboardButton_ucv_title) ?: ""
                subtitle.text = getString(R.styleable.FHTCDashboardButton_ucv_subtitle) ?: ""

            }
        }
    }

    fun setIcon(drawableRes: Int) {
        icon.setImageResource(drawableRes)
    }

    fun setTitle(text: String) {
        title.text = text
    }

    fun setSubtitle(text: String) {
        subtitle.text = text
    }
}
