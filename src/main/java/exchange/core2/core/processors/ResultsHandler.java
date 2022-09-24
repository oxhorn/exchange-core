package exchange.core2.core.processors;

import com.lmax.disruptor.EventHandler;
import exchange.core2.core.common.cmd.OrderCommand;
import exchange.core2.core.common.cmd.OrderCommandType;
import lombok.RequiredArgsConstructor;

import java.util.function.ObjLongConsumer;

@RequiredArgsConstructor
public final class ResultsHandler implements EventHandler<OrderCommand> {

    //创建ExchangeCorde由调用方传入，用于执行disuptor 消费完事件后将结果通知出去事
    private final ObjLongConsumer<OrderCommand> resultsConsumer;

    private boolean processingEnabled = true;

    @Override
    public void onEvent(OrderCommand cmd, long sequence, boolean endOfBatch) {

        if (cmd.command == OrderCommandType.GROUPING_CONTROL) {
            processingEnabled = cmd.orderId == 1;
        }

        if (processingEnabled) {
            resultsConsumer.accept(cmd, sequence);
        }

    }
}
