package tanvd.csc.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.util.*
import kotlin.reflect.KClass

object JsonUtils {
    val jsonMapper = ObjectMapper().apply {
        registerModule(KotlinModule())
        registerModule(JodaModule())
        setTimeZone(TimeZone.getDefault())
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    fun writeValueAsString(obj: Any) : String {
        return jsonMapper.writeValueAsString(obj)
    }

    fun <T : Any>readValue(serialized: String, klass: KClass<T>): T {
        return jsonMapper.readValue(serialized, klass.java)
    }
}