package com.vn.ezlearn.viewmodel

import android.app.Activity
import android.content.Context
import android.databinding.*
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import com.vn.ezlearn.R
import com.vn.ezlearn.models.MyContent

/**
 * Created by FRAMGIA\nguyen.duc.manh on 15/09/2017.
 */

class QuestionViewModel(context: Activity, private val content: MyContent?, position: Int,
                        size: Int) : BaseObservable() {

    private val context: Context
    var textPassage: ObservableField<Spanned>
    var textQuestion: ObservableField<Spanned>
    var visiablePassage: ObservableInt
    var visiableQuestion: ObservableInt

    var isNeedReview: ObservableBoolean
    var textNeedReview: ObservableField<Spanned>
    var drawableFlag: ObservableInt

    var answerA: ObservableField<Spanned>
    var answerB: ObservableField<Spanned>
    var answerC: ObservableField<Spanned>
    var answerD: ObservableField<Spanned>

    var tvQuestion: ObservableField<String>

    var visiableNext: ObservableInt? = null
    var visiablePre: ObservableInt? = null

    var suggest: ObservableField<Spanned>
    var visiableSuggest: ObservableInt

    init {
        this.context = context
        textPassage = ObservableField()
        textQuestion = ObservableField()
        visiablePassage = ObservableInt(View.GONE)
        visiableQuestion = ObservableInt(View.GONE)
        isNeedReview = ObservableBoolean(false)
        textNeedReview = ObservableField(
                Html.fromHtml(context.getString(R.string.needReview)))
        drawableFlag = ObservableInt(R.mipmap.ic_flag)

        answerA = ObservableField()
        answerB = ObservableField()
        answerC = ObservableField()
        answerD = ObservableField()

        visiableSuggest = ObservableInt(View.GONE)
        suggest = ObservableField()

        tvQuestion = ObservableField(
                context.getString(R.string.question) + " " + (position + 1) + "/" + size)

        if (content != null) {
            setQuestionData()
        }

    }

    private fun setQuestionData() {
        //        if (content.type == Question.TYPE_READING) {
        if (content != null && !content.passage.isEmpty()) {
            textPassage.set(Html.fromHtml(content.passage.replace("&nbsp;", "")))
            visiablePassage.set(View.VISIBLE)
        } else {
            visiablePassage.set(View.GONE)
        }
        //        } else {
        if (content != null && !content.content.content.isEmpty()) {
            textQuestion.set(Html.fromHtml(content.content.content.replace("<p>", "")
                    .replace("</p>", "")))
            visiableQuestion.set(View.VISIBLE)
        } else {
            visiableQuestion.set(View.GONE)
        }
        if (content != null && content.content.answer_list != null
                && content.content.answer_list!!.size >= 4) {
            answerA.set(Html.fromHtml(content.content.answer_list!![0].answer!!
                    .replace("<p>", "").replace("</p>", "")))
            answerB.set(Html.fromHtml(content.content.answer_list!![1].answer!!
                    .replace("<p>", "").replace("</p>", "")))
            answerC.set(Html.fromHtml(content.content.answer_list!![2].answer!!
                    .replace("<p>", "").replace("</p>", "")))
            answerD.set(Html.fromHtml(content.content.answer_list!![3].answer!!
                    .replace("<p>", "").replace("</p>", "")))
        }
        if (content != null && content.content.suggest != null) {
            suggest.set(Html.fromHtml(content.content.suggest))
        }
    }

    fun setNeedReview() {
        isNeedReview.set(!isNeedReview.get())
        if (isNeedReview.get()) {
            textNeedReview.set(Html.fromHtml(context.getString(R.string.unNeedReview)))
            drawableFlag.set(R.mipmap.ic_flag_red)
        } else {
            textNeedReview.set(Html.fromHtml(context.getString(R.string.needReview)))
            drawableFlag.set(R.mipmap.ic_flag)

        }
    }

    fun showSuggest() {
        visiableSuggest.set(View.VISIBLE)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imageResource")
        fun setImageResource(imageView: ImageView, resource: Int) {
            imageView.setImageResource(resource)
        }
    }
}
