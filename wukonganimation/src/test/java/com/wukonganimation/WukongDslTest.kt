package com.wukonganimation

import com.wukonganimation.action.ActionData
import com.wukonganimation.action.extend.sequenceAction
import com.wukonganimation.action.extend.spawnAction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WukongDslTest {

    @Test
    fun sequenceAction_buildsSequentialTreeWithNestedSpawnAndSequence() {
        var callbackInvoked = false
        val action = sequenceAction {
            fadeIn(100)
            spawn {
                fadeOut(120)
                sequence {
                    moveTo(200, 10f, 20f)
                    scaleTo(220, 2f, 3f)
                    wait(300)
                    callFunc {
                        callbackInvoked = true
                    }
                }
            }
            rotateBy(180, 90f)
        }

        val root = buildSequenceRoot(action)
        val rootChildren = root.animationDataArray!!
        assertEquals(ActionData.sequence, root.type)
        assertEquals(3, rootChildren.size)

        val fadeIn = rootChildren[0].invoke()
        assertEquals(ActionData.fadeIn, fadeIn.type)
        assertEquals(100L, fadeIn.time)

        val spawn = rootChildren[1].invoke()
        assertEquals(ActionData.spawn, spawn.type)
        val spawnChildren = spawn.animationDataArray!!
        assertEquals(2, spawnChildren.size)
        assertEquals(ActionData.fadeOut, spawnChildren[0].invoke().type)

        val nestedSequence = spawnChildren[1].invoke()
        assertEquals(ActionData.sequence, nestedSequence.type)
        val nestedChildren = nestedSequence.animationDataArray!!
        assertEquals(4, nestedChildren.size)
        assertEquals(ActionData.moveTo, nestedChildren[0].invoke().type)
        assertEquals(ActionData.scaleTo, nestedChildren[1].invoke().type)

        val waitAction = nestedChildren[2].invoke()
        assertEquals(null, waitAction.type)
        assertEquals(300L, waitAction.time)

        val callback = nestedChildren[3].invoke()
        assertEquals(ActionData.callFunc, callback.type)

        val move = nestedChildren[0].invoke()
        assertEquals(10f, move.x)
        assertEquals(20f, move.y)

        val scale = nestedChildren[1].invoke()
        assertEquals(2f, scale.scaleX)
        assertEquals(3f, scale.scaleY)

        assertFalse(callbackInvoked)
        callback.callback?.invoke()
        assertTrue(callbackInvoked)

        val rotate = rootChildren[2].invoke()
        assertEquals(ActionData.rotateBy, rotate.type)
        assertEquals(180L, rotate.time)
        assertEquals(90f, rotate.rotation)
    }

    @Test
    fun spawnAction_buildsParallelRoot() {
        val action = spawnAction {
            moveBy(100, 5f, 6f)
            rotateBy(100, 45f)
        }

        val root = buildSpawnRoot(action)
        val children = root.animationDataArray!!
        assertEquals(ActionData.spawn, root.type)
        assertEquals(2, children.size)
        assertEquals(ActionData.moveBy, children[0].invoke().type)
        assertEquals(ActionData.rotateBy, children[1].invoke().type)
    }

    private fun buildSequenceRoot(action: com.wukonganimation.action.chained.SequenceActionBuild): ActionData {
        val root = ActionData()
        root.type = ActionData.sequence
        root.animationDataArray = action.animationDataArray
        return root
    }

    private fun buildSpawnRoot(action: com.wukonganimation.action.chained.SpawnActionBuild): ActionData {
        val root = ActionData()
        root.type = ActionData.spawn
        root.animationDataArray = action.animationDataArray
        return root
    }
}
