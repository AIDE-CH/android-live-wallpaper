package me.learn.aidewallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import android.os.Handler;

public class AideWallpaperService  extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new AideEngine();
    }

    private class AideEngine extends WallpaperService.Engine{
        private SurfaceHolder mHolder;
        private boolean mVisible;
        private Handler mHandler;

        private int mCounter = 0;
        private Paint mBackGround = new Paint();
        private Paint mForeGround = new Paint();

        private Runnable mDrawTask = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        public  AideEngine(){
            mHandler = new Handler();

            mBackGround.setColor(Color.BLACK);
            mBackGround.setStyle(Paint.Style.FILL);
            mForeGround.setColor(Color.RED);
            mForeGround.setStyle(Paint.Style.FILL);
        }

        @Override
        public void onCreate(SurfaceHolder sh){
            super.onCreate(sh);
            mHolder = sh;
        }

        @Override
        public void onVisibilityChanged(boolean v) {
            mVisible = v;
            if(mVisible){
                mHandler.post(mDrawTask);
            }else{
                mHandler.removeCallbacks(mDrawTask);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawTask);
        }

        private void draw(){
            if(!mVisible) return;
            Canvas canvas = mHolder.lockCanvas();
            try{
                mCounter += 5;
                mCounter %= 360;
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mBackGround);
                canvas.drawArc(0, 0, 400, 400, 0, mCounter, true, mForeGround);
            }catch (Exception ex){
                Log.d("AideWallpaperService", ex.getMessage());
            }
            mHolder.unlockCanvasAndPost(canvas);
            mHandler.removeCallbacks(mDrawTask);
            mHandler.postDelayed(mDrawTask, 10);
        }
    }
}
