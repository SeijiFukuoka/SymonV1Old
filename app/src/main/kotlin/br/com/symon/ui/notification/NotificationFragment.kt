package br.com.symon.ui.notification

import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseFragment
import br.com.symon.common.inflate
import br.com.symon.data.model.Notification
import br.com.symon.injection.components.DaggerNotificationFragmentComponent
import br.com.symon.injection.components.NotificationFragmentComponent
import br.com.symon.injection.modules.NotificationFragmentModule
import br.com.symon.ui.notification.adapter.NotificationAdapter
import br.com.symon.ui.sales.SalesFragment
import kotlinx.android.synthetic.main.fragment_notification.*


class NotificationFragment: BaseFragment(), NotificationContract.View {

    companion object {
        private const val USER_TOKEN_EXTRA = "user_token_extra"

        private lateinit var userToken: String

        fun newInstance(userToken: String?): NotificationFragment {
            val fragment = NotificationFragment()
            val args = Bundle()
            args.putString(USER_TOKEN_EXTRA, userToken)
            fragment.arguments = args
            return fragment
        }
    }

    private val notificationFragmentComponent: NotificationFragmentComponent
    get() = DaggerNotificationFragmentComponent
            .builder()
            .applicationComponent((activity.application as CustomApplication).applicationComponent)
            .notificationFragmentModule(NotificationFragmentModule(this))
            .build()

    private var notificationAdapter: NotificationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        notificationFragmentComponent.inject(this)

        fragmentId = SalesFragment::class.java.canonicalName
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null && arguments.getString(USER_TOKEN_EXTRA) != null)
            userToken = arguments.getString(USER_TOKEN_EXTRA)

        setupRecyclerView()

        notificationFragmentComponent.notificationPresenter().getNotifications(userToken)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_notification)
    }

    private fun setupRecyclerView() {
        notificationRecyclerView.layoutManager = LinearLayoutManager(context)
        notificationRecyclerView.isNestedScrollingEnabled = false
        notificationRecyclerView.setHasFixedSize(true)
    }

    override fun showNotifications(notifications: MutableList<Notification>) {
        notificationAdapter = NotificationAdapter(notifications)
        notificationRecyclerView.adapter = notificationAdapter
    }

}