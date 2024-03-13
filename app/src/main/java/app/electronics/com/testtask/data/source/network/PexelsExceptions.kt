package app.electronics.com.testtask.data.source.network

class WebPexelsException(
    httpCode: Int,
    message: String?
) : Exception("$httpCode: $message")

class EmptyPexelsException : Exception()