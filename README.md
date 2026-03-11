```
    一个优雅的代码动画库。支持直接函数调用和链式调用。内部封装Tween来作为动画内核。
```

#### 文档导航

- [Kotlin 使用文档（当前页）](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
- [Java 使用文档](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
- [Tween 使用文档](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_TWEEN.md)
- [Tween 性能对比说明](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_TWEEN.md#tween-性能对比说明)

#### Demo展示

##### [github Demo apk下载](https://github.com/wufuqi123/WuKongAnimation/raw/main/apk/app-release.apk)   [码云 Demo apk下载](https://gitee.com/wu_fuqi/WuKongAnimation/raw/master/apk/app-release.apk)
1. github Demo apk 二维码

    ![下载二维码](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/img/github_apk.png)

1. 国内 Demo apk 二维码

    ![下载二维码](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/img/QRCode_258.png)

#### 图片和动画看不到
##### [码云链接请进入](https://gitee.com/wu_fuqi/WuKongAnimation/blob/master/README.md)

#### 性能对比说明（摘要）

如果你要看性能，请优先看 `Demo` 里的原生动画 vs WuKong 页面，并结合 [`README_TWEEN.md`](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_TWEEN.md) 的说明自己在本地复测。

参数参考：
- `scenario`：测试场景，当前有 `moveX`、`alpha`、`scale`、`rotation`、`complex-combo`
- `duration(ms)`：单段动画时长
- `iterations`：预热 1 次后，正式统计的轮数
- `view count`：同一轮里同时执行动画的 view 数量

当前测试环境：
- Android 16
- 存储 6G
- 内存 512MB
- CPU cores 14

说明：
- 对比页中的 WuKong 一侧，口径是 **Tween 类直接运行**，例如 `TweenManager.builder(...)` 或 `Tween(...)`
- `createAction()`、`createSequenceAction()`、`createSpawnAction()` 以及 `view.wukong { ... }` 这种链式 / Kotlin 语法糖写法，上面还有一层封装，通常达不到同样的数据
- 所以这组数字更适合作为 **Tween 内核直接运行参考**，不是高层封装写法的最终成绩

更完整的性能对比说明见：[`README_TWEEN.md`](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_TWEEN.md#tween-性能对比说明)

#### 当前介绍kotlin使用方式
##### [kotlin使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
[java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)


#### 基础功能
1. 添加依赖

    请在 build.gradle 下添加依赖。

    ``` 
        implementation 'cn.wufuqi:WuKongAnimation:1.1.0'
    ```


2. 设置jdk8或更高版本

    因为本sdk使用了jdk8才能使用的 Lambda 表达式，所以要在 build.gradle 下面配置jdk8或以上版本。

    ``` 
    android {
        ....

        compileOptions {
            sourceCompatibility JavaVersion.VERSION_1_8
            targetCompatibility JavaVersion.VERSION_1_8
        }
        
    }
    ```

3. 初始化SDK
    ``` java
        ActionManager.init(mApplication) // 尽可能早，推荐在Application中初始化
    ```

4. view.wukong 方式使用动画
    ``` java
        //当前为kotlin代码
        //默认顺序执行，并且会自动 start()
        view.wukong {
            fadeIn(time)
        }
    ```

5. runAction方式使用动画  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
    ``` java
        //当前为kotlin代码
        view.runAction(Action.fadeIn(time))//执行渐入动画
    ```

6. 链式动画  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
     ``` java
        //当前为kotlin代码
        //执行渐入动画
        view.createAction()
            .fadeIn(time)
            .start()
    ```

7. Tween 动画  [Tween动画请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_TWEEN.md)
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
    接下来介绍 <链式动画> 和 <action动画> <WuKong Kotlin DSL>的使用方式。
##### [Tween动画](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_TWEEN.md)不在当前文档讲述。

##### WuKong Kotlin DSL

使用时建议导入：`com.wukonganimation.action.extend.*`

```kotlin
view.wukong {
    fadeIn(300)

    // 这里是并行执行
    spawn {
        fadeOut(300)
        rotateBy(300, 180f)

        // 如果这里想要“里面顺序执行”，请用 sequence
        sequence {
            moveTo(300, 100f, 100f)
            scaleTo(300, 1.2f, 1.2f)
            wait(1000)
            callFunc {
                // 回调
            }
        }
    }

    rotateBy(300, 360f)
    callFunc {
        // 整段顺序动画结束后的回调节点
    }
}
```

说明：
- `view.wukong {}` 顶层默认是顺序执行，并且会自动 `start()`。
- `spawn {}` 内部是并行执行。
- `sequence {}` 内部是顺序执行。
- 如果你需要顶层并行，可以使用 `view.wukongSpawn { ... }`。

#### 透明度渐变动画

#### ![fade](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/gif/fade.gif)

1. WuKong Kotlin DSL使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        //执行渐入动画
        view.wukong {
             fadeIn(time)
        }
        
        //执行渐出动画
        view.wukong {
             fadeOut(time)
        }
   
        //指定透明度
        view.wukong {
             fadeTo(time,0f) //透明度传入 0-1
        }

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopWukong()
        //或者
        view.stopAction()
    ```

1. 链式使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        //执行渐入动画
        view.createAction()
            .fadeIn(time)
            .start()

        
        //执行渐出动画
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


2. runAction使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
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



1. WuKong Kotlin DSL使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        //位移到指定位置
        view.wukong {
             moveTo(time,x,y)
        }
        
        //根据当前位置，位移偏移
        view.wukong {
            moveBy(time,x,y)
        }

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopWukong()
        //或者
        view.stopAction()
    ```


2. 链式使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

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
    

3. runAction使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
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


1. WuKong Kotlin DSL使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        //缩放到指定大小
        view.wukong {
            scaleTo(time,x,y)
        }
        
        //根据当前大小，大小偏移
        view.wukong {
            scaleeBy(time,x,y)
        }

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopWukong()
        //或者
        view.stopAction()
    ```

1. 链式使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

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


2. runAction使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
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



1. WuKong Kotlin DSL使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        //旋转到指定角度
        view.wukong {
            rotateTo(time,rotation)
        }
        
        //根据当前角度，角度偏移
        view.wukong {
            rotateBy(time,rotation)
        }

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopWukong()
        //或者
        view.stopAction()
    ```


2. 链式使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

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


3. runAction使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
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



1. WuKong Kotlin DSL使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        //旋转到指定角度
        view.wukong {
            rotateXTo(time,rotateX)
        }
        
        //根据当前角度，角度偏移
        view.wukong {
            rotateXBy(time,rotateX)
        }

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopWukong()
        //或者
        view.stopAction()
    ```


2. 链式使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

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


3. runAction使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
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





1. WuKong Kotlin DSL使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        //旋转到指定角度
        view.wukong {
            rotateYTo(time,rotateY)
        }
        
        //根据当前角度，角度偏移
        view.wukong {
            rotateYBy(time,rotateY)
        }

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopWukong()
        //或者
        view.stopAction()
    ```


2. 链式使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

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


3. runAction使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
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



1. WuKong Kotlin DSL使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        //透明度渐入后渐出
        view.wukong {
            fadeIn(time)
            fadeOut(time)
        }
   
        //或者
        view.wukongSequence{
            fadeIn(time)
            fadeOut(time)
        }

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopWukong()
        //或者
        view.stopAction()
    ```



2. 链式使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

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

3. runAction使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
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


1. WuKong Kotlin DSL使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        //同时执行位移和旋转动画
        view.wukongSpawn {
            moveBy(time, x, y)
            rotateBy(time, rotation)
        }

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopWukong()
        //或者
        view.stopAction()
    ```


2. 链式使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

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

3. runAction使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
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

    停止动画  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```


#### 使用回调



1. WuKong Kotlin DSL使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        view.wukong {
            fadeIn(time)
            callFunc {
                Log.i(this::class.java.name, "fadeIn 执行完成")
            }
        }

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopWukong()
        //或者
        view.stopAction()
    ```


2. 链式使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

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


3. runAction使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
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



1. WuKong Kotlin DSL使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        view.wukong {
            wait(1000)
            fadeIn(time)
        }

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopWukong()
        //或者
        view.stopAction()
    ```


2. 链式使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

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


3. runAction使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
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

#### 复杂动画

1. WuKong Kotlin DSL使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        view.wukong {
            //立刻执行渐入动画
            fadeIn(100)
            //100毫秒后执行
            spawn {
                //fadeOut 和 sequence 是并行执行的
                //总时间100毫秒后执行fadeOut
                fadeOut(120)
                sequence {
                    //总时间100毫秒后执行moveTo
                    moveTo(200, 10f, 20f)
                    //总时间300毫秒后执行scaleTo
                    scaleTo(220, 2f, 3f)
                    //总时间520毫秒后执行
                    wait(300)
                    //总时间820毫秒后执行回调
                    callFunc {
                        //820毫秒后打印 callback invoked
                        Log.i("wukong", "callback invoked")
                    }
                }
            }
            //总时间820毫秒后执行rotateBy
            rotateBy(180, 90f)
            //总时间1000毫秒后执行回调
            callFunc {
                //1000毫秒后打印 animation completed
                Log.i("wukong", "animation completed")
            }
        }

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopWukong()
        //或者
        view.stopAction()
    ```


2. 链式使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        view.createAction()
            .fadeIn(100)
            .spawn(
                SpawnActionBuild()
                    .fadeOut(120)
                    .sequence(
                        SequenceActionBuild()
                            .moveTo(200, 10f, 20f)
                            .scaleTo(220, 2f, 3f)
                            .wait(300)
                            .callFunc {
                                Log.i("wukong", "callback invoked")
                            }
                    )
            )
            .rotateBy(180, 90f)
            .callFunc {
                Log.i("wukong", "animation completed")
            }
            .start()

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopAction()
    ```



#### 使用缓动函数

缓动函数计算引用 [tween.js](http://tweenjs.github.io/tween.js/) 的缓动函数。

// adapted from https://github.com/tweenjs/tween.js/blob/9f8c56c4d5856a970b45895bb58cd9d1d56cf3ea/src/Easing.ts

[缓动效果](http://tweenjs.github.io/tween.js/examples/03_graphs.html)

默认的缓动函数为 Easing.linear() 



1. WuKong Kotlin DSL使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        view.wukong {
           fadeIn(time,Easing.linear()) //使用线性缓动函数
        }

    ```

   停止动画
    ``` java
        //当前为kotlin代码
        view.stopWukong()
        //或者
        view.stopAction()
    ```


2. 链式使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)

     ``` java
        //当前为kotlin代码
        view.createSequenceAction()
            .fadeIn(time,Easing.linear()) //使用线性缓动函数
            .start()

    ```



3. runAction使用方式  [java使用方式请进入](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
    ``` java
        //当前为kotlin代码
        runView.runAction(Action.fadeIn(time,Easing.linear())) //使用线性缓动函数

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


#### 检测动画是否正在运行



tween检测是否运行

``` java
    //kotlin 写法
    tween.isRunning
    
    //java写法
    tween.isRunning()
```

action检测是否运行

``` java
    //kotlin 写法
    //只检测单个控件
    view.isRunningAction()
    //检测整个界面
    activity.isRunningAction()
    
    //java写法
    //只检测单个控件
    RunAction.isRunning(view)
    //检测整个界面
    RunAction.isRunning(activity)
```
