package com.qingbo.monk.Slides.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.R;
import com.qingbo.monk.Slides.widget.StackCardsView;
import com.qingbo.monk.Slides.widget.carditem.BaseCardItem;

/**
 * Created by wensefu .
 */
public class ScrollCardItem extends BaseCardItem {


    public ScrollCardItem(Context context) {
        super(context);
        swipeDir = StackCardsView.SWIPE_LEFT | StackCardsView.SWIPE_RIGHT;
    }

    @Override
    public View getView(View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.item_scrollcard, null);
        RecyclerView recyclerView = convertView.findViewById( R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new VerticalAdapter());
        return convertView;
    }

    private static class VerticalVH extends RecyclerView.ViewHolder {

        TextView textView;

        public VerticalVH(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_v);
        }
    }

    private class VerticalAdapter extends RecyclerView.Adapter<VerticalVH> {

        private String[] array = {
                "My life is brilliant.",
                "My life is brilliant.",
                "My love is pure.",
                "I saw an angel.",
                "Of that I'm sure.",
                "She smiled at me on the subway.",
                "She was with another man.",
                "But I won't lose no sleep on that,",
                "'Cause I've got a plan.",
                "You're beautiful. You're beautiful,",
                "You're beautiful, it's true.",
                "I saw your face in a crowded place,",
                "And I don't know what to do,",
                "'Cause I'll never be with you.",
                "Yeah, she caught my eye,",
                "As we walked on by.",
                "She could see from my face that I was,",
                "flying high,",
                "And I don't think that I'll see her again,",
                "But we shared a moment that will last till the end.",
                "You're beautiful. You're beautiful.",
                "You're beautiful, it's true.",
                "I saw your face in a crowded place,",
                "And I don't know what to do,",
                "'Cause I'll never be with you.",
                "You're beautiful. You're beautiful.",
                "You're beautiful, it's true.",
                "There must be an angel with a smile on her face,",
                "When she thought up that I should be with you.",
                "But it's time to face the truth,",
                "I will never be with you.",
        };

        @Override
        public VerticalVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_v, parent, false);
            return new VerticalVH(itemView);
        }

        @Override
        public void onBindViewHolder(VerticalVH holder, int position) {
            holder.textView.setText(array[position]);
        }

        @Override
        public int getItemCount() {
            return array.length;
        }
    }
}
