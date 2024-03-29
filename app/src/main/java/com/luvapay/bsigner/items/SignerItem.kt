package com.luvapay.bsigner.items

import android.text.TextUtils
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.luvapay.bsigner.AppBox
import com.luvapay.bsigner.R
import com.luvapay.bsigner.entities.Ed25519Signer
import com.luvapay.bsigner.utils.gone
import com.luvapay.bsigner.utils.prefetchText
import com.luvapay.bsigner.utils.visible
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_signer.view.itemAccount_container as container
import kotlinx.android.synthetic.main.item_signer.view.itemAccount_nameTv as nameTv
import kotlinx.android.synthetic.main.item_signer.view.itemAccount_publicKeyTv as publicKeyTv
import kotlinx.android.synthetic.main.item_signer.view.itemAccount_editBtn as editBtn
import kotlinx.android.synthetic.main.item_signer.view.itemAccount_removeBtn as removeBtn

data class SignerItem(val account: Ed25519Signer) : AbstractItem<SignerItem.ViewHolder>() {

    override var identifier: Long = account.objId
    override val layoutRes: Int = R.layout.item_signer
    override val type: Int = 0

    var canModify = false

    var card: Boolean = true

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<SignerItem>(itemView) {

        override fun bindView(item: SignerItem, payloads: MutableList<Any>) {

            if (!item.card) itemView.container.cardElevation = 0f

            itemView.nameTv.run {
                item.account.name.takeIf { it.isNotBlank() }?.let {
                    prefetchText(it)
                    visible()
                    itemView.publicKeyTv.maxLines = 1
                    itemView.publicKeyTv.ellipsize = TextUtils.TruncateAt.END
                }
            }

            itemView.publicKeyTv prefetchText item.account.publicKey

            if (item.canModify) {
                itemView.editBtn.visible()
                itemView.removeBtn.visible()
            } else {
                itemView.editBtn.gone()
                itemView.removeBtn.gone()
            }

            itemView.editBtn.setOnClickListener {
                MaterialDialog(itemView.context).show {
                    title(R.string.enter_account_name)
                    input{ _, text ->
                        AppBox.ed25519SignerBox.put(item.account.apply { name = text.toString() })
                    }
                    positiveButton(R.string.ok)
                    negativeButton(R.string.cancel)
                }
            }

            itemView.removeBtn.setOnClickListener {
                MaterialDialog(itemView.context).show {
                    message(R.string.warning_delete)
                    positiveButton(R.string.ok) {
                        AppBox.ed25519SignerBox.put(
                            item.account.apply { deleted = true }
                        )
                    }
                    negativeButton(R.string.cancel)
                }
            }
        }

        override fun unbindView(item: SignerItem) {
            itemView.nameTv.text = null
            itemView.publicKeyTv.text = null
        }

    }

}