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

public class RotateYByActivity extends BaseAminActivity {

    private long time = 1000L;
    private float rotationY = 180f;


    @NotNull
    @Override
    public String getDescribe() {
        return "view y角度  从180度 旋转偏移180度";
    }

    @NotNull
    @Override
    public String getResetBtnText() {
        return "旋转";
    }

    @Override
    public void initRunView(@NotNull View runView) {
        super.initRunView(runView);
        runView.setRotationY(180F);
    }

    @NotNull
    @Override
    public Tween clickTweenBtn(@NotNull View runView) {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.Companion.builder(runView)
                .to(new HashMap<String, Number>() {
                    {
                        put("rotationY", runView.getRotationY() + rotationY);
                    }
                })
                .time(time)
                .setExpire(true)
                .start();
    }

    @Override
    public void clickActionBtn(@NotNull View runView) {
        RunAction.INSTANCE.runAction(runView, Action.INSTANCE.rotateYBy(time, rotationY));
    }

    @Override
    public void clickChainedBtn(@NotNull View runView) {
        new SequenceActionRunBuild(runView)
                .rotateYBy(time, rotationY)
                .start();
    }
}
