/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.plugin.memory;

import com.facebook.presto.common.Page;
import com.facebook.presto.common.Utils;
import com.facebook.presto.common.block.Block;
import com.facebook.presto.common.block.RunLengthEncodedBlock;
import com.facebook.presto.common.type.TimeType;
import com.facebook.presto.common.type.TimestampType;
import com.facebook.presto.common.type.Type;
import com.facebook.presto.spi.ConnectorPageSource;
import com.facebook.presto.spi.PrestoException;
import com.facebook.presto.spi.UpdatablePageSource;
import io.airlift.slice.Slice;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Throwables.throwIfInstanceOf;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MemoryUpdatablePageSource
        implements UpdatablePageSource
{
    private final Block[] prefilledBlocks;
    private final int[] delegateIndexes;
    private final ConnectorPageSource delegate;

    public MemoryUpdatablePageSource(
            List<MemoryColumnHandle> columns,
            Map<Integer, Object> metadataValues,
            ConnectorPageSource delegate)
    {
        int size = requireNonNull(columns, "columns is null").size();
        this.delegate = requireNonNull(delegate, "delegate is null");


        prefilledBlocks = new Block[size];
        delegateIndexes = new int[size];

        int outputIndex = 0;
        int delegateIndex = 0;
        for (MemoryColumnHandle column : columns) {
                delegateIndexes[outputIndex] = delegateIndex;
                delegateIndex++;
            outputIndex++;
        }
    }

    @Override
    public long getCompletedBytes()
    {
        return delegate.getCompletedBytes();
    }

    @Override
    public long getCompletedPositions()
    {
        return delegate.getCompletedPositions();
    }

    @Override
    public long getReadTimeNanos()
    {
        return delegate.getReadTimeNanos();
    }

    @Override
    public boolean isFinished()
    {
        return delegate.isFinished();
    }

    @Override
    public Page getNextPage()
    {
        try {
            Page dataPage = delegate.getNextPage();
            if (dataPage == null) {
                return null;
            }

            int batchSize = dataPage.getPositionCount();
            Block[] blocks = new Block[prefilledBlocks.length];
            for (int i = 0; i < prefilledBlocks.length; i++) {
                if (prefilledBlocks[i] != null) {
                    blocks[i] = new RunLengthEncodedBlock(prefilledBlocks[i], batchSize);
                }
                else {
                    blocks[i] = dataPage.getBlock(delegateIndexes[i]);
                }
            }
            return new Page(batchSize, blocks);
        }
        catch (RuntimeException e) {
            closeWithSuppression(e);
            throwIfInstanceOf(e, PrestoException.class);
            throw new PrestoException(null, e);
        }
    }

    @Override
    public void deleteRows(Block rowIds)
    {

    }

    @Override
    public CompletableFuture<Collection<Slice>> finish()
    {
        return null;
    }

    @Override
    public void abort()
    {

    }

    @Override
    public void close()
    {
        try {
            delegate.close();
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String toString()
    {
        return delegate.toString();
    }

    @Override
    public long getSystemMemoryUsage()
    {
        long totalMemUsage = delegate.getSystemMemoryUsage();

        return totalMemUsage;
    }

    protected void closeWithSuppression(Throwable throwable)
    {
        requireNonNull(throwable, "throwable is null");
        try {
            close();
        }
        catch (RuntimeException e) {
            // Self-suppression not permitted
            if (throwable != e) {
                throwable.addSuppressed(e);
            }
        }
    }

    private Block nativeValueToBlock(Type type, Object prefilledValue)
    {
        if (prefilledValue != null && (type instanceof TimestampType && ((TimestampType) type).getPrecision() == MILLISECONDS || type instanceof TimeType)) {
            return Utils.nativeValueToBlock(type, MICROSECONDS.toMillis((long) prefilledValue));
        }
        return Utils.nativeValueToBlock(type, prefilledValue);
    }
}
