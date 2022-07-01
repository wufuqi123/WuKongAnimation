package com.example.hehe.ui;


import android.view.View;

import com.example.hehe.BaseAminActivity;
import com.wukonganimation.action.Action;
import com.wukonganimation.action.RunAction;
import com.wukonganimation.action.chained.SequenceActionRunBuild;
import com.wukonganimation.tween.Tween;
import com.wukonganimation.tween.TweenManager;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ScaleToActivity extends BaseAminActivity {

    private long time = 1000L;
    private float scaleX = 2f;
    private float scaleY = 2f;


    @NotNull
    @Override
    public String getDescribe() {
        return "view 缩放到2倍";
    }

    @NotNull
    @Override
    public String getResetBtnText() {
        return "缩放";
    }

    @NotNull
    @Override
    public Tween clickTweenBtn(@NotNull View runView) {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.Companion.builder(runView)
                .to(new HashMap<String, Number>() {
                    {
                        put("scaleX", scaleX);
                        put("scaleY", scaleY);
                    }
                })
                .time(time)
                .setExpire(true)
                .start();
    }

    @Override
    public void clickActionBtn(@NotNull View runView) {
        RunAction.INSTANCE.runAction(runView, Action.INSTANCE.scaleTo(time, scaleX, scaleY));
    }

    @Override
    public void clickChainedBtn(@NotNull View runView) {
        new SequenceActionRunBuild(runView)
                .scaleTo(time, scaleX, scaleY)
                .start();
    }
}
