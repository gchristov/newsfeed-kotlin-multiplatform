package com.gchristov.newsfeed.android.common.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class RealNavigator(private val context: Context) : Navigator {
    override fun openPost(postId: String) {
        "news://post?postId=$postId".openAsDeepLink(context)
    }
}

private fun String.openAsDeepLink(context: Context) {
    try {
        val link = Uri.parse(this)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = link
        }
        context.startActivity(intent)
    } catch (error: Throwable) {
        error.printStackTrace()
        Toast.makeText(context, error.localizedMessage, Toast.LENGTH_LONG).show()
    }
}