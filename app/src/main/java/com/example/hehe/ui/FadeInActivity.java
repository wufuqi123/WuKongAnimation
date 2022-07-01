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

public class FadeInActivity extends BaseAminActivity {

    private long time = 1000L;


    @NotNull
    @Override
    public String getDescribe() {
        return "view 透明度从0执行动画到 1";
    }

    @NotNull
    @Override
    public String getResetBtnText() {
        return "alpha";
    }


    @Override
    public void initRunView(@NotNull View runView) {
        super.initRunView(runView);
        runView.setAlpha(0f);
    }

    @NotNull
    @Override
    public Tween clickTweenBtn(@NotNull View runView) {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.Companion.builder(runView)
                .to(new HashMap<String, Number>() {
                    {
                        put("alpha", 1);
                    }
                })
                .time(time)
                .setExpire(true)
                .start();
    }

    @Override
    public void clickActionBtn(@NotNull View runView) {
        RunAction.INSTANCE.runAction(runView, Action.INSTANCE.fadeIn(time));
    }

    @Override
    public void clickChainedBtn(@NotNull View runView) {
        new SequenceActionRunBuild(runView)
                .fadeIn(time)
                .start();
    }
}
