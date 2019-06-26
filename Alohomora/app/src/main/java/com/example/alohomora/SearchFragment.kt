package com.example.alohomora

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.app.SearchManager
import android.view.*
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alohomora.Adapter.MyAdapter
import com.example.alohomora.Model.Item
import com.example.alohomora.Retrofit.DataAPI
import com.example.alohomora.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_main.*
import android.view.MenuInflater
import androidx.recyclerview.widget.RecyclerView

class SearchFragment : Fragment() {
    lateinit var myAPI: DataAPI
    lateinit var adapter: MyAdapter
    private var searchView: SearchView? = null
    var dataList: MutableList<Item> = ArrayList()
    var compositeDisposable = CompositeDisposable()
    private val queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rv = RecyclerView(context!!)
        rv.layoutManager = LinearLayoutManager(context)
        adapter = MyAdapter(context!!, dataList)
        rv.adapter = adapter
        fetchData()

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Init API
        val retrofit = RetrofitClient.getInstance
        myAPI = retrofit.create(DataAPI::class.java)

    }

    private fun fetchData(){
        compositeDisposable.add(myAPI.posts
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ posts -> displayData(posts)})
    }

    private fun displayData(posts: List<Item>?) {
        dataList.clear()
        dataList.addAll(posts!!)
        adapter.notifyDataSetChanged()
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_search,menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        if (searchView != null) {

            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
            searchView!!.maxWidth = Int.MAX_VALUE

            searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    adapter.filter.filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filter.filter(newText)
                    return false
                }
            })
            searchView!!.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        return if (id == R.id.action_search){
            true
        }
        else {
            searchView!!.setOnQueryTextListener(queryTextListener)
            super.onOptionsItemSelected(item)
        }
    }
}
