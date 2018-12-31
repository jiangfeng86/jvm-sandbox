package com.alibaba.jvm.sandbox.qatest.core.enhance.listener;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.event.LineEvent;
import com.alibaba.jvm.sandbox.core.enhance.weaver.EventListenerHandlers;
import com.alibaba.jvm.sandbox.core.util.ObjectIDs;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.getLength;
import static org.junit.Assert.assertEquals;

public class LineNumTracingEventListener extends InterruptedEventListener {
    private final List<Integer> lineTracing = new ArrayList<Integer>();

    @Override
    public void onEvent(Event event) throws Throwable {
        if (event instanceof LineEvent){
            LineEvent lineEvent = (LineEvent) event;
            lineTracing.add(((LineEvent) event).lineNumber);
        }

    }

    /**
     * 获取跟踪行信息
     *
     * @return 跟踪信息
     */
    public List<Integer> getLineTracing() {
        return lineTracing;
    }

    /**
     * 断言跟踪行信息
     *
     * @param exceptLineNums 期待的行号
     */
    public void assertLIneTracing(final Integer... exceptLineNums) {
        assertEventProcessor();
        assertArrayEquals(
                exceptLineNums,
                getLineTracing().toArray(new Integer[]{})
        );
    }

    // 检查内核事件处理器是否正确
    private void assertEventProcessor() {
        EventListenerHandlers
                .getSingleton()
                .checkEventProcessor(ObjectIDs.instance.identity(this));
    }

    private <E> void assertArrayEquals(E[] exceptArray, E[] actualArray) {
        assertEquals("except size not matched!", getLength(exceptArray), getLength(actualArray));
        for (int index = 0; index < exceptArray.length; index++) {
            assertEquals("[" + index + "] not matched", exceptArray[index], actualArray[index]);
        }
    }
}