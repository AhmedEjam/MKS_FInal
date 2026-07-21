package com.ahmedyejam.mks.ui.quiz

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class QuizTimerEngine(
    private val scope: CoroutineScope,
    private val timerState: MutableStateFlow<TimerState>,
) {
    private var timerJob: Job? = null

    fun start(
        quizTimerSeconds: Int,
        questionTimerSeconds: Int,
        isCompleted: () -> Boolean,
        isAnswered: () -> Boolean,
        onTimeout: () -> Unit,
        onQuizFinished: () -> Unit,
    ) {
        if (quizTimerSeconds <= 0 && questionTimerSeconds <= 0) {
            stop()
            return
        }
        stop()
        timerJob = scope.launch {
            while (isActive) {
                delay(1000)
                if (isCompleted()) break

                var shouldSubmit = false
                var quizFinished = false

                timerState.update { state ->
                    var newQuizTimeLeft = state.quizTimeLeft
                    var newQuestionTimeLeft = state.questionTimeLeft

                    if (quizTimerSeconds > 0 && state.quizTimeLeft > 0) {
                        newQuizTimeLeft--
                        if (newQuizTimeLeft == 0) quizFinished = true
                    }

                    if (!isAnswered() && questionTimerSeconds > 0 && state.questionTimeLeft > 0) {
                        newQuestionTimeLeft--
                        if (newQuestionTimeLeft == 0) shouldSubmit = true
                    }

                    state.copy(
                        timeLeft = state.timeLeft + 1,
                        quizTimeLeft = newQuizTimeLeft,
                        questionTimeLeft = newQuestionTimeLeft,
                    )
                }
                if (shouldSubmit) onTimeout()
                if (quizFinished) onQuizFinished()
            }
        }
    }

    fun stop() {
        timerJob?.cancel()
        timerJob = null
    }

    fun resetQuestionTimer(questionTimerSeconds: Int) {
        timerState.update { it.copy(questionTimeLeft = questionTimerSeconds) }
    }
}
