package com.ronin.phonenews.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ronin.cc.adapter.CompanyListAdapter
import com.ronin.cc.loadmore.CustomLoadMoreView
import com.ronin.cc.util.isNetwork
import com.ronin.phonenews.R
import com.ronin.phonenews.titles.ScaleTransitionPagerTitleView
import com.ronin.pullrefreshlibrary.PullToRefreshView
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator
import java.util.*

class NewsActivity : AppCompatActivity(), PullToRefreshView.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {
    override fun onRefresh() {

    }

    override fun onLoadMoreRequested() {

    }

    private val CHANNELS = arrayOf("CUPCAKE", "DONUT", "ECLAIR", "GINGERBREAD",
            "HONEYCOMB", "ICE_CREAM_SANDWICH", "JELLY_BEAN", "KITKAT", "LOLLIPOP", "M", "NOUGAT")
    private val mDataList = Arrays.asList(*CHANNELS)

    var indicator: MagicIndicator? = null

    internal var swipeLayout: PullToRefreshView? = null
    internal var recyclerView: RecyclerView? = null

    val adapter = CompanyListAdapter()

    lateinit var mErrorView: View
    lateinit var mLoadingView: View
    lateinit var mNoDataView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        initMagicIndicator()

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        swipeLayout = findViewById(R.id.swipeLayout) as PullToRefreshView

        swipeLayout!!.setOnRefreshListener(this)
        swipeLayout!!.setOnClickListener {

        }

        mErrorView = layoutInflater!!.inflate(R.layout.error_view,
                recyclerView!!.parent as ViewGroup, false)
        mErrorView.setOnClickListener {
            onRefresh()
        }
        mNoDataView = layoutInflater!!.inflate(R.layout.empty_view,
                recyclerView!!.parent as ViewGroup, false)
        mNoDataView.setOnClickListener {
            onRefresh()
        }
        mLoadingView = layoutInflater!!.inflate(R.layout.loading_view,
                recyclerView!!.parent as ViewGroup, false)

        //        infoAdapter.openLoadAnimation()
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN)
        adapter.setOnLoadMoreListener(this, recyclerView)
        adapter.setNotDoAnimationCount(3)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = adapter
        adapter.setLoadMoreView(CustomLoadMoreView())
        adapter.setOnItemLongClickListener { adapter, view, position ->

            true
        }
        adapter.emptyView = mLoadingView

        if (!isNetwork()) {
            adapter.emptyView = mErrorView
        }

    }

    private fun initMagicIndicator() {
        indicator = findViewById(R.id.magic_indicator) as MagicIndicator
        indicator?.setBackgroundColor(Color.WHITE)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList?.size ?: 0
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.textSize = 18f
                simplePagerTitleView.normalColor = Color.GRAY
                simplePagerTitleView.selectedColor = Color.BLACK
                simplePagerTitleView.setOnClickListener({
                    println("index=$index,title=${CHANNELS[index]}")

                })

                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = BezierPagerIndicator(context)
                indicator.setColors(Color.parseColor("#ff4a42"),
                        Color.parseColor("#fcde64"),
                        Color.parseColor("#73e8f4"),
                        Color.parseColor("#76b0ff"),
                        Color.parseColor("#c683fe"))
                return indicator
            }
        }
        indicator?.navigator = commonNavigator

    }

}
