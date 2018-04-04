package com.ronin.phonenews.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ronin.cc.adapter.NewsListAdapter
import com.ronin.cc.http.XHttp
import com.ronin.cc.loadmore.CustomLoadMoreView
import com.ronin.cc.util.getScreenHeight
import com.ronin.cc.util.isNetwork
import com.ronin.cc.util.toast
import com.ronin.net.api.ApiMethods
import com.ronin.net.base.SimpleObserver
import com.ronin.phonenews.R
import com.ronin.phonenews.bean.NewsBean
import com.ronin.phonenews.titles.ScaleTransitionPagerTitleView
import com.ronin.phonenews.util.DanmakuHelper
import com.ronin.phonenews.util.GsonUtil
import com.ronin.phonenews.util.XThread
import com.ronin.pullrefreshlibrary.PullToRefreshView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DefaultObserver
import io.reactivex.schedulers.Schedulers
import master.flame.danmaku.ui.widget.DanmakuView
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator
import test.bean.Movie

class NewsActivity : BaseActivity(), PullToRefreshView.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {
    val TAG = NewsActivity::javaClass.name

    val newsLinkMap = linkedMapOf(
            Pair("social", "社会新闻"),
            Pair("guonei", "国内新闻"),
            Pair("world", "国际新闻"),
            Pair("huabian", "娱乐新闻"),
            Pair("tiyu", "体育新闻"),
            Pair("nba", "NBA新闻"),
            Pair("football", "足球新闻"),
            Pair("keji", "科技新闻"),
            Pair("startup", "科技新闻"),
            Pair("apple", "苹果新闻"),
            Pair("military", "军事新闻"),
            Pair("mobile", "移动互联"),
            Pair("travel", "旅游咨询"),
            Pair("health", "健康知识"),
            Pair("qiwen", "奇闻异事"),
            Pair("meinv", "美女图片"),
            Pair("vr", "VR科技"),
            Pair("it", "IT资讯")
    )
    private val mDataList = newsLinkMap.values.toList()
    private val mKeyList = newsLinkMap.keys.toList()

    private var mCurIndex = 0
    private var mCurPage = 1
    private var mCurSearchWord = "北京"

    var indicator: MagicIndicator? = null
    val nagvHelper = FragmentContainerHelper()

    private var swipeLayout: PullToRefreshView? = null
    internal var recyclerView: RecyclerView? = null
    private var danmaku_view: DanmakuView? = null
    private var danmakuHelper: DanmakuHelper? = null

    val adapter = NewsListAdapter()

    lateinit var mErrorView: View
    lateinit var mLoadingView: View
    lateinit var mEmptyView: View

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        initMagicIndicator()
        initRecyclerView()
        initDanmaku()

        XThread.execute(Runnable {
            initData()
        })

    }

    private fun initDanmaku() {
        danmaku_view = findViewById(R.id.danmaku_view) as DanmakuView
        val layoutParams = danmaku_view!!.layoutParams
        layoutParams.height = getScreenHeight() / 3
        danmaku_view!!.layoutParams = layoutParams

        danmakuHelper = DanmakuHelper(danmaku_view!!)
    }

    private fun initData() {

        requestData(mKeyList[mCurIndex], isInit = true)
        ApiMethods.getTopMovie(object : SimpleObserver<Movie>() {
            override fun onNext(t: Movie) {
                Log.i(TAG, "onNext=${GsonUtil.toJson(t)}")
            }
        }, 0, 10)

    }

    private fun requestData(path: String, isInit: Boolean = false) {
        val rand = if (isInit) {
            mCurPage = 1
            1
        } else {
            0
        }

        XHttp.serviceApi.getNewsList(path, word = mCurSearchWord,
                page = mCurPage, rand = rand)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultObserver<NewsBean>() {
                    override fun onError(e: Throwable) {
                    }

                    override fun onNext(t: NewsBean) {
                        if (t!!.code == 200 && t.newslist!!.isNotEmpty()) {
                            if (isInit) {
                                adapter.data.clear()
                                adapter.setNewData(t.newslist)
                            } else {
                                adapter.addData(t.newslist)
                            }
                            adapter.notifyDataSetChanged()

                        } else {
                            adapter.emptyView = mEmptyView
                            adapter.setEnableLoadMore(false)
                        }
                    }

                    override fun onComplete() {
                        adapter.loadMoreComplete()
                    }


                })
    }


    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        swipeLayout = findViewById(R.id.swipeLayout) as PullToRefreshView

        swipeLayout!!.setOnRefreshListener(this)
        swipeLayout!!.setOnClickListener {
            toast("pull image")
        }

        mErrorView = layoutInflater!!.inflate(R.layout.error_view,
                recyclerView!!.parent as ViewGroup, false)
        mErrorView.setOnClickListener {
            onRefresh()
        }
        mEmptyView = layoutInflater!!.inflate(R.layout.empty_view,
                recyclerView!!.parent as ViewGroup, false)
        mEmptyView.setOnClickListener {
            onRefresh()
        }
        mLoadingView = layoutInflater!!.inflate(R.layout.loading_view,
                recyclerView!!.parent as ViewGroup, false)

        //        adapter.openLoadAnimation()
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN)
        adapter.setOnLoadMoreListener(this, recyclerView)
        adapter.setNotDoAnimationCount(3)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = adapter
        adapter.setLoadMoreView(CustomLoadMoreView())
        adapter.setOnItemLongClickListener { _, view, position ->

            true
        }
        adapter.setOnItemClickListener { _, view, position ->
            val bean = adapter.data[position]
            WebViewActivity.action(this, bean.title!!, bean.url!!)
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
        nagvHelper.handlePageSelected(0, false)
        val commonNavigator = CommonNavigator(this)
//        commonNavigator.isSkimOver = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.textSize = 18f
                simplePagerTitleView.normalColor = Color.GRAY
                simplePagerTitleView.selectedColor = Color.BLACK
                simplePagerTitleView.setOnClickListener({
                    if (mCurIndex != index) {
                        mCurIndex = index
                        nagvHelper.handlePageSelected(index)
                        recyclerView!!.smoothScrollToPosition(0)
                        requestData(mKeyList[mCurIndex], true)
                    }
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
        nagvHelper.attachMagicIndicator(indicator)
    }


    override fun onRefresh() {
        adapter.emptyView = mLoadingView
        adapter.setEnableLoadMore(false)
        swipeLayout!!.isRefreshing = true
        mHandler.postDelayed({
            swipeLayout!!.isRefreshing = false
            if (isNetwork()) {
                adapter.setEnableLoadMore(true)
                initData()
            } else {
                adapter.emptyView = mErrorView
            }
        }, 2000)
    }

    override fun onLoadMoreRequested() {
        if (mCurPage < 100) {
            mCurPage++
            requestData(mKeyList[mCurIndex])

        } else {
            adapter.loadMoreComplete()
            adapter.setEnableLoadMore(false)
        }
    }

    override fun onPause() {
        super.onPause()
        danmakuHelper!!.pause()
    }

    override fun onResume() {
        super.onResume()
        danmakuHelper!!.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        danmakuHelper!!.destroy()
    }


}
