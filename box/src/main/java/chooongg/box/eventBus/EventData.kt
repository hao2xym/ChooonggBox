package chooongg.box.eventBus

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

data class EventData<T>(
    val coroutineScope: CoroutineScope,
    val eventDispatcher: CoroutineDispatcher,
    val onEvent: (T) -> Unit,
    val exception: ((Throwable)->Unit)? = null
) {

    private val channel = Channel<T>()

    init {
        coroutineScope.launch {
            channel.consumeEach { // 消费者循环地消费消息
                launch(eventDispatcher) {
                    if (exception!=null) {
                        try{
                            onEvent(it)
                        } catch (e:Exception) {
                            exception.invoke(e)
                        }
                    } else {
                        onEvent(it)
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Suppress("UNCHECKED_CAST")
    fun postEvent(event: Any) {
        if (!channel.isClosedForSend) {
            coroutineScope.launch {
                channel.send(event as T)
            }
        } else {
            println("Channel is closed for send")
        }
    }

    fun cancel() {
        channel.cancel()
    }
}