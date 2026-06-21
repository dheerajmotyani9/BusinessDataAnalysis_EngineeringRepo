package mapitgis.jalnigam.nirmal.camera

import android.content.Context
import android.content.Intent
import android.os.Bundle
import mapitgis.jalnigam.BaseActivity
import mapitgis.jalnigam.databinding.ActivityViewimageBinding

class ViewImageActivity : BaseActivity() {

    companion object{
        fun viewImage(context: Context,imagePath: String?){
            val intent = Intent(context, ViewImageActivity::class.java).apply {
                putExtra("image_path", imagePath ?:"")
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val binding: ActivityViewimageBinding by lazy {
        ActivityViewimageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val imagePath = intent?.getStringExtra("image_path")
        imagePath?.let {
            binding.imagePath = it
        } ?: finish()

        binding.btnClose.setOnClickListener { finish() }
    }

}