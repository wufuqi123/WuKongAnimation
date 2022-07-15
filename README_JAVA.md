```
    一个优雅的代码动画库。支持直接函数调用和链式调用。内部封装Tween来作为动画内核。
```

#### 图片和动画看不到
##### [码云链接请进入](https://gitee.com/wu_fuqi/WuKongAnimation/blob/master/README_JAVA.md)


#### 当前介绍java使用方式
##### [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
[java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)


#### 动画使用
    接下来介绍 <链式动画> 和 <action动画> 的使用方式。
##### [Tween动画](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_TWEEN.md)不在当前文档讲述。

#### 透明度渐变动画

#### ![fade](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/fade.gif)

1. 链式使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)

     ``` java
        //当前为java代码
        //执行渐入动画
        new SequenceActionRunBuild(view)
            .fadeIn(time)
            .start()

        
        //执行渐入动画
        new SequenceActionRunBuild(view)
            .fadeOut(time)
            .start()

        //指定透明度
        new SequenceActionRunBuild(view)
            .fadeTo(time,0f) //透明度传入 0-1
            .start()
    ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


2. runAction使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
    ``` java
        //当前为java代码
        //执行渐入动画
        RunAction.INSTANCE.runAction(view, Action.fadeIn(time))

        //执行渐出动画
        RunAction.INSTANCE.runAction(view, Action.fadeOut(time))

        //指定透明度
        RunAction.INSTANCE.runAction(view, Action.fadeTo(time,0f))//透明度传入 0-1
     ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


#### 位移动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/move.gif)

1. 链式使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)

     ``` java
        //当前为java代码
        //位移到指定位置
        new SequenceActionRunBuild(view)
            .moveTo(time,x,y)
            .start()

        
        //根据当前位置，位移偏移
        new SequenceActionRunBuild(view)
            .moveBy(time,x,y)
            .start()

    ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```
    

2. runAction使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
    ``` java
        //当前为java代码
        //位移到指定位置
        RunAction.INSTANCE.runAction(view, Action.moveTo(time,x,y))

        //根据当前位置，位移偏移
        RunAction.INSTANCE.runAction(view, Action.moveBy(time,x,y))

     ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


#### 缩放动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/scale.gif)

1. 链式使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)

     ``` java
        //当前为java代码
        //缩放到指定大小
        new SequenceActionRunBuild(view)
            .scaleTo(time,x,y)
            .start()

        
        //根据当前大小，大小偏移
        new SequenceActionRunBuild(view)
            .scaleBy(time,x,y)
            .start()

    ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


2. runAction使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
    ``` java
        //当前为java代码
        //缩放到指定大小
        RunAction.INSTANCE.runAction(view, Action.scaleTo(time,x,y))

        //根据当前大小，大小偏移
        RunAction.INSTANCE.runAction(view, Action.scaleBy(time,x,y))

     ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


#### 旋转动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/rotate.gif)


1. 链式使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)

     ``` java
        //当前为java代码
        //旋转到指定角度
        new SequenceActionRunBuild(view)
            .rotateTo(time,rotation)
            .start()

        
        //根据当前角度，角度偏移
        new SequenceActionRunBuild(view)
            .rotateBy(time,rotation)
            .start()

    ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


2. runAction使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
    ``` java
        //当前为java代码
        //旋转到指定角度
        RunAction.INSTANCE.runAction(view, Action.rotateTo(time,rotation))

        //根据当前角度，角度偏移
        RunAction.INSTANCE.runAction(view, Action.rotateBy(time,rotation))

     ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


#### 旋转X坐标动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/rotateX.gif)


1. 链式使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)

     ``` java
        //当前为java代码
        //旋转到指定角度
        new SequenceActionRunBuild(view)
            .rotateXTo(time,rotateX)
            .start()

        
        //根据当前角度，角度偏移
        new SequenceActionRunBuild(view)
            .rotateXBy(time,rotateX)
            .start()

    ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


2. runAction使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
    ``` java
        //当前为java代码
        //旋转到指定角度
        RunAction.INSTANCE.runAction(view, Action.rotateXTo(time,rotateX))

        //根据当前角度，角度偏移
        RunAction.INSTANCE.runAction(view, Action.rotateXBy(time,rotateX))

     ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


#### 旋转Y坐标动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/rotateY.gif)


1. 链式使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)

     ``` java
        //当前为java代码
        //旋转到指定角度
        new SequenceActionRunBuild(view)
            .rotateYTo(time,rotateY)
            .start()

        
        //根据当前角度，角度偏移
        new SequenceActionRunBuild(view)
            .rotateYBy(time,rotateY)
            .start()

    ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


