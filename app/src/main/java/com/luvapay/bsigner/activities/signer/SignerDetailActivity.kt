package com.luvapay.bsigner.activities.signer

import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.luvapay.bsigner.AppBox
import com.luvapay.bsigner.R
import com.luvapay.bsigner.base.BaseActivity
import com.luvapay.bsigner.entities.Ed25519Signer
import com.luvapay.bsigner.utils.prefetchText
import com.luvapay.bsigner.utils.toQrCode
import com.luvapay.bsigner.utils.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.android.synthetic.main.activity_signer_detail.activityAccountDetail_toolbar as toolbar
import kotlinx.android.synthetic.main.activity_signer_detail.activityAccountDetail_publicTv as accountTv
import kotlinx.android.synthetic.main.activity_signer_detail.activityAccountDetail_nameTv as nameTv
import kotlinx.android.synthetic.main.activity_signer_detail.activityAccountDetail_qrCodeImg as qrCodeImg

class SignerDetailActivity : BaseActivity() {

    private lateinit var qrCode: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signer_detail)

        val objId = intent.getLongExtra(Ed25519Signer.OBJ_ID, -2)

        if (objId <= 0) {
            finish()
            return
        }

        toolbar.init()

        lifecycleScope.launch {
            val account = withContext(Dispatchers.Default) { return@withContext AppBox.ed25519SignerBox[objId] }
            account.name.takeIf { it.isNotBlank() }?.let { name ->
                nameTv.visible()
                nameTv prefetchText name
            }
            accountTv prefetchText account.publicKey

            account.publicKey.toQrCode(512)?.let { bitmap ->
                qrCode = bitmap
                qrCodeImg.setImageBitmap(bitmap)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        qrCode.recycle()
    }

}