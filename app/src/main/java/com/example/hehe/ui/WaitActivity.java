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

public class WaitActivity extends BaseAminActivity {

    private long time = 1000L;

    private long wait = 1000L;


    @NotNull
    @Override
    public String getDescribe() {
        return "view 等待"+wait+"毫秒后执行动画  fadeIn";
    }

    @NotNull
    @Override
    public String getResetBtnText() {
        return "顺序动画";
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
                        put("alpha", 1f);
                    }
                })
                .time(time)
                .setExpire(true)
                .delay(wait)
                .start();
    }

    @Override
    public void clickActionBtn(@NotNull View runView) {
        RunAction.INSTANCE.runAction(runView, Action.INSTANCE.sequence(Action.INSTANCE.wait(time),  Action.INSTANCE.fadeIn(time)));
    }

    @Override
    public void clickChainedBtn(@NotNull View runView) {
        new SequenceActionRunBuild(runView)
                .wait(wait)
                .fadeIn(time)
                .start();
    }
}
