package com.example.hehe.ui;


import android.util.Log;
import android.view.View;

import com.example.hehe.BaseAminActivity;
import com.wukonganimation.action.Action;
import com.wukonganimation.action.RunAction;
import com.wukonganimation.action.chained.SequenceActionRunBuild;
import com.wukonganimation.tween.Tween;
import com.wukonganimation.tween.TweenManager;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class CallbackActivity extends BaseAminActivity {

    private long time = 1000L;



    @NotNull
    @Override
    public String getDescribe() {
        return "view fadeIn 后 打印数据";
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
                .on(TweenManager.EVENT_END, objects -> {
                    Log.i(CallbackActivity.this.getClass().getName(),"fadeIn 执行完成");
                    return null;
                })
                .start();
    }

    @Override
    public void clickActionBtn(@NotNull View runView) {
        RunAction.INSTANCE.runAction(runView, Action.INSTANCE.sequence(Action.INSTANCE.fadeIn(time), Action.INSTANCE.callFunc(() -> {
            Log.i(CallbackActivity.this.getClass().getName(),"fadeIn 执行完成");
            return null;
        })));
    }

    @Override
    public void clickChainedBtn(@NotNull View runView) {
        new SequenceActionRunBuild(runView)
                .fadeIn(time)
                .callFunc(() -> {
                    Log.i(CallbackActivity.this.getClass().getName(),"fadeIn 执行完成");
                    return null;
                })
                .start();
    }
}
