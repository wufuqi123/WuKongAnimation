```
    一个优雅的代码动画库。支持直接函数调用和链式调用。内部封装Tween来作为动画内核。
```

#### Demo展示

##### [Demo apk下载](https://github.com/wufuqi123/WuKongAnimation/raw/main/apk/app-release.apk)
1. github Demo apk 二维码

    ![下载二维码](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/img/github_apk.png)

1. 国内 Demo apk 二维码

    ![下载二维码](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/img/QRCode_258.png)


#### 当前介绍kotlin使用方式
##### [kotlin使用方式](https://github.com/wufuqi123/WuKongAnimation/README.md)
[java使用方式](https://github.com/wufuqi123/WuKongAnimation/README_JAVA.md)


#### 基础功能
1. 添加依赖
    ``` 
        implementation 'io.github.wufuqi123:WuKongAnimation:1.0.0'
    ```

2. 初始化SDK
    ``` java
        ActionManager.init(mApplication) // 尽可能早，推荐在Application中初始化
    ```

3. runAction方式使用动画 [java使用方式](https://github.com/wufuqi123/WuKongAnimation/README_JAVA.md)
    ``` java
        //当前为kotlin代码
        view.runAction(Action.fadeIn(time))//执行渐入动画
    ```

4. 链式动画 [java使用方式](https://github.com/wufuqi123/WuKongAnimation/README_JAVA.md)
     ``` java
        //当前为kotlin代码
        //执行渐入动画
        view.createAction()
            .fadeIn(time)
            .start()
    ```

5. Tween 动画 [java使用方式](https://github.com/wufuqi123/WuKongAnimation/README_JAVA.md)
    ``` java
        //当前为kotlin代码
        //执行渐入动画
        //当前动画不会重复使用建议调用 setExpire(true)
        TweenManager.builder(runView)
            .to(mutableMapOf("alpha" to 1))
            .time(time)
            .setExpire(true)
            .start()
    ```

#### 动画使用
    接下来介绍 <链式动画> 和 <action动画> 的使用方式。
##### [Tween动画](https://github.com/wufuqi123/WuKongAnimation/README_TWEEN.md)不在当前文档讲述。

#### 透明度渐变动画

#### ![fade](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/fade.gif)

1. 链式使用方式 [java使用方式](https://github.com/wufuqi123/WuKongAnimation/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        //执行渐入动画
        view.createAction()
            .fadeIn(time)
            .start()

        
        //执行渐入动画
        view.createAction()
            .fadeOut(time)
            .start()

        //指定透明度
        view.createAction()
            .fadeTo(time,0f) //透明度传入 0-1
            .start()
    ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


2. runAction使用方式
    ``` java
        //当前为kotlin代码
        //执行渐入动画
        view.runAction(Action.fadeIn(time))

        //执行渐出动画
        view.runAction(Action.fadeOut(time))

        //指定透明度
        view.runAction(Action.fadeTo(time,0f))//透明度传入 0-1
     ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


#### 位移动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/move.gif)

1. 链式使用方式

     ``` java
        //当前为kotlin代码
        //位移到指定位置
        view.createAction()
            .moveTo(time,x,y)
            .start()

        
        //根据当前位置，位移偏移
        view.createAction()
            .moveBy(time,x,y)
            .start()

    ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```
    

2. runAction使用方式
    ``` java
        //当前为kotlin代码
        //位移到指定位置
        view.runAction(Action.moveTo(time,x,y))

        //根据当前位置，位移偏移
        view.runAction(Action.moveBy(time,x,y))

     ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


#### 缩放动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/scale.gif)

1. 链式使用方式

     ``` java
        //当前为kotlin代码
        //缩放到指定大小
        view.createAction()
            .scaleTo(time,x,y)
            .start()

        
        //根据当前大小，大小偏移
        view.createAction()
            .scaleBy(time,x,y)
            .start()

    ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


2. runAction使用方式
    ``` java
        //当前为kotlin代码
        //缩放到指定大小
        view.runAction(Action.scaleTo(time,x,y))

        //根据当前大小，大小偏移
        view.runAction(Action.scaleBy(time,x,y))

     ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


#### 旋转动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/rotate.gif)


1. 链式使用方式

     ``` java
        //当前为kotlin代码
        //旋转到指定角度
        view.createAction()
            .rotateTo(time,rotation)
            .start()

        
        //根据当前角度，角度偏移
        view.createAction()
            .rotateBy(time,rotation)
            .start()

    ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


2. runAction使用方式
    ``` java
        //当前为kotlin代码
        //旋转到指定角度
        view.runAction(Action.rotateTo(time,rotation))

        //根据当前角度，角度偏移
        view.runAction(Action.rotateBy(time,rotation))

     ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


#### 旋转X坐标动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/rotateX.gif)


1. 链式使用方式

     ``` java
        //当前为kotlin代码
        //旋转到指定角度
        view.createAction()
            .rotateXTo(time,rotateX)
            .start()

        
        //根据当前角度，角度偏移
        view.createAction()
            .rotateXBy(time,rotateX)
            .start()

    ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


2. runAction使用方式
    ``` java
        //当前为kotlin代码
        //旋转到指定角度
        view.runAction(Action.rotateXTo(time,rotateX))

        //根据当前角度，角度偏移
        view.runAction(Action.rotateXBy(time,rotateX))

     ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


#### 旋转Y坐标动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/rotateY.gif)


1. 链式使用方式

     ``` java
        //当前为kotlin代码
        //旋转到指定角度
        view.createAction()
            .rotateYTo(time,rotateY)
            .start()

        
        //根据当前角度，角度偏移
        view.createAction()
            .rotateYBy(time,rotateY)
            .start()

    ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


2. runAction使用方式
    ``` java
        //当前为kotlin代码
        //旋转到指定角度
        view.runAction(Action.rotateYTo(time,rotateY))

        //根据当前角度，角度偏移
        view.runAction(Action.rotateYBy(time,rotateY))

     ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


#### 顺序动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/sequence.gif)


1. 链式使用方式

     ``` java
        //当前为kotlin代码
        //透明度渐入后渐出
        view.createSequenceAction()
            .fadeIn(time)
            .fadeOut(time)
            .start()

    ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```

2. runAction使用方式
    ``` java
        //当前为kotlin代码
        //透明度渐入后渐出
        runView.runAction(Action.sequence(Action.fadeIn(time), Action.fadeOut(time)))

     ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


#### 同步动画

#### ![move](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/spawn.gif)


1. 链式使用方式

     ``` java
        //当前为kotlin代码
        //同时执行位移和旋转动画
        view.createSpawnAction()
            .moveBy(time, x, y)
            .rotateBy(time, rotation)
            .start()

    ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```

2. runAction使用方式
    ``` java
        //当前为kotlin代码
        //同时执行位移和旋转动画
        runView.runAction(
            Action.spawn(
                Action.moveBy(time, x, y),
                Action.rotateBy(time, rotation)
            )
        )

     ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


#### 使用回调

1. 链式使用方式

     ``` java
        //当前为kotlin代码
        //渐入后  执行打印
        view.createSequenceAction()
            .fadeIn(time)
            .callFunc {
                Log.i(this::class.java.name, "fadeIn 执行完成")
            }
            .start()

    ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


2. runAction使用方式
    ``` java
        //当前为kotlin代码
        //渐入后  执行打印
        runView.runAction(Action.sequence(Action.fadeIn(time), Action.callFunc {
            Log.i(this::class.java.name, "fadeIn 执行完成")
        }))

     ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


#### wait调用

1. 链式使用方式

     ``` java
        //当前为kotlin代码
        //等待1秒后执行渐入
        view.createSequenceAction()
            .wait(1000)
            .fadeIn(time)
            .start()

    ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


2. runAction使用方式
    ``` java
        //当前为kotlin代码
        //等待1秒后执行渐入
        runView.runAction(Action.sequence(Action.wait(1000),Action.fadeIn(time)))

     ```

    停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```
