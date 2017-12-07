package br.com.symon.ui.ratings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseFragment
import br.com.symon.common.inflate
import br.com.symon.data.model.UserLikeResponse
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerRatingsChildFragmentComponent
import br.com.symon.injection.components.RatingsChildFragmentComponent
import br.com.symon.injection.modules.RatingsChildFragmentModule
import kotlinx.android.synthetic.main.fragment_ratings_child.*

class RatingsChildFragment : BaseFragment(), RatingsChildFragmentContract.View {

    enum class RatingsChildType(val ratingChildType: Int) {
        FAVORITES(0),
        LIKES(1),
        DISLIKES(2),
        COMMENTS(3)
    }

    val ratingsChildComponent: RatingsChildFragmentComponent
        get() = DaggerRatingsChildFragmentComponent.builder()
                .applicationComponent((activity.application as CustomApplication).applicationComponent)
                .ratingsChildFragmentModule(RatingsChildFragmentModule(this))
                .build()

    companion object {
        public const val API_OPTION_KEY: String = "API_OPTION_KEY"

        fun newInstance(apiOptionKey: RatingsChildType): RatingsChildFragment {
            val f = RatingsChildFragment()
            val args = Bundle()
            args.putSerializable(API_OPTION_KEY, apiOptionKey)
            f.arguments = args
            return f
        }
    }

    private lateinit var apiOptionKey: RatingsChildType
    private lateinit var userTokenResponse: UserTokenResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ratingsChildComponent.inject(this)
        fragmentId = RatingsChildFragment::class.java.canonicalName
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_ratings_child)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null)
            apiOptionKey = arguments.getSerializable(API_OPTION_KEY) as RatingsChildType

        ratingsChildComponent.ratingsChildFragmentPresenter().getUserCache()
    }

    override fun setUser(userTokenResponse: UserTokenResponse) {
        this.userTokenResponse = userTokenResponse

        when (apiOptionKey) {
            RatingsChildType.FAVORITES -> {
                ratingsChildComponent.ratingsChildFragmentPresenter().loadLikes(userTokenResponse.token)
            }
            RatingsChildType.LIKES -> {
                textViewPosition.text = "LIKES"
            }
            RatingsChildType.DISLIKES -> {
                textViewPosition.text = "DISLIKES"
            }
            RatingsChildType.COMMENTS -> {
                textViewPosition.text = "COMMENTS"
            }
        }
    }

    override fun showLikes(userLikeResponse: MutableList<UserLikeResponse>) {
        userLikeResponse.get(0).let {
            textViewPosition.text = userLikeResponse.get(0).id.toString()
        }
    }
}
