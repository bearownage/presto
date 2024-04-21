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
package com.facebook.presto.sql.tree;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

public class Merge
        extends Statement
{
    private final Table target;
    private final Table source;
    private final Expression condition;

    public Merge(Table target, Table source, Expression condition)
    {
        this(Optional.empty(), target, source, condition);
    }

    public Merge(NodeLocation location, Table target, Table source, Expression condition)
    {
        this(Optional.of(location), target, source, condition);
    }

    public Merge(Optional<NodeLocation> location, Table target, Table source, Expression condition)
    {
        super(location);
        this.target = requireNonNull(target, "target is null");
        this.source = requireNonNull(source, "source is null");
        this.condition = requireNonNull(condition, "condition is null");
    }

    public Table getTarget()
    {
        return target;
    }

    public Table getSource()
    {
        return source;
    }

    public Expression getCondition()
    {
        return condition;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context)
    {
        return visitor.visitMerge(this, context);
    }

    @Override
    public List<Node> getChildren()
    {
        ImmutableList.Builder<Node> nodes = ImmutableList.builder();
        nodes.add(source);
        nodes.add(condition);
        return nodes.build();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(target, source, condition);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        Merge o = (Merge) obj;
        return Objects.equals(target, o.target) &&
                Objects.equals(source, o.source) &&
                Objects.equals(condition, o.condition);
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("target", target)
                .add("source", source)
                .add("condition", condition)
                .toString();
    }
}
