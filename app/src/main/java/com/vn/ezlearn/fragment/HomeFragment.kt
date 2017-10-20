package com.vn.ezlearn.fragment


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.vn.ezlearn.R
import com.vn.ezlearn.activity.MyApplication
import com.vn.ezlearn.adapter.HomeAdapter
import com.vn.ezlearn.config.EzlearnService
import com.vn.ezlearn.databinding.FragmentHomeBinding
import com.vn.ezlearn.modelresult.BannerResult
import com.vn.ezlearn.modelresult.ExamsResult
import com.vn.ezlearn.models.Banner
import com.vn.ezlearn.models.HomeObject
import com.vn.ezlearn.viewmodel.HomeViewModel
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*


/**
 * A simple [Fragment] subclass.
 */

class HomeFragment : Fragment(), BaseSliderView.OnSliderClickListener {

    private var homeBinding: FragmentHomeBinding? = null
    private var homeViewModel: HomeViewModel? = null
    private var homeAdapter: HomeAdapter? = null
    private var list: List<HomeObject>? = null
    private var bannerList: MutableList<Banner>? = null

    private var apiService: EzlearnService? = null
    private var mSubscription: Subscription? = null

    private var mBannerResult: BannerResult? = null
    private var mExamsResult: ExamsResult? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        homeBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_home, container, false)
        homeViewModel = HomeViewModel()
        homeBinding!!.homeViewModel = homeViewModel
        initList()
        initBanner()
        bindData()
        return homeBinding!!.root
    }

    private fun initList() {
        list = ArrayList()
        homeAdapter = HomeAdapter(activity, ArrayList())
        homeBinding!!.rvHome.adapter = homeAdapter
    }


    private fun bindData() {

        getListExamFree(1)
    }

    private fun getListExamFree(page: Int) {
        apiService = MyApplication.with(activity).getEzlearnService()
        mSubscription = apiService!!.getListFreeExams(page, 5)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<ExamsResult>() {
                    override fun onCompleted() {
                        if (mExamsResult!!.success && mExamsResult!!.data != null
                                && mExamsResult!!.data!!.list != null
                                && mExamsResult!!.data!!.list!!.size > 0) {
                            homeAdapter!!.add(HomeObject(getString(R.string.tryExam)))
                            for (i in 0 until mExamsResult!!.data!!.list!!.size) {
                                homeAdapter!!.add(HomeObject(
                                        mExamsResult!!.data!!.list!![i]))
                            }

                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(examsResult: ExamsResult?) {
                        if (examsResult != null) {
                            mExamsResult = examsResult
                        }
                    }
                })
    }

    private fun initBanner() {
        bannerList = ArrayList()
        apiService = MyApplication.with(activity).getEzlearnService()
        if (mSubscription != null && !mSubscription!!.isUnsubscribed)
            mSubscription!!.unsubscribe()
        mSubscription = apiService!!.banners
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<BannerResult>() {
                    override fun onCompleted() {
                        if (mBannerResult!!.success && mBannerResult!!.data != null
                                && mBannerResult!!.data!!.size > 0) {
                            for (i in 0 until mBannerResult!!.data!!.size) {
                                bannerList!!.add(Banner(mBannerResult!!.data!![i]))
                            }
                            homeAdapter!!.add(0, HomeObject(bannerList!!))
                            homeBinding!!.rvHome.smoothScrollToPosition(0)
                        }

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(bannerResult: BannerResult?) {
                        if (bannerResult != null) {
                            mBannerResult = bannerResult
                        }
                    }
                })
    }


    override fun onSliderClick(slider: BaseSliderView) {

    }

    override fun onDetach() {
        super.onDetach()
        if (mSubscription != null && !mSubscription!!.isUnsubscribed) mSubscription!!.unsubscribe()
        mSubscription = null
    }
}// Required empty public constructor
