package br.com.symon.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.com.symon.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginPagerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginPagerFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_login_pager, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        fun newInstance(): LoginPagerFragment {
            val fragment = LoginPagerFragment()
            return fragment
        }
    }
}// Required empty public constructor
