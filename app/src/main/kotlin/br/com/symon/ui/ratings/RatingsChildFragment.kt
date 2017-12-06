package br.com.symon.ui.ratings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.symon.R
import br.com.symon.base.BaseFragment
import br.com.symon.common.inflate
import kotlinx.android.synthetic.main.fragment_ratings_child.*


class RatingsChildFragment : BaseFragment(), View.OnClickListener {

    companion object {
        public const val POSITION_KEY: String = "FragmentPositionKey"

        fun newInstance(positionKey: Int): RatingsChildFragment {
            val f = RatingsChildFragment()
            val args = Bundle()
            args.putInt(POSITION_KEY, positionKey)
            f.arguments = args
            return f
        }
    }

    private var position: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_ratings_child)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null)
            position = arguments.getInt(POSITION_KEY)

        position = arguments.getInt(POSITION_KEY)
        textViewPosition.text = position.toString()
        textViewPosition.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        Toast.makeText(v.context, "Clicked Position: " + position, Toast.LENGTH_LONG).show()
    }
}
