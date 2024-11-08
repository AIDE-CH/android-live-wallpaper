package me.learn.aidewallpaper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import android.os.Handler;

import me.learn.gl.core.AObj;
import me.learn.gl.customobjs.PCTObj;
import me.learn.gl.customobjs.PathVert;
import me.learn.gl.customobjs.SkyBox;
import me.learn.gl.customobjs.SphereObj;
import me.learn.gl.customobjs.SphereVert2;
import me.learn.gl.ui.Scene;

public class AideWallpaperService  extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new AideEngine();
    }

    private class AideEngine extends WallpaperService.Engine{
        private SurfaceHolder mHolder;
        private WallpaperGLView mGlView;
        private Scene mScene;

        public  AideEngine(){
        }

        @Override
        public void onCreate(SurfaceHolder sh){
            super.onCreate(sh);
            mHolder = sh;
            mGlView = new WallpaperGLView(AideWallpaperService.this);
            // init our gl scene
            mScene = new Scene(AideWallpaperService.this, mGlView, null, null,
                    null, null, null);
            SkyBox sb = new SkyBox(300,
                    "images/milkyway2/right.png",
                    "images/milkyway2/left.png",
                    "images/milkyway2/top.png",
                    "images/milkyway2/bottom.png",
                    "images/milkyway2/front.png",
                    "images/milkyway2/back.png");
            mScene.addObj(sb);

            SphereObj earth = new SphereObj(1, 2,
                    "images/earth/right.png",
                    "images/earth/left.png",
                    "images/earth/top.png",
                    "images/earth/bottom.png",
                    "images/earth/front.png",
                    "images/earth/back.png"
            );
            earth.setUpdateCall((timestamp, obj) -> moveEarth(timestamp, obj));
            mScene.addObj(earth);

            SphereVert2 sv2 = new SphereVert2(1, 10);
            PCTObj sun = new PCTObj(sv2.getPositionsAndTexture(), false, true,
                    "images/sun.jpg");
            sun.setUpdateCall((timestamp, obj) -> moveSun(timestamp, obj));
            mScene.addObj(sun);
        }

        float theta = 0;
        private void moveEarth(long timestamp, AObj earth) {
            float slow = 5;
            theta -= (float) (1.0F/180.0F*Math.PI)/slow;
            float[] res = PathVert.ellipse(3, 0.5F, theta);
            float[] currPos = new float[]{res[1], 0, res[2]};
            earth.setTranslation(new float[]{0,0,0});
            earth.rotate(5F/slow, 0, 1, 0);
            earth.setTranslation(currPos);
        }

        private void moveSun(long timestamp, AObj sun) {
            sun.rotate(1.5F, 0, 1, 0);
        }

        @Override
        public void onVisibilityChanged(boolean v) {
            super.onVisibilityChanged(v);
            if(v){
                mGlView.onResume();
            }else{
                mGlView.onPause();
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mGlView.onDestroy();
        }

        class WallpaperGLView extends GLSurfaceView {
            WallpaperGLView(Context context) {
                super(context);
            }

            @Override
            public SurfaceHolder getHolder() {
                return getSurfaceHolder();
            }

            public void onDestroy() {
                super.onDetachedFromWindow();
            }
        }
    }
}
