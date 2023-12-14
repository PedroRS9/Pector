package es.ulpgc.pamn.pector.extensions

import kotlinx.coroutines.delay

suspend fun fillXpBar(beginXp: Int, endXp: Int, updateProgress: (Int) -> Unit) {
    // we iterate from starting xp to ending xp
    for (i in beginXp..endXp) {
        println(i)
        updateProgress(i)
        delay(100)
    }
}