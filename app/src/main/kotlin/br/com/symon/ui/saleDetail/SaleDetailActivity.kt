package br.com.symon.ui.saleDetail

import android.os.Bundle
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.data.model.Comment
import br.com.symon.injection.components.DaggerSaleDetailActivityComponent
import br.com.symon.injection.components.SaleDetailActivityComponent
import br.com.symon.injection.modules.SaleDetailActivityModule

class SaleDetailActivity : BaseActivity(), SaleDetailContract.View {

    private val saleDetailActivityComponent: SaleDetailActivityComponent
        get() = DaggerSaleDetailActivityComponent
                .builder()
                .applicationComponent((application as CustomApplication).applicationComponent)
                .saleDetailActivityModule(SaleDetailActivityModule(this))
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_detail)

        saleDetailActivityComponent.inject(this)
    }

    override fun showComments(commentList: MutableList<Comment>) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showFavoriteResponse() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSendCommentResponse() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}