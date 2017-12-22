package br.com.symon.ui.blockedUsers

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.repository.BlockedUsersRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class BlockedUsersPresenter @Inject constructor(val view: BlockedUsersContract.View, val blockedUsersRepository: BlockedUsersRepository) : BlockedUsersContract.Presenter {

    override fun loadBlockedUsers(userToken: String) {
        view.showLoading()
        blockedUsersRepository.getBlockedUsersList(userToken)
                .subscribe({
                    view.showUsersBlocked(it.body())
                }, {
                    GeneralErrorHandler(it, view, {})
                })
        view.hideLoading()
    }

    override fun unblockUser(userToken: String, blockedUserRequest: BlockUserRequest) {
        view.showLoading()
        blockedUsersRepository.unblockUser(userToken, blockedUserRequest)
                .subscribe({
                    view.showUnblockUserResponse()
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }
}