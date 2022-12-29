package nyp.sit.movieviewer.basic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_verification_code.*

class VerificationCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code)
        verifyBtn.setOnClickListener{
            val hasError = validation()
            if (!hasError){
                if (verifyET.text.toString()=="1234"){
                    displayToast("Code Verified")
                    val intent = Intent(this@VerificationCodeActivity,LoginActivity::class.java)
                    startActivity(intent)

                }else{
                    displayToast("Code Error")
                }
            }
        }
    }

    private fun validation(): Boolean {
        var hasError = false

        if (verifyET.text.isEmpty()){
            verifyET.error = "This field cannot be empty"
            hasError = true
        }

        return hasError
    }

    private fun displayToast(message:String){
        Toast.makeText(this@VerificationCodeActivity,message, Toast.LENGTH_LONG).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this@VerificationCodeActivity,RegistrationActivity::class.java)
        startActivity(intent)
        return super.onSupportNavigateUp()
    }
}