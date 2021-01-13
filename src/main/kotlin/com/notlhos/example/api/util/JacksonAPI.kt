package com.notlhos.example.api.util

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption


object JacksonAPI {

    val objectMapper by lazy { ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .enable(SerializationFeature.INDENT_OUTPUT)
        .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).registerKotlinModule() }

    fun <T> readFile(file: File, clazz: Class<T>) : T = objectMapper.readValue(file, clazz)
    fun <T> readPath(path: Path, clazz: Class<T>) : T = objectMapper.readValue(readPath(path), clazz)
    fun <T> readValue(json: String, clazz: Class<T>) : T = objectMapper.readValue(json, clazz)

    fun readFile(file: File) : String = String(Files.readAllBytes(file.toPath()))
    fun readPath(path: Path) : String = String(Files.readAllBytes(path))

    fun writeFile(file: File, value: Any) = writeFile(file, readAsString(value))
    fun writeFile(file: File, value: String) = Files.write(file.toPath(), value.toByteArray(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)

    fun writePath(path: Path, value: Any) = writePath(path, readAsString(value))
    fun writePath(path: Path, value: String) = Files.write(path, value.toByteArray(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)

    fun readAsString(value: Any): String = objectMapper.writeValueAsString(value)
    fun registerModule(module: Module) = objectMapper.registerModule(module)

}