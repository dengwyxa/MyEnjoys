package com.dengwy.myenjoys

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_enjoy.*

const val ARG_TYPE = "type"
const val TYPE_DRAMA = "日剧"
const val TYPE_MOVIE = "日影"
const val TYPE_MORNING_DRAMA = "晨间剧"
const val TYPE_SPECIAL = "SP"
const val TYPE_ANIMATION = "动画"

/**
 * A simple [Fragment] subclass.
 * Use the [EnjoyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnjoyFragment : Fragment() {
    private var type: String = TYPE_DRAMA
    private var enjoyViewModel : EnjoyViewModel? = null
    private var filteredEnjoys : LiveData<List<Enjoy>> = MutableLiveData<List<Enjoy>>()
    private var enjoyAdapter: EnjoyAdapter? = null

    init {
        setHasOptionsMenu(true)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(ARG_TYPE)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enjoy, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val factory = EnjoyViewModel.Factory(type, requireActivity().application)
        enjoyViewModel = ViewModelProviders.of(this, factory).get(EnjoyViewModel::class.java)
        enjoyAdapter = EnjoyAdapter(enjoyViewModel!!)
        recyclerView.adapter = enjoyAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        // 给adapter增加数据观察者。当数据被插入时，把layoutManager滚动到顶部
        recyclerView.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(0)
            }
        })

        filteredEnjoys = enjoyViewModel!!.findAllByType()
        filteredEnjoys.observe(viewLifecycleOwner, Observer {
            enjoyAdapter!!.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        val searchViewPlateId = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val searchCloseButtonId = searchView.context.resources.getIdentifier("android:id/search_close_btn", null, null)
        val searchPlateEditText = searchView.findViewById(searchViewPlateId) as EditText
        val searchCloseButton = searchView.findViewById(searchCloseButtonId) as ImageView
        searchPlateEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchCloseButton.performClick()
            }
            true
        }
        searchCloseButton.setOnClickListener {
            Log.d("EnjoyFragment", "setOnClickListener")
            searchView.setQuery("", false)
            searchView.isIconified = true
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("EnjoyFragment", "onQueryTextSubmit")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("EnjoyFragment", "onQueryTextChange:$newText")

                doSearch(newText)

                return false
            }
        })
        searchView.setOnCloseListener {
            Log.d("EnjoyFragment", "onClose")
            false
        }
    }

    fun doSearch(newText: String?) {
        filteredEnjoys.removeObservers(viewLifecycleOwner)
        if (newText == null || newText.isEmpty()) {
            filteredEnjoys = enjoyViewModel!!.findAllByType()

        } else {
            filteredEnjoys = enjoyViewModel!!.findByKeyword(newText.trim())

        }
        filteredEnjoys.observe(viewLifecycleOwner, Observer {
            enjoyAdapter!!.submitList(it)
        })
    }

    fun hideIme() {
        val acti : Activity = requireActivity()
        val imm = acti.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = acti.currentFocus
        if (view == null) {
            view = View(acti)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param type Parameter 1.
         * @return A new instance of fragment EnjoyFragment.
         */
        @JvmStatic
        fun newInstance(type: String) =
            EnjoyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TYPE, type)
                }
            }
    }
}