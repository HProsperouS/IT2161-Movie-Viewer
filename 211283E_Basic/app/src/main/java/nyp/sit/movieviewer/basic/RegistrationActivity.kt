package nyp.sit.movieviewer.basic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        registerBtn.setOnClickListener{
            val hasError = validation()
            if (hasError == false){
                val name = nameET.text.toString()
                val password = passwordET.text.toString()
                val email = emailET.text.toString()
                val adminNum = adminNumET.text.toString()
                val pemGrp = pemET.text.toString()

                val message:ArrayList<String> = arrayListOf(
                    "Login Name: ${name}",
                    "Password: ${password}",
                    "Email: ${email}",
                    "Admin Number: ${adminNum}",
                    "Pem Group: ${pemGrp}"
                )

                displayToast(message.joinToString("\n"))

                //Delay for 2 seconds before move to next activity
                //Handler() is depreciated now in place of it we have to use Handler(Looper.getMainLooper())
                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        val intent = Intent(this@RegistrationActivity,VerificationCodeActivity::class.java)
                        startActivity(intent)
                    }
                },2000)


            }
        }
    }

    private fun validation(): Boolean {
        var hasError = false

        if (nameET.text.isEmpty()){
            nameET.error = "This field cannot be empty"
            hasError = true
        }
        if (passwordET.text.isEmpty()){
            passwordET.error = "This field cannot be empty"
            hasError = true
        }
        if (emailET.text.isEmpty()){
            emailET.error = "This field cannot be empty"
            hasError = true
        }
        if (adminNumET.text.isEmpty()){
            adminNumET.error = "This field cannot be empty"
            hasError = true
        }
        if (pemET.text.isEmpty()){
            pemET.error = "This field cannot be empty"
            hasError = true
        }
        return hasError
    }

    private fun displayToast(message:String){
        Toast.makeText(this@RegistrationActivity,message, Toast.LENGTH_LONG).show()
    }
}