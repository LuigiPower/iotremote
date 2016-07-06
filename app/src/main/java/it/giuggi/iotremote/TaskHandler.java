package it.giuggi.iotremote;

import android.os.Handler;

import java.util.ArrayList;

/**
 * Created by Federico Giuggioloni on 06/07/16.
 * Se aggiungo questa riga magari
 * AndroidStudio smette di lamentarsi...
 */
public class TaskHandler
{
    private class RunnableWithDelay
    {
        private Runnable r;
        private long delay;
        private boolean periodically;

        private RunnableWithDelay(Runnable r, long delay, boolean periodically)
        {
            this.r = r;
            this.delay = delay;
            this.periodically = periodically;
        }
    }

    private static TaskHandler instance;

    private Handler handler;
    private ArrayList<RunnableWithDelay> runningList;

    private TaskHandler()
    {
        handler = new Handler();
        runningList = new ArrayList<>(5);
    }

    public static TaskHandler getInstance()
    {
        if(instance == null)
        {
            instance = new TaskHandler();
        }

        return instance;
    }

    public void clear()
    {
        onPause();
        runningList.clear();
    }

    public void runDelay(final Runnable r, long delay)
    {
        Runnable real = new Runnable()
        {
            @Override
            public void run()
            {
                r.run();
                runningList.remove(r);
            }
        };

        runningList.add(new RunnableWithDelay(r, delay, false));
        handler.postDelayed(real, delay);
    }

    public void runPeriodically(final Runnable r, final long delay)
    {
        Runnable real = new Runnable()
        {
            @Override
            public void run()
            {
                r.run();
                runningList.remove(r);
                runPeriodically(r, delay);
            }
        };

        runningList.add(new RunnableWithDelay(r, delay, true));
        handler.postDelayed(real, delay);
    }

    public void onPause()
    {
        handler.removeCallbacksAndMessages(null);
    }

    public void onResume()
    {
        for(RunnableWithDelay r : runningList)
        {
            if(r.periodically)
            {
                runPeriodically(r.r, r.delay);
            }
            else
            {
                runDelay(r.r, r.delay);
            }
        }
    }
}
