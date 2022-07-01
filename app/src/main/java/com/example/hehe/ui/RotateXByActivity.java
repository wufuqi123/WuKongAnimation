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

public class RotateXByActivity extends BaseAminActivity {

    private long time = 1000L;
    private float rotationX = 180f;


    @NotNull
    @Override
    public String getDescribe() {
        return "view x角度  从180度 旋转偏移180度";
    }

    @NotNull
    @Override
    public String getResetBtnText() {
        return "旋转";
    }

    @Override
    public void initRunView(@NotNull View runView) {
        super.initRunView(runView);
        runView.setRotationX(180F);
    }

    @NotNull
    @Override
    public Tween clickTweenBtn(@NotNull View runView) {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.Companion.builder(runView)
                .to(new HashMap<String, Number>() {
                    {
                        put("rotationX", runView.getRotationX() + rotationX);
                    }
                })
                .time(time)
                .setExpire(true)
                .start();
    }

    @Override
    public void clickActionBtn(@NotNull View runView) {
        RunAction.INSTANCE.runAction(runView, Action.INSTANCE.rotateXBy(time, rotationX));
    }

    @Override
    public void clickChainedBtn(@NotNull View runView) {
        new SequenceActionRunBuild(runView)
                .rotateXBy(time, rotationX)
                .start();
    }
}
