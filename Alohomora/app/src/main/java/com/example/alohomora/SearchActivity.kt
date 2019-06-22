package com.example.alohomora

import android.os.Bundle
import android.app.Activity
import android.app.SearchManager
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alohomora.Adapter.MyAdapter
import com.example.alohomora.Model.Item
import com.example.alohomora.Retrofit.DataAPI
import com.example.alohomora.Retrofit.RetrofitClient
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_main.*


class SearchActivity : AppCompatActivity(){
    lateinit var myAPI: DataAPI
    lateinit var adapter: MyAdapter
    var compositeDisposable = CompositeDisposable()

    var dataList: MutableList<Item> = ArrayList()
    var searchView:SearchView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Filters"

        //Init API
        val retrofit = RetrofitClient.getInstance
        myAPI = retrofit.create(DataAPI::class.java!!)

        //View
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)

        adapter = MyAdapter(this, dataList)
        recycler_view.adapter = adapter

        fetchData()

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_search,menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Int.MAX_VALUE

        searchView!!.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }


        })


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        return if (id == R.id.action_search){
            true
        }
        else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!searchView!!.isIconified)
        {
            searchView!!.isIconified=true
            return
        }
        super.onBackPressed()
    }
}




