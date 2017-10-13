package com.vn.ezlearn.adapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vn.ezlearn.BR;
import com.vn.ezlearn.R;
import com.vn.ezlearn.interfaces.OnClickQuestionPopupListener;
import com.vn.ezlearn.models.MyContent;
import com.vn.ezlearn.viewmodel.ItemQuestionDialogViewModel;

import java.util.List;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 21/09/2017.
 */

public class DialogListQuestionAdapter extends BaseRecyclerAdapter<MyContent,
        DialogListQuestionAdapter.ViewHolder> {
    private OnClickQuestionPopupListener onClickQuestionPopupListener;
    private boolean isShowPoint;

    public DialogListQuestionAdapter(Context context, List<MyContent> list,
                                     OnClickQuestionPopupListener onClickQuestionPopupListener) {
        super(context, list);
        this.onClickQuestionPopupListener = onClickQuestionPopupListener;
        isShowPoint = false;
    }

    public DialogListQuestionAdapter(Context context, List<MyContent> list) {
        super(context, list);
        isShowPoint = true;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ViewDataBinding viewDataBinding = holder.getViewDataBinding();
        viewDataBinding.setVariable(BR.itemQuestionDialogViewModel,
                new ItemQuestionDialogViewModel(mContext, list.get(position), position + 1,
                        isShowPoint));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_dialog_question, parent, false);

        return new ViewHolder(binding);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding mViewDataBinding;

        ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());

            mViewDataBinding = viewDataBinding;
            mViewDataBinding.executePendingBindings();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isShowPoint) {
                        onClickQuestionPopupListener.onClick(getAdapterPosition());
                    }

                }
            });
        }

        ViewDataBinding getViewDataBinding() {
            return mViewDataBinding;
        }
    }

    @BindingAdapter("backgroundResource")
    public static void setBackgroudResource(ImageView imageView, int resource) {
        imageView.setBackgroundResource(resource);
    }
}
