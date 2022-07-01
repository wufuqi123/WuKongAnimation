package com.example.hehe.ui;


import android.view.View;

import com.example.hehe.BaseAminActivity;
import com.wukonganimation.action.Action;
import com.wukonganimation.action.RunAction;
import com.wukonganimation.action.chained.SequenceActionRunBuild;
import com.wukonganimation.action.chained.SpawnActionRunBuild;
import com.wukonganimation.tween.Tween;
import com.wukonganimation.tween.TweenManager;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SpawnActivity extends BaseAminActivity {

    private long time = 1000L;

    private float x = 100f;
    private float y = 100f;
    private float rotation = 360f;

    @NotNull
    @Override
    public String getDescribe() {
        return "view 同步动画，moveBy + rotateBy";
    }

    @NotNull
    @Override
    public String getResetBtnText() {
        return "同步动画";
    }



    @NotNull
    @Override
    public Tween clickTweenBtn(@NotNull View runView) {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.Companion.builder(runView)
                .to(new HashMap<String, Number>() {
                    {
                        put("x", runView.getX() + x);
                        put("y", runView.getY() + y);
                        put("rotation", runView.getRotation() + rotation);
                    }
                })
                .time(time)
                .setExpire(true)
                .start();
    }

    @Override
    public void clickActionBtn(@NotNull View runView) {
        RunAction.INSTANCE.runAction(runView, Action.INSTANCE.spawn(Action.INSTANCE.moveBy(time, x, y), Action.INSTANCE.rotateBy(time, rotation)));
    }

    @Override
    public void clickChainedBtn(@NotNull View runView) {
        new SpawnActionRunBuild(runView)
                .moveBy(time, x, y)
                .rotateBy(time, rotation)
                .start();
    }
}
