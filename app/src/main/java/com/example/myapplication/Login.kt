package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.myapplication.api.Retro
import com.example.myapplication.api.API
import com.example.myapplication.model.User.UserRequest
import com.example.myapplication.model.User.UserResponse
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.inputNameAccount
import kotlinx.android.synthetic.main.activity_login.inputPassword
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Response

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initAction()
    }

    fun initAction(){
        // Change Register Layout
        register.setOnClickListener {
            val i: Intent = Intent(this, Register::class.java)
            startActivity(i)
            finish()
        }
        login()
    }

    //Login function check api
    fun login(){
        btnLogin.setOnClickListener(){
            val i: Intent = Intent(this, MenuFood::class.java)
            // values on textfield
            val nameAccount = inputNameAccount.text.toString().trim()
            val Pass = inputPassword.text.toString().trim()

            // Check empty values
            if(nameAccount.isEmpty()){
                inputNameAccount.error = "Account Name is Required"
                inputNameAccount.requestFocus()
                YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(inputNameAccount)
                return@setOnClickListener
            }
            if(Pass.isEmpty()){
                inputPassword.error = "Password is Required"
                inputPassword.requestFocus()
                YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(inputPassword)
                return@setOnClickListener
            }

            // If not empty all -> try to request api
            val request = UserRequest()
            request.username = nameAccount
            request.pass = Pass
            val retro = Retro().getRetroClientInstance().create(API::class.java)
            retro.login(request).enqueue(object : retrofit2.Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    val user = response.body()

                    // Check Login: if login success -> data not null else data null.
                    if(user.data == null){
                        Toast.makeText(applicationContext,user!!.msg.toString(),Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(applicationContext,user!!.msg.toString(),Toast.LENGTH_SHORT).show()

                        //NOTE:
                        //This line summary is announcing that you need to add Role data for project
                        //When a member of your team has already add role on our API
                        var token:String = user!!.data?.token.toString()
                        var fullname:String = user!!.data?.account!!.fullName.toString()
                        var id:String = user!!.data?.account!!.id.toString()
                        var roleId:String = user!!.data?.account!!.roleId.toString()
                        var img:String = user!!.data?.account!!.img.toString()
                        var area:String = "Login"
                        i.putExtra("token",token)
                        i.putExtra("fullname",fullname)
                        i.putExtra("id",id)
                        i.putExtra("roleId",roleId)
                        i.putExtra("img",img)
                        startActivity(i)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e("Error", t.message!!)
                }
            })
        }
    }

}