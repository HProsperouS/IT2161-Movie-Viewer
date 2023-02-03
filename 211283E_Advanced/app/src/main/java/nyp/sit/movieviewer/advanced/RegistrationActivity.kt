package nyp.sit.movieviewer.advanced

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class RegistrationActivity : AppCompatActivity() {
    var appCoroutineScope: CoroutineScope? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        appCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

        // Initialize AWS Mobile Client
        AWSMobileClient.getInstance().initialize(this,object : Callback<UserStateDetails> {

            override fun onResult(result: UserStateDetails?) {
                Log.d("Cognito", result?.userState?.name.toString())
            }

            override fun onError(e: Exception?) {
                Log.d("Cognito", "There is an error - ${e.toString()}")
            }

        })
    }

    fun runRegister(v: View) {
        val hasError = validation()
        if (hasError == false){
            val loginName = nameET.text.toString()
            val password = passwordET.text.toString()
            val email = emailET.text.toString()
            val adminNum = adminNumET.text.toString()
            val pemGrp = pemET.text.toString()

//            val message:ArrayList<String> = arrayListOf(
//                "Login Name: ${loginName}",
//                "Password: ${password}",
//                "Email: ${email}",
//                "Admin Number: ${adminNum}",
//                "Pem Group: ${pemGrp}"
//            )
//
//            displayToast(message.joinToString("\n"))

            appCoroutineScope?.launch(Dispatchers.IO) {

                // Assign the user details to the respective cognito attributes
                val userPool = CognitoUserPool(v.context, AWSMobileClient.getInstance().configuration)

                val userAttributes = CognitoUserAttributes()
                userAttributes.addAttribute("email", email)
                userAttributes.addAttribute("custom:AdminNumber", adminNum)
                userAttributes.addAttribute("custom:PemGrp",pemGrp)

                //Sign up user with the attributes and listen for result
                userPool.signUp(
                    loginName,
                    password,
                    userAttributes,
                    null ,object : SignUpHandler {
                        override fun onSuccess(
                            user: CognitoUser?,
                            signUpResult: SignUpResult?
                        ){
                            Log.d("Cognito", "Sign up success ${signUpResult?.userConfirmed}")

                            // Create an Intent to start the LoginActivity
                            val intent = Intent(v.context, LoginActivity::class.java)
                            // Start the LoginActivity
                            startActivity(intent)
                            this@RegistrationActivity.runOnUiThread(java.lang.Runnable {
                                displayToast("You have successfully registered")
                            })
                        }

                        override fun onFailure(exception: Exception?) {
                            Log.d("Cognito", "Exception: ${exception?.message}")
                            this@RegistrationActivity.runOnUiThread(java.lang.Runnable {
                                displayToast("There is an error, please try again")
                            })
                        }
                    }
                )
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