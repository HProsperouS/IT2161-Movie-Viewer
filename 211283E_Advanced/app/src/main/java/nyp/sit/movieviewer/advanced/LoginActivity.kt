package nyp.sit.movieviewer.advanced

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignInState

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    var appCoroutineScope: CoroutineScope? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        appCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

        // Initialize AWS Mobile Client
        AWSMobileClient.getInstance().initialize(this,object : Callback<UserStateDetails> {

            override fun onResult(result: UserStateDetails?) {
                Log.d("Cognito", result?.userState?.name.toString())
                if(result?.userState == UserState.SIGNED_IN){
                    val intent = Intent(this@LoginActivity, ViewListOfMoviesActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onError(e: Exception?) {
                Log.d("Cognito", "There is an error - ${e.toString()}")
            }

        })

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

    fun runLogin(v: View) {
        // Make use of AWSMobileClient to SignIn.
        appCoroutineScope?.launch {
            AWSMobileClient.getInstance().signIn(
                loginET.text.toString(),
                passwordET.text.toString(),
                null,object : Callback<SignInResult> {
                    override fun onResult(result: SignInResult?) {
                        Log.d("Cognito", "Sign in result: ${result.toString()}")
                        if (result?.signInState == SignInState.DONE){
                            val intent = Intent(v.context,ViewListOfMoviesActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onError(e: Exception?) {
                        Log.d("Cognito", "Sign in error: ${e.toString()}")
                    }
                }
            )
        }
    }
}