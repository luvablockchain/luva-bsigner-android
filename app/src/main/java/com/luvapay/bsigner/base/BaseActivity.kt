package com.luvapay.bsigner.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.coroutineScope
import com.luvapay.bsigner.activities.passcode.VerifyPinActivity
import com.luvapay.bsigner.model.InitData
import com.luvapay.bsigner.utils.LOGIN_PIN_REQUEST_CODE
import com.luvapay.bsigner.utils.LocaleManager
import com.luvapay.bsigner.utils.Prefs
import com.luvapay.bsigner.utils.isAppLocked
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivityForResult

abstract class BaseActivity : AppCompatActivity() {

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Logger.d(newConfig.locale)
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase == null) super.attachBaseContext(newBase) else super.attachBaseContext(LocaleManager.updateResources(newBase))
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
        if (isAppLocked()) {
            startActivityForResult<VerifyPinActivity>(LOGIN_PIN_REQUEST_CODE)
        }
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    fun Toolbar.init() {
        setSupportActionBar(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.setNavigationOnClickListener { onBackPressed() }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    open fun init(initData: InitData) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_PIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            
        }
    }

}