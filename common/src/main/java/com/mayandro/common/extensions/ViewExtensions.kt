package com.mayandro.common.extensions

import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView

fun ImageView.rotate(){
    val rotateAnimation = RotateAnimation(
        0f, 360f,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f

    )
    rotateAnimation.duration = 3000
    rotateAnimation.repeatCount = Animation.INFINITE
    this.startAnimation(rotateAnimation)
}
