package com.fp.fproject.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fp.fproject.R
import android.widget.Button
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import com.facebook.AccessToken
import com.facebook.FacebookSdk
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth


class StartActivity : AppCompatActivity() {

    private lateinit var btn_face: com.facebook.login.widget.LoginButton
    private lateinit var btn_gog: Button
    private lateinit var start_w: Button
    private lateinit var srt_skp: Button
    private lateinit var sign_in: Button
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true


    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123



    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_F_project)
        setContentView(R.layout.activity_start)

        btn_gog = findViewById(R.id.btn_go)
        start_w = findViewById(R.id.start_w)
        srt_skp = findViewById(R.id.srt_skp)
        sign_in = findViewById(R.id.sign_in)

        start_w.setOnClickListener{
            val intent = Intent(this,Sin_upActivity::class.java)
            startActivity(intent)
        }

        srt_skp.setOnClickListener{
            val intent = Intent(Intent(this,HomeScreenActivity::class.java))
            startActivity(intent)
        }
        sign_in.setOnClickListener{
            val intent = Intent(Intent(this,Log_inActivity::class.java))
            startActivity(intent)
        }



        auth = Firebase.auth

        auth = FirebaseAuth.getInstance()
        FacebookSdk.sdkInitialize(applicationContext)



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.default_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btn_gog.setOnClickListener {
            gog_signIn()
        }
        btn_face = findViewById(R.id.lgbtn_face)
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        arrayOf<String?>("email", "public_profile")
        btn_face.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.d("Start Activity", "facebook:onSuccess:$result")
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    Log.d("Start Activity", "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("Start Activity", "facebook:onError", error)
                }
            },
        )


    }

    private fun gog_signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        callbackManager.onActivityResult(requestCode, resultCode, data)


        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("Start Activity", "Google sign in failed", e)
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("Start Activity", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Start Activity", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Start Activity", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    // You can navigate to another activity or perform any other action here
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Start Activity", "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)

    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // User is signed in, update UI accordingly
            // For example, show a welcome message, navigate to the main activity, etc.
        } else {
            // User is signed out or authentication failed, update UI accordingly
            // For example, hide certain UI elements, show a sign-in button, etc.
            Toast.makeText(
                baseContext,
                "Authentication failed.",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

}