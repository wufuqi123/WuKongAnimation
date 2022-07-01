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

public class RotateToActivity extends BaseAminActivity {

    private long time = 1000L;
    private float rotation = 180f;


    @NotNull
    @Override
    public String getDescribe() {
        return "view 旋转180度";
    }

    @NotNull
    @Override
    public String getResetBtnText() {
        return "旋转";
    }

    @NotNull
    @Override
    public Tween clickTweenBtn(@NotNull View runView) {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.Companion.builder(runView)
                .to(new HashMap<String, Number>() {
                    {
                        put("rotation", rotation);
                    }
                })
                .time(time)
                .setExpire(true)
                .start();
    }

    @Override
    public void clickActionBtn(@NotNull View runView) {
        RunAction.INSTANCE.runAction(runView, Action.INSTANCE.rotateTo(time, rotation));
    }

    @Override
    public void clickChainedBtn(@NotNull View runView) {
        new SequenceActionRunBuild(runView)
                .rotateTo(time, rotation)
                .start();
    }
}
