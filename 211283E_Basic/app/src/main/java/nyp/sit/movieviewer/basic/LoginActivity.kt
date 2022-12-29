package nyp.sit.movieviewer.basic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginBtn.setOnClickListener{
            val hasError = validation()
                if (hasError == false){
                    if (loginET.text.toString() == "testuser" && passwordET.text.toString() == "testuser"){
                        val intent = Intent(this@LoginActivity,SimpleViewListOfMoviesActivity::class.java)
                        startActivity(intent)
                    }else{
                        displayToast("Login Error")
                    }
                }else{
                    return@setOnClickListener
                }
        }
        registerBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validation(): Boolean {
        var hasError = false

        if (loginET.text.isEmpty()){
            loginET.error = "This field cannot be empty"
            hasError = true
        }
        if (passwordET.text.isEmpty()){
            passwordET.error = "This field cannot be empty"
            hasError = true
        }
        return hasError
    }


    private fun displayToast(message:String){
        Toast.makeText(this@LoginActivity,message, Toast.LENGTH_LONG).show()
    }


}