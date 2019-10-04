package com.luvapay.bsigner.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import com.luvapay.bsigner.R
import com.luvapay.bsigner.base.BaseActivity
import com.luvapay.bsigner.fragments.HomeFragment
import com.luvapay.bsigner.fragments.SettingsFragment
import com.luvapay.bsigner.fragments.TransactionFragment
import com.luvapay.bsigner.utils.getColorCompat
import com.luvapay.bsigner.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.activity_home_main.activityHome_menuBtn as menuBtn

class HomeActivity : BaseActivity() {

    private val vm: HomeViewModel by viewModel()

    private val homeFrag: HomeFragment by lazy { HomeFragment() }
    private val transactionFrag: TransactionFragment by lazy { TransactionFragment() }
    private val settingsFrag: SettingsFragment by lazy { SettingsFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_main)

        menuBtn.setOnClickListener {
            vm.canModify.value = !(vm.canModify.value ?: true)
        }

        vm.canModify.observe(this, Observer {
            updateBtnUI(it)
        })

        activityMain_nav.apply {
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        homeFrag.replace()
                    }
                    R.id.nav_transactions -> {
                        transactionFrag.replace()
                    }
                    R.id.nav_settings -> {
                        settingsFrag.replace()
                    }
                    else -> return@setOnNavigationItemSelectedListener false
                }
                true
            }
            selectedItemId = R.id.nav_home
        }
    }

    private fun updateBtnUI(canModify: Boolean) {
        if (canModify) {
            menuBtn.apply {
                setBackgroundColor(getColorCompat(R.color.colorPrimary))
                icon = getDrawable(R.drawable.ic_check)
            }
        } else {
            menuBtn.apply {
                setBackgroundColor(getColorCompat(R.color.colorAccent))
                icon = getDrawable(R.drawable.ic_edit)
            }
        }
    }

    private fun Fragment.replace() {
        supportFragmentManager.commit {
            replace(R.id.activityHome_fragmentContainer, this@replace)
        }
    }

}