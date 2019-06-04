package com.krcell.classkeeper.extentions

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.krcell.classkeeper.viewmodel.ViewModelFactory


fun AppCompatActivity.addFragment(layoutRes: Int, otherFragment: Fragment) {
    val fm = supportFragmentManager
    fm.beginTransaction()
        .add(layoutRes, otherFragment)
        .commit()
}

fun Activity.toActivity(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
}

inline val Activity.contentView: View?
    get() = findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)
