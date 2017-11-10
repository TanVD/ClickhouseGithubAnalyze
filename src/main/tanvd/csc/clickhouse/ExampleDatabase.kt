package tanvd.csc.clickhouse

import tanvd.aorm.Database

object ExampleDatabase : Database() {
    override val name: String = "default"

    override val url: String = "jdbc:clickhouse://localhost:8123"
    override val password: String = ""
    override val user: String = "default"

    override val useSsl: Boolean = false
    override val sslCertPath: String = ""
    override val sslVerifyMode: String = ""
}