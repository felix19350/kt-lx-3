package lx.kotlin.infra.persistence

import org.testcontainers.containers.CassandraContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.CassandraQueryWaitStrategy
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy

class KCassandraContainer : CassandraContainer<KCassandraContainer>()

object CassandraTestContainer {
    val instance by lazy { initCassandra() }

    private fun initCassandra(): KCassandraContainer {
        val instance = KCassandraContainer()
        instance.setWaitStrategy(CassandraQueryWaitStrategy())
        instance.startupAttempts = 1
        instance.withInitScript("database.cql")
        instance.start()
        return instance
    }
}

/*class KAlpineContainer : GenericContainer<KAlpineContainer>("alpine:3.2") {
    init {
        withExposedPorts(80)
            .withEnv("MAGIC_NUMBER", "42")
            .withCommand(
                "/bin/sh", "-c",
                "while true; do echo \"\$MAGIC_NUMBER\" | nc -l -p 80; done"
            )
    }
}

object AlpineHttpServer {
    val instance by lazy { init() }

    private fun init():KAlpineContainer {
        val instance = KAlpineContainer()
        instance.setWaitStrategy(HostPortWaitStrategy())
        instance.startupAttempts = 1
        instance.start()
        return instance
    }
}*/
