package it.giuggi.iotremote.ui.utility;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import it.giuggi.iotremote.R;

/**
 * Created by Federico Giuggioloni on 03/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class OnScrollShrink extends RecyclerView.OnScrollListener
{
    private boolean open = true;
    private boolean animating = false;
    private final Animation slideInTop;
    private final Animation slideOutTop;
    private View toShrink;

    public OnScrollShrink(View toShrink)
    {
        this.toShrink = toShrink;
        this.slideInTop = AnimationUtils.loadAnimation(toShrink.getContext(), R.anim.slide_in_top);
        this.slideOutTop = AnimationUtils.loadAnimation(toShrink.getContext(), R.anim.slide_out_top);

        slideOutTop.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                OnScrollShrink.this.animating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                OnScrollShrink.this.toShrink.setVisibility(View.GONE);
                OnScrollShrink.this.animating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        slideInTop.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                OnScrollShrink.this.animating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                OnScrollShrink.this.animating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }

    @Override
    public void onScrollStateChanged(RecyclerView view, int scrollState)
    {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        if(animating)
        {
            return;
        }

        if(dy > 0 && open)
        {
            //Scrolled going down
            toShrink.startAnimation(slideOutTop);
            open = false;
        }
        else if(dy < -10 && !open)
        {
            //Scrolled going up
            toShrink.startAnimation(slideInTop);
            toShrink.setVisibility(View.VISIBLE);
            open = true;
        }
    }

}