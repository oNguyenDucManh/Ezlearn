package com.vn.ezlearn.fragment


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vn.ezlearn.R
import com.vn.ezlearn.activity.MyApplication
import com.vn.ezlearn.adapter.PackageAdapter
import com.vn.ezlearn.config.EzlearnService
import com.vn.ezlearn.databinding.FragmentPackageBinding
import com.vn.ezlearn.modelresult.BaseListResult
import com.vn.ezlearn.models.Package
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * A simple [Fragment] subclass.
 */
class PackageFragment : Fragment() {
    private lateinit var packageBinding: FragmentPackageBinding
    private lateinit var packageAdapter: PackageAdapter
    private var apiService: EzlearnService? = null
    private var mSubscription: Subscription? = null
    private var isAttach = true
    private lateinit var mPackageResult: BaseListResult<Package>
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        packageBinding = DataBindingUtil.inflate(
                inflater!!, R.layout.fragment_package, container, false)
        apiService = MyApplication.with(activity).getEzlearnService()
        bindData()
        return packageBinding.rvList
    }

    private fun bindData() {
        packageAdapter = PackageAdapter(activity, ArrayList())
        packageBinding.rvList.adapter = packageAdapter

        if (mSubscription != null && !mSubscription!!.isUnsubscribed)
            mSubscription!!.unsubscribe()
        mSubscription = apiService!!.getListPackage()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<BaseListResult<Package>>() {
                    override fun onCompleted() {
                        if (isAttach) {
                            if (mPackageResult.success && mPackageResult.data != null) {
                                packageAdapter.addAll(mPackageResult.data!!.list!!)
                            }
                        }

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(packageResult: BaseListResult<Package>?) {
                        if (packageResult != null) {
                            mPackageResult = packageResult
                        }
                    }
                })
    }

    override fun onDetach() {
        super.onDetach()
        isAttach = false
    }

    override fun onResume() {
        super.onResume()
        isAttach = true
    }

    override fun onDestroy() {
        super.onDestroy()
        isAttach = false
    }
}
