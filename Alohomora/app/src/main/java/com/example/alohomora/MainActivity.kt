package com.example.alohomora

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var providers: List<AuthUI.IdpConfig>
    val MY_REQUEST_CODE: Int = 7117
    private var navigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView = findViewById<BottomNavigationView>(R.id.navigationView)
        navigationView!!.setOnNavigationItemSelectedListener(this)

        //init
        providers = listOf(AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.PhoneBuilder().build())

        showSignInOptions()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_inicio -> {

                setSupportActionBar(toolbar)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.title = "Filters"
                // Get the text fragment instance
                val searchFragment = SearchFragment()

                // Get the support fragment manager instance
                val manager = supportFragmentManager

                // Begin the fragment transition using support fragment manager
                val transaction = manager.beginTransaction()

                // Replace the fragment on container
                transaction.replace(R.id.container,searchFragment)
                transaction.addToBackStack(null)

                // Finishing the transition
                transaction.commit()
            }
            R.id.navigation_reservas -> {
            }
            R.id.navigation_liberar -> {
            }
            R.id.navigation_perfil -> {
                // Ta aqui só pra testar essa bosta por enquanto
                AuthUI.getInstance().signOut(this@MainActivity)
                    .addOnCompleteListener {
                        showSignInOptions()
                    }
                    .addOnFailureListener{
                            e-> Toast.makeText( this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                    }
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_REQUEST_CODE)
        {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK)
            {
                val user = FirebaseAuth.getInstance().currentUser // get current user
                Toast.makeText(this,""+user!!.email,Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this,""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSignInOptions(){
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.MyTheme)
            .build(),MY_REQUEST_CODE)
    }

}

/*
class MainActivity : AppCompatActivity() {



    private var navigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView = findViewById<BottomNavigationView>(R.id.navigationView);

        //init
        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            //AuthUI.IdpConfig.FacebookBuilder().build(),
            //AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
            )
        ShowSignInOptions()

        // Event
        btn_sign_out.setOnClickListener {
            //Signout
            AuthUI.getInstance().signOut(this@MainActivity)
                .addOnCompleteListener {
                    btn_sign_out.isEnabled=false
                    ShowSignInOptions()
                }
                .addOnFailureListener{
                    e-> Toast.makeText( this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_REQUEST_CODE)
        {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK)
            {
                val user = FirebaseAuth.getInstance().currentUser // get current user
                Toast.makeText(this,""+user!!.email,Toast.LENGTH_SHORT).show()
                btn_sign_out.isEnabled = true
            }
            else
            {
                Toast.makeText(this,""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ShowSignInOptions(){

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.MyTheme)
            .build(),MY_REQUEST_CODE)

    }



}
*/