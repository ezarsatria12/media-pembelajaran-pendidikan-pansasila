package com.fikri.mediapembelajaranpendidikanpansasila.utils

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize

// 1. Penyimpan Status (Ditambah itemSize untuk merekam ukuran asli)
internal class DragTargetInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
    var dataToDrop by mutableStateOf<Any?>(null)
    var itemSize by mutableStateOf(IntSize.Zero) // <--- PENYELAMAT KITA
}

internal val LocalDragTargetInfo = compositionLocalOf { DragTargetInfo() }

// 2. Wadah Utama
@Composable
fun DraggableScreen(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val state = remember { DragTargetInfo() }
    CompositionLocalProvider(
        LocalDragTargetInfo provides state
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            content()

            // Menggambar bayangan
            if (state.isDragging) {
                var targetSize by remember { mutableStateOf(IntSize.Zero) }
                Box(modifier = Modifier
                    .graphicsLayer {
                        val offset = (state.dragPosition + state.dragOffset)
                        alpha = if (targetSize == IntSize.Zero) 0f else 0.9f
                        translationX = offset.x.minus(targetSize.width / 2)
                        translationY = offset.y.minus(targetSize.height / 2)
                    }
                    .onGloballyPositioned { targetSize = it.size }
                ) {
                    // KUNCI PERBAIKAN: Bungkus komponen dengan ukuran aslinya yang dikonversi dari Pixel ke Dp
                    with(LocalDensity.current) {
                        Box(modifier = Modifier.size(
                            width = state.itemSize.width.toDp(),
                            height = state.itemSize.height.toDp()
                        )) {
                            state.draggableComposable?.invoke()
                        }
                    }
                }
            }
        }
    }
}

// 3. Komponen yang BISA DITARIK
@Composable
fun <T> DragTarget(
    modifier: Modifier = Modifier,
    dataToDrop: T,
    content: @Composable (() -> Unit)
) {
    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    var currentSize by remember { mutableStateOf(IntSize.Zero) } // Rekam ukuran saat komponen dibuat
    val currentState = LocalDragTargetInfo.current

    Box(modifier = modifier
        .onGloballyPositioned {
            currentPosition = it.positionInWindow()
            currentSize = it.size // Simpan ukuran aslinya (dalam Pixel)
        }
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    currentState.dataToDrop = dataToDrop
                    currentState.isDragging = true
                    currentState.dragPosition = currentPosition + offset
                    currentState.draggableComposable = content
                    currentState.itemSize = currentSize // Kirim ukurannya ke mesin pusat
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)
                },
                onDragEnd = {
                    currentState.isDragging = false
                    currentState.dragOffset = Offset.Zero
                },
                onDragCancel = {
                    currentState.dragOffset = Offset.Zero
                    currentState.isDragging = false
                }
            )
        }) {
        Box(modifier = Modifier.graphicsLayer { alpha = if (currentState.isDragging && currentState.dataToDrop == dataToDrop) 0f else 1f }) {
            content()
        }
    }
}

// 4. Komponen LUBANG TARGET (Tetap sama)
@Composable
fun <T> DropTarget(
    modifier: Modifier,
    onDrop: (T) -> Unit,
    content: @Composable (BoxScope.(isHovered: Boolean) -> Unit)
) {
    val dragInfo = LocalDragTargetInfo.current
    val dragPosition = dragInfo.dragPosition
    val dragOffset = dragInfo.dragOffset
    var isHovered by remember { mutableStateOf(false) }

    Box(modifier = modifier.onGloballyPositioned { coordinates ->
        coordinates.boundsInWindow().let { rect ->
            isHovered = rect.contains(dragPosition + dragOffset)
        }
    }) {
        if (isHovered && !dragInfo.isDragging && dragInfo.dataToDrop != null) {
            val droppedData = dragInfo.dataToDrop as? T
            if (droppedData != null) {
                LaunchedEffect(droppedData) {
                    onDrop(droppedData)
                    dragInfo.dataToDrop = null
                }
            }
        }
        content(isHovered)
    }
}