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
package com.facebook.presto.sql.analyzer.utils;

import com.facebook.presto.common.resourceGroups.QueryType;
import com.facebook.presto.sql.tree.*;
import com.google.common.collect.ImmutableMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public final class StatementUtils
{
    private StatementUtils() {}

    private static final Map<Class<? extends Statement>, QueryType> STATEMENT_QUERY_TYPES;

    static {
        ImmutableMap.Builder<Class<? extends Statement>, QueryType> builder = ImmutableMap.builder();

        builder.put(Query.class, QueryType.SELECT);

        builder.put(Explain.class, QueryType.EXPLAIN);
        builder.put(Analyze.class, QueryType.ANALYZE);

        builder.put(CreateTableAsSelect.class, QueryType.INSERT);
        builder.put(Insert.class, QueryType.INSERT);
        builder.put(RefreshMaterializedView.class, QueryType.INSERT);

        builder.put(Delete.class, QueryType.DELETE);
        builder.put(Update.class, QueryType.UPDATE);
        builder.put(MergeInto.class, QueryType.MERGE);

        builder.put(ShowCatalogs.class, QueryType.DESCRIBE);
        builder.put(ShowCreate.class, QueryType.DESCRIBE);
        builder.put(ShowCreateFunction.class, QueryType.DESCRIBE);
        builder.put(ShowFunctions.class, QueryType.DESCRIBE);
        builder.put(ShowGrants.class, QueryType.DESCRIBE);
        builder.put(ShowRoles.class, QueryType.DESCRIBE);
        builder.put(ShowRoleGrants.class, QueryType.DESCRIBE);
        builder.put(ShowSchemas.class, QueryType.DESCRIBE);
        builder.put(ShowSession.class, QueryType.DESCRIBE);
        builder.put(ShowStats.class, QueryType.DESCRIBE);
        builder.put(ShowTables.class, QueryType.DESCRIBE);
        builder.put(ShowColumns.class, QueryType.DESCRIBE);
        builder.put(DescribeInput.class, QueryType.DESCRIBE);
        builder.put(DescribeOutput.class, QueryType.DESCRIBE);

        builder.put(CreateSchema.class, QueryType.DATA_DEFINITION);
        builder.put(DropSchema.class, QueryType.DATA_DEFINITION);
        builder.put(RenameSchema.class, QueryType.DATA_DEFINITION);
        builder.put(CreateType.class, QueryType.DATA_DEFINITION);
        builder.put(AddColumn.class, QueryType.DATA_DEFINITION);
        builder.put(CreateTable.class, QueryType.DATA_DEFINITION);
        builder.put(RenameTable.class, QueryType.DATA_DEFINITION);
        builder.put(RenameColumn.class, QueryType.DATA_DEFINITION);
        builder.put(DropColumn.class, QueryType.DATA_DEFINITION);
        builder.put(DropTable.class, QueryType.DATA_DEFINITION);
        builder.put(DropConstraint.class, QueryType.DATA_DEFINITION);
        builder.put(AddConstraint.class, QueryType.DATA_DEFINITION);
        builder.put(CreateView.class, QueryType.DATA_DEFINITION);
        builder.put(TruncateTable.class, QueryType.DATA_DEFINITION);
        builder.put(DropView.class, QueryType.DATA_DEFINITION);
        builder.put(CreateMaterializedView.class, QueryType.DATA_DEFINITION);
        builder.put(DropMaterializedView.class, QueryType.DATA_DEFINITION);
        builder.put(CreateFunction.class, QueryType.CONTROL);
        builder.put(AlterFunction.class, QueryType.DATA_DEFINITION);
        builder.put(DropFunction.class, QueryType.CONTROL);
        builder.put(Use.class, QueryType.CONTROL);
        builder.put(SetSession.class, QueryType.CONTROL);
        builder.put(ResetSession.class, QueryType.CONTROL);
        builder.put(StartTransaction.class, QueryType.CONTROL);
        builder.put(Commit.class, QueryType.CONTROL);
        builder.put(Rollback.class, QueryType.CONTROL);
        builder.put(Call.class, QueryType.DATA_DEFINITION);
        builder.put(CreateRole.class, QueryType.DATA_DEFINITION);
        builder.put(DropRole.class, QueryType.DATA_DEFINITION);
        builder.put(GrantRoles.class, QueryType.DATA_DEFINITION);
        builder.put(RevokeRoles.class, QueryType.DATA_DEFINITION);
        builder.put(SetRole.class, QueryType.CONTROL);
        builder.put(Grant.class, QueryType.DATA_DEFINITION);
        builder.put(Revoke.class, QueryType.DATA_DEFINITION);
        builder.put(Prepare.class, QueryType.CONTROL);
        builder.put(Deallocate.class, QueryType.CONTROL);

        STATEMENT_QUERY_TYPES = builder.build();
    }

    public static Set<QueryType> getAllQueryTypes()
    {
        return unmodifiableSet(new HashSet<>(STATEMENT_QUERY_TYPES.values()));
    }

    public static Optional<QueryType> getQueryType(Class<? extends Statement> statement)
    {
        return Optional.ofNullable(STATEMENT_QUERY_TYPES.get(statement));
    }

    public static boolean isTransactionControlStatement(Statement statement)
    {
        return statement instanceof StartTransaction || statement instanceof Commit || statement instanceof Rollback;
    }
}
