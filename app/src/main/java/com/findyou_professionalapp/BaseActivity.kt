package com.findyou_professionalapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle


class BaseActivity : Activity(){

    public lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context =  getMainApplication();

    }

    fun getIntent(cname: Class<*>?): Intent {
        val intent = Intent()
        intent.setClass(this, cname!!)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return intent
    }

    fun getMainApplication(): AppActivity {
        return application as AppActivity
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}