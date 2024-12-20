package com.example.vinsenstory.utils

import android.net.Uri
import java.io.File

fun Uri.toFile(): File {
    return File(this.path ?: throw IllegalArgumentException("Invalid URI"))
}
