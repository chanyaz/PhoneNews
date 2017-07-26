package com.ronin.phonenews.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ronin.cc.adapter.CompanyListAdapter
import com.ronin.cc.loadmore.CustomLoadMoreView
import com.ronin.cc.util.isNetwork
import com.ronin.phonenews.R
import com.ronin.pullrefreshlibrary.PullToRefreshView

/**
 * Created by hackware on 2016/9/10.
 */

class NewsPagerAdapter(private val mDataList: List<String>?, private val context: Context) : PagerAdapter(),
        PullToRefreshView.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    val tag = "msg"

    val adapter = CompanyListAdapter()

    lateinit var mErrorView: View
    lateinit var mLoadingView: View
    lateinit var mNoDataView: View

    private var mInflater: LayoutInflater? = null
    private val mViewHolder: ViewHolder? = null
    private val mContainerArray = SparseArray<View>()

    override fun getCount(): Int {
        return mDataList?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view: View? = mContainerArray.get(position)
        if (view == null) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(container.context)
            }
            val mViewHolder = ViewHolder()
            view = mInflater!!.inflate(R.layout.layout_recyclerview, container, false)
            mViewHolder.recyclerView = view!!.findViewById(R.id.recyclerView) as RecyclerView
            mViewHolder.swipeLayout = view!!.findViewById(R.id.swipeLayout) as PullToRefreshView
            mViewHolder.pos = position
            view.tag = mViewHolder
            initRecyclerView(mViewHolder.recyclerView!!, mViewHolder.swipeLayout!!)

            mContainerArray.put(position, view)
            container.addView(view)
        } else {
            if (view.parent == null) {
                container.addView(view)
            }
        }


        return view
    }

    private fun initRecyclerView(recyclerView: RecyclerView, swipeLayout: PullToRefreshView) {
        swipeLayout.setOnRefreshListener(this)
        swipeLayout.setOnClickListener {
            Log.d(tag, "click")
        }

        mErrorView = mInflater!!.inflate(R.layout.error_view,
                recyclerView.parent as ViewGroup, false)
        mErrorView.setOnClickListener {
            onRefresh()
        }
        mNoDataView = mInflater!!.inflate(R.layout.empty_view,
                recyclerView.parent as ViewGroup, false)
        mNoDataView.setOnClickListener {
            onRefresh()
        }
        mLoadingView = mInflater!!.inflate(R.layout.loading_view,
                recyclerView.parent as ViewGroup, false)

        //        infoAdapter.openLoadAnimation()
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN)
        adapter.setOnLoadMoreListener(this, recyclerView)
        adapter.setNotDoAnimationCount(3)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.setLoadMoreView(CustomLoadMoreView())
        adapter.setOnItemLongClickListener { adapter, view, position ->

            Log.d(tag, "position=$position")

            true
        }
        adapter.emptyView = mLoadingView

        if (!context.isNetwork()) {
            adapter.emptyView = mErrorView

        }

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any?): Int {
        val view = `object` as View?
        val viewHolder = view!!.tag as ViewHolder
        val pos = viewHolder.pos
        if (pos >= 0) {
            return pos
        }
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mDataList!![position]
    }

    override fun onRefresh() {

    }

    override fun onLoadMoreRequested() {
    }

    private class ViewHolder {
        internal var pos: Int = 0
        internal var swipeLayout: PullToRefreshView? = null
        internal var recyclerView: RecyclerView? = null
    }
}
