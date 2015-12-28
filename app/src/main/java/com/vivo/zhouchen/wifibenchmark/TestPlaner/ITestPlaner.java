package com.vivo.zhouchen.wifibenchmark.TestPlaner;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;

/**
 * Created by vivo on 2015/10/21.
 */
public interface ITestPlaner {
    Queue<Runnable> actions = new LinkedList<Runnable>();
    Map<String,String> errorReasons = new HashMap<String, String>();
//    Map<String,Map<String,S


    //    BlockingDeque<Runnable> actions;
    Map<String,String> checkConditions();

    Queue<Runnable> getTestActions();

    boolean excuteTest(Queue<Runnable> actions);

    boolean execute();
    boolean reportResults();

    boolean excuateATest();

    boolean stopTestForcely();

}


