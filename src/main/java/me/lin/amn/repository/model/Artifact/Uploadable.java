package me.lin.amn.repository.model.artifact;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Lin on 6/20/16.
 */
public interface Uploadable {
    void upload() throws BrokenBarrierException, InterruptedException;
    void setFileInfo(String fileName, long len, InputStream is, CyclicBarrier barrier);
}
