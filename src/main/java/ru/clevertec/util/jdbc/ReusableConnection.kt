package ru.clevertec.util.jdbc

import java.sql.Connection

class ReusableConnection(private val connection: Connection, private val releaser: ConnectionReleaser) :
    Connection by connection {

    override fun close() {
        releaser.releaseConnection(this)
    }

    fun reallyClose() {
        connection.close()
    }
}