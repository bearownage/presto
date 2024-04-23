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
import com.facebook.presto.common.block.Block;
import com.facebook.presto.spi.UpdatablePageSource;
import io.airlift.slice.Slice;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MemoryUpdatablePageSource
        implements UpdatablePageSource
{
    @Override
    public void deleteRows(Block rowIds)
    {
        UpdatablePageSource.super.deleteRows(rowIds);
    }

    @Override
    public void updateRows(Page page, List<Integer> columnValueAndRowIdChannels)
    {
        UpdatablePageSource.super.updateRows(page, columnValueAndRowIdChannels);
    }

    @Override
    public CompletableFuture<Collection<Slice>> finish()
    {
        return null;
    }

    @Override
    public void abort()
    {
        UpdatablePageSource.super.abort();
    }

    @Override
    public long getCompletedBytes()
    {
        return 0;
    }

    @Override
    public long getCompletedPositions()
    {
        return 0;
    }

    @Override
    public long getReadTimeNanos()
    {
        return 0;
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }

    @Override
    public Page getNextPage()
    {
        return null;
    }

    @Override
    public long getSystemMemoryUsage()
    {
        return 0;
    }

    @Override
    public void close() throws IOException
    {
    }
}
