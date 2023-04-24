package be.mgx.util

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.CompletableFuture

class WindowAdapterImpl(
    private val completableFuture: CompletableFuture<Int>
): WindowAdapter() {
    override fun windowClosed(e: WindowEvent?) {
        super.windowClosed(e)
        completableFuture.complete(0)
    }
}