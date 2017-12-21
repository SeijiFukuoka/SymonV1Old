package br.com.symon.ui.retrievePassword

import br.com.symon.base.BaseView
import br.com.symon.data.model.responses.RetrievePasswordResponse


interface RetrievePasswordContract {
    interface View : BaseView {
        fun showRetrievePasswordResponse(retrievePasswordResponse: RetrievePasswordResponse)
        fun showErrorMessage(message: String?)
    }

    interface Presenter {
        fun requestNewPassword(userEmail: String)
    }
}