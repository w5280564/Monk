package com.qingbo.monk.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.R;
import com.qingbo.monk.bean.HomeFllowBean;
import com.xunda.lib.common.common.utils.T;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Admin on
 */
public class Follow_Adapter extends RecyclerView.Adapter<Follow_Adapter.MyViewHolder> {
    HomeFllowBean.DataDTO otherList;
    Context context;
    private int myposition;

    public Follow_Adapter(Context context, HomeFllowBean.DataDTO otherList) {
        this.otherList = otherList;
        this.context = context;
    }

    public void addMoreData(List<T> otherList) {
//        this.otherList = otherList;
        notifyDataSetChanged();
    }

    public void removeData(int position) {
//        otherList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myview = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.follow_adapter, null));
        return myview;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(v -> {
                int pos = holder.getLayoutPosition();
                mOnItemClickLitener.onItemClick(holder.itemView, position);
            });

            holder.itemView.setOnLongClickListener(v -> {
                int pos = holder.getLayoutPosition();
                mOnItemClickLitener.onItemLongClick(holder.itemView, position);
                return false;
            });

        }

        if (onItemAddRemoveClickLister != null) {
//            holder.change_Btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    if (v instanceof Button) {
//                        v.setEnabled(false);
//                    }
//                    onItemAddRemoveClickLister.onItemRemoveClick(v, position);
//                }
//            });

        }


        if (otherList.getList().isEmpty()) {
            return;
        }
        final HomeFllowBean.DataDTO.ListDTO oneData = otherList.getList().get(position);
        holder.title_Tv.setText(oneData.getTitle());
        holder.content_Tv.setText(oneData.getContent());
//        holder.name_Txt.setText(oneData.getCardName());
//        String timeStr = "无限期";
//        if (oneData.getExpirationDate() != null) {
//            String forMat = "YYYY.MM.dd";
//            timeStr = "有效期至：" + StaticData.toDateDay(forMat, oneData.getExpirationDate());
//        }
//        holder.time_Txt.setText(timeStr);

//        String typeStr = "";
//        int type = oneData.getType();
//        int status = oneData.getStatus();
        //1折扣卷 2体验卷 3经验卷
//        int avatarId = R.drawable.mo_icon;
//        if (type == 1) {
//            typeStr = "优惠券不可叠加";
//            avatarId = R.mipmap.card_adapter_coupon;
//            changeColor(type, status, holder.change_Btn, holder.select_Btn);
//        } else if (type == 2) {
//            typeStr = "使用后VIP功能限时尊享";
//            avatarId = R.mipmap.card_adapter_attempt;
//            changeColor(type, status, holder.change_Btn, holder.select_Btn);
//        } else if (type == 3) {
//            avatarId = R.mipmap.card_adapter_exp;
//            changeColor(type, status, holder.change_Btn, holder.select_Btn);
//            typeStr = "使用后用户经验+" + oneData.getRules().intValue();
//        }
//        Glide.with(context).load(avatarId).into(holder.head_Simple);
//        holder.type_Txt.setText(typeStr);
    }

//    private void changeColor(int type, Integer status, Button change_Btn, Button select_Btn) {
//        if (status == 0) {
//            change_Btn.setVisibility(View.VISIBLE);
//            if (type == 1) {
//                StaticData.changeShapColor(change_Btn, ContextCompat.getColor(context, R.color.blue));
//            }else if (type == 2) {
//                StaticData.changeShapColor(change_Btn, ContextCompat.getColor(context, R.color.purpleTxt));
//            }else if (type == 3) {
//                StaticData.changeShapColor(change_Btn, ContextCompat.getColor(context, R.color.yellowfive));
//            }
//        } else if (status == 1) {
//            select_Btn.setVisibility(View.VISIBLE);
//            select_Btn.setText("已使用");
//        } else if (status == 2) {
//            select_Btn.setVisibility(View.VISIBLE);
//            select_Btn.setText("已过期");
//        }
//    }


    @Override
    public int getItemCount() {
        return (otherList == null) ? 0 : otherList.getList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return myposition = position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title_Tv,content_Tv;
        private Button change_Btn, select_Btn;
        private ImageView head_Simple;
        private TextView name_Txt, time_Txt, type_Txt;

        public MyViewHolder(View itemView) {
            super(itemView);
            // 解决view宽和高不显示的问题
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);
//            head_Simple = itemView.findViewById(R.id.head_Simple);
//            name_Txt = itemView.findViewById(R.id.name_Txt);
//            time_Txt = itemView.findViewById(R.id.time_Txt);
//            type_Txt = itemView.findViewById(R.id.type_Txt);
//            change_Btn = itemView.findViewById(R.id.change_Btn);
//            select_Btn = itemView.findViewById(R.id.select_Btn);
            title_Tv = itemView.findViewById(R.id.title_Tv);
            content_Tv = itemView.findViewById(R.id.content_Tv);

        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener listener) {
        mOnItemClickLitener = listener;
    }


    public interface OnItemAddRemoveClickLister {

        void onItemRemoveClick(View view, int position);
    }

    private OnItemAddRemoveClickLister onItemAddRemoveClickLister;

    public void setOnItemAddRemoveClickLister(OnItemAddRemoveClickLister ItemListener) {
        onItemAddRemoveClickLister = ItemListener;
    }


}
