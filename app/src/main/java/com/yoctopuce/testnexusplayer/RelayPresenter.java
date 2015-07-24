package com.yoctopuce.testnexusplayer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.ViewGroup;

public class RelayPresenter extends Presenter
{

    private static final String TAG = "RelaPres";
    private static Context mContext;
    private final static int CARD_HEIGHT = 176;
    private final static int CARD_WIDTH = 313;


    static class ViewHolder extends Presenter.ViewHolder
    {
        private Relay _relay;
        private ImageCardView mCardView;
        private Drawable mDefaultCardImage;

        public ViewHolder(ImageCardView view)
        {
            super(view);
            mCardView = view;
            mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.movie);
        }

        public void setRelay(Relay r)
        {
            _relay = r;
            mCardView.setTitleText(r.getName());
            mCardView.setContentText(r.getHwid());
            Drawable img;
            if (r.ison()) {
                img = mContext.getResources().getDrawable(R.drawable.on);
            } else {
                img = mContext.getResources().getDrawable(R.drawable.off);
            }
            mCardView.setMainImage(img);
            mCardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
        }

        public Relay getRelay()
        {
            return _relay;
        }

        public ImageCardView getCardView()
        {
            return mCardView;
        }

    }

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent)
    {
        mContext = parent.getContext();
        ImageCardView cardView = new ImageCardView(mContext);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item)
    {
        Relay relay = (Relay) item;
        ((ViewHolder) viewHolder).setRelay(relay);
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder)
    {
    }
}