2. runAction使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
    ``` java
        //当前为java代码
        //旋转到指定角度
        RunAction.INSTANCE.runAction(view, Action.rotateYTo(time,rotateY))

        //根据当前角度，角度偏移
        RunAction.INSTANCE.runAction(view, Action.rotateYBy(time,rotateY))

     ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


#### 顺序动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/sequence.gif)


1. 链式使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)

     ``` java
        //当前为java代码
        //透明度渐入后渐出
        new SequenceActionRunBuild(view)
            .fadeIn(time)
            .fadeOut(time)
            .start()

    ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```

2. runAction使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
    ``` java
        //当前为java代码
        //透明度渐入后渐出
        RunAction.INSTANCE.runAction(view, Action.sequence(Action.fadeIn(time), Action.fadeOut(time)))

     ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


#### 同步动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/spawn.gif)


1. 链式使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)

     ``` java
        //当前为java代码
        //同时执行位移和旋转动画
        new SpawnActionRunBuild(view)
            .moveBy(time, x, y)
            .rotateBy(time, rotation)
            .start()

    ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```

2. runAction使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
    ``` java
        //当前为java代码
        //同时执行位移和旋转动画
        RunAction.INSTANCE.runAction(view, 
            Action.spawn(
                Action.moveBy(time, x, y),
                Action.rotateBy(time, rotation)
            )
        )

     ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


#### 使用回调

1. 链式使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)

     ``` java
        //当前为java代码
        //渐入后  执行打印
        new SequenceActionRunBuild(view)
            .fadeIn(time)
            .callFunc(() -> {
                Log.i(CallbackActivity.this.getClass().getName(),"fadeIn 执行完成");
                return null;
            })
            .start()

    ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


2. runAction使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
    ``` java
        //当前为java代码
        //渐入后  执行打印
        RunAction.INSTANCE.runAction(runView, Action.INSTANCE.sequence(Action.INSTANCE.fadeIn(time), Action.INSTANCE.callFunc(() -> {
            Log.i(CallbackActivity.this.getClass().getName(),"fadeIn 执行完成");
            return null;
        })));

     ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


#### wait调用

1. 链式使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)

     ``` java
        //当前为java代码
        //等待1秒后执行渐入
        new SequenceActionRunBuild(view)
            .wait(1000)
            .fadeIn(time)
            .start()

    ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```


2. runAction使用方式   [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
    ``` java
        //当前为java代码
        //等待1秒后执行渐入
        RunAction.INSTANCE.runAction(view, Action.sequence(Action.wait(1000),Action.fadeIn(time)))

     ```

    停止动画
    ``` java
        //当前为java代码
        RunAction.INSTANCE.stopAction(view)
    ```



#### 使用缓动函数

缓动函数计算引用 [tween.js](http://tweenjs.github.io/tween.js/) 的缓动函数。

// adapted from https://github.com/tweenjs/tween.js/blob/9f8c56c4d5856a970b45895bb58cd9d1d56cf3ea/src/Easing.ts

[缓动效果](http://tweenjs.github.io/tween.js/examples/03_graphs.html)

默认的缓动函数为 Easing.linear() 

1. 链式使用方式  [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)

     ``` java
        //当前为java代码
        new SequenceActionRunBuild(view)
            .fadeIn(time,Easing.linear()) //使用线性缓动函数
            .start()

    ```



2. runAction使用方式  [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
    ``` java
        //当前为java代码
        RunAction.INSTANCE.runAction(view, Action.fadeIn(time,Easing.linear())) //使用线性缓动函数

     ```





#### 设置全局速度


设置全部动画的速度为1倍


``` java

    //设置全部动画的速度为1倍
    TweenManager.speed = 1.0

```


#### 设置全局暂停动画


设置动画全局暂停


``` java

    //设置动画全局暂停
    TweenManager.pause()

```


#### 设置全局恢复动画


设置动画全局恢复


``` java

    //设置动画全局恢复
    TweenManager.pause()

```


