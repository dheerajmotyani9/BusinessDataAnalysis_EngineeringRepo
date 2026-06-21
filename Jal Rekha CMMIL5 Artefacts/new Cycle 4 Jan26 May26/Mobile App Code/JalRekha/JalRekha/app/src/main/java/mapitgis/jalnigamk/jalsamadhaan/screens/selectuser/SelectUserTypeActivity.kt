package mapitgis.jalnigamk.jalsamadhaan.screens.selectuser

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import mapitgis.jalnigam.core.Utility
import mapitgis.jalnigam.databinding.ActivitySelectUserTypeBinding
import mapitgis.jalnigamk.UserType
import mapitgis.jalnigamk.UserTypeManager
import mapitgis.jalnigamk.jalsamadhaan.screens.citizenlogin.CitizenLoginActivity

class SelectUserTypeActivity : AppCompatActivity() {

    private val binding: ActivitySelectUserTypeBinding by lazy {
        ActivitySelectUserTypeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.citizenLogin.setOnClickListener {
            UserTypeManager.saveUserType(baseContext, UserType.CITIZEN)
            startActivity(Intent(this, CitizenLoginActivity::class.java))
            finish()
        }

        binding.departmentLogin.setOnClickListener {
            UserTypeManager.saveUserType(baseContext, UserType.DEPARTMENT)
            Utility.goFirst(this)
        }

        UserTypeManager.saveUserType(baseContext, UserType.DEPARTMENT) //for skipping select user type screen

        //check if user is already selected
        val userType = UserTypeManager.getUserType(this)
        userType?.let {
            when (it) {
                UserType.CITIZEN -> binding.citizenLogin.callOnClick()
                UserType.DEPARTMENT -> binding.departmentLogin.callOnClick()
            }
            finish()
        }
    }
}