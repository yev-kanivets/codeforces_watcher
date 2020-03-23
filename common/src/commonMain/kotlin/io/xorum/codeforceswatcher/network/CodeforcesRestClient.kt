package io.xorum.codeforceswatcher.network

import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.Json
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.utils.EmptyContent
import io.ktor.http.URLProtocol
import io.xorum.codeforceswatcher.network.responses.ActionsResponse
import io.xorum.codeforceswatcher.network.responses.RatingChangeResponse
import io.xorum.codeforceswatcher.network.responses.UsersResponse
import kotlinx.serialization.*
import kotlinx.serialization.internal.UnitDescriptor
import kotlinx.serialization.json.Json.Companion.nonstrict

private const val CODEFORCES_API_LINK = "www.codeforces.com/api/"

object CodeforcesRestClient {
    private val codeforcesApiClient = makeCodeforcesApiClient()

    suspend fun getUsers(handles: String) = try {
        codeforcesApiClient.get<UsersResponse>(path = "user.info") {
            parameter("handles", handles)
        }
    } catch (t: Throwable) {
        null
    }

    suspend fun getRating(handle: String) = try {
        codeforcesApiClient.get<RatingChangeResponse>(path = "user.rating") {
            parameter("handle", handle)
        }
    } catch (t: Throwable) {
        null
    }

    suspend fun getActions(maxCount: Int = 100, lang: String) = try {
        codeforcesApiClient.get<ActionsResponse>(path = "recentActions") {
            parameter("maxCount", maxCount)
            parameter("lang", lang)
        }
    } catch (t: Throwable) {
        null
    }

    @UseExperimental(UnstableDefault::class)
    private fun makeCodeforcesApiClient(): HttpClient = HttpClient {
        expectSuccess = false
        defaultRequest {
            url {
                host = CODEFORCES_API_LINK
                protocol = URLProtocol.HTTPS
            }
        }
        Json {
            serializer = KotlinxSerializer(json = nonstrict).apply {
                register(EmptyContentSerializer)
            }
        }
        Logging {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }

    }

    private object EmptyContentSerializer : KSerializer<EmptyContent> {

        override val descriptor: SerialDescriptor = UnitDescriptor

        override fun deserialize(decoder: Decoder): EmptyContent =
                EmptyContent.also { decoder.decodeUnit() }

        override fun serialize(encoder: Encoder, obj: EmptyContent) = encoder.encodeUnit()
    }
}