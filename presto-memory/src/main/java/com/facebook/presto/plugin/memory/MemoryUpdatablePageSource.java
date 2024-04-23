package com.facebook.presto.plugin.memory;

import com.facebook.presto.common.Page;
import com.facebook.presto.common.block.Block;
import com.facebook.presto.spi.UpdatablePageSource;
import io.airlift.slice.Slice;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MemoryUpdatablePageSource implements UpdatablePageSource {

    @Override
    public void deleteRows(Block rowIds) {
        UpdatablePageSource.super.deleteRows(rowIds);
    }

    @Override
    public void updateRows(Page page, List<Integer> columnValueAndRowIdChannels) {
        UpdatablePageSource.super.updateRows(page, columnValueAndRowIdChannels);
    }

    @Override
    public CompletableFuture<Collection<Slice>> finish() {
        return null;
    }

    @Override
    public void abort() {
        UpdatablePageSource.super.abort();
    }

    @Override
    public long getCompletedBytes() {
        return 0;
    }

    @Override
    public long getCompletedPositions() {
        return 0;
    }

    @Override
    public long getReadTimeNanos() {
        return 0;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Page getNextPage() {
        return null;
    }

    @Override
    public long getSystemMemoryUsage() {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
