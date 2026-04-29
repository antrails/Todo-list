package com.example.todolist

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var navigationView: NavigationView
    private lateinit var btnSidebar: ImageButton
    private lateinit var btnUser: ImageButton
    private lateinit var btnCalender: ImageButton
    private lateinit var btnHome: ImageButton
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnHome = findViewById<ImageButton>(R.id.btnHome)
        btnCalender = findViewById<ImageButton>(R.id.btnCalendar)
        btnSidebar = findViewById<ImageButton>(R.id.btnSidebar)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        navigationView = findViewById<NavigationView>(R.id.navigationView_sidebar)




        if (savedInstanceState == null)
        {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_id, HomeScreenFragment())
                .commit()
        }



        btnSidebar.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
            {
                drawerLayout.closeDrawer(GravityCompat.START)
            }else
            {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.nav_knowUs ->{
                    val dialogView = layoutInflater.inflate(R.layout.dialog_about_us, null)

                    val dialog = AlertDialog.Builder(this)
                        .setView(dialogView)
                        .create()

                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                    dialogView.findViewById<Button>(R.id.btnCloseAbout).setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }

//                R.id.nav_feedback->{}

                R.id.nav_logout->{
                    SessionManager.currentUser = null
                    val intent = android.content.Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        btnHome.setOnClickListener {
            if (SessionManager.currentUser == null) {
                Toast.makeText(this, "請先登入", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            supportFragmentManager.popBackStack(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_id, HomeScreenFragment())
                .commit()
        }


        btnCalender.setOnClickListener {
            if (SessionManager.currentUser == null) {
                Toast.makeText(this, "請先登入", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_id, CalenderFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}