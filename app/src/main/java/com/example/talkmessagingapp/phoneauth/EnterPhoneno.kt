package com.example.talkmessagingapp.phoneauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.talkmessagingapp.R
import com.example.talkmessagingapp.databinding.ActivityEnterPhonenoBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class EnterPhoneno : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    lateinit var binding: ActivityEnterPhonenoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityEnterPhonenoBinding = ActivityEnterPhonenoBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        val actionBar = supportActionBar
        actionBar!!.title = "Login to Talk"


        binding.btnGetotp.setOnClickListener {
            val phoneno: String = binding.etPhone.text.toString().trim()

            if(phoneno.isNotEmpty())
            {
                sendVerificationCode(phoneNo = "+91"+phoneno )
            }

            else
            {
                Toast.makeText(applicationContext,"Please Enter phone no.", Toast.LENGTH_LONG).show()

            }
        }

        binding.btnSignup.setOnClickListener {
            val otp: String = binding.etOtp.text.toString().trim()

            if(otp.isNotEmpty())
            {
                verifyVerificationcode(otp)
            }

            else
            {
                Toast.makeText(applicationContext,"Please Enter otp.", Toast.LENGTH_LONG).show()

            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                //This message automatically detects sms, so we signup without users' effort
                val code= credential.smsCode
                if(code!=null)
                {
                    binding.etOtp.setText(code)
                }

            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext,"Auth Fail", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

                storedVerificationId = verificationId
                resendToken = token
                binding.phoneLayout.visibility= View.GONE
                binding.otpLayout.visibility= View.VISIBLE
            }
        }

    }

    private fun sendVerificationCode(phoneNo: String){

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNo)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyVerificationcode(code: String){
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signUP(credential)
    }

    private fun signUP(credential: PhoneAuthCredential)
    {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = task.result?.user
                    Toast.makeText(applicationContext,"Signed Up Successfully",Toast.LENGTH_LONG).show()
                    val intent= Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(applicationContext,"CODE entered was incorrect",Toast.LENGTH_LONG).show()
                        binding.etOtp.setText("")
                    }

                }
            }
    }

}
