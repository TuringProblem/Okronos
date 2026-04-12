package org.rsmod.coroutine.suspension

import org.rsmod.coroutine.resume.ResumeCondition
import kotlin.coroutines.Continuation

public data class GameCoroutineSuspension<T>(
    public val continuation: Continuation<T>,
    public val condition: ResumeCondition<T>,
)
