package com.github.amunandar7.mvp.view

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessRecyclerViewScrollListener : RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    var visibleThreshold = 5
        private set

    // The current offset index of data you have loaded
    private var currentPage = 1

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    var isLoading = true
        private set

    //set startingPageIndex   default: 0
    // Sets the starting page index
    var startingPageIndex = 0
        private set

    // Sets the  footerViewType
    private val defaultNoFooterViewType = -1
    private var footerViewType = -1
    private var isLastPage = false
    private val mTag = "scroll-listener"
    var mLayoutManager: RecyclerView.LayoutManager

    constructor(layoutManager: LinearLayoutManager) {
        init()
        mLayoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        init()
        mLayoutManager = layoutManager
        visibleThreshold = visibleThreshold * layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        init()
        mLayoutManager = layoutManager
        visibleThreshold = visibleThreshold * layoutManager.spanCount
    }

    //init from  self-define
    private fun init() {
        footerViewType = getFooterViewType(defaultNoFooterViewType)
        startingPageIndex = startingPageIndex
        val threshold = visibleThreshold
        if (threshold > visibleThreshold) {
            visibleThreshold = threshold
        }
    }

    // This happens many times a second during a scroll, so be wary of the type you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {

        ////when dy=0---->list is clear totalItemCount == 0 or init load  previousTotalItemCount=0
        if (dy <= 0) {
            return
        }
        //        Log.i(mTag, "onScrolled-------dy:" + dy);
        val adapter = view.adapter
        val totalItemCount = adapter!!.itemCount
        val lastVisibleItemPosition = lastVisibleItemPosition
        val isAllowLoadMore =
            lastVisibleItemPosition + visibleThreshold > totalItemCount
        if (isAllowLoadMore) {
            if (isUseFooterView) {
                if (!isFooterView(adapter)) {
                    if (totalItemCount < previousTotalItemCount) { //swiprefresh reload result to change listsize ,reset pageindex
                        currentPage = startingPageIndex
                        isLastPage = false
                        //                            Log.i(mTag, "****totalItemCount:" + totalItemCount + ",previousTotalItemCount:" + previousTotalItemCount + ",currentpage=startingPageIndex");
                    } else if (totalItemCount == previousTotalItemCount) { //if load failure or load empty data , we rollback  pageindex
                        currentPage =
                            if (currentPage == startingPageIndex) startingPageIndex else --currentPage
                        isLastPage = true
                        //                            Log.i(mTag, "!!!!currentpage:" + currentPage);
                    }
                    isLoading = false
                }
            } else {
                if (totalItemCount > previousTotalItemCount) {
                    isLoading = false
                }
            }
            if (!isLoading && !isLastPage) {

                // If it isn’t currently loading, we check to see if we have breached
                // the visibleThreshold and need to reload more data.
                // If we do need to reload some more data, we execute onLoadMore to fetch the data.
                // threshold should reflect how many total columns there are too
                previousTotalItemCount = totalItemCount
                currentPage++
                onLoadMore(currentPage, totalItemCount)
                isLoading = true
                Log.i(
                    mTag,
                    "request pageindex:$currentPage,totalItemsCount:$totalItemCount"
                )
            }
        }
    }

    override fun onScrollStateChanged(
        recyclerView: RecyclerView,
        newState: Int
    ) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    //        Log.i(mTag, "isUseFooterView:" + isUse);
    val isUseFooterView: Boolean
        get() =//        Log.i(mTag, "isUseFooterView:" + isUse);
            footerViewType != defaultNoFooterViewType

    fun isFooterView(padapter: RecyclerView.Adapter<*>?): Boolean {
        var isFooterView = false
        val ptotalItemCount = padapter!!.itemCount
        if (ptotalItemCount > 0) {
            val lastPosition = ptotalItemCount - 1
            val lastViewType = padapter.getItemViewType(lastPosition)

            //  check the lastview is footview
            isFooterView = lastViewType == footerViewType
        }
        //        Log.i(mTag, "isFooterView:" + isFooterView);
        return isFooterView
    }

    // get maximum element within the list
    private val lastVisibleItemPosition: Int
        private get() {
            var lastVisibleItemPosition = 0
            if (mLayoutManager is StaggeredGridLayoutManager) {
                val lastVisibleItemPositions =
                    (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            } else if (mLayoutManager is LinearLayoutManager) {
                lastVisibleItemPosition =
                    (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            } else if (mLayoutManager is GridLayoutManager) {
                lastVisibleItemPosition =
                    (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
            }
            return lastVisibleItemPosition
        }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    // set FooterView type
    // if don't use footview loadmore  default: -1
    abstract fun getFooterViewType(defaultNoFooterViewType: Int): Int

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    fun reset() {
        currentPage = 1
        previousTotalItemCount = 0
        isLastPage = false
        isLoading = true
    }
}