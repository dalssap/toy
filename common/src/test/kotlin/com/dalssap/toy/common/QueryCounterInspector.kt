package com.dalssap.toy.common

import org.hibernate.resource.jdbc.spi.StatementInspector

class QueryCounterInspector: StatementInspector {
    override fun inspect(sql: String): String {
        when {
            sql.startsWith("select", ignoreCase = true) -> QueryCountHolder.increment("select")
            sql.startsWith("insert", ignoreCase = true) -> QueryCountHolder.increment("insert")
            sql.startsWith("update", ignoreCase = true) -> QueryCountHolder.increment("update")
            sql.startsWith("delete", ignoreCase = true) -> QueryCountHolder.increment("delete")
        }

        return sql
    }
}

class QueryCountHolder private constructor() {
    companion object {
        private val counter: ThreadLocal<Map<String, Long>> = ThreadLocal.withInitial { mutableMapOf() }

        fun increment(type: String) {
            val current = counter.get().toMutableMap()
            current[type] = (current[type] ?: 0) + 1
            counter.set(current)
        }

        fun insertCount() = counter.get()["insert"] ?: 0
        fun selectCount() = counter.get()["select"] ?: 0
        fun updateCount() = counter.get()["update"] ?: 0
        fun deleteCount() = counter.get()["delete"] ?: 0

        fun clear() {
            counter.set(mutableMapOf())
        }
    }
}