package com.rozaq.githubapp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rozaq.githubapp.R
import com.rozaq.githubapp.data.model.User
import com.rozaq.githubapp.databinding.ActivityMainBinding
import com.rozaq.githubapp.ui.detail.DetailUserActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : UserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocated()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    startActivity(it)
                }
            }

        })
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            btnSearch.setOnClickListener { 
                
            }
            
            etQuery.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        viewModel.getSearchUsers().observe(this, {
            if (it != null)
                adapter.setList(it)
            showLoading(false)
        })

    }

    private  fun searchUser() {
        binding.apply {
            val query = etQuery.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUsers(query)
        }
    }

    private fun showLoading (state : Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.languages_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

//        val id = item.itemId
//        if (id == R.id.local_english) {
//            Toast.makeText(this,"English Languages ComingSoon", Toast.LENGTH_SHORT).show()
//        }
//        if (id == R.id.local_indonesia) {
//            Toast.makeText(this,"Indonesia Languages ComingSoon", Toast.LENGTH_SHORT).show()
//        }
        when (item.itemId){
            R.id.languages_settings -> showLanguages()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showLanguages() {
        val listItems = arrayOf("Indonesia", "English")

        val mBuilder = AlertDialog.Builder(this@MainActivity)
        mBuilder.setTitle("Choose Languages")
        mBuilder.setSingleChoiceItems(listItems, -1) {dialog, which ->
            if (which == 0) {
                setLanguages("En")
                recreate()
            } else if (which == 1) {
                setLanguages("ID")
                recreate()
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()

        mDialog.show()
    }

    private fun setLanguages(Lang: String) {

        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocated() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_lang","")
        if (language != null) {
            setLanguages(language)
        }
    }


}