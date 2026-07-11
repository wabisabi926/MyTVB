package com.tutu.myblbl.feature.player.danmaku

import com.tutu.myblbl.feature.player.danmaku.model.RenderSnapshot
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RenderSnapshotTest {

    @Test
    fun writerCannotReuseSnapshotWhileMainThreadHoldsReadLease() {
        val snapshot = RenderSnapshot()

        assertTrue(snapshot.tryAcquireRead())
        assertFalse(snapshot.tryBeginWrite())

        snapshot.releaseRead()
        assertTrue(snapshot.tryBeginWrite())
        snapshot.endWrite()
    }

    @Test
    fun readerCannotObserveSnapshotDuringPublication() {
        val snapshot = RenderSnapshot()

        assertTrue(snapshot.tryBeginWrite())
        assertFalse(snapshot.tryAcquireRead())

        snapshot.endWrite()
        assertTrue(snapshot.tryAcquireRead())
        snapshot.releaseRead()
    }

    @Test
    fun cacheResultOnlyCommitsToCurrentActiveRenderingItem() {
        assertTrue(shouldApplyBlblCacheResult(3, 3, 3, rendering = true, active = true))
        assertFalse(shouldApplyBlblCacheResult(2, 3, 2, rendering = true, active = true))
        assertFalse(shouldApplyBlblCacheResult(3, 3, 2, rendering = true, active = true))
        assertFalse(shouldApplyBlblCacheResult(3, 3, 3, rendering = false, active = true))
        assertFalse(shouldApplyBlblCacheResult(3, 3, 3, rendering = true, active = false))
    }
}
