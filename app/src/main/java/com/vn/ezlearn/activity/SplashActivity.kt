package com.vn.ezlearn.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.vn.ezlearn.R
import com.vn.ezlearn.config.EzlearnService
import com.vn.ezlearn.databinding.ActivitySplashBinding
import com.vn.ezlearn.modelresult.CategoryResult
import com.vn.ezlearn.models.Category

import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SplashActivity : AppCompatActivity() {
    private var splashBinding: ActivitySplashBinding? = null
    private var apiService: EzlearnService? = null
    private var mSubscription: Subscription? = null
    private var mCategoryResult: CategoryResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        loadDataCategory()
    }

    private fun loadDataCategory() {
        apiService = MyApplication.with(this).getEzlearnService()
        if (mSubscription != null && !mSubscription!!.isUnsubscribed)
            mSubscription!!.unsubscribe()
        mSubscription = apiService!!.category
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<CategoryResult>() {
                    override fun onCompleted() {
                        if (mCategoryResult!!.success) {
                            if (mCategoryResult!!.data != null
                                    && mCategoryResult!!.data!!.isNotEmpty()) {
                                for (category in mCategoryResult!!.data!!) {
                                    if (category.children != null
                                            && category.children!!.isNotEmpty()) {
                                        for (categoryChild in category.children!!) {
                                            if (categoryChild.children != null
                                                    && categoryChild.children!!.isNotEmpty()) {
                                                category.levelChild = Category.LEVEL_3
                                                category.typeMenu = Category.TYPE_PARENT
                                            } else {
                                                category.levelChild = Category.LEVEL_2
                                                category.typeMenu = Category.TYPE_NORMAL
                                            }
                                        }
                                    } else {
                                        category.levelChild = Category.LEVEL_1
                                        category.typeMenu = Category.TYPE_NORMAL
                                    }
                                }
                            }

                            val intent = Intent(this@SplashActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            MyApplication.with(this@SplashActivity).categoryResult = mCategoryResult
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onError(e: Throwable) {
                        goToMain()
                    }

                    override fun onNext(categoryResult: CategoryResult?) {
                        if (categoryResult != null) {
                            mCategoryResult = categoryResult
                        }
                    }
                })
    }

    private inner class Task : Runnable {
        override fun run() {
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            goToMain()
        }


    }

    private fun goToMain() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mSubscription != null && !mSubscription!!.isUnsubscribed) mSubscription!!.unsubscribe()
        mSubscription = null
    }

    companion object {
        private val TAG = SplashActivity::class.java.simpleName
    }
}
