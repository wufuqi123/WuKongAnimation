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

public class FadeToActivity extends BaseAminActivity {

    private long time = 1000L;
    private float alpha = 0.5f;


    @NotNull
    @Override
    public String getDescribe() {
        return "view 透明度执行动画到 " + alpha;
    }

    @NotNull
    @Override
    public String getResetBtnText() {
        return "alpha";
    }

    @NotNull
    @Override
    public Tween clickTweenBtn(@NotNull View runView) {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.Companion.builder(runView)
                .to(new HashMap<String, Number>() {
                    {
                        put("alpha", alpha);
                    }
                })
                .time(time)
                .setExpire(true)
                .start();
    }

    @Override
    public void clickActionBtn(@NotNull View runView) {
        RunAction.INSTANCE.runAction(runView, Action.INSTANCE.fadeTo(time, alpha));
    }

    @Override
    public void clickChainedBtn(@NotNull View runView) {
        new SequenceActionRunBuild(runView)
                .fadeTo(time, alpha)
                .start();
    }
}
