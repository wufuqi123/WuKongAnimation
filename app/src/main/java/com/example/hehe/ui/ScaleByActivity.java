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

public class ScaleByActivity extends BaseAminActivity {

    private long time = 1000L;
    private float scaleX = 2f;
    private float scaleY = 2f;


    @NotNull
    @Override
    public String getDescribe() {
        return "view 从2倍 缩放偏移2倍";
    }

    @NotNull
    @Override
    public String getResetBtnText() {
        return "缩放";
    }


    @Override
    public void initRunView(@NotNull View runView) {
        super.initRunView(runView);
        runView.setScaleX(2f);
        runView.setScaleY(2f);
    }

    @NotNull
    @Override
    public Tween clickTweenBtn(@NotNull View runView) {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.Companion.builder(runView)
                .to(new HashMap<String, Number>() {
                    {
                        put("scaleX", runView.getScaleX() + scaleX);
                        put("scaleY", runView.getScaleY() + scaleY);
                    }
                })
                .time(time)
                .setExpire(true)
                .start();
    }

    @Override
    public void clickActionBtn(@NotNull View runView) {
        RunAction.INSTANCE.runAction(runView, Action.INSTANCE.scaleBy(time, scaleX, scaleY));
    }

    @Override
    public void clickChainedBtn(@NotNull View runView) {
        new SequenceActionRunBuild(runView)
                .scaleBy(time, scaleX, scaleY)
                .start();
    }
}
