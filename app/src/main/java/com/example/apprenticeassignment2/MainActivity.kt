package com.example.apprenticeassignment2

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apprenticeassignment2.services.MainAdapter
import com.example.apprenticeassignment2.services.PexelApiService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), MainAdapter.OnImageClick {
    var searchText: String = ""
    var counter = 1
    var mIsLastPage = false
    var mIsLoading = false
    lateinit var adapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = MainAdapter(
            mutableListOf(),
            this
        )
        recycler_view.adapter = adapter
        recycler_view.layoutManager = GridLayoutManager(applicationContext, 3)
        //search button on click action
        search_button.setOnClickListener {
            counter = 1
            //check for user input
            if (!search.text.isNullOrEmpty()) {
                searchText = search.text.toString()

                // load the first page
                loadMoreItems(true)


            }
        }

        // attach scroll listener
        setScrollListener()


    }

    override fun onClick(imageUrl: String) {
        ImageActivity.start(this, imageUrl)
    }

//function to check if phone has internet connectivity
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
        }
        return false
    }

//function which checks for scrolling in the recyclerview and calls the API if last item is reached
//https://stackoverflow.com/questions/51822115/how-to-handle-pagination-load-more-in-retrofit-2-0/51823545#51823545
    private fun setScrollListener() {
        // initialise loading state
        mIsLoading = false;
        mIsLastPage = false;

        // amount of items you want to load per page
        val pageSize = 20;

        val layoutManager = recycler_view.layoutManager as GridLayoutManager

        // set up scroll listener
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy);
                // number of visible items
                val visibleItemCount = layoutManager.childCount;
                // number of items in layout
                val totalItemCount = layoutManager.itemCount;
                // the position of first visible item
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                val isNotLoadingAndNotLastPage = !mIsLoading && !mIsLastPage;
                // flag if number of visible items is at the last
                val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
                // validate non negative values
                val isValidFirstItem = firstVisibleItemPosition >= 0;
                // validate total items are more than possible visible items
                val totalIsMoreThanVisible = totalItemCount >= pageSize;
                // flag to know whether to load more
                val shouldLoadMore =
                    isValidFirstItem && isAtLastItem && totalIsMoreThanVisible && isNotLoadingAndNotLastPage

                if (shouldLoadMore) loadMoreItems(false);
            }
        });

    }
//calls API service to load the images
    private fun loadMoreItems(isFirstPage: Boolean) {
        // change loading state
        mIsLoading = true


        // show loader
        group_loading.visibility = View.VISIBLE
        //check internet
        if (isOnline(applicationContext)) {
            GlobalScope.launch(Dispatchers.Main) {

                //call API
                val postsRequest = PexelApiService.getApi().getImage(counter, searchText, 30)

                val postsResponse = postsRequest.await()
                if (postsResponse.isSuccessful) {
                    val body = postsResponse.body()!!
                    val photos = body.photos
                    //if first page then set new list for recycler view else add on to the existing list
                    if (isFirstPage) adapter.setList(photos)
                    else adapter.addPhotos(photos)

                    //increment counter
                    counter++
                    //check if last item
                    mIsLastPage = body.totalResults == adapter.items.size
                    mIsLoading = false

                    group_loading.visibility = View.GONE
                    recycler_view.alpha = 1F

                } else {
                    Log.e("MainActivity", "Error ${postsResponse.code()}")
                }
            }
        } else {
            textView_loading.text = getString(R.string.no_internet)
            recycler_view.alpha = 0.2F
        }
    }

}




